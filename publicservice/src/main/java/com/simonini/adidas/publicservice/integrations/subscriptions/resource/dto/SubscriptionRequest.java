package com.simonini.adidas.publicservice.integrations.subscriptions.resource.dto;

import com.simonini.adidas.publicservice.domain.Gender;
import com.simonini.adidas.publicservice.validator.BirthDateValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequest {

    private String firstName;
    private Gender gender;
    @Email(message = "Wrong email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @BirthDateValue(message = "Not a valid [ MM/dd/yyyy ] birth date")
    private String dateOfBirth;
    @NotNull(message = "Consent to subscription cannot be empty")
    private Boolean consentSubscribe;
    @NotBlank(message = "NewsletterId cannot be empty")
    private String newsletterId;
}

