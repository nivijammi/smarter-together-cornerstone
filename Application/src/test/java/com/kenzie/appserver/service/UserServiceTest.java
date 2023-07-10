package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.UserProfileRequest;
import com.kenzie.appserver.controller.model.UserProfileResponse;
import com.kenzie.appserver.exception.UserNotFoundException;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.converter.ZonedDateTimeConverter;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService subject;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        subject = new UserService(userRepository);
    }

    @Test
    void addNewUser_validUserId_addsUser(){
        UserProfileRequest request = new UserProfileRequest();
        String firstName = "name1";
        String lastName = "lastName1";
        String email = "name.lastname1@aol.com";
        String password = "Password1!";
        String creationDate = "2016-08-22";

        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);
        request.setPassword(password);
        request.setCreationDate(creationDate);


        UserRecord record = new UserRecord();
        record.setFirstName(firstName);
        record.setLastName(lastName);
        record.setEmail(email);
        record.setPassword(password);
        record.setDateCreated(new ZonedDateTimeConverter().unconvert(creationDate));

        UserProfileResponse response = subject.addNewUser(request);


        verify(userRepository, times(1)).save(record);

        assertNotNull(response);
        assertEquals(firstName, response.getFirstName());
        assertEquals(lastName, response.getLastName());
        assertEquals(email, response.getEmail());

        assertNotNull(response.getPassword());
        assertNotNull(response.getCreationDate());

    }
    @Test
    public void ValidUserId_validId() {
        String validId = "validId";
        when(userRepository.existsById(validId)).thenReturn(true);

        assertDoesNotThrow(() -> {
            subject.isValidUserId(validId);
        });
        verify(userRepository, times(1)).existsById(validId);
    }

    @Test
    public void createUserRecord_createsARecord() {

        UserProfileRequest userProfileRequest = new UserProfileRequest();
        String firstName = "name1";
        String lastName = "lastName1";
        String email = "name.lastname1@aol.com";
        String password = "Password1!";
        String creationDate = "2016-08-22";

        userProfileRequest.setEmail(email);
        userProfileRequest.setPassword(password);
        userProfileRequest.setLastName(lastName);
        userProfileRequest.setFirstName(firstName);
        userProfileRequest.setCreationDate(creationDate);

        String userId = email;

        UserRecord record = subject.createUserRecord(userProfileRequest, userId);

        Assertions.assertEquals(userId, record.getEmail());
        Assertions.assertEquals(userProfileRequest.getPassword(), record.getPassword());
        Assertions.assertEquals(userProfileRequest.getLastName(), record.getLastName());
        Assertions.assertEquals(userProfileRequest.getFirstName(), record.getFirstName());

    }

    @Test
    public void recordToResponse_WithNullRecord() {
        UserRecord record = null;
        UserProfileResponse response = subject.recordToResponse(record);
        assertNull(response);
    }

    @Test
    void addNewUser_invalidUserId_throwException() {
        UserProfileRequest request = new UserProfileRequest();
        request.setEmail("");

        assertThrows(ResponseStatusException.class, () -> subject.addNewUser(request));
    }

    // source : Coach Elise
    @Test
    void addNewUser_duplicateUserId_throwException() {

        UserProfileRequest request = new UserProfileRequest();
        String firstName = "name1";
        String lastName = "lastName1";
        String email = "name.lastname1@aol.com";
        String password = "Password1!";
        String creationDate = "2016-08-22";

        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);
        request.setPassword(password);
        request.setCreationDate(creationDate);


        UserRecord record = new UserRecord();
        record.setFirstName(firstName);
        record.setLastName(lastName);
        record.setEmail(email);
        record.setPassword(password);
        record.setDateCreated(new ZonedDateTimeConverter().unconvert(creationDate));


        doThrow(ResponseStatusException.class).when(userRepository).save(record);
        assertThrows(ResponseStatusException.class, () -> subject.addNewUser(request));
    }

    @Test
    void findByUserId_existingUser_returnUser() {
        String userId = "name.lastname2@aol.com";

        String firstName = "name2";
        String lastName = "lastName2";
        String email = "name.lastname2@aol.com";
        String password = "Password1!";
        String creationDate = "2016-08-22";


        UserRecord record = new UserRecord();
        record.setEmail(email);
        record.setPassword(password);
        record.setLastName(lastName);
        record.setFirstName(firstName);
        record.setDateCreated(new ZonedDateTimeConverter().unconvert(creationDate));

        when(userRepository.findById(userId)).thenReturn(Optional.of(record));
        User result = subject.findByUserId(userId);

        verify(userRepository, times(1)).findById(userId);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword());
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
    }

    @Test
    void findByUserId_nonExistingUser_returnNull() {

        String userId = "name.lastname2@aol.com";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User result = subject.findByUserId(userId);

        verify(userRepository, times(1)).findById(userId);

        assertNull(result);
    }

    @Test
    void updateUser_existingUser_updateUserRecord() {
        String firstName3 = "name3";
        String lastName3 = "lastName3";
        String email3 = "name.lastname3@aol.com";
        String password3 = "Password3!";
        String creationDate3 = "2016-08-22";

        String firstName4 = "name4";
        String lastName4 = "lastName4";
        String password4 = "Password4!";
        String creationDate4 = "2016-08-22";


        User user = new User(email3, password3,
                lastName3, firstName3, new ZonedDateTimeConverter().unconvert(creationDate3));


        UserRecord existingUserRecord = new UserRecord();
        existingUserRecord.setEmail(email3);
        existingUserRecord.setPassword(password4);
        existingUserRecord.setLastName(lastName4);
        existingUserRecord.setFirstName(firstName4);
        existingUserRecord.setDateCreated(new ZonedDateTimeConverter().unconvert(creationDate4));

        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(existingUserRecord));

        subject.updateUser(user);

        verify(userRepository, times(1)).findById(user.getEmail());
        verify(userRepository, times(1)).save(existingUserRecord);

        assertEquals(user.getEmail(), existingUserRecord.getEmail());
        assertEquals(user.getPassword(), existingUserRecord.getPassword());
        assertEquals(user.getLastName(), existingUserRecord.getLastName());
        assertEquals(user.getFirstName(), existingUserRecord.getFirstName());
        assertEquals(user.getCreationDate(), existingUserRecord.getDateCreated());
    }


    @Test
    void updateUser_nonExistingUser_throwUserNotFoundException() {
        String firstName3 = "name3";
        String lastName3 = "lastName3";
        String email3 = "name.lastname3@aol.com";
        String password3 = "Password3!";
        String creationDate3 = "2016-08-22";


        User user = new User(email3, password3,
                lastName3, firstName3, new ZonedDateTimeConverter().unconvert(creationDate3));


        when(userRepository.findById(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> subject.updateUser(user));

        verify(userRepository, times(1)).findById(user.getEmail());
    }

    @Test
    void getUser_existingUser_returnUserProfileResponse() {
        String userId = "name.lastname6@aol.com";

        String firstName6 = "name6";
        String lastName6 = "lastName6";
        String email6 = "name.lastname6@aol.com";
        String password6 = "Password6!";
        String creationDate6 = "2016-08-22";

        UserRecord userRecord = new UserRecord();
        userRecord.setEmail(email6);
        userRecord.setPassword(password6);
        userRecord.setLastName(lastName6);
        userRecord.setFirstName(firstName6);
        userRecord.setDateCreated(new ZonedDateTimeConverter().unconvert(creationDate6));

        when(userRepository.findById(userId)).thenReturn(Optional.of(userRecord));

        UserProfileResponse response = subject.getUser(userId);

        verify(userRepository, times(1)).findById(userId);

        assertNotNull(response);
        assertEquals(userRecord.getEmail(), response.getEmail());
        assertEquals(userRecord.getPassword(), response.getPassword());
        assertEquals(userRecord.getLastName(), response.getLastName());
        assertEquals(userRecord.getFirstName(), response.getFirstName());
        assertEquals(new ZonedDateTimeConverter().convert(userRecord.getDateCreated()), response.getCreationDate());
    }

    @Test
    void getUser_nonExistingUser_returnNull() {
        String userId = "name.lastname6@aol.com";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        UserProfileResponse response = subject.getUser(userId);
        assertNull(response);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void deleteUser_existingUser_userDeleted() {
        String userId = "name.lastname7@aol.com";

        subject.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void isValidUserId_userValid() {
        String validId = "validId";
        when(userRepository.existsById(validId)).thenReturn(true);

        boolean result = subject.isValidUserId(validId);

        assertTrue(result);
        verify(userRepository, times(1)).existsById(validId);
    }




}
