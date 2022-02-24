package com.simonini.adidas.emailservice.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.simonini.adidas.emailservice.listener.dto.SendEmailDLQResponse;
import com.simonini.adidas.emailservice.listener.dto.SendEmailRequest;
import com.simonini.adidas.emailservice.listener.dto.SendEmailResponse;

public interface SerializeHelper {

  SendEmailRequest getEmailRequestMessage(String emailRequest) throws JsonProcessingException;

  String getFeedbackMessageString(SendEmailResponse sendEmailResponse)
      throws JsonProcessingException;

  String getDLQMessageString(SendEmailDLQResponse sendEmailDLQResponse)
      throws JsonProcessingException;
}
