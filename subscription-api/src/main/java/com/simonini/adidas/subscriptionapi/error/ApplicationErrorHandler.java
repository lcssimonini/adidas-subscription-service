package com.simonini.adidas.subscriptionapi.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.status;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.simonini.adidas.subscriptionapi.error.exceptions.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationErrorHandler {

  public static final String EXCEPTION_LOG_MESSAGE = "An exception has occurred";
  public static final String FIELD_ERROR_MESSAGE_TEMPLATE = "error on field [%s]: [%s]";

  @ExceptionHandler(InvalidFormatException.class)
  public ResponseEntity<ErrorResponse> handleInvalidFormatException(
      final InvalidFormatException exception) {
    ResponseEntity<ErrorResponse> error = badRequest().body(getErrorResponse(exception));
    log.error(EXCEPTION_LOG_MESSAGE, exception);
    return error;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      final MethodArgumentNotValidException exception) {
    ResponseEntity<ErrorResponse> error = badRequest().body(getErrorResponse(exception));
    log.error(EXCEPTION_LOG_MESSAGE, exception);
    return error;
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException exception) {
    ResponseEntity<ErrorResponse> error = status(NOT_FOUND).body(getErrorResponse(exception));
    log.error(EXCEPTION_LOG_MESSAGE, exception);
    return error;
  }

  private ErrorResponse getErrorResponse(InvalidFormatException exception) {
    return ErrorResponse.builder()
        .statusCode(BAD_REQUEST.value())
        .errorMessages(List.of(exception.getMessage()))
        .build();
  }

  private ErrorResponse getErrorResponse(NotFoundException exception) {
    return ErrorResponse.builder()
        .statusCode(exception.getHttpStatus().value())
        .errorMessages(List.of(exception.getMessage()))
        .build();
  }

  private ErrorResponse getErrorResponse(MethodArgumentNotValidException exception) {
    return ErrorResponse.builder()
        .statusCode(BAD_REQUEST.value())
        .errorMessages(getFieldErrorMessages(exception))
        .build();
  }

  private List<String> getFieldErrorMessages(MethodArgumentNotValidException exception) {
    return exception.getBindingResult().getFieldErrors().stream()
        .map(this::getFormattedMessage)
        .collect(Collectors.toList());
  }

  private String getFormattedMessage(FieldError fieldError) {
    return String.format(
        FIELD_ERROR_MESSAGE_TEMPLATE, fieldError.getField(), fieldError.getDefaultMessage());
  }
}
