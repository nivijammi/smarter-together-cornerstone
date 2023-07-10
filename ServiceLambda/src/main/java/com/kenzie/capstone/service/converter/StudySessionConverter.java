package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.StudySession;
import com.kenzie.capstone.service.model.StudySessionRecord;
import com.kenzie.capstone.service.model.StudySessionRequest;
import com.kenzie.capstone.service.model.StudySessionResponse;

import java.time.ZonedDateTime;
import java.util.Random;

import static java.util.UUID.randomUUID;

public class StudySessionConverter {

    private static ZonedDateTimeConverter converter = new ZonedDateTimeConverter();


    public static StudySessionRecord fromRequestToRecord(StudySessionRequest request) {
        StudySessionRecord record = new StudySessionRecord();
        record.setSessionId(randomUUID().toString());
        record.setUserId(request.getUserId());
        record.setSubject(request.getSubject());
        record.setDuration(request.getDuration());
        record.setDate(converter.unconvert(request.getDate()));
        record.setNotes(request.getNotes());
        return record;
    }

    public static StudySessionResponse fromRecordToResponse(StudySessionRecord record) {
        StudySessionResponse sessionResponse = new StudySessionResponse();
        sessionResponse.setSessionId(record.getSessionId());
        sessionResponse.setUserId(record.getUserId());
        sessionResponse.setSubject(record.getSubject());
        sessionResponse.setDuration(record.getDuration());
        sessionResponse.setDate(converter.convert(record.getDate()));
        sessionResponse.setNotes(record.getNotes());
        return sessionResponse;
    }

    public static StudySession fromRecordToStudySession(StudySessionRecord record) {
        StudySession session = new StudySession();
        session.setSessionId(record.getSessionId());
        session.setUserId(record.getUserId());
        session.setSubject(record.getSubject());
        session.setDuration(record.getDuration());
        session.setDate(converter.convert(record.getDate()));
        session.setNotes(record.getNotes());
        return session;
    }


}
