package com.kenzie.appserver.controller;


import com.kenzie.appserver.service.StudySessionServiceApp;
import com.kenzie.capstone.service.model.StudySessionRequest;
import com.kenzie.capstone.service.model.StudySessionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class StudySessionController {

    @Autowired
    private StudySessionServiceApp studySessionServiceApp;

    @PostMapping("/session/add")
    public ResponseEntity<StudySessionResponse> addStudySession(@RequestBody StudySessionRequest request) {

        return null;
    }



}
