package com.laboschqpa.imageconverter.service;

import com.laboschqpa.imageconverter.api.dto.imagevariant.ProcessCreationJobRequest;
import com.laboschqpa.imageconverter.exceptions.TooManyActiveJobsException;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Service
@RequiredArgsConstructor
public class ImageVariantCreationJobScheduler {
    private static final String METRIC_NAME_ACTIVE_JOB_COUNT_GAUGE = "active_job_count_gauge";
    private static final String METRIC_NAME_JOB_REQUEST_COUNT = "job_request_count";
    private static final String METRIC_NAME_TERMINATED_JOB_COUNT = "terminated_job_count";

    private static final String TAG_NAME_JOB_TYPE = "jobType";
    private static final String TAG_VALUE_IMAGE_VARIANT_CREATION = "imageVariantCreation";

    private static final String TAG_NAME_REACTION = "reaction";
    private static final String TAG_VALUE_ACCEPT = "accept";
    private static final String TAG_VALUE_REJECT = "reject";

    private static final String TAG_NAME_RESULT = "result";
    private static final String TAG_VALUE_SUCCESS = "success";
    private static final String TAG_VALUE_FAILURE = "failure";

    private static final int MAX_PARALLEL_JOBS = 20;

    private final MeterRegistry meterRegistry;
    private final ImageVariantCreationJobProcessor imageVariantCreationJobProcessor;

    private final AtomicInteger numberOfActiveJobs = new AtomicInteger(0);

    @PostConstruct
    private void postConstruct() {
        final List<Tag> tags = new ArrayList<>(2);
        tags.add(new ImmutableTag(TAG_NAME_JOB_TYPE, TAG_VALUE_IMAGE_VARIANT_CREATION));

        meterRegistry.gauge(METRIC_NAME_ACTIVE_JOB_COUNT_GAUGE, tags, numberOfActiveJobs);
    }

    public void scheduleCreationJob(ProcessCreationJobRequest request) {
        final long jobId = request.getJobId();

        incrementNumberOfActiveJobs();

        imageVariantCreationJobProcessor
                .processCreationJobAsync(request)
                .doOnSuccess(o -> {
                    log.info("Image variant creation job succeeded. jobId: {}", jobId);
                    meterRegistry.counter(METRIC_NAME_TERMINATED_JOB_COUNT,
                            TAG_NAME_JOB_TYPE, TAG_VALUE_IMAGE_VARIANT_CREATION,
                            TAG_NAME_RESULT, TAG_VALUE_SUCCESS
                    ).increment();
                })
                .doOnError(throwable -> {
                    log.error("Error during image variant creation job. jobId: {}", jobId, throwable);
                    meterRegistry.counter(METRIC_NAME_TERMINATED_JOB_COUNT,
                            TAG_NAME_JOB_TYPE, TAG_VALUE_IMAGE_VARIANT_CREATION,
                            TAG_NAME_RESULT, TAG_VALUE_FAILURE
                    ).increment();

                    //TODO: Do callback to FileHost to indicate that the job failed (to not to rely on timeout so much).
                })
                .onErrorResume(throwable -> Mono.empty())
                .doFinally(signalType -> decrementNumberOfActiveJobs())
                .subscribe();

        log.info("Image variant creation job queued. jobId: {}", jobId);
    }

    private void incrementNumberOfActiveJobs() {
        final boolean acceptJob;
        synchronized (numberOfActiveJobs) {
            if (numberOfActiveJobs.get() < MAX_PARALLEL_JOBS) {
                numberOfActiveJobs.incrementAndGet();
                acceptJob = true;
            } else {
                acceptJob = false;
            }
        }

        final String reaction = acceptJob ? TAG_VALUE_ACCEPT : TAG_VALUE_REJECT;
        meterRegistry.counter(METRIC_NAME_JOB_REQUEST_COUNT,
                TAG_NAME_JOB_TYPE, TAG_VALUE_IMAGE_VARIANT_CREATION,
                TAG_NAME_REACTION, reaction
        ).increment();

        if (!acceptJob) {
            throw new TooManyActiveJobsException();
        }
    }

    private void decrementNumberOfActiveJobs() {
        numberOfActiveJobs.decrementAndGet();
    }
}
