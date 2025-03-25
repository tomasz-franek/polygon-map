package org.polygonMap.backend.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Validators {

    public <T> ValidationResult validate(ValidationResult result, Validator... validators) {
        Arrays.stream(validators).forEach(v -> v.validate(result));
        return result;
    }


    public <T> Validator notNullValue(T value, String fieldName, String errorMessage) {
        return (result) -> {
            if (value == null) {
                result.addFieldError(fieldName, errorMessage);
            }
            return result;
        };
    }

    public Validator validateValuesInNotNullableField(Object entity) {
        return (result) -> {
            Arrays.stream(entity.getClass().getDeclaredFields()).sequential().forEach(field -> {
                Nullable nullableAnnotation = field.getAnnotation(Nullable.class);
                if (nullableAnnotation == null) {
                    Optional<Method> method = getGetterMethodForField(entity, field);
                    if (method.isPresent()) {
                        try {
                            if (method.get().invoke(entity) == null) {
                                result.addError(new ValidationResult.FieldContext(List.of(field.getName())),
                                        String.format("Value in the mandatory field '%s' is null", field.getName()));
                            }
                        } catch (IllegalAccessException | InvocationTargetException ignored) {
                        }
                    }
                }
            });
            return result;
        };
    }

    private static Optional<Method> getGetterMethodForField(Object entity, Field field) {
        Method[] methods = entity.getClass().getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(element -> element.getName().toLowerCase().equals("get" + field.getName().toLowerCase()))
                .findFirst();
    }
}
