package com.medicaldata.rest.business.exceptions;

public class SignTransactionException extends RuntimeException {

  public SignTransactionException(String errorMessage) {
    super(String.format("Impossible to sign the transaction. %s", errorMessage));
  }
}
