package com.simonini.adidas.subscriptionapi.integrations.email;

import com.simonini.adidas.subscriptionapi.integrations.email.dto.SendEmailRequest;
import com.simonini.adidas.subscriptionapi.integrations.email.dto.SendEmailResponse;

public interface SendEmailService {

  SendEmailResponse sendEmail(SendEmailRequest sendEmailRequest);
}
