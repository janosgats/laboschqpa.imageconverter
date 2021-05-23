package com.laboschqpa.imageconverter.model;

import com.laboschqpa.imageconverter.api.dto.imagevariant.ProcessCreationJobRequest;
import com.laboschqpa.imageconverter.util.SelfValidator;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
@Getter
public class ProcessCreationJobCommand extends SelfValidator {
    private final Long jobId;
    private final Long originalFileId;
    private final Integer variantSize;

    public ProcessCreationJobCommand(ProcessCreationJobRequest request) {
        this.jobId = request.getJobId();
        this.originalFileId = request.getOriginalFileId();
        this.variantSize = request.getVariantSize();
    }
}
