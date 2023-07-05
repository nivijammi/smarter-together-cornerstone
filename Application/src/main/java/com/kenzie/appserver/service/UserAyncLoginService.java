package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.MemberRepository;
import com.kenzie.appserver.repositories.model.MemberRecord;
import com.kenzie.appserver.service.model.Member;
import com.kenzie.appserver.service.model.MemberValidationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;

/**
 * https://stackoverflow.com/questions/67708402/writing-asynchronous-rest-api-using-spring-boot-and-completablefuture-and-its-th
 */
@Service
public class UserAyncLoginService {
    @Autowired
    private MemberRepository memberRepository;
    private Executor customForkJoinPoolExecutor = new ForkJoinPool();

    public UserAyncLoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Step towards non-blocking
     * https://stackoverflow.com/questions/43019126/completablefuture-thenapply-vs-thencompose
     *
     */
    public CompletableFuture<MemberValidationStatus> authenticateUserAsync(String email, String password) {
        CompletableFuture<MemberValidationStatus> memberValidationStatus = CompletableFuture.supplyAsync(() -> authenticateUser(email, password), customForkJoinPoolExecutor);
        return memberValidationStatus;
    }

    // have time hash it on front end
    // backend should always just see a hash.
    public MemberValidationStatus authenticateUser(String email, String password) {
        // hash the pass word and save it in the database

        // Retrieve the storedUser by email from the repository
        //MemberRecord storedUser = memberRepository.findMemberById(email);
        Optional<MemberRecord> findById = memberRepository.findById(email);//Bl

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

    //    public CompletableFuture<List<String>> getSomethingAsync(String groupId) {
    //        CompletableFuture<List<String>> users = CompletableFuture.supplyAsync(() -> getUsers(groupId));
    //        return users;
    //    }
    //    private List<String> getUsers(String groupId) {
    //        List<String> users = new ArrayList<>();
    //        for (int i=0; i<1000; i++){
    //            users.add("User"+i);
    //        }
    //        return users;
    //    }
}

