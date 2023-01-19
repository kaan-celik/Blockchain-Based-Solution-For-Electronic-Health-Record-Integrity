package com.medicaldata.rest.controller;

import static org.springframework.http.ResponseEntity.status;

import com.medicaldata.rest.business.exceptions.AlgorandInteractionError;
import com.medicaldata.rest.data.model.SurveyBlockchain;

import java.util.List;
import java.util.Optional;

import com.medicaldata.rest.middleware.handler.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(headers = "Accept=application/json")
public class ListController {

  private Logger logger = LoggerFactory.getLogger(ListController.class);

  private RequestHandler requestHandler;

  public ListController(
          RequestHandler requestHandler) {
    this.requestHandler = requestHandler;
  }

  @GetMapping("/surveys")
  public ResponseEntity<List<SurveyBlockchain>> getSurveys() {

    logger.info("Request: All surveys will be retrieve...");
    return ResponseEntity.ok(requestHandler.retrieveSurveys());
  }

  @GetMapping("/surveys/{appId}")
  public ResponseEntity<Optional<SurveyBlockchain>> getSurveyById(@PathVariable long appId) {

    logger.info("Request: Survey " + appId + " will be retrieve");
    return ResponseEntity.ok(requestHandler.findSurveyByApplicationId(appId));
  }


  @ExceptionHandler(AlgorandInteractionError.class)
  public ResponseEntity serverError(RuntimeException e) {
    logger.error("An error occurred.", e);
    return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }

}
