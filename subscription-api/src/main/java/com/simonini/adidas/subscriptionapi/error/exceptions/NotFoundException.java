package com.simonini.adidas.subscriptionapi.error.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends RuntimeException {

  private final String message;
  private final HttpStatus httpStatus;

  public NotFoundException(String id) {
    this.httpStatus = HttpStatus.NOT_FOUND;
    this.message = "Subscription with id: " + id + " was not found";
  }
}
