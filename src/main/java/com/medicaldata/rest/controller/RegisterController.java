package com.medicaldata.rest.controller;

import static org.springframework.http.ResponseEntity.status;

import com.medicaldata.rest.business.exceptions.InvalidMnemonicKeyException;
import com.medicaldata.rest.business.exceptions.OptinAlreadyDoneException;
import com.medicaldata.rest.data.model.Consent;
import com.medicaldata.rest.business.request.OptinRequest;
import com.medicaldata.rest.middleware.handler.RequestHandler;
import com.medicaldata.rest.middleware.converter.RequestConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(headers = "Accept=application/json")
public class RegisterController {

  private Logger logger = LoggerFactory.getLogger(RegisterController.class);
  private RequestHandler requestHandler;
  private RequestConverter requestConverter;

  public RegisterController(RequestHandler requestHandler,
                            RequestConverter requestConverter) {

    this.requestHandler = requestHandler;
    this.requestConverter = requestConverter;
  }

  @PostMapping("consent/survey/{appId}")
  public ResponseEntity<Void> consentConfirm(@PathVariable long appId, @RequestBody String mnemonic){
    logger.info("Incoming consent request");
    try{
        mnemonic = mnemonic.replace("\"","");
        Consent consent = new Consent(appId,true,new Date());
        OptinRequest request = new OptinRequest(mnemonic);
      requestHandler.optin(requestConverter.fromRequestToDomain(appId, request),consent);
        return ResponseEntity.ok().build();
    }
    catch (Exception ex)
    {
      logger.info(ex.getMessage());
      return ResponseEntity.status(403).build();
    }

  }

  @ExceptionHandler({OptinAlreadyDoneException.class})
  public ResponseEntity preconditionFailedExceptionHandler(RuntimeException e) {
    return status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
  }

  @ExceptionHandler({RuntimeException.class, InvalidMnemonicKeyException.class})
  public ResponseEntity serverError(RuntimeException e) {
    return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }
}
