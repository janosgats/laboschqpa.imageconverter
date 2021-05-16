package com.laboschqpa.imageconverter.api.dto.imagevariant;

import com.laboschqpa.imageconverter.util.SelfValidator;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = false)
@Data
public class ProcessCreationJobRequest extends SelfValidator {
    @NotNull
    @Min(0)
    private Long jobId;
    @NotNull
    @Min(0)
    private Long originalFileId;
    @NotNull
    @Min(1)
    private Integer variantSize;
}
