package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.StudySessionRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NonCachingStudySessionDao implements StudySessionDao {

    private DynamoDBMapper mapper;
    static final Logger log = LogManager.getLogger();


    public NonCachingStudySessionDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public StudySessionRecord addStudySession(StudySessionRecord session) {
        try {
            mapper.save(session, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "sessionId",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new InvalidDataException("StudySession already exists");
        }

        return session;
    }

    public boolean deleteStudySession(StudySessionRecord session) {
        try {
            mapper.delete(session, new DynamoDBDeleteExpression()
                    .withExpected(ImmutableMap.of(
                            "sessionId",
                            new ExpectedAttributeValue().withValue(new AttributeValue(session.getSessionId()))
                                    .withExists(true)
                    )));
        } catch (AmazonDynamoDBException e) {
            log.info(e.getMessage());
            log.info(e.getStackTrace());
            return false;
        }

        return true;
    }

    public List<StudySessionRecord> findSessionBySubject(String subject) {
        StudySessionRecord sessionRecord = new StudySessionRecord();
        sessionRecord.setSubject(subject);

        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":subject", new AttributeValue().withS(sessionRecord.getSubject()));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("subject = :subject")
                .withExpressionAttributeValues(valueMap);

        return mapper.scan(StudySessionRecord.class, scanExpression);

//        DynamoDBQueryExpression<StudySessionRecord> queryExpression = new DynamoDBQueryExpression<StudySessionRecord>()
//                .withHashKeyValues(sessionRecord)
//                .withIndexName("SubjectIndex")
//                .withConsistentRead(false);
//
//        return mapper.query(StudySessionRecord.class, queryExpression);
    }

    public List<StudySessionRecord> findAllSessions() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
//                .withFilterExpression("sessionId");  //will it find all?

        return mapper.scan(StudySessionRecord.class, scanExpression);
    }

    //single record needed, but keep it a list for now?
    //
    public StudySessionRecord findStudySessionBySessionId(String sessionId) {
        StudySessionRecord sessionRecord = new StudySessionRecord();
        sessionRecord.setSessionId(sessionId);

//        DynamoDBQueryExpression<StudySessionRecord> queryExpression = new DynamoDBQueryExpression<StudySessionRecord>()
//                .withHashKeyValues(sessionRecord)
//                .withIndexName("sessionId")
//                .withConsistentRead(false);
//
//        List<StudySessionRecord> queryResult = mapper.query(StudySessionRecord.class, queryExpression);
//
//        if (!queryResult.isEmpty()) {
//            return queryResult.get(0);
//        } else {
//            return null; // or handle the case when no record is found
//        }
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":sessionId", new AttributeValue().withS(sessionRecord.getSessionId()));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("sessionId = :sessionId")
                .withExpressionAttributeValues(valueMap);

        return mapper.scan(StudySessionRecord.class, scanExpression).get(0);

    }


    //TODO create a findSessionsByUserId.... and others after testing these
    //maybe use this?>? Implement and test later??
    public List<StudySessionRecord> findSessionsByUserId(String userId) {
        StudySessionRecord sessionRecord = new StudySessionRecord();
        sessionRecord.setUserId(userId);

        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":userId", new AttributeValue().withS(sessionRecord.getUserId()));

//        DynamoDBQueryExpression<StudySessionRecord> queryExpression = new DynamoDBQueryExpression<StudySessionRecord>()
////                .withHashKeyValues(sessionRecord)
//                .withKeyConditionExpression("userId = :userId")
//                .withExpressionAttributeValues(valueMap);
////                .withIndexName("userId")
////                .withConsistentRead(false);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("userId = :userId")
                .withExpressionAttributeValues(valueMap);

        return mapper.scan(StudySessionRecord.class, scanExpression);
    }

}
