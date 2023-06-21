package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.StudyGroupRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface StudyGroupRepository extends CrudRepository<StudyGroupRecord, String> {
    //List<StudyGroupRecord> findByTopic(String topic);

}
