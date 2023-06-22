package com.kenzie.capstone.service.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.StudySessionRequest;

public class JsonStringToStudySessionConverter {

    public StudySessionRequest convert(String body) {
        try{
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            StudySessionRequest studySessionRequest = gson.fromJson(body, StudySessionRequest.class);
            return studySessionRequest;
        } catch (Exception e) {
            throw new InvalidDataException("StudySession could not be deserialized");
        }
    }
}
