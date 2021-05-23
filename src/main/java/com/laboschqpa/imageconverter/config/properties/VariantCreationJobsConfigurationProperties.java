package com.laboschqpa.imageconverter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "variantcreationjobs")
public class VariantCreationJobsConfigurationProperties {
    /**
     * Don't forget to <b>multiply</b> this <b>by the count of running instances</b>!
     */
    private Integer maxActiveJobCount;
    /**
     * The queue count does NOT include the momentarily processed (active) job count.
     * <br/>
     * Don't forget to <b>multiply</b> this <b>by the count of running instances</b>!
     */
    private Integer maxQueuedJobCount;
}
