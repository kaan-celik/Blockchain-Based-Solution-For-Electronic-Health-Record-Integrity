package com.medicaldata.rest.controller;

import static org.springframework.http.ResponseEntity.status;

import com.medicaldata.rest.business.exceptions.VoteException;
import com.medicaldata.rest.business.request.SurveyRequest;
import com.medicaldata.rest.business.exceptions.AlreadyVotedException;
import com.medicaldata.rest.middleware.handler.RequestHandler;
import com.medicaldata.rest.middleware.converter.RequestConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(headers = "Accept=application/json")
public class FillController {

  private Logger logger = LoggerFactory.getLogger(FillController.class);

  private final RequestHandler requestHandler;
  private final RequestConverter requestConverter;


  public FillController(RequestHandler requestHandler, RequestConverter requestConverter) {

    this.requestHandler = requestHandler;
    this.requestConverter = requestConverter;
  }

  @PostMapping("/fill/survey/{appId}")
  public ResponseEntity<Void> fill(@PathVariable long appId, @RequestBody SurveyRequest fillRequest) throws NoSuchAlgorithmException {
    logger.info("Request: Filling request for application {}", appId);
    requestHandler.fill(requestConverter.fromRequestToDomain(appId, fillRequest));
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(value = {AlreadyVotedException.class})
  public ResponseEntity preconditionFailedExceptionHandler(RuntimeException e) {
    logger.error("An error occurred.", e);
    return status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
  }


  @ExceptionHandler(value = {RuntimeException.class, VoteException.class})
  public ResponseEntity genericErrorHandler(RuntimeException e) {
    logger.error("Something went wrong voting the app. " , e);
    return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }
}
