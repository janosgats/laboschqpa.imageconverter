package com.laboschqpa.imageconverter.service.variantjob;

public class Metric {
    static final String TAG_NAME_JOB_TYPE = "jobType";
    static final String TAG_VALUE_IMAGE_VARIANT_CREATION = "imageVariantCreation";

    static final String METRIC_NAME_SCHEDULE_JOB_REQUEST_COUNT = "schedule_job_request_count";
    static final String TAG_NAME_REACTION = "reaction";
    static final String TAG_VALUE_ACCEPT = "accept";
    static final String TAG_VALUE_REJECT = "reject";

    static final String METRIC_NAME_JOB_COUNT_GAUGE = "job_count_gauge";
    static final String TAG_NAME_STAGE = "stage";
    static final String TAG_VALUE_QUEUED = "queued";
    static final String TAG_VALUE_ACTIVE = "active";


    static final String METRIC_NAME_PICKED_UP_JOB_COUNT = "picked_up_job_count";

    static final String METRIC_NAME_FINISHED_JOB_COUNT = "finished_job_count";
    static final String TAG_NAME_RESULT = "result";
    static final String TAG_VALUE_SUCCESS = "success";
    static final String TAG_VALUE_FAILURE = "failure";
}
