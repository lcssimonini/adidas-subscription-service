package com.simonini.adidas.emailservice.validator;

import com.simonini.adidas.emailservice.listener.dto.SendEmailRequest;
import java.util.List;

public interface RequestValidator {

  List<String> validateRequest(SendEmailRequest request);
}
