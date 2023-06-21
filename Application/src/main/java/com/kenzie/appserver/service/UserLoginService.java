package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.UserLoginRepository;
import com.kenzie.appserver.repositories.model.UserLoginRecord;
import com.kenzie.appserver.service.model.LogInValidationResult;
import com.kenzie.appserver.service.model.User;
import com.kenzie.appserver.service.model.UserDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;


public class UserLoginService {


    // source: https://stackoverflow.com/questions/8204680/java-regex-email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final String ERROR_CODE = "4000";
    private static final String APPROVAL_CODE = "2000";
    private UserLoginRepository userLoginRepository;


    public UserLoginService(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

    public boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * checks for the length, presence of uppercase and lowercase letters, digits, and special characters.
     * @param password
     * @return
     */
    public boolean isValidPassword(String password) {
        // password validation logic here
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
        // Check if all requirements met
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;

    }
    // have time hash it on front end
    // backend should always just see a hash.
    public LogInValidationResult authenticateUser(String email, String password) {
        // "test@example.com".equals(email) && "password123".equals(password)
       // hash the pass word and save it in the database
        List<User> users = new ArrayList<>();
        Iterable<UserLoginRecord> userLoginRecords = userLoginRepository.findAll();
        LogInValidationResult result = new LogInValidationResult();
        //System.out.println("New user email: " + email);

        for (UserLoginRecord record : userLoginRecords) {
            System.out.println("Registered user: " + email);
            boolean isPasswordValid = isValidPassword(password);

            if (isValidEmail(email)) {
                // if password is valid but user email and password already exists
                if (record.getUserEmail().equals(email) && isPasswordValid) {
                    System.out.println("User Already exists! and password is valid");
                    result.setLogInAuthorizationCode(APPROVAL_CODE);
                    return result;

                    // If password is valid but user does not exist, then add it to the database
                } else if (!record.getUserEmail().equals(email) && isPasswordValid) {
                    System.out.println("New user! and password is valid");
                    record.setUserEmail(email);
                    record.setUserPassword(password);
                    record.setLogInAuthorizationCode(APPROVAL_CODE);
                    userLoginRepository.save(record);
                    result.setLogInAuthorizationCode(APPROVAL_CODE);
                    return result;
                } else {
                    // if password is not valid
                    result.setLogInAuthorizationCode(ERROR_CODE);
                    return result;
                }
            }
            result.setLogInAuthorizationCode(ERROR_CODE);
            return result;
        }
        return result;
    }

    public UserDetail addNewUserDetails(UserDetail user) {

        UserLoginRecord record = new UserLoginRecord();
        record.setUserEmail(user.getUserEmail());
        record.setUserPassword(user.getUserPassWord());
        userLoginRepository.save(record);
        return user;
    }
}
