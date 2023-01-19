package com.medicaldata.rest.middleware.converter;

import com.medicaldata.rest.business.request.OptinRequest;
import com.medicaldata.rest.business.request.SurveyRequest;
import com.medicaldata.rest.business.service.AccountCreatorService;
import com.medicaldata.rest.data.model.Question;
import com.medicaldata.rest.data.model.QuestionOption;
import com.medicaldata.rest.data.model.Survey;
import com.medicaldata.rest.business.request.FillRequest;
import com.medicaldata.rest.business.request.OptinAppRequest;

import java.util.stream.Collectors;

public class RequestConverter {

    private AccountCreatorService accountCreatorService;

    public RequestConverter(AccountCreatorService accountCreatorService) {
        this.accountCreatorService = accountCreatorService;
    }

    public Survey fromRequestToDomain(SurveyRequest survey) {
        return new Survey(survey.getName(),
                survey.getQuestionList().stream().map(s -> new Question(s.getQuestion(),
                        s.getQuestionOptionList().stream().map(a -> new QuestionOption(a.getOption(), a.isIsselected())).collect(Collectors.toList())
                )).collect(Collectors.toList()),
                survey.getPassword(),
                survey.getDescription());
    }

    public OptinAppRequest fromRequestToDomain(long appId, OptinRequest optinRequest) {
        return new OptinAppRequest(appId, accountCreatorService.createAccountFrom(optinRequest.getMnemonicKey()));
    }

    public FillRequest fromRequestToDomain(long appId, SurveyRequest voteRequest) {
        return new FillRequest(appId, accountCreatorService.createAccountFrom(voteRequest.getPassword()), voteRequest);
    }

}
