package com.kenzie.appserver.lambdaTesting;

import com.kenzie.capstone.service.client.StudySessionServiceClient;
import com.kenzie.capstone.service.model.StudySession;
import com.kenzie.capstone.service.model.StudySessionRequest;
import com.kenzie.capstone.service.model.StudySessionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.amazonaws.util.ValidationUtils.assertNotNull;
import static org.junit.Assert.assertEquals;

public class GetStudySessionBySessionId {

    private StudySessionServiceClient client;

    @BeforeEach
    void setUp() {
        client = new StudySessionServiceClient();
    }

    @Test
    void studySessionServiceClient_getStudySessionBySessionId() {
        String userId = "Tester";
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

        StudySessionResponse response = client.addStudySession(request);
        String sessionId = response.getSessionId();

        //WHEN
        StudySession session = client.getStudySessionBySessionId(sessionId);

        //THEN

        assertNotNull(session.getSessionId(), "The sessionId is not null");
        Assertions.assertEquals(userId, session.getUserId());
        Assertions.assertEquals(subject, session.getSubject());
        Assertions.assertEquals(duration, session.getDuration());
        assertNotNull(session.getDate(), "The date is not null");
        Assertions.assertEquals(notes, session.getNotes());



    }


}
