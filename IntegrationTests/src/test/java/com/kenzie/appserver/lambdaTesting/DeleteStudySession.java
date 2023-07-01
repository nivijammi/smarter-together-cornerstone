package com.kenzie.appserver.lambdaTesting;

import com.kenzie.capstone.service.client.StudySessionServiceClient;
import com.kenzie.capstone.service.model.StudySessionRequest;
import com.kenzie.capstone.service.model.StudySessionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DeleteStudySession {

    private StudySessionServiceClient client;

    @BeforeEach
    void setUp() {
        client = new StudySessionServiceClient();
    }

    @Test
    void studySessionServiceClient_deleteStudySession_isValid() {
        //GIVEN
        String userId = UUID.randomUUID().toString();
        String subject = "testSubject";
        int duration = 10;
        String date = "2023-10-10"; //zoneddatetime.now?
        String notes = "testNotes";

        StudySessionRequest request = new StudySessionRequest();
        request.setUserId(userId);
        request.setSubject(subject);
        request.setDuration(duration);
        request.setDate(date);
        request.setNotes(notes);

        //can you not do an integration test for deleting??
        StudySessionResponse response = client.addStudySession(request);
        //WHEN

        boolean deleteStudySession = client.deleteStudySessionBySessionId(response.getSessionId());

        //THEN
        assertTrue(deleteStudySession);
//        assertNull(response.getSessionId(), "The studySessionId is not ");  // it wont be null, it just wont exist in the table
//        assert

    }



}
