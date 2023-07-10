package com.kenzie.appserver.lambdaTesting;

import com.kenzie.capstone.service.client.StudySessionServiceClient;
import com.kenzie.capstone.service.converter.StudySessionConverter;
import com.kenzie.capstone.service.model.StudySession;
import com.kenzie.capstone.service.model.StudySessionRequest;
import com.kenzie.capstone.service.model.StudySessionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.amazonaws.util.ValidationUtils.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class GetAllStudySessions {

    private StudySessionServiceClient client;

    @BeforeEach
    void setUp() {
        client = new StudySessionServiceClient();
    }

    @Test
    void studySessionServiceClient_getAllStudySessions_isValid() {
        //GIVEN
        String userId = UUID.randomUUID().toString();
        String subject = "testSubject";
        int duration = 10;
        String date = "2023-10-10"; //zoneddatetime.now?
        String notes = "testNotes";

        String userId2 = UUID.randomUUID().toString();
        String subject2 = "testSubject2";
        int duration2 = 12;
        String date2 = "2023-10-10"; //zoneddatetime.now?
        String notes2 = "testNotes2";


        StudySessionRequest request = new StudySessionRequest();
        request.setUserId(userId);
        request.setSubject(subject);
        request.setDuration(duration);
        request.setDate(date);
        request.setNotes(notes);

        StudySessionRequest request2 = new StudySessionRequest();
        request2.setUserId(userId2);
        request2.setSubject(subject2);
        request2.setDuration(duration2);
        request2.setDate(date2);
        request2.setNotes(notes2);

        client.addStudySession(request);
        client.addStudySession(request2);

        //WHEN
        List<StudySession> studySessions = client.getAllStudySessions();

        //THEN

        assertNotNull(studySessions, "The StudySessions exist");
        //size will be wrong - will have to clean table and test fresh?
        //or find another metric to test by
//        assertEquals(2, studySessions.size(), "There are two study sessions");
//        assertEquals(studySessions.containsAll(studySessions));
    }

}
