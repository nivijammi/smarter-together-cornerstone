package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.StudyGroupMemberId;
import com.kenzie.appserver.repositories.model.StudyGroupMemberRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface StudyGroupMemberRepository extends CrudRepository<StudyGroupMemberRecord,StudyGroupMemberId> {

    // boolean existsById(String groupId);
    @Override
    boolean existsById(StudyGroupMemberId studyGroupMemberId);

    // Returns a single record/row within the partition
    Optional<StudyGroupMemberRecord> findById(StudyGroupMemberId studyGroupMemberId);

    // Returns all the records/rows within the partition
    Optional<List<StudyGroupMemberRecord>> findByGroupId(String groupId);
    Optional<StudyGroupMemberRecord> findByMemberId(String userId);

}
