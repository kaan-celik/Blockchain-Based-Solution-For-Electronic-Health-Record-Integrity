package com.medicaldata.rest.data.repository;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Address;
import com.medicaldata.rest.business.request.FillRequest;
import com.medicaldata.rest.business.request.OptinAppRequest;
import com.medicaldata.rest.data.model.Consent;
import com.medicaldata.rest.data.model.Survey;
import com.medicaldata.rest.data.model.SurveyBlockchain;

import java.security.NoSuchAlgorithmException;

public interface AlgorandRepository {

    SurveyBlockchain save(Survey survey);

    Account getUserAccount (String mnemonicKey);

    Long createSecurityToken (String mnemonicKey);

    Long sendSecurityToken(String mnemonicKey, Address receiverAddress);

    Long revokeSecurityToken(String mnemonicKey, Address revokeAddress);

    Long freezeSecurityToken(String mnemonicKey, Address receiverAddress);

    Long optInSecurityToken(String mnemonicKey);

    Boolean isUserRegistered(OptinAppRequest optinAppRequest);

    Boolean isUserFilled(long appId, Address address);

    void register(OptinAppRequest optinAppRequest, Consent consent) throws NoSuchAlgorithmException;

    void fill(FillRequest fillRequest) throws NoSuchAlgorithmException;
}
