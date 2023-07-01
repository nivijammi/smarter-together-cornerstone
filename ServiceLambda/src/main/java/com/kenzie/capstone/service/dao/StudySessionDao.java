package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.StudySession;
import com.kenzie.capstone.service.model.StudySessionRecord;
import dagger.Provides;

import java.util.List;

public interface StudySessionDao {

    StudySessionRecord addStudySession(StudySessionRecord studySessionRecord);
    public boolean deleteStudySession(StudySessionRecord studySessionRecord);
//    List<StudySessionRecord> findBySessionId(String sessionId);
    List<StudySessionRecord> findSessionBySubject(String subject);
    List<StudySessionRecord> findAllSessions();

    List<StudySessionRecord> findSessionsByUserId(String userId);
    public StudySessionRecord findStudySessionBySessionId(String sessionId);





}
