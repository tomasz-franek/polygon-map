package org.polygonMap.backend.exceptions;

import lombok.Getter;
import org.polygonMap.backend.validators.ValidationResult;

@Getter
public class ValidationException extends RuntimeException {

    private final ValidationResult validationResult;

    public ValidationException(String message, ValidationResult validationResult) {
        super(message);
        this.validationResult = validationResult;
    }
}
