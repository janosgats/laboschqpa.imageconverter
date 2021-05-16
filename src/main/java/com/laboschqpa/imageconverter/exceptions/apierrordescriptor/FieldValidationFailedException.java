package com.laboschqpa.imageconverter.exceptions.apierrordescriptor;

import com.laboschqpa.imageconverter.enums.apierrordescriptor.FieldValidationFailedApiError;
import com.laboschqpa.imageconverter.model.FieldValidationError;
import lombok.Getter;

import java.util.Collection;

public class FieldValidationFailedException extends ApiErrorDescriptorException {
    @Getter
    private Collection<FieldValidationError> fieldValidationErrors;

    public FieldValidationFailedException(Collection<FieldValidationError> inputFieldErrors) {
        this(inputFieldErrors, null);
    }

    public FieldValidationFailedException(Collection<FieldValidationError> inputFieldErrors, String message) {
        super(FieldValidationFailedApiError.FIELD_VALIDATION_FAILED, message);
        this.fieldValidationErrors = inputFieldErrors;
    }

    @Override
    public Object getPayload() {
        return this.getFieldValidationErrors();
    }
}
