package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.UserLoginRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


    @EnableScan
    public interface UserLoginRepository extends CrudRepository<UserLoginRecord, String> {
        // imp: has to match the Record
        UserLoginRecord findByUserEmail(String userEmail);
    }

