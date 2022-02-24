package com.simonini.adidas.emailservice.sender;

import com.simonini.adidas.emailservice.listener.dto.SendEmailRequest;
import java.util.List;

public interface EmailDLQSender {

  void sendDLQMessage(SendEmailRequest request, List<String> errorMessages);
}
