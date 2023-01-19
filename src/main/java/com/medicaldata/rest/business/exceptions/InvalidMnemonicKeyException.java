package com.medicaldata.rest.business.exceptions;

public class InvalidMnemonicKeyException extends RuntimeException {

  public InvalidMnemonicKeyException(String message) {
    super(message);
  }
}
