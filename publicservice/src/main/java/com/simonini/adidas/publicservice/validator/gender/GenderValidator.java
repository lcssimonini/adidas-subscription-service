package com.simonini.adidas.publicservice.validator.gender;

import com.simonini.adidas.publicservice.domain.Gender;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Optional;

import static java.lang.Boolean.TRUE;

public class GenderValidator implements ConstraintValidator<GenderValue, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value).map(v ->
                Arrays.stream(Gender.values())
                        .anyMatch(genderValue -> genderValue.name().equalsIgnoreCase(value)))
                .orElse(TRUE);
    }
}
