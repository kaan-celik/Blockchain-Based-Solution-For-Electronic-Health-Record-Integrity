package com.medicaldata.rest.data.model;

import java.util.List;

public class Question {
    private String question;
    private List<QuestionOption> questionOptionList;

    public Question(String question, List<QuestionOption> questionOptionList) {
        this.question = question;
        this.questionOptionList = questionOptionList;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public List<QuestionOption> getQuestionOptionList() {
        return questionOptionList;
    }

    public void setQuestionOptionList(List<QuestionOption> questionOptionList) {
        this.questionOptionList = questionOptionList;
    }
}
