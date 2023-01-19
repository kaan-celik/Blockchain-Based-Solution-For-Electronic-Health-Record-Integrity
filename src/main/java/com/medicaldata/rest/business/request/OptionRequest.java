package com.medicaldata.rest.business.request;

public class OptionRequest {
    private String option;
    private boolean isselected;

    public OptionRequest() {

    }

    public OptionRequest(String option, boolean isselected) {
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
