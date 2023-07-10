package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.NoteResponse;
import com.kenzie.appserver.controller.model.UserProfileRequest;
import com.kenzie.appserver.controller.model.UserProfileResponse;
import com.kenzie.appserver.repositories.converter.ZonedDateTimeConverter;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.Note;
import com.kenzie.appserver.service.model.User;
import com.kenzie.capstone.service.client.StudySessionServiceClient;
import com.kenzie.capstone.service.model.StudySession;
import com.kenzie.capstone.service.model.StudySessionRequest;
import com.kenzie.capstone.service.model.StudySessionResponse;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserProfileController {
    // todo: missing an annotation
    @Autowired
    private UserService userService;

    @Autowired
    private StudySessionServiceClient studySessionServiceClient;

    UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserProfileResponse> addNewUser(@RequestBody UserProfileRequest userProfileRequest) {

        if (userProfileRequest.getEmail() == null || userProfileRequest.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User Name");
        }

        UserProfileResponse response = userService.addNewUser(userProfileRequest);

        return ResponseEntity.created(URI.create("/users/" + response.getEmail())).body(response);
    }

    @PutMapping("/{userID}")
    public ResponseEntity<UserProfileResponse> updateUserProfile(@PathVariable String email, @RequestBody UserProfileRequest userProfileRequest) {
        if (userProfileRequest.getEmail() == null || userProfileRequest.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is required");
        }

        User existingUser = userService.findByUserId(email);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        existingUser.setEmail(userProfileRequest.getEmail());
        existingUser.setPassword(userProfileRequest.getPassword());
        existingUser.setLastName(userProfileRequest.getLastName());
        existingUser.setFirstName(userProfileRequest.getFirstName());
        existingUser.setCreationDate(new ZonedDateTimeConverter().unconvert(userProfileRequest.getCreationDate()));

        userService.updateUser(existingUser);

        UserProfileResponse userResponse = convertToUserProfileResponse(existingUser);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{userID}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable("userId") String userId) {
        UserProfileResponse userProfileResponse = userService.getUser(userId);
        if (userProfileResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userProfileResponse);
    }

    private UserProfileResponse convertToUserProfileResponse(User user) {
        return new UserProfileResponse(user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                new ZonedDateTimeConverter().convert(user.getCreationDate()));
    }

    @DeleteMapping("/{userID}")
    public ResponseEntity deleteUserById(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Lambda connections
     */

    @PostMapping("/studysession/add")
    public ResponseEntity<StudySessionResponse> addStudySession(@RequestBody StudySessionRequest studySessionRequest) {
        if (studySessionRequest.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UserId - error from application");
        }
        StudySessionResponse studySessionResponse = studySessionServiceClient.addStudySession(studySessionRequest);

        return ResponseEntity.ok(studySessionResponse);
    }

    //  public boolean deleteStudySessionBySessionId(String sessionId) {
    @DeleteMapping("/studysession/delete/{sessionId}")
    public ResponseEntity deleteStudySessionBySessionId(@PathVariable("sessionId") String sessionId) {
        studySessionServiceClient.deleteStudySessionBySessionId(sessionId);
        return ResponseEntity.ok().build();
    }

    // public StudySession getStudySessionBySessionId(String sessionId) {
    @GetMapping("studysession/session/{sessionId}")
    public ResponseEntity<StudySession> getStudySessionBySessionId(@PathVariable("sessionId") String sessionId) {
        StudySession studySession = studySessionServiceClient.getStudySessionBySessionId(sessionId);

        if (studySession == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studySession);
    }


    //public List<StudySession> getStudySessionsByUserId(String userId) {
    @GetMapping("studysession/{userId}")
    public ResponseEntity<List<StudySession>> getStudySessionsByUserId(@PathVariable("userId") String userId) {
        List<StudySession> studySessionList = studySessionServiceClient.getStudySessionsByUserId(userId);

        if (studySessionList == null || studySessionList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(studySessionList);

    }

    @GetMapping("studysession/subject/{subject}")
    public ResponseEntity<List<StudySession>> getStudySessionsBySubject(@PathVariable("subject") String subject) {
        List<StudySession> studySessionList = studySessionServiceClient.getStudySessionsBySubject(subject);

        if (studySessionList == null || studySessionList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(studySessionList);
    }

    @GetMapping("studysession/all")
    public ResponseEntity<List<StudySession>> getAllStudySessions() {
        List<StudySession> studySessionList = studySessionServiceClient.getAllStudySessions();

        if (studySessionList == null || studySessionList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(studySessionList);
    }
}