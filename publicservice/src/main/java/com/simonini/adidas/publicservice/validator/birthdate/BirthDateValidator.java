package com.simonini.adidas.publicservice.validator.birthdate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BirthDateValidator implements ConstraintValidator<BirthDateValue, String> {

  private static final String DATE_PATTERN = "MM/dd/yyyy";

  @Override
  public boolean isValid(String customDateField, ConstraintValidatorContext cxt) {
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
    try {
      sdf.setLenient(false);
      sdf.parse(customDateField);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }
}
