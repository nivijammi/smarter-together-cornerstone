package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.MemberRepository;
import com.kenzie.appserver.repositories.model.MemberRecord;
import com.kenzie.appserver.service.model.MemberValidationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserAyncLoginServiceTest {

    private MemberRepository memberRepository;
    private UserAyncLoginService subject;

    @BeforeEach
    void setup() {
        memberRepository = mock(MemberRepository.class);
        subject = new UserAyncLoginService(memberRepository);
    }

    @Test
    public void authenticateUser_validatePassword_PasswordMatches() { //throws ExecutionException, InterruptedException
        String email = "person@aol.com";
        String password = "amethyst";
        String hashedPassword = "88670bf6f7563a74404a67a9f22b0c09d6fe5e458e0a7568aa6efa73baf8825e";

        MemberRecord storedUser = new MemberRecord();
        storedUser.setEmail(email);
        storedUser.setPassword(hashedPassword);

        when(memberRepository.findById(email)).thenReturn(Optional.of(storedUser));

        CompletableFuture<MemberValidationStatus> result = subject.authenticateUserAsync(email, password);
        try {
            MemberValidationStatus memberValidationStatus = result.get();
            assertTrue(memberValidationStatus.isUserFound());
            assertTrue(memberValidationStatus.isPasswordMatched());
            verify(memberRepository).findById(email);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void authenticateUser_validatePassword_PasswordDoesNotMatches() {

        String email = "person@aol.com";
        String password = "amethyst";
        String hashedPassword = "wrongPassword";

        MemberRecord storedUser = new MemberRecord();
        storedUser.setEmail(email);
        storedUser.setPassword(hashedPassword);

        when(memberRepository.findById(email)).thenReturn(Optional.of(storedUser));

        CompletableFuture<MemberValidationStatus> result = subject.authenticateUserAsync(email, password);

        try {
            MemberValidationStatus memberValidationStatus = result.get();
            assertTrue(memberValidationStatus.isUserFound());
            assertFalse(memberValidationStatus.isPasswordMatched());
            verify(memberRepository).findById(email);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void authenticateUser_userNotFound() {

        String email = "test@example.com";
        String password = "password";
        when(memberRepository.findById(email)).thenReturn(Optional.empty());


        CompletableFuture<MemberValidationStatus> result = subject.authenticateUserAsync(email, password);

        try {
            MemberValidationStatus memberValidationStatus = result.get();
            assertFalse(memberValidationStatus.isUserFound());
            assertFalse(memberValidationStatus.isPasswordMatched());
            verify(memberRepository).findById(email);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void hashPassword_idempotency() {

        String clearText = "amethyst";
        String firstHash = subject.hashPassword(clearText);
        String secondHash = subject.hashPassword(clearText);

        assertEquals(firstHash, secondHash);
    }
    @Test
    public void hashPassword_correctHashReturned() {
        String clearText = "amethyst";
        String hashedPassword = subject.hashPassword(clearText);
        System.out.println("hash: "+ hashedPassword);

        assertEquals("88670bf6f7563a74404a67a9f22b0c09d6fe5e458e0a7568aa6efa73baf8825e", hashedPassword);
    }

}