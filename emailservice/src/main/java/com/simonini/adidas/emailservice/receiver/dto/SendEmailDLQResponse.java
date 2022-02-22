package com.simonini.adidas.emailservice.receiver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailDLQResponse implements Serializable {

    private String entityId;
    private List<String> errorMessages;
}
