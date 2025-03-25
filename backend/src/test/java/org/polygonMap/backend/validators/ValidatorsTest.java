package org.polygonMap.backend.validators;

import org.junit.jupiter.api.Test;
import org.polygonMap.model.Polygon;

import static org.assertj.core.api.Assertions.assertThat;

class ValidatorsTest {

    private Validators validators = new Validators();

    @Test
    void validateValuesInNotNullableField() {
        Polygon polygon = new Polygon();
        ValidationResult validationResult = new ValidationResult();
        validators.validate(validationResult, validators.validateValuesInNotNullableField(polygon));
        validators.validateValuesInNotNullableField(polygon);

        assertThat(validationResult.getErrors().size()).isEqualTo(1);
        assertThat(validationResult.getErrors().getFirst().message()).isEqualTo(
                "Value in the mandatory field 'id' is null");
    }
}