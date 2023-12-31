package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.UserProfileRequest;
import com.kenzie.appserver.controller.model.UserProfileResponse;
import com.kenzie.appserver.exception.UserNotFoundException;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.converter.ZonedDateTimeConverter;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import com.kenzie.capstone.service.client.StudySessionServiceClient;
import com.kenzie.capstone.service.model.StudySession;
import com.kenzie.capstone.service.model.StudySessionRequest;
import com.kenzie.capstone.service.model.StudySessionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudySessionServiceClient studySessionServiceClient;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse addNewUser(UserProfileRequest userProfileRequest) {
        String email = userProfileRequest.getEmail();

        if (email != null && email.isEmpty()) { //&& !isValidUserId(email)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User Id");
        }
        if(isValidUserId(email)){
            UserRecord existingRecord = createUserRecord(userProfileRequest, email);
            return recordToResponse(existingRecord);

        }
        UserRecord record = createUserRecord(userProfileRequest, email);

        userRepository.save(record);

        return recordToResponse(record);
    }

    public User findByUserId(String id) {
        Optional<UserRecord> userById = userRepository.findById(id);
        if(userById.isEmpty()) {
            return null;
        }
        UserRecord userRecord = userById.get();
        // convert from record to study group(domain object)
        return buildUser(userRecord);
    }

    public void updateUser(User user) {
        Optional<UserRecord> existingUser = userRepository.findById(user.getEmail());
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found for userID: " + user.getEmail());
        }
        // Update the properties of the existing note with the new values
        UserRecord userRecord = existingUser.get();
        userRecord.setEmail(user.getEmail());
        userRecord.setPassword(user.getPassword());
        userRecord.setLastName(user.getLastName());
        userRecord.setFirstName(user.getFirstName());
        userRecord.setDateCreated(user.getCreationDate());
        userRepository.save(userRecord);
    }

    public UserProfileResponse getUser(String userId) {
        Optional<UserRecord> record = userRepository.findById(userId);

        return record.map(this::recordToResponse).orElse(null);

    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public boolean isValidUserId(String email) {
        return  userRepository.existsById(email); // false
    }

    public UserRecord createUserRecord(UserProfileRequest userProfileRequest, String userId) {
        UserRecord record = new UserRecord();
        record.setEmail(userId);
        record.setPassword(userProfileRequest.getPassword());
        record.setLastName(userProfileRequest.getLastName());
        record.setFirstName(userProfileRequest.getFirstName());
        record.setDateCreated(new ZonedDateTimeConverter().unconvert(userProfileRequest.getCreationDate()));

        return record;
    }

    public UserProfileResponse recordToResponse(UserRecord record) {

        if (record == null) {
            return null;
        }

        return new UserProfileResponse(record.getEmail(),
                record.getPassword(),
                record.getFirstName(),
                record.getLastName(),
                new ZonedDateTimeConverter().convert(record.getDateCreated()));
    }

    private User buildUser(UserRecord record) {
        return new User(record.getEmail(), record.getPassword(),
                record.getLastName(), record.getFirstName(),
                record.getDateCreated());
    }
}