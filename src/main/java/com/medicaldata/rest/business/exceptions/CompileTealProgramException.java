package com.medicaldata.rest.business.exceptions;

public class CompileTealProgramException extends RuntimeException {

  public CompileTealProgramException(Exception e) {
    super("Something goes wrong during TEAL program compilation: " + e.getMessage(), e);
  }
}
