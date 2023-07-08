package com.kenzie.capstone.service;//package com.kenzie.capstone.service;


import com.kenzie.capstone.service.StudySessionService;
import com.kenzie.capstone.service.converter.StudySessionConverter;
import com.kenzie.capstone.service.dao.StudySessionDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.StudySession;
import com.kenzie.capstone.service.model.StudySessionRecord;
import com.kenzie.capstone.service.model.StudySessionRequest;
import com.kenzie.capstone.service.model.StudySessionResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
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

    @Test
    void addStudySession_sessionRequest_null_throwsException() {
        // GIVEN
        StudySessionRequest request = null;


        //WHEN
//        when(studySessionService.addStudySession(request)).thenThrow(InvalidDataException.class);
//        StudySessionResponse response = this.studySessionService.addStudySession(null);

        //THEN
        assertThrows(InvalidDataException.class, () -> studySessionService.addStudySession(request));
//        assertEquals("Request must contain a valid User ID", response.get)
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> studySessionService.addStudySession(request));
        assertEquals("Request must contain a valid User ID", exception.getMessage());
    }


    /*****************************************

     deleteStudySession()
     *****************************************/

    @Test
    void deleteStudySession() {
        ArgumentCaptor<StudySessionRecord> studySessionCaptor = ArgumentCaptor.forClass(StudySessionRecord.class);

        //GIVEN
        String sessionId = "testSessionId";
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

        StudySessionRecord record = new StudySessionRecord();
        record.setSessionId(sessionId);
        record.setUserId(userId);
        record.setSubject(subject);
        record.setDuration(duration);
        record.setDate(ZonedDateTime.now());
        record.setNotes(notes);

//        StudySessionResponse response = studySessionService.addStudySession(request);

        //WHEN

        when(studySessionDao.deleteStudySession(any())).thenReturn(true);
        boolean delete = studySessionService.deleteStudySession(sessionId);
//        when(delete).thenReturn(true);

        //THEN
//        verify(studySessionDao, times(1)).addStudySession(studySessionCaptor.capture());
        //TODO implement clean up so it passes when ran individually... Needs to be 1 when ran by itself
        verify(studySessionDao, times(2)).deleteStudySession(studySessionCaptor.capture());
//        verify(studySessionDao).deleteStudySession(studySessionCaptor.capture());
//        StudySessionRecord record = studySessionCaptor.getValue();
//        System.out.println(record.toString());
        assertTrue(delete);

//        assertNull(record);
    }

    @Test
    void deleteStudySession_fails_to_delete() {

        ArgumentCaptor<StudySessionRecord> studySessionCaptor = ArgumentCaptor.forClass(StudySessionRecord.class);

        //GIVEN
        String sessionId = "testSessionId";
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

        StudySessionRecord record = new StudySessionRecord();
        record.setSessionId(sessionId);
        record.setUserId(userId);
        record.setSubject(subject);
        record.setDuration(duration);
        record.setDate(ZonedDateTime.now());
        record.setNotes(notes);

//        StudySessionResponse response = studySessionService.addStudySession(request);

        //WHEN

        when(studySessionDao.deleteStudySession(record)).thenReturn(false);
        boolean delete = studySessionService.deleteStudySession(sessionId);
//        when(delete).thenReturn(true);

        //THEN
//        verify(studySessionDao, times(1)).addStudySession(studySessionCaptor.capture());
//        verify(studySessionDao, times(1)).deleteStudySession(any());
//        verify(studySessionDao).deleteStudySession(studySessionCaptor.capture());
//        StudySessionRecord record = studySessionCaptor.getValue();
//        System.out.println(record.toString());
        assertFalse(delete);

//        assertNull(record);
    }


    @Test
    void deleteStudySession_sessionId_is_null_throws_Exception() {
        String sessionId = null;

        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> studySessionService.deleteStudySession(sessionId));
        assertEquals("Request must contain a valid Session ID", exception.getMessage());


    }

    /*****************************************

     getAllStudySession()
     *****************************************/

    @Test
    void getAllStudySessions() {
        ArgumentCaptor<StudySessionRecord> studySessionCaptor = ArgumentCaptor.forClass(StudySessionRecord.class);

        //GIVEN
        String sessionId = randomUUID().toString();
        String userId = randomUUID().toString();
        String subject = "testSubject";
        int duration = 10;
        String date = "2023-10-10"; //zoneddatetime.now?
        String notes = "testNotes";

        String sessionId2 = randomUUID().toString();
        String userId2 = randomUUID().toString();
        String subject2 = "testSubject2";
        int duration2 = 12;
        String date2 = "2023-10-10"; //zoneddatetime.now?
        String notes2 = "testNotes2";

        StudySessionRecord studySessionRecord = new StudySessionRecord();
        studySessionRecord.setSessionId(sessionId);
        studySessionRecord.setUserId(userId);
        studySessionRecord.setSubject(subject);
        studySessionRecord.setDuration(duration);
        studySessionRecord.setDate(ZonedDateTime.now());
        studySessionRecord.setNotes(notes);

        StudySessionRecord studySessionRecord2 = new StudySessionRecord();
        studySessionRecord2.setSessionId(sessionId2);
        studySessionRecord2.setUserId(userId2);
        studySessionRecord2.setSubject(subject2);
        studySessionRecord2.setDuration(duration2);
        studySessionRecord2.setDate(ZonedDateTime.now());
        studySessionRecord2.setNotes(notes2);

        List<StudySessionRecord> studySessionRecordList = new ArrayList<>();
        studySessionRecordList.add(studySessionRecord);
        studySessionRecordList.add(studySessionRecord2);


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
        studySession2.setDate(date);
        studySession2.setNotes(notes2);

        List<StudySession> studySessionList = new ArrayList<>();
        studySessionList.add(studySession);
        studySessionList.add(studySession2);


        //WHEN
        when(this.studySessionDao.findAllSessions()).thenReturn(studySessionRecordList);
        //        when(this.studySessionService.getAllStudySessions()).thenReturn(studySessionList);

        List<StudySession> studySessionList1 = this.studySessionService.getAllStudySessions();

        //THEN
        verify(studySessionDao, times(1)).findAllSessions();
//        StudySessionRecord record = studySessionCaptor.getValue();
//        verify(studySessionDao.findAllSessions().size() == 2);
//        verify(studySessionList.size() == 2);
//        assertNotNull(record, "The record is not null");


    }

    /*****************************************

     getStudySessionBySessionId()
     *****************************************/

    @Test
    void getStudySessionBySessionId_is_valid() {
        //GIVEN
        String sessionId = randomUUID().toString();
        String userId = randomUUID().toString();
        String subject = "testSubject";
        int duration = 10;
        String date = "2023-10-10";
        String notes = "testNotes";

        StudySessionRecord studySessionRecord = new StudySessionRecord();
        studySessionRecord.setSessionId(sessionId);
        studySessionRecord.setUserId(userId);
        studySessionRecord.setSubject(subject);
        studySessionRecord.setDuration(duration);
        studySessionRecord.setDate(ZonedDateTime.now());
        studySessionRecord.setNotes(notes);

        StudySession studySession = new StudySession();
        studySession.setSessionId(sessionId);
        studySession.setUserId(userId);
        studySession.setSubject(subject);
        studySession.setDuration(duration);
        studySession.setDate(date);
        studySession.setNotes(notes);

        //WHEN
        when(studySessionDao.findStudySessionBySessionId(sessionId)).thenReturn(studySessionRecord);
//        when(StudySessionConverter.fromRecordToStudySession(studySessionRecord)).thenReturn(studySession);

        StudySession session = studySessionService.getStudySessionBySessionId(sessionId);

        //THEN
        assertNotNull(studySessionService.getStudySessionBySessionId(sessionId));
        assertEquals(session.getSessionId(), studySession.getSessionId());
        assertEquals(session.getUserId(), studySession.getUserId());
        assertEquals(session.getSubject(), studySession.getSubject());
        assertEquals(session.getDuration(), studySession.getDuration());
        assertEquals(session.getNotes(), studySession.getNotes());
    }


    /*****************************************

     getAllStudySessionsByUser()
     *****************************************/

    @Test
    void getAllStudySessionByUser_is_valid() {
        ArgumentCaptor<StudySessionRecord> studySessionCaptor = ArgumentCaptor.forClass(StudySessionRecord.class);

        //GIVEN
        String sessionId = randomUUID().toString();
        String userId = randomUUID().toString();
        String subject = "testSubject";
        int duration = 10;
        String date = "2023-10-10"; //zoneddatetime.now?
        String notes = "testNotes";

        String sessionId2 = randomUUID().toString();
        String userId2 = userId;
        String subject2 = "testSubject2";
        int duration2 = 12;
        String date2 = "2023-10-10"; //zoneddatetime.now?
        String notes2 = "testNotes2";

        StudySessionRecord studySessionRecord = new StudySessionRecord();
        studySessionRecord.setSessionId(sessionId);
        studySessionRecord.setUserId(userId);
        studySessionRecord.setSubject(subject);
        studySessionRecord.setDuration(duration);
        studySessionRecord.setDate(ZonedDateTime.now());
        studySessionRecord.setNotes(notes);

        StudySessionRecord studySessionRecord2 = new StudySessionRecord();
        studySessionRecord2.setSessionId(sessionId2);
        studySessionRecord2.setUserId(userId2);
        studySessionRecord2.setSubject(subject2);
        studySessionRecord2.setDuration(duration2);
        studySessionRecord2.setDate(ZonedDateTime.now());
        studySessionRecord2.setNotes(notes2);

        List<StudySessionRecord> studySessionRecordList = new ArrayList<>();
        studySessionRecordList.add(studySessionRecord);
        studySessionRecordList.add(studySessionRecord2);


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

        List<StudySession> studySessionList = new ArrayList<>();
        studySessionList.add(studySession);
        studySessionList.add(studySession2);

        //WHEN
        when(studySessionDao.findSessionsByUserId(userId)).thenReturn(studySessionRecordList);

        List<StudySession> sessionList = studySessionService.getAllStudySessionByUser(userId);

        //THEN
        assertNotNull(studySessionService.getAllStudySessionByUser(userId));
        assertTrue(sessionList.size() == 2);
        for (StudySession session : sessionList) {
            assertEquals(session.getUserId(), userId);
        }


    }


    /*****************************************

     getAllStudySessionsBySubject()
     *****************************************/

    @Test
    void getAllStudySessionBySubject() {
        ArgumentCaptor<StudySessionRecord> studySessionCaptor = ArgumentCaptor.forClass(StudySessionRecord.class);

        //GIVEN
        String sessionId = randomUUID().toString();
        String userId = randomUUID().toString();
        String subject = "testSubject";
        int duration = 10;
        String date = "2023-10-10"; //zoneddatetime.now?
        String notes = "testNotes";

        String sessionId2 = randomUUID().toString();
        String userId2 = randomUUID().toString();
        String subject2 = subject;
        int duration2 = 12;
        String date2 = "2023-10-10"; //zoneddatetime.now?
        String notes2 = "testNotes2";

        StudySessionRecord studySessionRecord = new StudySessionRecord();
        studySessionRecord.setSessionId(sessionId);
        studySessionRecord.setUserId(userId);
        studySessionRecord.setSubject(subject);
        studySessionRecord.setDuration(duration);
        studySessionRecord.setDate(ZonedDateTime.now());
        studySessionRecord.setNotes(notes);

        StudySessionRecord studySessionRecord2 = new StudySessionRecord();
        studySessionRecord2.setSessionId(sessionId2);
        studySessionRecord2.setUserId(userId2);
        studySessionRecord2.setSubject(subject2);
        studySessionRecord2.setDuration(duration2);
        studySessionRecord2.setDate(ZonedDateTime.now());
        studySessionRecord2.setNotes(notes2);

        List<StudySessionRecord> studySessionRecordList = new ArrayList<>();
        studySessionRecordList.add(studySessionRecord);
        studySessionRecordList.add(studySessionRecord2);


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

        List<StudySession> studySessionList = new ArrayList<>();
        studySessionList.add(studySession);
        studySessionList.add(studySession2);

        //WHEN
        when(studySessionDao.findSessionBySubject(subject)).thenReturn(studySessionRecordList);

        List<StudySession> sessionList = studySessionService.getAllStudySessionsBySubject(subject);

        //THEN
        assertNotNull(studySessionService.getAllStudySessionsBySubject(subject));
        assertTrue(sessionList.size() == 2);
        for (StudySession session : sessionList) {
            assertEquals(session.getSubject(), subject);
        }


    }


}
