package com.medicaldata.rest.business.service;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.logic.StateSchema;
import com.algorand.algosdk.transaction.Transaction;
import com.medicaldata.rest.business.exceptions.BlockChainParameterException;
import com.medicaldata.rest.business.exceptions.InvalidSenderAddressException;
import com.medicaldata.rest.business.request.SurveyRequest;
import com.medicaldata.rest.data.AES;
import com.medicaldata.rest.data.model.Consent;
import com.medicaldata.rest.data.model.Survey;
import com.medicaldata.rest.business.request.FillRequest;
import com.medicaldata.rest.business.request.OptinAppRequest;
import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

public class TransactionBuilder {

    public static final int STATIC_GLOBAL_VARIABLES_NUMBER = 64;
    public static final int STATIC_LOCAL_VARIABLES_NUMBER = 3;
    public static final String APPLICATION_TAG = "[algomedapplication] ";

    private Logger logger = LoggerFactory.getLogger(TransactionBuilder.class);

    private SmartContractUtilService smartContractUtilService;

    public TransactionBuilder(SmartContractUtilService smartContractUtilService) {
        this.smartContractUtilService = smartContractUtilService;
    }

    public Transaction buildTransaction(Survey survey,
                                        TEALProgram approvalProgram, TEALProgram clearStateProgram, String sender) {
        Transaction transaction;
        try {
            transaction = Transaction.ApplicationCreateTransactionBuilder()
                    .sender(sender)
                    .note(Bytes.concat(APPLICATION_TAG.getBytes(StandardCharsets.UTF_8), survey.getName().getBytes(StandardCharsets.UTF_8)))
                    .args(asList(new Date().toString().getBytes(StandardCharsets.UTF_8)))
                    .suggestedParams(smartContractUtilService.getParameters())
                    .approvalProgram(approvalProgram)
                    .clearStateProgram(clearStateProgram)
                    .foreignAssets(new ArrayList<Long>(Arrays.asList(105379867L)))
                    .globalStateSchema(new StateSchema(
                            0 , STATIC_GLOBAL_VARIABLES_NUMBER))
                    .localStateSchema(new StateSchema(0, STATIC_LOCAL_VARIABLES_NUMBER))
                    .build();
        } catch (IllegalArgumentException e) {
            logger.error("Something goes wrong with Sender Address transaction", e);
            throw new InvalidSenderAddressException(e.getMessage());
        } catch (Exception e) {
            logger.error("Something goes wrong getting blockchain parameters transaction", e);
            throw new BlockChainParameterException(e.getMessage());
        }
        return transaction;
    }


    public Transaction buildTransaction(OptinAppRequest optinAppRequest, Consent consent) throws NoSuchAlgorithmException {
        return Transaction.ApplicationOptInTransactionBuilder()
                .suggestedParams(smartContractUtilService.getParameters())
                .sender(optinAppRequest.getAccount().getAddress()).
                args(getContractArgs(consent))
                .applicationId(optinAppRequest.getAppId())
                .foreignAssets(new ArrayList<Long>(Arrays.asList(105379867L)))
                .build();
    }

    public Transaction buildTransaction(FillRequest voteAppRequest) throws NoSuchAlgorithmException {
        return Transaction.ApplicationCallTransactionBuilder()
                .suggestedParams(smartContractUtilService.getParameters())
                .sender(voteAppRequest.getAccount().getAddress())
                .args(getContractArgs(voteAppRequest.getFullSurvey()))
                .applicationId(voteAppRequest.getAppId())
                .foreignAssets(new ArrayList<Long>(Arrays.asList(105379867L)))
                .build();
    }


    private List<byte[]> getContractArgs(SurveyRequest survey) throws NoSuchAlgorithmException {
        byte[] jsonData = survey.Hash(survey.convertToJson());
        byte[] encryptedData = AES.encrypt(survey.FormatTheAnswers());
        return asList(jsonData,encryptedData);
    }

    private List<byte[]> getContractArgs(Consent consent) throws NoSuchAlgorithmException {
        byte[] jsonData = consent.Hash(consent.convertToJson());
        return asList(jsonData);
    }
}
