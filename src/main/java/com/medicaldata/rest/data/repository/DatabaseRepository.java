package com.medicaldata.rest.data.repository;
import com.medicaldata.rest.data.model.SurveyBlockchain;
import com.medicaldata.rest.data.model.Users;

import java.util.List;
import java.util.Optional;

public interface DatabaseRepository {
    void save(SurveyBlockchain survey);

    List<Users> getUsers(long applicationId);

    List<SurveyBlockchain> find();

    Optional<SurveyBlockchain> findBy(long applicationId);
}
