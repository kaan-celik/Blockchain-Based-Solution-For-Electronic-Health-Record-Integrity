package com.medicaldata.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = {"com.medicaldata.rest"})
public class MedicalDataApplication {

  public static void main(String[] args) {
    SpringApplication.run(MedicalDataApplication.class, args);
  }

}
