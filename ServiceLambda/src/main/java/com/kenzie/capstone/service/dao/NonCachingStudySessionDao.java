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

import java.util.List;

public class NonCachingStudySessionDao implements StudySessionDao{

    private DynamoDBMapper mapper;
    static final Logger log = LogManager.getLogger();


    public NonCachingStudySessionDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public StudySessionRecord addStudySession(StudySessionRecord session) {
        try {
            mapper.save(session, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "SessionId",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new InvalidDataException("StudySession already exists");
        }

        return session;
    }

    public boolean deleteStudySession(StudySessionRecord session){
        try{
            mapper.delete(session, new DynamoDBDeleteExpression()
                    .withExpected(ImmutableMap.of(
                            "SessionId",
                            new ExpectedAttributeValue().withValue(new AttributeValue(session.getSessionId())).withExists(true)
                    )));
        } catch (AmazonDynamoDBException e ) {
            log.info(e.getMessage());
            log.info(e.getStackTrace());
            return false;
        }

        return true;
    }

    public List<StudySessionRecord> findSessionBySubject(String subject){
        StudySessionRecord sessionRecord = new StudySessionRecord();
        sessionRecord.setSubject(subject);

        DynamoDBQueryExpression<StudySessionRecord> queryExpression = new DynamoDBQueryExpression<StudySessionRecord>()
                .withHashKeyValues(sessionRecord)
                .withIndexName("SubjectIndex")
                .withConsistentRead(false);

        return mapper.query(StudySessionRecord.class, queryExpression);
    }

    public List<StudySessionRecord> findAllSessions(){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("SessionId");  //will it find all?

        return mapper.scan(StudySessionRecord.class, scanExpression);
    }


    //TODO create a findSessionsByUserId.... and others after testing these
    //maybe use this?>? Implement and test later??
    public List<StudySessionRecord> findSessionsByUserId(String userId) {
        StudySessionRecord sessionRecord = new StudySessionRecord();
        sessionRecord.setSubject(userId);

        DynamoDBQueryExpression<StudySessionRecord> queryExpression = new DynamoDBQueryExpression<StudySessionRecord>()
                .withHashKeyValues(sessionRecord)
                .withIndexName("UserIdIndex")
                .withConsistentRead(false);

        return mapper.query(StudySessionRecord.class, queryExpression);
    }

}
