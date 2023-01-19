package com.medicaldata.rest.controller;

import static org.springframework.http.ResponseEntity.status;

import com.algorand.algosdk.crypto.Address;
import com.medicaldata.rest.business.entities.BodyObject;
import com.medicaldata.rest.data.model.SurveyBlockchain;
import com.medicaldata.rest.data.model.Survey;
import com.medicaldata.rest.business.request.SurveyRequest;
import com.medicaldata.rest.middleware.handler.RequestHandler;
import com.medicaldata.rest.middleware.converter.RequestConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(headers = "Accept=application/json")
public class CreateController {

  private RequestHandler requestHandler;
  private RequestConverter requestConverter;

  private Logger logger = LoggerFactory.getLogger(CreateController.class);

  public CreateController(RequestHandler requestHandler, RequestConverter requestConverter) {
    this.requestHandler = requestHandler;
    this.requestConverter = requestConverter;
  }

  @PostMapping("/token/create")
  public ResponseEntity<Long> createToken(@RequestBody String mnemonic) {

    logger.info("TokenCreation - Arrived request");
    Long assetId = requestHandler.createTokenTx(mnemonic);
    return ResponseEntity.ok(assetId);
  }

  @PostMapping("/token/send")
  public ResponseEntity<Long> sendToken(@RequestBody BodyObject data) throws NoSuchAlgorithmException {
    logger.info("Token Sending - Arrived request");
    Address receiverAddress = new Address(data.address);
    Long something = requestHandler.sendTokenTx(data.mnemonic, receiverAddress);
    return  ResponseEntity.ok(something);

  }

  @PostMapping("/token/revoke")
  public ResponseEntity<Long> revokeToken(@RequestBody BodyObject data) throws NoSuchAlgorithmException {
    logger.info("Token Sending - Arrived request");
    Address receiverAddress = new Address(data.address);
    Long something = requestHandler.revokeTokenTx(data.mnemonic,receiverAddress);
    return  ResponseEntity.ok(something);

  }

  @PostMapping("/token/freeze")
  public ResponseEntity<Long> freezeToken(@RequestBody BodyObject data) throws NoSuchAlgorithmException {
    logger.info("Token Sending - Arrived request");
    Address receiverAddress = new Address(data.address);
    Long something = requestHandler.freezeTokenTx(data.mnemonic,receiverAddress);
    return  ResponseEntity.ok(something);

  }

  @PostMapping("/survey/create")
  public ResponseEntity<SurveyBlockchain> createSurveyTransaction(@RequestBody SurveyRequest surveyRequest) {

    logger.info("SurveyRequest - Arrived request");
    Survey survey = requestConverter.fromRequestToDomain(surveyRequest);
    logger.info("Request adapted to survey object");
    SurveyBlockchain createdSurvey = requestHandler.create(survey);
    return ResponseEntity.ok(createdSurvey);
  }

  @ExceptionHandler(value = {RuntimeException.class})
  public ResponseEntity genericErrorExceptionHandler(RuntimeException e){
    logger.error("An error occurred. " , e);
    return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }
}
