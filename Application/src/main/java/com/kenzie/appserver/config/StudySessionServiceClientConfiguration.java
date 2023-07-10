package com.kenzie.appserver.config;

import com.kenzie.capstone.service.client.StudySessionServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudySessionServiceClientConfiguration {

    @Bean
    public StudySessionServiceClient studySessionServiceClient() {
        return new StudySessionServiceClient();
    }

}
