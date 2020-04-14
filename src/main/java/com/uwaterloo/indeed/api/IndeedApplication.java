package com.uwaterloo.indeed.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.uwaterloo.indeed.api.cfg.IndeedApiConfiguration;

@EnableConfigurationProperties(IndeedApiConfiguration.class)
@SpringBootApplication(scanBasePackages="com.uwaterloo.indeed")
public class IndeedApplication {

    public static void main(String[] args) {
        SpringApplication.run(IndeedApplication.class, args);
    }

}
