package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.MemberRepository;
import com.kenzie.appserver.repositories.model.MemberRecord;
import com.kenzie.appserver.service.model.Member;
import com.kenzie.appserver.service.model.MemberValidationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;


import java.util.NoSuchElementException;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserLogInServiceTest {


    private MemberRepository memberRepository;
    private UserLoginService userLoginService;

    @BeforeEach
    void setup() {
        memberRepository = mock(MemberRepository.class);
        userLoginService = new UserLoginService(memberRepository);
    }

    @Test
    public void isValidEmail_validEmail_returnTrue() {
        String email = "person@aol.com";
        boolean isValidEmail = userLoginService.isValidEmail(email);
        assertTrue(isValidEmail);
    }

    @Test
    public void isValidEmail_invalidEmail_returnFalse() {
        String email = "invalidEmail.email";
        boolean isValidEmail = userLoginService.isValidEmail(email);
        assertFalse(isValidEmail);
    }

    @Test
    public void isValidPassword_validPassword_returnTrue() {
        String password = "Algorithm1!";
        boolean isValid = userLoginService.isPasswordStrengthGood(password);
        assertTrue(isValid);
    }
    @Test
    public void isValidPassword_passwordWithNoUpperCase_returnFalse() {
        String password = "algorithm1!";
        boolean isValid = userLoginService.isPasswordStrengthGood(password);
        assertFalse(isValid);
    }

    @Test
    public void isValidPassword_passwordWithNoLowerCase_returnFalse() {
        String password = "ALGORITHM1!";
        boolean isValid = userLoginService.isPasswordStrengthGood(password);
        assertFalse(isValid);
    }

    @Test
    public void isValidPassword_passwordWithNoCharacter_returnFalse() {
        String password = "algorithm1";
        boolean isValid = userLoginService.isPasswordStrengthGood(password);
        assertFalse(isValid);
    }

    @Test
    public void hashPassword_idempotency() {

        String clearText = "amethyst";
        String firstHash = userLoginService.hashPassword(clearText);
        String secondHash = userLoginService.hashPassword(clearText);

        assertEquals(firstHash, secondHash);
    }
    @Test
    public void hashPassword_correctHashReturned() {
        String clearText = "amethyst";
        String hashedPassword = userLoginService.hashPassword(clearText);
        System.out.println("hash: "+ hashedPassword);

        assertEquals("88670bf6f7563a74404a67a9f22b0c09d6fe5e458e0a7568aa6efa73baf8825e", hashedPassword);
    }

    @Test
    public void authenticateUser_validatePassword_PasswordMatches() {
        // Arrange
        String email = "person@aol.com";
        String password = "amethyst";
        String hashedPassword = "88670bf6f7563a74404a67a9f22b0c09d6fe5e458e0a7568aa6efa73baf8825e";

        MemberRecord storedUser = new MemberRecord();
        storedUser.setEmail(email);
        storedUser.setPassword(hashedPassword);

        when(memberRepository.findById(email)).thenReturn(Optional.of(storedUser));

        MemberValidationStatus result = userLoginService.authenticateUser(email, password);

        assertTrue(result.isUserFound());
        assertTrue(result.isPasswordMatched());
        verify(memberRepository).findById(email);
    }
    @Test
    public void authenticateUser_validatePassword_PasswordDoesNotMatches() {
        // Arrange
        String email = "person@aol.com";
        String password = "amethyst";
        String hashedPassword = "wrongPassword";

        MemberRecord storedUser = new MemberRecord();
        storedUser.setEmail(email);
        storedUser.setPassword(hashedPassword);

        when(memberRepository.findById(email)).thenReturn(Optional.of(storedUser));

        MemberValidationStatus result = userLoginService.authenticateUser(email, password);

        assertTrue(result.isUserFound());
        assertFalse(result.isPasswordMatched());
        verify(memberRepository).findById(email);
    }

    @Test
    public void authenticateUser_UserNotFound() {
        // Arrange
        String email = "notThere@aol.com";
        String password = "amethyst";

        Mockito.when(memberRepository.findById(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userLoginService.authenticateUser(email,password);
        });
        verify(memberRepository).findById(email);
    }

    @Test
    public void doesUserExist_ExistingUser() {
        String email = "person@aol.com";
        MemberRecord existingUser = new MemberRecord();
        existingUser.setEmail(email);

        when(memberRepository.findById(email)).thenReturn(Optional.of(existingUser));

        boolean result = userLoginService.doesUserExist(email);

        Assertions.assertTrue(result);
        verify(memberRepository).findById(email);
    }

    @Test
    public void doesUserExist_NonExistingUser() {
        String email = "person@aol.com";

        Mockito.when(memberRepository.findById(email)).thenReturn(Optional.empty());

        boolean result = userLoginService.doesUserExist(email);

        Assertions.assertFalse(result);
        verify(memberRepository).findById(email);
    }

    @Test
    public void registerUser_registersUser() {
        String email = "person@aol.com";
        String password = "amethyst";
        Member user = new Member(email,password);

        ArgumentCaptor<MemberRecord> recordCaptor = ArgumentCaptor.forClass(MemberRecord.class);

        Member result = userLoginService.registerUser(user);

        verify(memberRepository).save(recordCaptor.capture());
        MemberRecord savedRecord = recordCaptor.getValue();
        Assertions.assertEquals(user.getEmail(), savedRecord.getEmail());
        Assertions.assertEquals(user.getPassword(), savedRecord.getPassword());
        Assertions.assertEquals(user, result);
    }

    @Test
    public void registerUser_withNoEmail_doesNotRegistersUser() {
        String email = "";
        String password = "amethyst";
        Member user = new Member(email, password);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userLoginService.registerUser(user);
        });
    }
    @Test
    public void registerUser_withNoPassword_doesNotRegistersUser() {
        String email = "person@aol.com";
        String password = "";
        Member user = new Member(email,password);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userLoginService.registerUser(user);
        });
    }


}






