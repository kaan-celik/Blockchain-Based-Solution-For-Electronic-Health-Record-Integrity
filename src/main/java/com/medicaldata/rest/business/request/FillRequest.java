package com.medicaldata.rest.business.request;

import com.algorand.algosdk.account.Account;

import java.util.Objects;

public class FillRequest {

    private final long appId;
    private final Account account;
    private final SurveyRequest fullSurvey;

    public FillRequest(long appId, Account account, SurveyRequest fullSurvey) {
        this.appId = appId;
        this.account = account;
        this.fullSurvey = fullSurvey;
    }

    public long getAppId() {
        return appId;
    }

    public Account getAccount() {
        return account;
    }

    public SurveyRequest getFullSurvey() {
        return fullSurvey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FillRequest that = (FillRequest) o;
        return appId == that.appId &&
                Objects.equals(account, that.account) &&
                Objects.equals(fullSurvey, that.fullSurvey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, account, fullSurvey);
    }

    @Override
    public String toString() {
        return "FillRequest{" +
                "appId=" + appId +
                ", account=" + account +
                ", fullSurvey='" + fullSurvey + '\'' +
                '}';
    }
}
