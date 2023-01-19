CREATE SCHEMA IF NOT EXISTS medicaldb
    AUTHORIZATION postgres;

-- Table: medicaldb.survey

-- DROP TABLE IF EXISTS medicaldb.survey;

CREATE TABLE IF NOT EXISTS medicaldb.survey
(
    id serial NOT NULL,
    name character varying(1024) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    app_id character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT survey_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS medicaldb.survey
    OWNER to postgres;



-- Table: medicaldb.question

-- DROP TABLE IF EXISTS medicaldb.question;

CREATE TABLE IF NOT EXISTS medicaldb.question
(
    id serial NOT NULL,
    survey_id integer NOT NULL,
    question text COLLATE pg_catalog."default",
    CONSTRAINT question_pkey PRIMARY KEY (id),
    CONSTRAINT survey_id_fk FOREIGN KEY (survey_id)
        REFERENCES medicaldb.survey (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS medicaldb.question
    OWNER to postgres;



-- Table: medicaldb.option

-- DROP TABLE IF EXISTS medicaldb.option;

CREATE TABLE IF NOT EXISTS medicaldb.option
(
    id serial NOT NULL,
    question_id integer NOT NULL,
    option text COLLATE pg_catalog."default",
    CONSTRAINT option_pkey PRIMARY KEY (id),
    CONSTRAINT question_id_fk FOREIGN KEY (question_id)
        REFERENCES medicaldb.question (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS medicaldb.option
    OWNER to postgres;
