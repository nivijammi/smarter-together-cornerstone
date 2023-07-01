package com.kenzie.appserver;

import com.kenzie.capstone.service.client.StudySessionServiceClient;
import com.kenzie.capstone.service.model.StudySessionRequest;
import com.kenzie.capstone.service.model.StudySessionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AddStudySession {

    private StudySessionServiceClient client;

    @BeforeEach
    void setUp() {
        client = new StudySessionServiceClient();
    }

    @Test
    void studySessionServiceClient_addStudySession_isValid() {
        //GIVEN
        String userId = "testcustomerId";
        String subject = "testSubject";
        int duration = 10;
        String date = "testdate";
        String notes = "testNotes";

        StudySessionRequest request = new StudySessionRequest();
        request.setUserId(userId);
        request.setSubject(subject);
        request.setDuration(duration);
        request.setDate(date);
        request.setNotes(notes);

        //WHEN
        StudySessionResponse response = client.addStudySession(request);

        //THEN
        assertTrue(response.getUserId().equals(userId), "The userId matches.");
        assertNotNull(response.getDate(), "The date is not null");



    }




}
