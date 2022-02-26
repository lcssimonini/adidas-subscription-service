package com.simonini.adidas.publicservice.resource;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonini.adidas.publicservice.integrations.subscriptions.resource.dto.SubscriptionRequest;
import com.simonini.adidas.publicservice.integrations.subscriptions.resource.dto.SubscriptionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class SubscriptionResourceTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private static final String SUBSCRIPTION_URL = "/subscriptions";

  private final SubscriptionRequest validRequest1 =
      SubscriptionRequest.builder()
          .email("email@email.com")
          .firstName("name")
          .gender("MALE")
          .dateOfBirth("10/13/2000")
          .newsletterId("newsletterId1")
          .consentSubscribe(TRUE)
          .build();

  @Test
  void shouldCreateNewSubscription() throws Exception {
    saveNewSubscription(validRequest1);
  }

  @Test
  void shouldGetSubscriptionDetails() throws Exception {
    mockMvc
        .perform(
            get(SUBSCRIPTION_URL + "/subscriptionId")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.id", is("subscriptionId")))
        .andExpect(jsonPath("$.firstName", is("name")))
        .andExpect(jsonPath("$.email", is("email@email.com")))
        .andExpect(jsonPath("$.gender", is("MALE")))
        .andExpect(jsonPath("$.dateOfBirth", notNullValue()))
        .andExpect(jsonPath("$.consentSubscribe", is(true)))
        .andExpect(jsonPath("$.newsletterId", is("newsletterId1")))
        .andExpect(jsonPath("$.emailSent", is(FALSE)))
        .andExpect(jsonPath("$.canceled", is(FALSE)));
  }

  @Test
  void shouldCancelSubscription() throws Exception {
    mockMvc
        .perform(
            delete(SUBSCRIPTION_URL + "/subscriptionId")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  void shouldFindAllSubscriptionsPaged() throws Exception {
    mockMvc
        .perform(
            get(SUBSCRIPTION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.content", hasSize(20)))
        .andExpect(jsonPath("$.totalPages", is(1)));
  }

  private SubscriptionResponse saveNewSubscription(SubscriptionRequest request) throws Exception {
    return objectMapper.readValue(
        mockMvc
            .perform(
                post(SUBSCRIPTION_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is("subscriptionId")))
            .andExpect(jsonPath("$.firstName", is(request.getFirstName())))
            .andExpect(jsonPath("$.email", is(request.getEmail())))
            .andExpect(jsonPath("$.gender", is(request.getGender())))
            .andExpect(jsonPath("$.dateOfBirth", notNullValue()))
            .andExpect(jsonPath("$.consentSubscribe", is(request.getConsentSubscribe())))
            .andExpect(jsonPath("$.newsletterId", is(request.getNewsletterId())))
            .andExpect(jsonPath("$.emailSent", is(FALSE)))
            .andExpect(jsonPath("$.canceled", is(FALSE)))
            .andReturn()
            .getResponse()
            .getContentAsString(),
        SubscriptionResponse.class);
  }
}
