package com.laboschqpa.imageconverter.service.variantjob;

import com.laboschqpa.imageconverter.api.dto.imagevariant.ProcessCreationJobRequest;
import com.laboschqpa.imageconverter.config.properties.VariantCreationJobsConfigurationProperties;
import com.laboschqpa.imageconverter.exceptions.TooManyJobsException;
import com.laboschqpa.imageconverter.model.ProcessCreationJobCommand;
import com.laboschqpa.imageconverter.model.VariantCreationJobQueue;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ImageVariantCreationJobScheduler {
    private final VariantCreationJobsConfigurationProperties configurationProperties;
    private final MeterRegistry meterRegistry;

    private VariantCreationJobQueue jobQueue;

    @PostConstruct
    private void postConstruct() {
        jobQueue = new VariantCreationJobQueue(configurationProperties.getMaxQueuedJobCount());

        final List<Tag> queuedTags = new ArrayList<>(2);
        queuedTags.add(new ImmutableTag(Metric.TAG_NAME_JOB_TYPE, Metric.TAG_VALUE_IMAGE_VARIANT_CREATION));
        queuedTags.add(new ImmutableTag(Metric.TAG_NAME_STAGE, Metric.TAG_VALUE_QUEUED));
        meterRegistry.gauge(Metric.METRIC_NAME_JOB_COUNT_GAUGE, queuedTags, jobQueue, VariantCreationJobQueue::getCurrentLength);
    }

    public void scheduleJob(ProcessCreationJobRequest request) {
        final long jobId = request.getJobId();

        try {
            jobQueue.enqueue(new ProcessCreationJobCommand(request));

            meterRegistry.counter(Metric.METRIC_NAME_SCHEDULE_JOB_REQUEST_COUNT,
                    Metric.TAG_NAME_JOB_TYPE, Metric.TAG_VALUE_IMAGE_VARIANT_CREATION,
                    Metric.TAG_NAME_REACTION, Metric.TAG_VALUE_ACCEPT
            ).increment();
            log.debug("Image variant creation job queued. jobId: {}", jobId);
        } catch (TooManyJobsException e) {
            meterRegistry.counter(Metric.METRIC_NAME_SCHEDULE_JOB_REQUEST_COUNT,
                    Metric.TAG_NAME_JOB_TYPE, Metric.TAG_VALUE_IMAGE_VARIANT_CREATION,
                    Metric.TAG_NAME_REACTION, Metric.TAG_VALUE_REJECT
            ).increment();
            log.info("Image variant creation job rejected. jobId: {}", jobId);

            throw e;
        }
    }

    public boolean isQueueEmpty() {
        return jobQueue.isEmpty();
    }

    public ProcessCreationJobCommand popJob() {
        return jobQueue.pop();
    }
}
