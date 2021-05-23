package com.laboschqpa.imageconverter.service.variantjob;

import com.laboschqpa.imageconverter.config.properties.VariantCreationJobsConfigurationProperties;
import com.laboschqpa.imageconverter.model.ProcessCreationJobCommand;
import com.laboschqpa.imageconverter.service.apiclient.filehost.FileHostApiClient;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Service
@RequiredArgsConstructor
public class ImageVariantCreationJobConsumer {
    private static final long FIXED_RATE_JOB_QUEUE_POLLING_INTERVAL_MS = 2500;

    private final VariantCreationJobsConfigurationProperties configurationProperties;
    private final MeterRegistry meterRegistry;
    private final ImageVariantCreationJobProcessor imageVariantCreationJobProcessor;
    private final FileHostApiClient fileHostApiClient;
    private final ImageVariantCreationJobScheduler imageVariantCreationJobScheduler;

    private final AtomicInteger numberOfActiveJobs = new AtomicInteger(0);

    @PostConstruct
    private void postConstruct() {
        final List<Tag> activeTags = new ArrayList<>(2);
        activeTags.add(new ImmutableTag(Metric.TAG_NAME_JOB_TYPE, Metric.TAG_VALUE_IMAGE_VARIANT_CREATION));
        activeTags.add(new ImmutableTag(Metric.TAG_NAME_STAGE, Metric.TAG_VALUE_ACTIVE));
        meterRegistry.gauge(Metric.METRIC_NAME_JOB_COUNT_GAUGE, activeTags, numberOfActiveJobs);
    }


    @Scheduled(fixedRate = FIXED_RATE_JOB_QUEUE_POLLING_INTERVAL_MS)
    public void consumeQueue() {
        while (!imageVariantCreationJobScheduler.isQueueEmpty()) {
            boolean acceptJob = pickUpOneJobIfThereIsAvailableSlot();
            if (!acceptJob) {
                break;
            }

            meterRegistry.counter(Metric.METRIC_NAME_PICKED_UP_JOB_COUNT,
                    Metric.TAG_NAME_JOB_TYPE, Metric.TAG_VALUE_IMAGE_VARIANT_CREATION
            ).increment();

            consumeJobAsync(imageVariantCreationJobScheduler.popJob());
        }
    }

    private void consumeJobAsync(ProcessCreationJobCommand command) {
        final long jobId = command.getJobId();

        imageVariantCreationJobProcessor
                .processCreationJob(command)
                .doOnSuccess(o -> {
                    log.info("Image variant creation job succeeded. jobId: {}", jobId);
                    meterRegistry.counter(Metric.METRIC_NAME_FINISHED_JOB_COUNT,
                            Metric.TAG_NAME_JOB_TYPE, Metric.TAG_VALUE_IMAGE_VARIANT_CREATION,
                            Metric.TAG_NAME_RESULT, Metric.TAG_VALUE_SUCCESS
                    ).increment();
                })
                .doOnError(processingThrowable -> {
                    log.error("Error during image variant creation job. jobId: {}", jobId, processingThrowable);
                    meterRegistry.counter(Metric.METRIC_NAME_FINISHED_JOB_COUNT,
                            Metric.TAG_NAME_JOB_TYPE, Metric.TAG_VALUE_IMAGE_VARIANT_CREATION,
                            Metric.TAG_NAME_RESULT, Metric.TAG_VALUE_FAILURE
                    ).increment();

                    fileHostApiClient.signalFailedJobInJobProcessor(jobId)
                            .doOnError(signalingThrowable -> {
                                log.error("Error while signaling failed job to FileHost. jobId: {}", jobId, signalingThrowable);
                            })
                            .onErrorResume(throwable -> Mono.empty())
                            .subscribe();
                })
                .onErrorResume(throwable -> Mono.empty())
                .doFinally(signalType -> decrementNumberOfActiveJobs())
                .subscribe();

        log.info("Image variant creation job picked up. jobId: {}", jobId);
    }


    private boolean pickUpOneJobIfThereIsAvailableSlot() {
        final boolean acceptJob;
        synchronized (numberOfActiveJobs) {
            if (numberOfActiveJobs.get() < configurationProperties.getMaxActiveJobCount()) {
                numberOfActiveJobs.incrementAndGet();
                acceptJob = true;
            } else {
                acceptJob = false;
            }
        }

        return acceptJob;
    }

    private void decrementNumberOfActiveJobs() {
        numberOfActiveJobs.decrementAndGet();
    }
}
