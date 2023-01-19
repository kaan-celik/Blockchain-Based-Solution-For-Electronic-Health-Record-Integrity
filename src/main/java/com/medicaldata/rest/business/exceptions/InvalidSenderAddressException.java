package com.medicaldata.rest.business.exceptions;

public class InvalidSenderAddressException extends RuntimeException {

  public InvalidSenderAddressException(String message) {
    super("Something went wrong with sender address during transaction creation: " + message);
  }
}
