package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.UserLoginRepository;
import com.kenzie.appserver.repositories.model.UserLoginRecord;
import com.kenzie.appserver.service.model.UserDetail;
import com.kenzie.appserver.service.model.UserValidationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.regex.Pattern;

@Service
public class UserLoginService {


    // source: https://stackoverflow.com/questions/8204680/java-regex-email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Autowired
    private UserLoginRepository userLoginRepository;


    public UserLoginService(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

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



    // have time hash it on front end
    // backend should always just see a hash.
    public UserValidationStatus authenticateUser(String email, String password) {
        // "test@example.com".equals(email) && "password123".equals(password)
        // hash the pass word and save it in the database

        // Retrieve the storedUser by email from the repository
        UserLoginRecord storedUser = userLoginRepository.findByUserEmail(email);
        // Scenario #1: user not found
        // convert it into userDetail
        if (storedUser == null) {
            return new UserValidationStatus(false, false);
        }

        // Check if a storedUser with the given email exists and if the password matches
        // Scenario #2: found the record,

        String hashedStoredPassword = storedUser.getUserPassword();
        String hashedUserProvidedPassword = hashPassword(password);

        // Scenario #2.1 check stored password [hashed] does not match the storedUser provided password [to be hashed]
        if(!hashedStoredPassword.equals(hashedUserProvidedPassword)){
            System.out.println("Password does not match");
            return new UserValidationStatus(true, false);
        }

        // Scenario #2.2 stored password [hashed] matches the storedUser provided password [to be hashed]
        System.out.println("User found and password matches");
        return new UserValidationStatus(true, true);
    }

    public boolean doesUserExist(String userEmail) {
        UserLoginRecord user = userLoginRepository.findByUserEmail(userEmail);
        if(user != null){
            return true;
        }
        return false;
    }

    public UserDetail registerUser(UserDetail user) {
        UserLoginRecord record = new UserLoginRecord();
        record.setUserEmail(user.getUserEmail());
        record.setUserPassword(user.getHashedPassword());
        userLoginRepository.save(record);
        return user;
    }





}
