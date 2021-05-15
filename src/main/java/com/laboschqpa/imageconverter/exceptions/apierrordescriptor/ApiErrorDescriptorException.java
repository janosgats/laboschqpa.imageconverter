package com.laboschqpa.imageconverter.exceptions.apierrordescriptor;

import com.laboschqpa.imageconverter.api.errorhandling.ApiErrorDescriptor;
import lombok.Getter;

public class ApiErrorDescriptorException extends RuntimeException {
    @Getter
    private ApiErrorDescriptor apiErrorDescriptor;

    public ApiErrorDescriptorException(ApiErrorDescriptor apiErrorDescriptor) {
        this(apiErrorDescriptor, null);
    }

    public ApiErrorDescriptorException(ApiErrorDescriptor apiErrorDescriptor, String message) {
        super(message);
        this.apiErrorDescriptor = apiErrorDescriptor;
    }

    public ApiErrorDescriptorException(ApiErrorDescriptor apiErrorDescriptor, String message, Throwable cause) {
        super(message, cause);
        this.apiErrorDescriptor = apiErrorDescriptor;
    }
}
