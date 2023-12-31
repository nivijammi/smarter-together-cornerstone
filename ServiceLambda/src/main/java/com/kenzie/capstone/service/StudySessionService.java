package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.StudySessionConverter;
import com.kenzie.capstone.service.dao.StudySessionDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;


public class StudySessionService {

    private StudySessionDao sessionDao;
    private ExecutorService executor;
    static final Logger log = LogManager.getLogger();

    @Inject
    public StudySessionService(StudySessionDao sessionDao) {
        this.sessionDao = sessionDao;
        this.executor = Executors.newCachedThreadPool();  //is this needed for ours???
    }

    /**
     * Takes in a StudySessionRequest, converts to a Record to save to DynamoDB table, then
     * converts back to response and returns response
     *
     * @param sessionRequest - StudySessionRequest
     * @return
     */
    public StudySessionResponse addStudySession(StudySessionRequest sessionRequest) {
        if (sessionRequest == null || sessionRequest.getUserId() == null || sessionRequest.getUserId().length() == 0) {
            throw new InvalidDataException("Request must contain a valid User ID");
        }

        StudySessionRecord record = StudySessionConverter.fromRequestToRecord(sessionRequest);
        sessionDao.addStudySession(record);
        return StudySessionConverter.fromRecordToResponse(record);
    }

    /**
     *
     * @param sessionId
     * @return
     */
    public boolean deleteStudySession(String sessionId) {
        boolean deleted = true;

        if (sessionId == null) {
            throw new InvalidDataException("Request must contain a valid Session ID");
        }

        StudySessionRecord record = new StudySessionRecord();
        record.setSessionId(sessionId);

        boolean delete = sessionDao.deleteStudySession(record);

        if (!delete) {
            deleted = false;
        }
        return deleted;
    }

    public StudySession getStudySessionBySessionId(String sessionId) {

        if (sessionId == null) {
            throw new InvalidDataException("Request must contain a valid Session ID");
        }

        StudySessionRecord record = sessionDao.findStudySessionBySessionId(sessionId);


        StudySessionConverter studySessionConverter = new StudySessionConverter();

        StudySession session = StudySessionConverter.fromRecordToStudySession(record);

        return session;
    }
//        List<StudySessionRecord> records = sessionDao.findStudySessionBySessionId(sessionId);
//
//        return records.stream()
//                .map(StudySessionConverter::fromRecordToStudySession)
//                .collect(Collectors.toList());
//
//    }

    public List<StudySession> getAllStudySessionByUser(String userId) {

        if (userId == null) {
            throw new InvalidDataException("Request must contain a valid userId");
        }

        List<StudySessionRecord> records = sessionDao.findSessionsByUserId(userId);

        return records.stream()
                .map(StudySessionConverter::fromRecordToStudySession)
                .collect(Collectors.toList());
    }


    public List<StudySession> getAllStudySessionsBySubject(String subject) {

        if (subject == null) {
            throw new InvalidDataException("Request must contain a valid subject");
        }

        List<StudySessionRecord> records = sessionDao.findSessionBySubject(subject);

        return records.stream()
                .map(StudySessionConverter::fromRecordToStudySession)
                .collect(Collectors.toList());
    }

    public List<StudySession> getAllStudySessions() {
        List<StudySessionRecord> records = sessionDao.findAllSessions();

        if (records == null) {
            throw new InvalidDataException("Request must contain a valid record");
        }

        return records.stream()
                .map(StudySessionConverter::fromRecordToStudySession)
                .collect(Collectors.toList());
    }

//    public List<LeaderboardEntry> getStudySessionLeaderboard() {
//        List<StudySessionRecord> sessionRecords = this.sessionDao.findAllSessions()
//    }


}
