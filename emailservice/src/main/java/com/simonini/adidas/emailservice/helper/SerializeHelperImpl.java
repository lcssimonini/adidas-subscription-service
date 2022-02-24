package com.simonini.adidas.emailservice.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonini.adidas.emailservice.listener.dto.SendEmailDLQResponse;
import com.simonini.adidas.emailservice.listener.dto.SendEmailRequest;
import com.simonini.adidas.emailservice.listener.dto.SendEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SerializeHelperImpl implements SerializeHelper {

  private final ObjectMapper objectMapper;

  @Override
  public SendEmailRequest getEmailRequestMessage(String emailRequest)
      throws JsonProcessingException {
    return objectMapper.readValue(emailRequest, SendEmailRequest.class);
  }

  @Override
  public String getFeedbackMessageString(SendEmailResponse sendEmailResponse)
      throws JsonProcessingException {
    return objectMapper.writeValueAsString(sendEmailResponse);
  }

  @Override
  public String getDLQMessageString(SendEmailDLQResponse sendEmailDLQResponse)
      throws JsonProcessingException {
    return objectMapper.writeValueAsString(sendEmailDLQResponse);
  }
}
