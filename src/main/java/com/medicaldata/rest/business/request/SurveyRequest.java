package com.medicaldata.rest.business.request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public class SurveyRequest {
    private String name;
    private String description;
    private String password;
    private List<QuestionRequest> questionList;

    public SurveyRequest(String name, String description, String password, List<QuestionRequest> questionList) {
        this.name = name;
        this.description = description;
        this.password = password;
        this.questionList = questionList;
    }

    public SurveyRequest() {

    }

    public List<QuestionRequest> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionRequest> questionList) {
        this.questionList = questionList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String convertToJson()
    {
        String json ="";
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(this);
            System.out.println("ResultingJSONstring = " + json);
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

    public String FormatTheAnswers()
    {
        String FormattedAnswer = "";

         for (QuestionRequest questionRequest : questionList)
         {
             Integer questionIndex = questionList.indexOf(questionRequest);
             Optional<OptionRequest> selectedOption = questionRequest.getQuestionOptionList().stream().filter(x -> x.isIsselected()).findFirst();
            if(!selectedOption.isEmpty())
            {
                Integer index = questionRequest.getQuestionOptionList().indexOf(selectedOption.get());
                FormattedAnswer += questionIndex.toString() + ":"+ index.toString() + ",";
            }
            else{
                FormattedAnswer += questionIndex.toString() + ":_,";
            }
         }
        FormattedAnswer = FormattedAnswer.substring(0, FormattedAnswer.length()-1);

         return FormattedAnswer;
    }


}
