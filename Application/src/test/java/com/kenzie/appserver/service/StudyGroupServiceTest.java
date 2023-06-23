package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.repositories.StudyGroupMemberRepository;
import com.kenzie.appserver.repositories.StudyGroupRepository;
import com.kenzie.appserver.repositories.model.StudyGroupRecord;
import com.kenzie.appserver.service.model.StudyGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StudyGroupServiceTest {

    private StudyGroupMemberRepository studyGroupMemberRepository;
    private StudyGroupRepository studyGroupRepository;
    private StudyGroupService studyGroupService;
    private CacheStore cacheStore;

    @BeforeEach
    void setup() {
        studyGroupMemberRepository = mock(StudyGroupMemberRepository.class);
        studyGroupRepository = mock(StudyGroupRepository.class);
        cacheStore = mock(CacheStore.class);
        studyGroupService = new StudyGroupService(studyGroupRepository,studyGroupMemberRepository,cacheStore);
    }

    @Test
    void addNewStudyGroup_addsANewStudyGroup(){

        StudyGroup group = new StudyGroup("1", "Group1", "Discussion", ZonedDateTime.now(), true);
        StudyGroupRecord record = new StudyGroupRecord();
        record.setGroupId("1");
        record.setGroupName("Group1");
        record.setCreationDate(ZonedDateTime.now());
        record.setActive(true);

        when(studyGroupRepository.save(record)).thenReturn(record);

        // class under test
        studyGroupService.addNewStudyGroup(group);


    }


}
