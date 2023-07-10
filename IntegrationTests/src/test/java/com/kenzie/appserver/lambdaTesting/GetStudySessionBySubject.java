package com.kenzie.appserver.lambdaTesting;

import com.kenzie.capstone.service.client.StudySessionServiceClient;
import com.kenzie.capstone.service.model.StudySession;
import com.kenzie.capstone.service.model.StudySessionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.amazonaws.util.ValidationUtils.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetStudySessionBySubject {

    private StudySessionServiceClient client;

    @BeforeEach
    void setUp() {
        client = new StudySessionServiceClient();
    }

    @Test
    void studySessionServiceClient_getStudySessionBySubject_isValid() {
        String userId = "Tester";
        String subject = "NewSubject1";
        int duration = 10;
        String date = "2023-10-10"; //zoneddatetime.now?
        String notes = "testNotes";

        String userId2 = userId;
        String subject2 = "NewSubject1";
        int duration2 = 12;
        String date2 = "2023-10-10"; //zoneddatetime.now?
        String notes2 = "testNotes2";

        String userId3 = "Not Tester";
        String subject3 = "testSubject";
        int duration3 = 10;
        String date3 = "2023-10-10"; //zoneddatetime.now?
        String notes3 = "testNotes";


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

        StudySessionRequest request3 = new StudySessionRequest();
        request3.setUserId(userId3);
        request3.setSubject(subject3);
        request3.setDuration(duration3);
        request3.setDate(date3);
        request3.setNotes(notes3);

        client.addStudySession(request);
        client.addStudySession(request2);
        client.addStudySession(request3);

        //WHEN
        List<StudySession> studySessions = client.getStudySessionsBySubject(subject);

        assertNotNull(studySessions, "The StudySessions exist");
        assertEquals(2, studySessions.size(), "There are two study sessions");

        //Cleanup
        for(StudySession session : studySessions) {
            client.deleteStudySessionBySessionId(session.getSessionId());
        }
    }
}
