package com.simonini.adidas.subscriptionapi.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BirthDateValidator implements ConstraintValidator<BirthDateValue, String> {

  private static final String DATE_PATTERN = "MM/dd/yyyy";

  @Override
  public boolean isValid(String customDateField, ConstraintValidatorContext cxt) {
    return Optional.ofNullable(customDateField)
        .map(
            value -> {
              SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
              try {
                sdf.setLenient(false);
                sdf.parse(customDateField);
                return true;
              } catch (ParseException e) {
                return false;
              }
            })
        .orElse(false);
  }
}
