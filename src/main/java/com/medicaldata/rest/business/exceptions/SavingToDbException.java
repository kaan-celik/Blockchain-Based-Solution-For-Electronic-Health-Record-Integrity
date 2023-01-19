package com.medicaldata.rest.business.exceptions;

public class SavingToDbException extends RuntimeException {

  public SavingToDbException(String name, String message) {
    super(String.format("An error occours while inserting the survey:%s. %s.", name, message));
  }
}
