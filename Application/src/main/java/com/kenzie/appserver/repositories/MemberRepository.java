package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.MemberRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface MemberRepository extends CrudRepository<MemberRecord, String> {
    // imp: has to match the Record
    Optional<MemberRecord> findById(String email); // memberId is email
}

