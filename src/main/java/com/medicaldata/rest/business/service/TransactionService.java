package com.medicaldata.rest.business.service;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.util.Encoder;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse;
import com.algorand.algosdk.v2.client.model.PostTransactionsResponse;
import com.medicaldata.rest.business.SmartContractFactory;
import com.medicaldata.rest.data.model.Survey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.medicaldata.rest.business.AlgorandUtils;
import com.medicaldata.rest.business.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;

public class TransactionService {

  private Logger logger = LoggerFactory.getLogger(TransactionService.class);

  private AlgodClient algodClient;
  private SmartContractFactory smartContractFactory;
  private TransactionBuilder transactionBuilder;

  public TransactionService(AlgodClient algodClient, SmartContractFactory smartContractFactory, TransactionBuilder transactionBuilder) {
    this.algodClient = algodClient;
    this.transactionBuilder = transactionBuilder;
    this.smartContractFactory = smartContractFactory;
  }

  public Transaction createUnsignedTxFor(Survey survey, Account account) {

    Long lastRound = getBlockChainLastRound();

    TEALProgram approvalProgram = smartContractFactory.createApprovalProgramFrom(survey);
    TEALProgram clearStateProgram = smartContractFactory.createClearStateProgram();

    return transactionBuilder
            .buildTransaction(survey, approvalProgram, clearStateProgram, account.getAddress().toString());
  }

  public byte[] sign(Transaction unsignedTx, Account account)
          throws JsonProcessingException, NoSuchAlgorithmException {

    SignedTransaction signedTx = account.signTransaction(unsignedTx);

    return Encoder.encodeToMsgPack(signedTx);
  }

  public String send(byte[] encodedTxBytes) {
    Response<PostTransactionsResponse> response;

    try {
      response = algodClient.RawTransaction().rawtxn(encodedTxBytes)
              .execute(AlgorandUtils.txHeaders, AlgorandUtils.txValues);
    } catch (Exception e) {
      throw  new AlgorandInteractionError(e.getMessage());
    }

    if(response.code() == 200){
      return response.body().txId;
    }

    logger.error("Sending transaction to algorand blockchain response has code {}. Message {}", response.code(), response.message());
    throw new AlgorandInteractionError(response.code(), response.message());

  }

  public void waitForConfirmation(String txID) {

    try {

      Long lastRound = this.algodClient.GetStatus().execute(AlgorandUtils.headers, AlgorandUtils.values)
              .body().lastRound;
      while (true) {

        // Check the pending transactions
        Response<PendingTransactionResponse> pendingInfo = algodClient
                .PendingTransactionInformation(txID)
                .execute(AlgorandUtils.headers, AlgorandUtils.values);
        if (pendingInfo.body().confirmedRound != null && pendingInfo.body().confirmedRound > 0) {
          // Got the completed Transaction
          System.out.println(
                  "Transaction " + txID + " confirmed in round " + pendingInfo.body().confirmedRound);
          break;
        }
        lastRound++;
        algodClient.WaitForBlock(lastRound).execute(AlgorandUtils.headers, AlgorandUtils.values);
      }
    } catch (Exception e) {
      throw new WaitingTransactionConfirmationException(e, txID);
    }
  }

  public String write(Account account, Transaction unsignedTransaction) {

    try {
      byte[] signedTxBytes = sign(unsignedTransaction, account);
      String transactionId = send(signedTxBytes);
      waitForConfirmation(transactionId);
      return transactionId;

    } catch (JsonProcessingException e) {
      logger.error("Something goes wrong encoding transaction for account with address {}",
              account.getAddress().toString(), e);
      throw new EncodeTransactionException(e.getMessage());
    } catch (NoSuchAlgorithmException e) {
      logger.error("Something goes wrong signing transaction for account with address {}",
              account.getAddress().toString(), e);
      throw new SignTransactionException(e.getMessage());
    }  catch (Exception e) {
      logger.error("Something goes wrong sending transaction for account with address {}", account.getAddress().toString(), e);
      throw new SendingTransactionException(e.getMessage());
    }
  }


  private Long getBlockChainLastRound() {

    Long lastRound;
    try {
      lastRound = algodClient.GetStatus().execute(AlgorandUtils.headers, AlgorandUtils.values).body().lastRound;
    } catch (Exception e) {
      logger.error("Something goes wrong getting last blockchain round", e);
      throw new NodeStatusException(e.getMessage());
    }
    return lastRound;
  }
}