package com.kenzie.appserver.service;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.service.model.StudyGroup;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@IntegrationTest
public class StudyGroupCacheManagerTest {
    @Autowired
    CacheManager cacheManager;

    @Autowired
    private CacheStore cacheStore;

    @Autowired
    private StudyGroupService subject;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    @Test
    public void studyGroupCache_InsertIntoCache() throws Exception {
        String groupId = UUID.randomUUID().toString();
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        StudyGroup studyGroup = new StudyGroup(groupId, groupName, discussionTopic, date, active);
        subject.addNewStudyGroup(studyGroup);
        subject.findByCachedGroupId(groupId);

        StudyGroup studyGroupFromCache = cacheStore.get(studyGroup.getGroupId());

        assertThat(studyGroupFromCache).isNotNull();
        assertThat(studyGroupFromCache.getGroupId()).isEqualTo(groupId);

    }

    @Test
    public void studyGroupCache_EvictFromCache() throws Exception {
        String groupId = UUID.randomUUID().toString();
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        StudyGroup studyGroup = new StudyGroup(groupId, groupName, discussionTopic, date, active);
        subject.addNewStudyGroup(studyGroup);
        subject.findByCachedGroupId(groupId);

        StudyGroup studyGroupFromCache = cacheStore.get(studyGroup.getGroupId());

        subject.updateStudyGroup(studyGroup);

        StudyGroup studyGroupFromCacheAfterUpdate = cacheStore.get(studyGroup.getGroupId());

        assertThat(studyGroupFromCache).isNotNull();
        assertThat(studyGroupFromCache.getGroupId()).isEqualTo(groupId);
        assertThat(studyGroupFromCacheAfterUpdate).isNull();

    }


}
