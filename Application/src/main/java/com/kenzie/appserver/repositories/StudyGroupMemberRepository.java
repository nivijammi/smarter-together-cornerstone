package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.StudyGroupMemberId;
import com.kenzie.appserver.repositories.model.StudyGroupMemberRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface StudyGroupMemberRepository extends CrudRepository<StudyGroupMemberRecord,StudyGroupMemberId> {

    boolean existsById(String groupId);
    Optional<StudyGroupMemberRecord> findById(StudyGroupMemberId studyGroupMemberId);

    //Iterable<StudyGroupMemberRecord> findAllById(Iterable<String> groupIds);
}
