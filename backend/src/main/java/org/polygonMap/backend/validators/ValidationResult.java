package org.polygonMap.backend.validators;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@Getter
public class ValidationResult {

    private final List<ValidationError> errors = new LinkedList<>();

    public ValidationResult add(ValidationResult result) {
        this.errors.addAll(result.getErrors());
        return this;
    }

    public void addFieldError(String fieldName, String message) {
        errors.add(new ValidationError(new FieldContext(List.of(fieldName)), message));
    }

    public void addError(Context context, String message) {
        errors.add(new ValidationError(context, message));
    }

    public boolean isFailing() {
        return !errors.isEmpty();
    }


    public record ValidationError(@JsonUnwrapped Context context, String message) {
    }

    public interface Context {
        static Supplier<Context> contextSupplier(Object object) {
            return () -> new StringContext(object.getClass().getSimpleName());
        }
    }

    public record FieldContext(Collection<String> fieldNames) implements Context {
    }

    public record StringContext(String text) implements Context {
    }
}
