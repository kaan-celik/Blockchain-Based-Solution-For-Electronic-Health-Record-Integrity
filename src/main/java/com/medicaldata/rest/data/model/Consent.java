package com.medicaldata.rest.data.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Consent {

    private long surveyId;
    private boolean isConfirmed;
    private Date confirmDate;

    public Consent(long surveyId, boolean isConfirmed, Date confirmDate) {
        this.surveyId = surveyId;
        this.isConfirmed = isConfirmed;
        this.confirmDate = confirmDate;
    }

    public long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(long surveyId) {
        this.surveyId = surveyId;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    @Override
    public String toString() {
        return "Consent{" +
                "surveyId=" + surveyId +
                ", isConfirmed=" + isConfirmed +
                ", confirmDate=" + confirmDate +
                '}';
    }


    public String convertToJson()
    {
        String json ="";
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(this);
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public byte[] Hash(String json) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(json.getBytes(StandardCharsets.UTF_8));
        return hash;
    }
}
