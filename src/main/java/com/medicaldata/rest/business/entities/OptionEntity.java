package com.medicaldata.rest.business.entities;

public class OptionEntity {
     private Long id;
     private Long question_id;
     private String option;


    public OptionEntity(Long id, Long question_id, String option) {
        this.id = id;
        this.question_id = question_id;
        this.option = option;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
