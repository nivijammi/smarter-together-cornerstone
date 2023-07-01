package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.StudySession;
import com.kenzie.capstone.service.model.StudySessionRequest;
import com.kenzie.capstone.service.model.StudySessionResponse;

import java.util.List;

public class StudySessionServiceClient {

    private static final String LOG_SESSION_ENDPOINT = "studysession/log";
    private static final String ADD_SESSION_ENDPOINT = "studysession/add";  //removed BySessionId /{sessionId}
    private static final String GET_SESSION_BY_SESSIONID_ENDPOINT = "studysession/session/{sessionId}";
    private static final String GET_SESSIONS_BY_SUBJECT_ENDPOINT = "studysession/subject/{subject}";
    private static final String GET_SESSIONS_BY_USERID_ENDPOINT = "studysession/{userId}";
    private static final String GET_ALL_SESSIONS_ENDPOINT = "studysession/all"; //all?
    private static final String DELETE_SESSION_ENDPOINT = "studysession/delete/{sessionId}";
    private static final String SET_STUDY_GOAL_ENDPOINT = "studysession/goals";  // study goals a part of the studysession client?
    private static final String GET_SESSION_STATISTICS = "studysession/stats/{userId}";  // same as last, not a part of studysession ?

    //TODO add in a version to add before the endpoint /v1

    private ObjectMapper mapper;

    public StudySessionServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public StudySessionResponse logStudySession(StudySessionRequest studySessionRequest) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(studySessionRequest);
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.postEndpoint(LOG_SESSION_ENDPOINT, request);
        StudySessionResponse studySessionResponse;
        try{
            studySessionResponse = mapper.readValue(response, StudySessionResponse.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return studySessionResponse;
    }

    public StudySessionResponse addStudySession(StudySessionRequest studySessionRequest) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(studySessionRequest);
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }


        String response = endpointUtility.postEndpoint(ADD_SESSION_ENDPOINT, request);
        StudySessionResponse studySessionResponse;
        try{
            studySessionResponse = mapper.readValue(response, StudySessionResponse.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return studySessionResponse;
    }



    public StudySession getStudySessionBySessionId(String sessionId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        //discuss get or put
        String response = endpointUtility.getEndpoint(GET_SESSION_BY_SESSIONID_ENDPOINT.replace("{sessionId}", sessionId));
        StudySession session;
        try {
            session = mapper.readValue(response, new TypeReference<StudySession>(){});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }

        return session;
    }

    public List<StudySession> getStudySessionsByUserId(String userId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_SESSIONS_BY_USERID_ENDPOINT.replace("{userId}", userId));
        List<StudySession> studySessions;
        try {
            studySessions = mapper.readValue(response, new TypeReference<List<StudySession>>(){});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return studySessions;
    }

    public List<StudySession> getStudySessionsBySubject(String subject) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_SESSIONS_BY_SUBJECT_ENDPOINT.replace("{subject}", subject));
        List<StudySession> studySessions;
        try {
            studySessions = mapper.readValue(response, new TypeReference<List<StudySession>>(){});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return studySessions;
    }

    public List<StudySession> getAllStudySessions() {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_ALL_SESSIONS_ENDPOINT);
        List<StudySession> studySessionList;
        try{
            studySessionList = mapper.readValue(response, new TypeReference<>(){});
        } catch(Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return studySessionList;
    }

    public boolean deleteStudySessionBySessionId(String sessionId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(sessionId);
        } catch(JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.postEndpoint(DELETE_SESSION_ENDPOINT.replace("{sessionId}", sessionId), request);

        boolean outcome;

        try {
            outcome = mapper.readValue(response, Boolean.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }

        return outcome;
    }

    public StudySession setStudyGoal() {

        return new StudySession();
    }

    public StudySession getStudySessionStats() {

        return new StudySession();
    }





}
