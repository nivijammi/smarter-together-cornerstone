package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.capstone.service.client.StudySessionServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudySessionServiceApp {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private StudySessionServiceClient studySessionServiceClient;

    public StudySessionServiceApp(UserRepository userRepository, UserService userService,
                               StudySessionServiceClient studySessionServiceClient) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.studySessionServiceClient = studySessionServiceClient;
    }

/**
 * public StudySessionResponse addStudySession(StudySessionRequest studySessionRequest) {}
 */


/**
 * public boolean deleteStudySessionBySessionId(String sessionId) {}
 */


/**
 * public List<StudySession> getAllStudySessions() {}
 */


/**
 * public StudySession getStudySessionBySessionId(String sessionId) {}
 */


/**
 * public List<StudySession> getStudySessionsByUserId(String userId) {}
 */


/**
 * public List<StudySession> getStudySessionsBySubject(String subject) {}
 */






}
