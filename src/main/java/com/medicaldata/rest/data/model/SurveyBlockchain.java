package com.medicaldata.rest.data.model;

import java.util.List;
import java.util.Objects;

public class SurveyBlockchain extends Survey {

    private Long appId;

    public SurveyBlockchain(Long appId, String name, List<Question> questionList, String password, String description) {
        super(name, questionList, password, description);
        this.appId = appId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "SurveyBlockchain{" +
                "appId=" + appId + " " + super.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SurveyBlockchain that = (SurveyBlockchain) o;
        return Objects.equals(appId, that.appId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), appId);
    }
}
