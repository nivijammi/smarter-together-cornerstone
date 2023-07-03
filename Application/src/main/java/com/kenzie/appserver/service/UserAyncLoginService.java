package com.kenzie.appserver.service;

import com.amazonaws.services.ec2.model.EgressOnlyInternetGateway;
import com.kenzie.appserver.repositories.MemberRepository;
import com.kenzie.appserver.repositories.model.MemberRecord;
import com.kenzie.appserver.service.model.Member;
import com.kenzie.appserver.service.model.MemberValidationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

@Service
public class UserAyncLoginService {

    // source: https://stackoverflow.com/questions/8204680/java-regex-email

    @Autowired
    private MemberRepository memberRepository;
    public UserAyncLoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    /**
     * Checks for email validation
     * */

    public boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * checks for the length, presence of uppercase and lowercase letters, digits, and special characters.
     *
     * @param password
     * @return
     */
    public boolean isPasswordStrengthGood(String password) {
        // password validation logic
        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // Special characters
        String charList = "!@#$%^&*()-_+=~`[]{}|:;\"<>,.?/";

        // Iterate over each character in the password
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (charList.contains(String.valueOf(c))) {
                hasSpecialChar = true;
            }
        }
        // if all requirements met
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;

    }

    // source : https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
    //Applies Sha256 to a string and returns the result.
    public String hashPassword(String clearText){

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Applies sha256 to our clearText,
            byte[] hash = digest.digest(clearText.getBytes("UTF-8"));
            StringBuffer hashedHexStrBuffer = new StringBuffer(); // This will contain hash as hexidecimal
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hashedHexStrBuffer.append('0');
                hashedHexStrBuffer.append(hex);
            }
            return hashedHexStrBuffer.toString();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


    public CompletableFuture<MemberValidationStatus> authenticateUserAsync(String email, String password) {
        CompletableFuture<MemberValidationStatus> memberValidationStatus = CompletableFuture.supplyAsync(() -> authenticateUser(email, password));
        return memberValidationStatus;
    }


    // have time hash it on front end
    // backend should always just see a hash.
    public MemberValidationStatus authenticateUser(String email, String password) {
        // hash the pass word and save it in the database

        // Retrieve the storedUser by email from the repository
        //MemberRecord storedUser = memberRepository.findMemberById(email);
        Optional<MemberRecord> findById = memberRepository.findById(email);

        // Scenario #1: user not found
        // convert it into userDetail
        if (findById.isEmpty()) {
            return new MemberValidationStatus(false, false);
        }

        MemberRecord storedUser = findById.get();

        // Check if a storedUser with the given email exists and if the password matches
        // Scenario #2: found the record,

        String hashedStoredPassword = storedUser.getPassword();
        String hashedUserProvidedPassword = hashPassword(password);

        // Scenario #2.1 check stored password [hashed] does not match the storedUser provided password [to be hashed]
        if(!hashedStoredPassword.equals(hashedUserProvidedPassword)){
            System.out.println("Password does not match");
            return new MemberValidationStatus(true, false);
        }

        // Scenario #2.2 stored password [hashed] matches the storedUser provided password [to be hashed]
        System.out.println("User found and password matches");
        return new MemberValidationStatus(true, true);
    }

    public boolean doesUserExist(String email) {
        //MemberRecord user = memberRepository.findMemberById(userEmail);
        Optional<MemberRecord> findById = memberRepository.findById(email);

        if (findById.isEmpty()) {
            return false;
        }

        MemberRecord storedUser = findById.get();
        return true;
    }

    public Member registerUser(Member user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty or null");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty or null");
        }
        MemberRecord record = new MemberRecord();
        record.setEmail(user.getEmail());
        record.setPassword(user.getPassword());
        memberRepository.save(record);
        return user;
    }

    public CompletableFuture<List<String>> getSomethingAsync(String groupId) {
        CompletableFuture<List<String>> users = CompletableFuture.supplyAsync(() -> getUsers(groupId));
        return users;
    }

    private List<String> getUsers(String groupId) {
        System.out.println(groupId);
        //Iterable<MemberRecord> memberRecords = memberRepository.findAll();
        List<String> users = new ArrayList<>();
        for (int i=0; i<1000; i++){
            users.add("User"+i);
        }
        return users;
    }
}

