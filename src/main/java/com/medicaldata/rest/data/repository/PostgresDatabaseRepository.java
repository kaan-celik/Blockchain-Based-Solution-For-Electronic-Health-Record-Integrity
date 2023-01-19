package com.medicaldata.rest.data.repository;

import com.medicaldata.rest.business.entities.OptionEntity;
import com.medicaldata.rest.business.entities.SurveyEntity;
import com.medicaldata.rest.business.entities.QuestionEntity;
import com.medicaldata.rest.business.exceptions.SavingToDbException;
import com.medicaldata.rest.data.model.Question;
import com.medicaldata.rest.data.model.QuestionOption;
import com.medicaldata.rest.data.model.SurveyBlockchain;
import com.medicaldata.rest.data.model.Users;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class PostgresDatabaseRepository implements DatabaseRepository {

    private Logger logger = LoggerFactory.getLogger(PostgresDatabaseRepository.class);

    private final static String Key = "PostgresTestKey";

    private final static String Algorithm = "AES";

    private final static String AlgorithmMode = "aes-cbc/pad:pkcs";

    private final static String EncodeMode = "escape";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public  final String GET_SURVEYS = "SELECT id, convert_from(decrypt(decode(name,'escape'),'"+Key+"','"+AlgorithmMode+"'),'UTF-8') as name, convert_from(decrypt(decode(description,'escape'),'"+Key+"','"+AlgorithmMode+"'),'UTF-8') as description, app_id FROM medicaldb.survey order by id ASC";

    private final String INSERT_SURVEY = "INSERT INTO medicaldb.survey "
            + "(name, description, app_id) "
            + "VALUES (encode(encrypt(decode(:NAME,:ENCODE),decode(:M_KEY,:ENCODE),CAST(:ALGORITHM AS TEXT)),:ENCODE), encode(encrypt(decode(:DESCRIPTION,:ENCODE),decode(:M_KEY,:ENCODE),CAST(:ALGORITHM AS TEXT)),:ENCODE),:APP_ID)";

    private final String INSERT_QUESTION = "INSERT INTO medicaldb.question (survey_id, question) VALUES (:SURVEY_ID, encode(encrypt(decode(:QUESTION,:ENCODE),decode(:M_KEY,:ENCODE),CAST(:ALGORITHM AS TEXT)),:ENCODE))";

    private final String INSERT_OPTION = "INSERT INTO medicaldb.option (question_id, option) VALUES (:QUESTION_ID, encode(encrypt(decode(:OPTION,:ENCODE),decode(:M_KEY,:ENCODE),CAST(:ALGORITHM AS TEXT)),:ENCODE))";

    private final  String GET_SURVEY = "SELECT id, convert_from(decrypt(decode(name,'escape'),'"+Key+"','"+AlgorithmMode+"'),'UTF-8') as name, convert_from(decrypt(decode(description,'escape'),'"+Key+"','"+AlgorithmMode+"'),'UTF-8') as description, app_id FROM medicaldb.survey WHERE app_id = ";

    private final String GET_QUESTION = "SELECT id, survey_id, convert_from(decrypt(decode(question,'escape'),'"+Key+"','"+AlgorithmMode+"'),'UTF-8') as question FROM medicaldb.question WHERE survey_id = ";

    private final String GET_OPTION = "SELECT id, question_id, convert_from(decrypt(decode(option,'escape'),'"+Key+"','"+AlgorithmMode+"'),'UTF-8')  as option  FROM medicaldb.option WHERE question_id = " ;

    private final String GET_USER = "SELECT id, address, appid FROM medicaldb.acl WHERE appid =";

    public PostgresDatabaseRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(SurveyBlockchain survey) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        GeneratedKeyHolder questionKeyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(INSERT_SURVEY, surveyParameters(survey), keyHolder);

            Integer surveyId =  (Integer) keyHolder.getKeys().get("id");

            for (Question question : survey.getQuestionList())
            {
                jdbcTemplate.update(INSERT_QUESTION, questionParameters(question, surveyId), questionKeyHolder);
                Integer questionId =  (Integer) questionKeyHolder.getKeys().get("id");
                for(QuestionOption questionOption : question.getQuestionOptionList())
                {
                    jdbcTemplate.update(INSERT_OPTION, optionParameters(questionOption, questionId));
                }
            }
        } catch (Exception e) {
            logger.error("An error occours trying to save in the DB", e);
            throw new SavingToDbException(survey.getName(), e.getMessage());
        }
    }

    @Override
    public List<Users> getUsers (long appId){
        try {
            List<Users> users = jdbcTemplate.query(GET_USER + "'" + appId + "'", blockchainUserMapper());
            return users;

        } catch (Exception ex) {
            logger.info(ex.getMessage());
            return null;
        }
    }


    @Override
    public Optional<SurveyBlockchain> findBy(long appId) {

        Optional<SurveyBlockchain> survey = fromEntityToDomain(jdbcTemplate.query(GET_SURVEY + "'" + appId + "'", surveyMapper()))
                .stream()
                .findFirst();
        return survey;
    }



    @Override
    public List<SurveyBlockchain> find() {
        List<SurveyBlockchain> surveys = fromAllEntityToDomain(jdbcTemplate.query(GET_SURVEYS , surveyMapper()));
        return surveys;
    }

    private List<SurveyBlockchain> fromAllEntityToDomain(List<SurveyEntity> surveyEntities) {

        return surveyEntities.stream().map(
                survey -> new SurveyBlockchain(survey.getAppId(), survey.getName(),
                        new ArrayList<Question>(),"", survey.getDescription())).collect(toList());

    }


    private List<SurveyBlockchain> fromEntityToDomain(List<SurveyEntity> surveyEntities) {

        List<SurveyBlockchain> surveyBlockchain = surveyEntities.stream().map(
                survey -> new SurveyBlockchain(survey.getAppId(), survey.getName(),
                        new ArrayList<Question>(),"", survey.getDescription())).collect(toList());

        if(surveyBlockchain != null)
        {
            surveyBlockchain.get(0).setQuestionList(jdbcTemplate.query(GET_QUESTION + surveyEntities.get(0).getId(), questionMapper())
                    .stream()
                    .map(q -> new Question(q.getQuestion(), new ArrayList<QuestionOption>()))
                    .collect(Collectors.toList()));
            List<Question> questionlist = surveyBlockchain.get(0).getQuestionList();
            List<QuestionEntity> QuestionRepoList = jdbcTemplate.query(GET_QUESTION + surveyEntities.get(0).getId(), questionMapper());

            int counter = 0;

            for (Question question : surveyBlockchain.get(0).getQuestionList()) {
                question.setQuestionOptionList(jdbcTemplate.query(GET_OPTION + QuestionRepoList.get(counter).getId(), optionMapper())
                        .stream()
                        .map(o -> new QuestionOption(o.getOption(), false))
                        .collect(Collectors.toList()));
                counter++;
            }
        }

        return surveyBlockchain;
    }

    private ResultSetExtractor<List<Users>> blockchainUserMapper() {
        ResultSetExtractor<List<Users>> list =  JdbcTemplateMapperFactory
                .newInstance()
                .addKeys("id")
                .newResultSetExtractor(Users.class);

        return list;

    }


    private ResultSetExtractor<List<SurveyEntity>> surveyMapper() {
        ResultSetExtractor<List<SurveyEntity>> list =  JdbcTemplateMapperFactory
                     .newInstance()
                     .addKeys("id")
                     .newResultSetExtractor(SurveyEntity.class);

        return list;

    }

    private ResultSetExtractor<List<QuestionEntity>> questionMapper() {
        ResultSetExtractor<List<QuestionEntity>> list =  JdbcTemplateMapperFactory
                .newInstance()
                .addKeys("id")
                .newResultSetExtractor(QuestionEntity.class);

        return list;

    }
    private ResultSetExtractor<List<OptionEntity>> optionMapper() {
        ResultSetExtractor<List<OptionEntity>> list =  JdbcTemplateMapperFactory
                .newInstance()
                .addKeys("id")
                .newResultSetExtractor(OptionEntity.class);

        return list;

    }

    private MapSqlParameterSource questionParameters(Question question, Integer surveyId) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("QUESTION", question.getQuestion());
        params.addValue("SURVEY_ID", surveyId);
        params.addValue("M_KEY",Key);
        params.addValue("ALGORITHM",AlgorithmMode);
        params.addValue("ENCODE",EncodeMode);

        return params;
    }

    private MapSqlParameterSource optionParameters(QuestionOption questionOption, Integer questionId) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("OPTION", questionOption.getOption());
        params.addValue("QUESTION_ID", questionId);
        params.addValue("M_KEY",Key);
        params.addValue("ALGORITHM",AlgorithmMode);
        params.addValue("ENCODE",EncodeMode);
        return params;
    }

    private String Encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        SecretKey key = new SecretKeySpec(Key.getBytes(StandardCharsets.UTF_8),"AES");
        Cipher cipher = Cipher.getInstance(Algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder()
                .encodeToString(cipherText);

    }


    private String Decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        SecretKey key = new SecretKeySpec(Key.getBytes(StandardCharsets.UTF_8),"AES");
        Cipher cipher = Cipher.getInstance(Algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] plainText = cipher.doFinal(cipherText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder()
                .encodeToString(plainText);

    }


    private MapSqlParameterSource surveyParameters(SurveyBlockchain survey) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("NAME", survey.getName());
        params.addValue("DESCRIPTION", survey.getDescription());
        params.addValue("APP_ID", survey.getAppId());
        params.addValue("M_KEY",Key);
        params.addValue("ALGORITHM",AlgorithmMode);
        params.addValue("ENCODE",EncodeMode);

        return params;
    }




}
