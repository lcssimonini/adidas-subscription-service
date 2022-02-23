package com.simonini.adidas.emailservice.validator;

import com.simonini.adidas.emailservice.listener.dto.SendEmailRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendRequestValidator implements RequestValidator {

  private final SmartValidator validator;

  @Override
  public List<String> validateRequest(SendEmailRequest request) {
    Errors errors = new BeanPropertyBindingResult(request, request.getClass().getName());
    validator.validate(request, errors);
    return errors.getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());
  }
}
