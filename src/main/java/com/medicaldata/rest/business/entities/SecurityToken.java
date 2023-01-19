package com.medicaldata.rest.business.entities;

import com.algorand.algosdk.crypto.Address;

import java.math.BigInteger;

public class SecurityToken {

    private Address sender;
    private Address receiver;
    private BigInteger totalCount;
    private String name;
    private String unit;
    private String metaDataHash;
    private Address manager;
    private Address reserve;
    private Address freeze;
    private Address clawBack;

    private static final String NAME = "Medical Survey Token";
    private static final String UNIT = "MST";
    private static final BigInteger TOTAL_COUNT = BigInteger.valueOf(100);


    public SecurityToken(Address sender, Address receiver, String metaDataHash) {
        this.sender = sender;
        this.receiver = receiver;
        this.totalCount = TOTAL_COUNT;
        this.name = NAME;
        this.unit = UNIT;
        this.metaDataHash = metaDataHash;
        this.manager = sender;
        this.reserve = sender;
        this.freeze = sender;
        this.clawBack = sender;
    }

    public SecurityToken(Address sender, Address fromClawBack) {
        this.sender = sender;
        this.receiver = fromClawBack;
        this.totalCount = TOTAL_COUNT;
        this.name = NAME;
        this.unit = UNIT;
        this.manager = sender;
        this.reserve = sender;
        this.freeze = sender;
        this.clawBack = sender;
    }

    public SecurityToken(Address sender) {
        this.sender = sender;
        this.totalCount = TOTAL_COUNT;
        this.name = NAME;
        this.unit = UNIT;
        this.manager = sender;
        this.reserve = sender;
        this.freeze = sender;
        this.clawBack = sender;
    }



    public Address getSender() {
        return sender;
    }

    public void setSender(Address sender) {
        this.sender = sender;
    }

    public Address getReceiver() {
        return receiver;
    }

    public void setReceiver(Address receiver) {
        this.receiver = receiver;
    }

    public BigInteger getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigInteger totalCount) {
        this.totalCount = totalCount;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getMetaDataHash() {
        return metaDataHash;
    }

    public void setMetaDataHash(String metaDataHash) {
        this.metaDataHash = metaDataHash;
    }

    public Address getManager() {
        return manager;
    }

    public void setManager(Address manager) {
        this.manager = manager;
    }

    public Address getReserve() {
        return reserve;
    }

    public void setReserve(Address reserve) {
        this.reserve = reserve;
    }

    public Address getFreeze() {
        return freeze;
    }

    public void setFreeze(Address freeze) {
        this.freeze = freeze;
    }

    public Address getClawBack() {
        return clawBack;
    }

    public void setClawBack(Address clawBack) {
        this.clawBack = clawBack;
    }
}
