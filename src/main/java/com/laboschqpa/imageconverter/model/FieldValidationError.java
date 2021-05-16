package com.laboschqpa.imageconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldValidationError {
    private String fieldKey;
    private String errorMessage;
}
