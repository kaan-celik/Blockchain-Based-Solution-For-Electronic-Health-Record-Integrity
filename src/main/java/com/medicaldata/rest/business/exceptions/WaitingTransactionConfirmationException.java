package com.medicaldata.rest.business.exceptions;

public class WaitingTransactionConfirmationException extends RuntimeException {

  public WaitingTransactionConfirmationException(Exception e, String transactionId) {
    super(String.format("Impossible to wait the transaction confirmation for transaction with id %s. %s ", transactionId, e.getMessage()), e);
  }
}
