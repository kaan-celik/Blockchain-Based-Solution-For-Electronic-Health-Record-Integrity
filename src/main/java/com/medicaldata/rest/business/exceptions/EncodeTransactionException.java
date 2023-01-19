package com.medicaldata.rest.business.exceptions;

public class EncodeTransactionException extends RuntimeException {

  public EncodeTransactionException(String errorMessage) {
    super(String.format("Impossible to encode the transaction. %s ", errorMessage));
  }
}
