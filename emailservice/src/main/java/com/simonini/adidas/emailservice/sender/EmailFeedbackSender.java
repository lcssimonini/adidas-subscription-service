package com.simonini.adidas.emailservice.sender;

import com.simonini.adidas.emailservice.listener.dto.SendEmailRequest;

public interface EmailFeedbackSender {

  void sendEmailFeedback(SendEmailRequest sendEmailRequest);
}
