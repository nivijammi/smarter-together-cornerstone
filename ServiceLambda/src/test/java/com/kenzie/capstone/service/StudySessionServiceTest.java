package com.kenzie.capstone.service;//package com.kenzie.capstone.service;


import com.kenzie.capstone.service.StudySessionService;
import com.kenzie.capstone.service.dao.StudySessionDao;
import com.kenzie.capstone.service.model.StudySession;
import com.kenzie.capstone.service.model.StudySessionRecord;
import com.kenzie.capstone.service.model.StudySessionRequest;
import com.kenzie.capstone.service.model.StudySessionResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Line 9 indicates that the test class will be instantiated
// once per test class, meaning that a single instance of
// the test class will be created and reused for all test
// methods within this class.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudySessionServiceTest {

    private StudySessionDao studySessionDao;
    private StudySessionService studySessionService;

    @BeforeAll
    void setup() {
        this.studySessionDao = mock(StudySessionDao.class);
        this.studySessionService = new StudySessionService(studySessionDao);
    }

    /*****************************************
    
     addStudySession()
    
     *****************************************/
    @Test
    void addStudySession() {
        ArgumentCaptor<StudySessionRecord> studySessionCaptor = ArgumentCaptor.forClass(StudySessionRecord.class);

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
        StudySessionResponse response = this.studySessionService.addStudySession(request);

        //THEN
        verify(studySessionDao, times(1)).addStudySession(studySessionCaptor.capture());
        StudySessionRecord record = studySessionCaptor.getValue();

        assertNotNull(record, "The record is valid");
        assertEquals(userId, record.getUserId(), "The record userId should match");
        assertNotNull(record.getSessionId(), "The sessionId is generated");
        assertEquals(subject, record.getSubject(), "The record subject should match");
        assertEquals(duration, record.getDuration(), "The record duration should match");
        assertNotNull(record.getDate(), "The record date exists");
        assertEquals(notes, record.getNotes(), "The record notes should match");
        
        assertNotNull(response, "A response is returned");
        assertEquals(userId, response.getUserId(), "The response userId should match");
        assertNotNull(response.getSessionId(), "The sessionId is generated");
        assertEquals(subject, response.getSubject(), "The response subject should match");
        assertEquals(duration, response.getDuration(), "The response duration should match");
        assertNotNull(response.getDate(), "The response date exists");
        assertEquals(notes, response.getNotes(), "The response notes should match");
    }




    /*****************************************

     deleteStudySession()

     *****************************************/
    
    @Test
    void deleteStudySession() {
        ArgumentCaptor<StudySessionRecord> studySessionCaptor = ArgumentCaptor.forClass(StudySessionRecord.class);

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

        StudySessionResponse response = this.studySessionService.addStudySession(request);

        //WHEN
        boolean delete = this.studySessionService.deleteStudySession(response.getSessionId());

        //THEN
        verify(studySessionDao, times(1)).deleteStudySession(studySessionCaptor.capture());
        StudySessionRecord record = studySessionCaptor.getValue();

//        assertNull(record);
        
    }

    /*****************************************

     getAllStudySession()

     *****************************************/
    
    @Test
    void getAllStudySessions() {
        ArgumentCaptor<StudySessionRecord> studySessionCaptor = ArgumentCaptor.forClass(StudySessionRecord.class);

        //GIVEN
        String sessionId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String subject = "testSubject";
        int duration = 10;
        String date = "2023-10-10"; //zoneddatetime.now?
        String notes = "testNotes";

        String sessionId2 = UUID.randomUUID().toString();
        String userId2 = UUID.randomUUID().toString();
        String subject2 = "testSubject2";
        int duration2 = 12;
        String date2 = "2023-10-10"; //zoneddatetime.now?
        String notes2 = "testNotes2";

        StudySession studySession = new StudySession();
        studySession.setSessionId(sessionId);
        studySession.setUserId(userId);
        studySession.setSubject(subject);
        studySession.setDuration(duration);
        studySession.setDate(date);
        studySession.setNotes(notes);

        StudySession studySession2 = new StudySession();
        studySession2.setSessionId(sessionId2);
        studySession2.setUserId(userId2);
        studySession2.setSubject(subject2);
        studySession2.setDuration(duration2);
        studySession2.setDate(date2);
        studySession2.setNotes(notes2);

//        StudySessionRequest request = new StudySessionRequest();
//        request.setUserId(userId);
//        request.setSubject(subject);
//        request.setDuration(duration);
//        request.setDate(date);
//        request.setNotes(notes);
//
//        StudySessionRequest request2 = new StudySessionRequest();
//        request2.setUserId(userId2);
//        request2.setSubject(subject2);
//        request2.setDuration(duration2);
//        request2.setDate(date2);
//        request2.setNotes(notes2);
        List<StudySession> studySessionList = new ArrayList<>();
        studySessionList.add(studySession);
        studySessionList.add(studySession2);

        //WHEN
        when(this.studySessionService.getAllStudySessions()).thenReturn(studySessionList);

        //THEN
        verify(studySessionDao, times(1)).findAllSessions();
        StudySessionRecord record = studySessionCaptor.getValue();
        verify(studySessionDao.findAllSessions().size() == 2);

        
        
    }
    
    











}
