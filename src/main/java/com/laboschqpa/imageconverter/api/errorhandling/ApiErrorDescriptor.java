package com.laboschqpa.imageconverter.api.errorhandling;

import com.laboschqpa.imageconverter.annotation.ApiErrorCategory;

public interface ApiErrorDescriptor {
    default String getApiErrorCategory() {
        ApiErrorCategory apiErrorCategory = this.getClass().getAnnotation(ApiErrorCategory.class);
        return apiErrorCategory != null ? apiErrorCategory.value() : null;
    }

    Integer getApiErrorCode();

    String getApiErrorName();
}
