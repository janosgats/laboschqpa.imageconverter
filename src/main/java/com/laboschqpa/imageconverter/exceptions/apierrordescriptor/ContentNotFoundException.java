package com.laboschqpa.imageconverter.exceptions.apierrordescriptor;

import com.laboschqpa.imageconverter.enums.apierrordescriptor.ContentApiError;

public class ContentNotFoundException extends ApiErrorDescriptorException {
    public ContentNotFoundException(String message) {
        super(ContentApiError.CONTENT_IS_NOT_FOUND, message);
    }
}
