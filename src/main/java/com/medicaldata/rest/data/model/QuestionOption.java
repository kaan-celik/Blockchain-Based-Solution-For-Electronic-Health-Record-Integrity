package com.medicaldata.rest.data.model;

public class QuestionOption {
    private String option;
    private boolean isselected;

    public QuestionOption(String option, boolean isselected) {
        this.option = option;
        this.isselected = isselected;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isIsselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }
}
