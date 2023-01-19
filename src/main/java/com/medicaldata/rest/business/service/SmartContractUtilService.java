package com.medicaldata.rest.business.service;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import com.medicaldata.rest.business.exceptions.RetrievingApplicationIdException;
import com.medicaldata.rest.business.AlgorandUtils;

public class SmartContractUtilService {

  private final AlgodClient algodClient;

  public SmartContractUtilService(AlgodClient algodClient) {
    this.algodClient = algodClient;
  }

  public Long getApplicationId(String transactionId) {
    PendingTransactionResponse pTrx;
    try {
      pTrx = algodClient.PendingTransactionInformation(transactionId)
          .execute(AlgorandUtils.headers, AlgorandUtils.values).body();
    } catch (Exception e) {
      throw new RetrievingApplicationIdException(e, transactionId);
    }
    return pTrx.applicationIndex;
  }

  public Long getAssetId(String transactionId) {
    PendingTransactionResponse pTrx;
    try {
      pTrx = algodClient.PendingTransactionInformation(transactionId)
              .execute(AlgorandUtils.headers, AlgorandUtils.values).body();
    } catch (Exception e) {
      throw new RetrievingApplicationIdException(e, transactionId);
    }
    return pTrx.assetIndex;
  }

  public TransactionParametersResponse getParameters() {
    try {
      return algodClient.TransactionParams().execute(AlgorandUtils.headers, AlgorandUtils.values).body();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}