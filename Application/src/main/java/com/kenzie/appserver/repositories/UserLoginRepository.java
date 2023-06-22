package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.UserLoginRecord;
import com.kenzie.appserver.service.model.User;
import com.kenzie.appserver.service.model.UserDetail;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


    @EnableScan
    public interface UserLoginRepository extends CrudRepository<UserLoginRecord, String> {
        UserDetail findByEmail(String email);
    }

