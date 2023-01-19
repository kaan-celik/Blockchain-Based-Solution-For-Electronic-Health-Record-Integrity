package com.medicaldata.rest.data.repository;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.AccountResponse;
import com.medicaldata.rest.business.entities.SecurityToken;
import com.medicaldata.rest.business.service.*;
import com.medicaldata.rest.business.exceptions.AlgorandInteractionError;
import com.medicaldata.rest.business.exceptions.InvalidMnemonicKeyException;
import com.medicaldata.rest.business.request.FillRequest;
import com.medicaldata.rest.business.request.OptinAppRequest;
import com.medicaldata.rest.data.model.Consent;
import com.medicaldata.rest.data.model.Survey;
import com.medicaldata.rest.data.model.SurveyBlockchain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.medicaldata.rest.business.AlgorandUtils.headers;
import static com.medicaldata.rest.business.AlgorandUtils.values;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AlgorandRepositoryImp implements AlgorandRepository {

    private final TransactionBuilder transactionBuilder;
    private final TransactionService transactionService;
    private AccountCreatorService accountCreatorService;
    private SmartContractUtilService smartContractUtilService;
    private IndexerClient indexerClient;

    private Logger logger = LoggerFactory.getLogger(AlgorandRepositoryImp.class);

    public AlgorandRepositoryImp(TransactionBuilder transactionBuilder, TransactionService transactionService,
                                 AccountCreatorService accountCreatorService,
                                 SmartContractUtilService smartContractUtilService,
                                 IndexerClient indexerClient) {
        this.transactionBuilder = transactionBuilder;
        this.transactionService = transactionService;
        this.accountCreatorService = accountCreatorService;
        this.smartContractUtilService = smartContractUtilService;
        this.indexerClient = indexerClient;
    }

    @Override
    public SurveyBlockchain save(Survey survey) {
        try {
            Account account = accountCreatorService.createAccountFrom(survey.getPassword());

            Transaction unsignedTx = transactionService.createUnsignedTxFor(survey, account);

            String transactionId = transactionService.write(account, unsignedTx);

            Long appId = smartContractUtilService.getApplicationId(transactionId);

            return adaptSurveyToContract(survey, appId);

        } catch (Exception e) {
            logger.error("An error occurred while saving survey: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Account getUserAccount(String mnemonicKey) {
        return accountCreatorService.createAccountFrom(mnemonicKey);
    }

    @Override
    public Long createSecurityToken(String mnemonicKey) {
        Account account = accountCreatorService.createAccountFrom(mnemonicKey);
        SecurityToken securityToken = new SecurityToken(account.getAddress());
        SecurityTokenService securityTokenService = new SecurityTokenService(securityToken, smartContractUtilService);
        Transaction unsignedTransaction = securityTokenService.CreateToken();
        String transactionId = transactionService.write(account, unsignedTransaction);
        Long assetId = smartContractUtilService.getAssetId(transactionId);
        return assetId;
    }

    @Override
    public Long sendSecurityToken(String mnemonicKey, Address receiverAddress) {
        Account senderAccount = accountCreatorService.createAccountFrom(mnemonicKey);
        SecurityToken securityToken = new SecurityToken(senderAccount.getAddress(),receiverAddress,"");
        SecurityTokenService securityTokenService = new SecurityTokenService(securityToken, smartContractUtilService);
        Transaction unsignedTransaction = securityTokenService.transferToken(105379867,1);
        String transactionId = transactionService.write(senderAccount, unsignedTransaction);
        Long assetId = smartContractUtilService.getAssetId(transactionId);
        return assetId;
    }

    @Override
    public Long revokeSecurityToken(String mnemonicKey, Address receiverAddress){
        Account senderAccount = accountCreatorService.createAccountFrom(mnemonicKey);
        SecurityToken securityToken = new SecurityToken(senderAccount.getAddress(),receiverAddress);
        SecurityTokenService securityTokenService = new SecurityTokenService(securityToken, smartContractUtilService);
        Transaction unsignedTransaction = securityTokenService.clawbackToken(105379867);
        String transactionId = transactionService.write(senderAccount, unsignedTransaction);
        Long assetId = smartContractUtilService.getAssetId(transactionId);
        return assetId;
    }

    @Override
    public Long freezeSecurityToken(String mnemonicKey, Address receiverAddress){
        Account senderAccount = accountCreatorService.createAccountFrom(mnemonicKey);
        SecurityToken securityToken = new SecurityToken(senderAccount.getAddress(),receiverAddress);
        SecurityTokenService securityTokenService = new SecurityTokenService(securityToken, smartContractUtilService);
        Transaction unsignedTransaction = securityTokenService.freezeToken(105379867, true);
        String transactionId = transactionService.write(senderAccount, unsignedTransaction);
        Long assetId = smartContractUtilService.getAssetId(transactionId);
        return assetId;
    }

    @Override
    public Long optInSecurityToken(String mnemonicKey){

        Account senderAccount = accountCreatorService.createAccountFrom(mnemonicKey);
        SecurityToken securityToken = new SecurityToken(senderAccount.getAddress());
        SecurityTokenService securityTokenService = new SecurityTokenService(securityToken, smartContractUtilService);
        Transaction unsignedTransaction = securityTokenService.optInToken(105379867);
        String transactionId = transactionService.write(senderAccount, unsignedTransaction);
        Long assetId = smartContractUtilService.getAssetId(transactionId);
        return assetId;

    }

    @Override
    public Boolean isUserRegistered(OptinAppRequest optinAppRequest) {
        Response<AccountResponse> response;

        try {
            response = indexerClient
                    .lookupAccountByID(optinAppRequest.getAccount().getAddress()).execute(headers, values);
        } catch (Exception e) {
            throw new AlgorandInteractionError(e.getMessage());
        }

        if (response.code() == 200) {
            return response.body().account.appsLocalState.stream()
                    .anyMatch(app -> app.id == optinAppRequest.getAppId());
        } else {
            logger.error(
                    "An error occurs about user registration information. Code: {}. Error: {}. Application: {}. User Address {}",
                    response.code(), response.message(), optinAppRequest.getAppId(),
                    optinAppRequest.getAccount().getAddress().toString());
            throw new AlgorandInteractionError(response.code(), response.message());
        }
    }

    @Override
    public Boolean isUserFilled(long appId, Address address) {
        Response<AccountResponse> response;
        try {
            response = indexerClient.lookupAccountByID(address)
                    .execute(headers, values);
        } catch (Exception e) {
            throw new AlgorandInteractionError(e.getMessage());
        }

        if (response.code() == 200) {
            return response.body().account.appsLocalState.stream().filter(app -> app.id.equals(appId))
                    .anyMatch(app -> app.keyValue.stream()
                            .anyMatch(tealKeyValue -> tealKeyValue.key.equals(Base64.getEncoder()
                                    .encodeToString("Completed".getBytes(UTF_8)))));
        }

        throw new AlgorandInteractionError(response.code(), response.message());
    }

    @Override
    public void register(OptinAppRequest optinAppRequest, Consent consent) throws NoSuchAlgorithmException {
        try {
            Transaction transaction = transactionBuilder
                    .buildTransaction(optinAppRequest,consent);
            transactionService.write(optinAppRequest.getAccount(), transaction);
        } catch (Exception e) {
            logger.error("An error occurred while sending the transaction for the user address {} with application {} ",
                    optinAppRequest.getAccount().getAddress().toString(), optinAppRequest.getAppId(), e);
            throw e;
        }
    }

    @Override
    public void fill(FillRequest fillRequest) throws NoSuchAlgorithmException {
        try {
            Transaction transaction = transactionBuilder
                    .buildTransaction(fillRequest);
            transactionService.write(fillRequest.getAccount(), transaction);
        } catch (Exception e) {
            logger.error("An error occurred while sending the transaction for the user address {} with application {}",
                    fillRequest.getAppId(), fillRequest.getAccount().getAddress().toString(), e);
            throw e;
        }
    }

    private SurveyBlockchain adaptSurveyToContract(Survey survey, Long appId) {
        return new SurveyBlockchain(appId, survey.getName(), survey.getQuestionList(),
                survey.getPassword(), survey.getDescription());
    }
}
