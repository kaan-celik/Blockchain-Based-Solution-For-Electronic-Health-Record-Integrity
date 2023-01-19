package com.medicaldata.rest.business.exceptions;

public class BlockChainParameterException extends RuntimeException {

  public BlockChainParameterException(String message) {
    super("Something goes wrong building transaction: "+ message);
  }
}
