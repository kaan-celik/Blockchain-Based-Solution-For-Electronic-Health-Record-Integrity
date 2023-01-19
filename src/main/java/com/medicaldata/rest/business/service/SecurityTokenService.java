package com.medicaldata.rest.business.service;

import com.algorand.algosdk.transaction.Transaction;
import com.medicaldata.rest.business.entities.SecurityToken;

public class SecurityTokenService {

    private SecurityToken securityToken;


    private SmartContractUtilService smartContractUtilService;

    private final int ASSET_DECIMAL = 0;

    private Transaction transaction;

    public SecurityTokenService(SecurityToken securityToken, SmartContractUtilService smartContractUtilService) {
        this.securityToken = securityToken;
        this.smartContractUtilService = smartContractUtilService;
    }


    public void setSecurityToken(SecurityToken securityToken) {
        this.securityToken = securityToken;
    }

    /**
     *
     * Create an aseet creation transaction
     * @return Transaction transaction
     */
    public Transaction CreateToken(){
         transaction = Transaction.AssetCreateTransactionBuilder()
                .sender(securityToken.getSender())
                .assetTotal(securityToken.getTotalCount())
                .assetDecimals(ASSET_DECIMAL)
                .assetUnitName(securityToken.getUnit())
                .assetName(securityToken.getName())
                .metadataHashUTF8(securityToken.getMetaDataHash())
                .manager(securityToken.getManager())
                .reserve(securityToken.getReserve())
                .freeze(securityToken.getFreeze())
                .clawback(securityToken.getClawBack())
                .defaultFrozen(false)
                .suggestedParams(smartContractUtilService.getParameters())
                .build();
        return transaction;
    }

    /**
     *
     * Create an aseet modification transaction
     * @param assetID
     * @return Transaction transaction
     */
    public Transaction ModifyToken(long assetID){
         transaction = Transaction.AssetConfigureTransactionBuilder()
                .sender(securityToken.getSender())
                .assetIndex(assetID)
                .manager(securityToken.getManager())
                .reserve(securityToken.getReserve())
                .freeze(securityToken.getFreeze())
                .clawback(securityToken.getClawBack())
                .suggestedParams(smartContractUtilService.getParameters())
                .build();
        return  transaction;
    }

    /**
     * Create an aseet transfer transaction
     * @param assetID
     * @param amount
     * @return Transaction transaction
     */
    public Transaction transferToken(long assetID, long amount){
         transaction = Transaction.AssetTransferTransactionBuilder()
                .sender(securityToken.getSender())
                .assetReceiver(securityToken.getReceiver())
                .assetAmount(amount)
                .assetIndex(assetID)
                .suggestedParams(smartContractUtilService.getParameters())
                .build();

        return transaction;
    }


    /**
     * Create an aseet opt-in transaction
     * @param assetID
     * @return Transaction transaction
     */
    public Transaction optInToken(long assetID){
         transaction = Transaction.AssetAcceptTransactionBuilder()
                .acceptingAccount(securityToken.getSender())
                .assetIndex(assetID)
                .suggestedParams(smartContractUtilService.getParameters())
                .build();

        return transaction;
    }

    /**
     *
     * Create an asset freeze transaction
     * @param assetID
     * @param isFreeze
     * @return Transaction transaction
     */

    public Transaction freezeToken (long assetID, boolean isFreeze){

         transaction = Transaction.AssetFreezeTransactionBuilder()
                .sender(securityToken.getSender())
                .freezeTarget(securityToken.getReceiver())
                .freezeState(isFreeze)
                .assetIndex(assetID)
                .suggestedParams(smartContractUtilService.getParameters())
                .build();

         return transaction;
    }


    /**
     * Create an asset revoke transaction
     * @param assetID
     * @return Transaction transaction
     */
    public Transaction clawbackToken (long assetID){

        Transaction transaction = Transaction.AssetClawbackTransactionBuilder()
                .sender(securityToken.getSender())
                .assetClawbackFrom(securityToken.getReceiver())
                .assetReceiver(securityToken.getSender())
                .assetAmount(1)
                .assetIndex(assetID)
                .suggestedParams(smartContractUtilService.getParameters())
                .build();

        return transaction;
    }
}
