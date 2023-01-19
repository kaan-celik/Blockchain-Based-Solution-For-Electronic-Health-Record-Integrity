package com.medicaldata.rest.middleware.handler;

import com.algorand.algosdk.crypto.Address;
import com.medicaldata.rest.business.request.OptinAppRequest;
import com.medicaldata.rest.business.exceptions.AlreadyVotedException;
import com.medicaldata.rest.business.exceptions.OptinAlreadyDoneException;
import com.medicaldata.rest.data.model.Consent;
import com.medicaldata.rest.data.model.Survey;
import com.medicaldata.rest.data.model.SurveyBlockchain;
import com.medicaldata.rest.data.repository.AlgorandRepository;
import com.medicaldata.rest.business.request.FillRequest;
import com.medicaldata.rest.data.repository.DatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public class RequestHandler {

  private final AlgorandRepository algorandRepository;
  private final DatabaseRepository databaseRepository;

  private Logger logger = LoggerFactory.getLogger(RequestHandler.class);

  public RequestHandler(AlgorandRepository algorandRepository , DatabaseRepository databaseRepository) {
    this.algorandRepository = algorandRepository;
    this.databaseRepository = databaseRepository;
  }

  public void fill(FillRequest fillRequest) throws NoSuchAlgorithmException {

    if(algorandRepository.isUserFilled(fillRequest.getAppId(), fillRequest.getAccount().getAddress())){
      throw new AlreadyVotedException(fillRequest.getAccount().getAddress().toString(), fillRequest.getAppId());
    }
    algorandRepository.fill(fillRequest);
  }

  public void optin(OptinAppRequest optinAppRequest, Consent consent) throws NoSuchAlgorithmException {

    try {
      if (algorandRepository.isUserRegistered(optinAppRequest)) {
        throw new OptinAlreadyDoneException(optinAppRequest.getAppId());
      }
      algorandRepository.register(optinAppRequest, consent);
    } catch (Exception ex) {
      throw ex;
    }
  }

  public List<SurveyBlockchain> retrieveSurveys() {
    return databaseRepository.find();
  }


  public Optional<SurveyBlockchain> findSurveyByApplicationId(long applicationId) {

    Optional<SurveyBlockchain> surveyBlockchain = databaseRepository.findBy(applicationId);
    return surveyBlockchain;
  }

  public SurveyBlockchain create(Survey poolTemp) {
    SurveyBlockchain surveyBlockchain = algorandRepository.save(poolTemp);

    logger.info(String.format("Survey has been created with application id: %s", surveyBlockchain.getAppId()));

    databaseRepository.save(surveyBlockchain);

    return surveyBlockchain;
  }

  public Long createTokenTx(String mnemonicKey) {
    return algorandRepository.createSecurityToken(mnemonicKey);
  }

  public Long sendTokenTx(String mnemonicKey, Address address) {
    return algorandRepository.sendSecurityToken(mnemonicKey, address);
  }

  public Long revokeTokenTx(String mnemonicKey, Address address) {
    return algorandRepository.revokeSecurityToken(mnemonicKey, address);
  }

  public Long freezeTokenTx(String mnemonicKey, Address address) {
    return algorandRepository.freezeSecurityToken(mnemonicKey, address);
  }

}
