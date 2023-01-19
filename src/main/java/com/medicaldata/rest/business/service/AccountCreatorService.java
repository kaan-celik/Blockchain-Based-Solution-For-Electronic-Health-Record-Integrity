package com.medicaldata.rest.business.service;


import com.algorand.algosdk.account.Account;
import com.medicaldata.rest.business.exceptions.InvalidMnemonicKeyException;
import java.security.GeneralSecurityException;

public class AccountCreatorService {

  public Account createAccountFrom(String mnemonicKey){

    try {
      return new Account(mnemonicKey);
    } catch (GeneralSecurityException e) {
      throw new InvalidMnemonicKeyException(e.getMessage());
    }
  }
}
