package com.medicaldata.rest.business.request;

import java.util.List;

public class QuestionRequest {
    private String question;
    private List<OptionRequest> questionOptionList;

    public QuestionRequest() {

    }

    public QuestionRequest(String question, boolean isAnswered, List<OptionRequest> questionOptionList) {
        this.question = question;
        this.questionOptionList = questionOptionList;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public List<OptionRequest> getQuestionOptionList() {
        return questionOptionList;
    }

    public void setQuestionOptionList(List<OptionRequest> questionOptionList) {
        this.questionOptionList = questionOptionList;
    }
}
