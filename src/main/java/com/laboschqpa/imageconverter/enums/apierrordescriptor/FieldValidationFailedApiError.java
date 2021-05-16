package com.laboschqpa.imageconverter.enums.apierrordescriptor;

import com.laboschqpa.imageconverter.annotation.ApiErrorCategory;
import com.laboschqpa.imageconverter.api.errorhandling.ApiErrorDescriptor;

@ApiErrorCategory("fieldValidationFailed")
public enum FieldValidationFailedApiError implements ApiErrorDescriptor {
    FIELD_VALIDATION_FAILED(1),
    HTTP_MESSAGE_IS_NOT_READABLE(2);

    private Integer apiErrorCode;

    FieldValidationFailedApiError(Integer apiErrorCode) {
        this.apiErrorCode = apiErrorCode;
    }

    @Override
    public Integer getApiErrorCode() {
        return apiErrorCode;
    }

    @Override
    public String getApiErrorName() {
        return toString();
    }
}
