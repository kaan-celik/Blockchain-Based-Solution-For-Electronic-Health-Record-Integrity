package com.medicaldata.rest.business.entities;

import java.util.List;

public class QuestionEntity {

    private Long id;
    private Long surveyId;
    private String question;
    private List<OptionEntity> options;

    public QuestionEntity(Long id, Long surveyId, String question, List<OptionEntity> options) {
        this.id = id;
        this.surveyId = surveyId;
        this.question = question;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<OptionEntity> getOptions() {
        return options;
    }

    public void setOptions(List<OptionEntity> options) {
        this.options = options;
    }
}
