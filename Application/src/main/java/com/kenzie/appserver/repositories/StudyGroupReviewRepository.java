package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.*;
import com.kenzie.appserver.service.model.StudyGroupReview;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface StudyGroupReviewRepository extends CrudRepository<StudyGroupReviewRecord, StudyGroupReviewId> {
    // Returns a single record/row within the partition
    Optional<StudyGroupReviewRecord> findById(StudyGroupReviewId studyGroupReviewId);

    Optional<List<StudyGroupReviewRecord>> findByGroupId(String groupId);


    Optional<StudyGroupReviewRecord> findByReviewId(String reviewId);
}

