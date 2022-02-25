package com.simonini.adidas.subscriptionapi;

import static com.simonini.adidas.subscriptionapi.domain.Gender.FEMALE;
import static com.simonini.adidas.subscriptionapi.domain.Gender.MALE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonini.adidas.subscriptionapi.repository.SubscriptionRepository;
import com.simonini.adidas.subscriptionapi.resource.dto.SubscriptionRequest;
import com.simonini.adidas.subscriptionapi.resource.dto.SubscriptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class SubscriptionResourceIntegratedTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private static final String SUBSCRIPTION_URL = "/subscriptions";

  @Autowired private SubscriptionRepository repository;

  private final SubscriptionRequest validRequest1 =
      SubscriptionRequest.builder()
          .email("email@email.com")
          .firstName("name")
          .gender(MALE)
          .dateOfBirth("10/13/2000")
          .newsletterId("newsletterId1")
          .consentSubscribe(TRUE)
          .build();

  private final SubscriptionRequest validRequest2 =
      SubscriptionRequest.builder()
          .email("other@email.com")
          .firstName("other")
          .gender(FEMALE)
          .dateOfBirth("10/16/2002")
          .newsletterId("newsletterId2")
          .consentSubscribe(TRUE)
          .build();

  @BeforeEach
  void clean() {
    repository.deleteAll();
  }

  @Test
  void shouldCreateNewSubscription() throws Exception {
    saveNewSubscription(validRequest1);
  }

  @Test
  void shouldNotCreateInvalidPayload() throws Exception {
    mockMvc
        .perform(
            post(SUBSCRIPTION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(SubscriptionRequest.builder().build())))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.statusCode", is(400)))
        .andExpect(jsonPath("$.errorMessages", hasSize(4)))
        .andExpect(
            jsonPath(
                "$.errorMessages",
                containsInAnyOrder(
                    "error on field [email]: [Email cannot be empty]",
                    "error on field [dateOfBirth]: [Not a valid [ MM/dd/yyyy ] birth date]",
                    "error on field [newsletterId]: [NewsletterId cannot be empty]",
                    "error on field [consentSubscribe]: [Consent to subscription cannot be empty]")));
  }

  @Test
  void shouldGetSubscriptionDetails() throws Exception {
    SubscriptionResponse subscriptionResponse = saveNewSubscription(validRequest1);

    mockMvc
        .perform(
            get(SUBSCRIPTION_URL + "/" + subscriptionResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.id", is(subscriptionResponse.getId())))
        .andExpect(jsonPath("$.firstName", is(subscriptionResponse.getFirstName())))
        .andExpect(jsonPath("$.email", is(subscriptionResponse.getEmail())))
        .andExpect(jsonPath("$.gender", is(subscriptionResponse.getGender())))
        .andExpect(jsonPath("$.dateOfBirth", notNullValue()))
        .andExpect(jsonPath("$.consentSubscribe", is(subscriptionResponse.isConsentSubscribe())))
        .andExpect(jsonPath("$.newsletterId", is(subscriptionResponse.getNewsletterId())))
        .andExpect(jsonPath("$.emailSent", is(FALSE)))
        .andExpect(jsonPath("$.canceled", is(FALSE)));
  }

  @Test
  void shouldGetNotFound() throws Exception {
    mockMvc
        .perform(
            get(SUBSCRIPTION_URL + "/" + "anyid")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldCancelSubscription() throws Exception {
    SubscriptionResponse subscriptionResponse = saveNewSubscription(validRequest1);

    mockMvc
        .perform(
            delete(SUBSCRIPTION_URL + "/" + subscriptionResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());

    mockMvc
        .perform(
            get(SUBSCRIPTION_URL + "/" + subscriptionResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.id", is(subscriptionResponse.getId())))
        .andExpect(jsonPath("$.canceled", is(TRUE)));
  }

  @Test
  void shouldNotCancelSubscriptionNotFound() throws Exception {
    mockMvc
        .perform(
            delete(SUBSCRIPTION_URL + "/" + "anyid")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldFindAllSubscriptionsPaged() throws Exception {
    mockMvc
        .perform(
            get(SUBSCRIPTION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.content", hasSize(0)));

    SubscriptionResponse subscriptionResponse1 = saveNewSubscription(validRequest1);
    SubscriptionResponse subscriptionResponse2 = saveNewSubscription(validRequest2);

    mockMvc
        .perform(
            get(SUBSCRIPTION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(
            jsonPath("$.content[?(@.id)].id")
                .value(
                    containsInAnyOrder(
                        subscriptionResponse1.getId(), subscriptionResponse2.getId())));
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
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.firstName", is(request.getFirstName())))
            .andExpect(jsonPath("$.email", is(request.getEmail())))
            .andExpect(jsonPath("$.gender", is(request.getGender().name())))
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
