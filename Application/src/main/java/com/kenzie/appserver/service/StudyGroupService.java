package com.kenzie.appserver.service;


import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.exception.StudyGroupNotFoundException;
import com.kenzie.appserver.exception.UserNotFoundException;
import com.kenzie.appserver.repositories.StudyGroupMemberRepository;
import com.kenzie.appserver.repositories.StudyGroupRepository;
import com.kenzie.appserver.repositories.model.StudyGroupMemberId;
import com.kenzie.appserver.repositories.model.StudyGroupMemberRecord;
import com.kenzie.appserver.repositories.model.StudyGroupRecord;
import com.kenzie.appserver.service.model.StudyGroup;
import com.kenzie.appserver.service.model.StudyGroupMember;
import com.kenzie.appserver.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * The application caches the study groups to improve performance and reduce the load on your backend database.
 * The study groups are frequently accessed and queried by multiple users,
 * retrieving the study group data from the database every time can introduce latency
 * and impact the overall response time of the application.
 * By using in-memory caching, the application can store the study group data in memory
 * and serve subsequent requests directly from the cache.
 *
 * Also, the application uses in-memory caching as study group does not require frequent updates
 */

@Service
public class StudyGroupService {
    @Autowired
    private StudyGroupRepository studyGroupRepository;
    @Autowired
    private StudyGroupMemberRepository studyGroupMemberRepository;

    private CacheStore cache;

    public StudyGroupService(StudyGroupRepository studyGroupRepository, StudyGroupMemberRepository studyGroupMemberRepository) {
        this.studyGroupRepository = studyGroupRepository;
        this.studyGroupMemberRepository = studyGroupMemberRepository;
    }

    // addNewStudyGroup
    public StudyGroup addNewStudyGroup(StudyGroup group) {
        // add to the repo
        StudyGroupRecord record = buildStudyGroupRecord(group);
        studyGroupRepository.save(record);
        StudyGroup newStudyGroup = buildStudyGroup(record);
        return newStudyGroup;
    }
    // helper method
    private StudyGroup buildStudyGroup(StudyGroupRecord record) {
        StudyGroup studyGroup = new StudyGroup(record.getGroupId(), record.getGroupName(),
                                                record.getDiscussionTopic(), record.getCreationDate(),
                                                record.isActive());
        return studyGroup;
    }
    // helper method
    private StudyGroupRecord buildStudyGroupRecord(StudyGroup studyGroup) {
        StudyGroupRecord record = new StudyGroupRecord();
        record.setGroupId(UUID.randomUUID().toString());
        record.setGroupName(studyGroup.getGroupName());
        record.setDiscussionTopic(studyGroup.getDiscussionTopic());
        record.setCreationDate(studyGroup.getCreationDate());
        record.setActive(true);
        return record;
    }


    // addUserToStudyGroup
    public StudyGroup findByGroupId(String groupId) {
        Optional<StudyGroupRecord> groupById = studyGroupRepository.findById(groupId);
        if(!groupById.isPresent()) {
            return null;
        }
        StudyGroupRecord studyGroupRecord = groupById.get();
        // convert from record to study group(domain object)
        StudyGroup studyGroup = buildStudyGroup(studyGroupRecord);
        return studyGroup;
    }

    public StudyGroup findByCachedGroupId(String groupId) {
        StudyGroup cachedStudyGroup = cache.get(groupId);
        // Check if studyGroup is cached and return it if true
        if (cachedStudyGroup != null) {
            return cachedStudyGroup;
        }
        // if not cached, find the study group
        StudyGroup studyGroupFromBackendService = studyGroupRepository
                .findById(groupId)
                .map(studyGroup-> new StudyGroup(studyGroup.getGroupId(),
                        studyGroup.getGroupName(),
                        studyGroup.getDiscussionTopic(),
                        studyGroup.getCreationDate(),
                        studyGroup.isActive()))
                .orElse(null);

        // if study group found, cache it
        if (studyGroupFromBackendService != null) {
            cache.add(studyGroupFromBackendService.getGroupId(),studyGroupFromBackendService);
        }
        return studyGroupFromBackendService;
    }


    public StudyGroupMember addUserToStudyGroup(StudyGroup studyGroup, String userId) {
        if (studyGroup == null) {
            throw new StudyGroupNotFoundException("Study group is null...");
        }
        if (userId == null) {
            throw new UserNotFoundException("userId is null...");
        }

        //StudyGroupMemberRecord record = buildStudyGroupMemberRecord(studyGroup, userId);
        StudyGroupMemberId studyGroupMemberId = new StudyGroupMemberId(studyGroup.getGroupId(), userId);
        StudyGroupMemberRecord studyGroupRecord = new StudyGroupMemberRecord(studyGroupMemberId, studyGroup.getGroupName(), studyGroup.getDiscussionTopic(), studyGroup.getCreationDate(), studyGroup.isActive());
        studyGroupMemberRepository.save(studyGroupRecord);
        StudyGroupMember studyGroupMember = buildStudyGroupMember(studyGroupRecord);
        return studyGroupMember;
    }
    public User findUserById(String userId) {
        //        Optional<User> user = userRepository.findById(userId);
        //        if(!user.isPresent()) {
        //            return null;
        //        }
        //
        //        UserRecord userRecord = user.get();
        //        // convert from record to study group(domain object)
        //        User user = buildStudyGroup(userRecord);
        return new User(userId, "", "", "") ;
    }


    private static StudyGroupMember buildStudyGroupMember(StudyGroupMemberRecord record) {
        StudyGroupMember studyGroupMember = new StudyGroupMember(record.getGroupId(),
                record.getUserId(), record.getGroupName(),
                record.getDiscussionTopic(),
                record.getCreationDate(),
                record.isActive());
        return studyGroupMember;
    }
    private static StudyGroupMemberRecord buildStudyGroupMemberRecord(StudyGroup studyGroup, String userId) {
        StudyGroupMemberRecord record = new StudyGroupMemberRecord();
        record.setGroupId(studyGroup.getGroupId());
        record.setUserId(userId);
        record.setGroupName(studyGroup.getGroupName());
        record.setDiscussionTopic(studyGroup.getDiscussionTopic()); // ???
        record.setCreationDate(studyGroup.getCreationDate());       // TODO: Current data time needs to be set
        record.setActive(studyGroup.isActive());                    // TODO: Redundant. Can be removed
        return record;
    }
    public List<StudyGroupMember> getStudyGroupMembers(String groupId) {

        Optional<List<StudyGroupMemberRecord>> studyGroupMembers = studyGroupMemberRepository.findByGroupId(groupId);
        if (studyGroupMembers.isEmpty()) {
            return Collections.emptyList();
        }

        List<StudyGroupMemberRecord> studyGroupMemberRecords = studyGroupMembers.get();
        List<StudyGroupMember> members = new ArrayList<>();
        // TODO: Convert to List<StudyGroupMember>
        for(StudyGroupMemberRecord groupMemberRecord: studyGroupMemberRecords){
            StudyGroupMember member = buildStudyGroupMember(groupMemberRecord);
            members.add(member);
        }
        return members;
    }

    //TODO
    public void removeUserFromStudyGroup(String groupId, String userId) {
        StudyGroupMemberId studyGroupMemberId = new StudyGroupMemberId(groupId, userId);

        if(!studyGroupMemberRepository.existsById(studyGroupMemberId)) {
            throw new StudyGroupNotFoundException("Study group not found for groupId: " + groupId);
        }

        Optional<StudyGroupMemberRecord> studyGroupMemberRecordById = studyGroupMemberRepository.findById(studyGroupMemberId);
        StudyGroupMemberRecord studyGroupMemberRecord = studyGroupMemberRecordById.get();
        studyGroupMemberRepository.delete(studyGroupMemberRecord);
    }

    //TODO: NIVI

    public List<StudyGroup> getAllStudyGroups() {
        Iterable<StudyGroupRecord> studyGroupRecords = studyGroupRepository.findAll();
        List<StudyGroup> studyGroups = new ArrayList<>();

        for (StudyGroupRecord studyGroupRecord : studyGroupRecords) {
            StudyGroup studyGroup = buildStudyGroup(studyGroupRecord);
            studyGroups.add(studyGroup);
        }

        return studyGroups;
    }


    public void updateStudyGroup(StudyGroup studyGroup) {
        Optional<StudyGroupRecord> existingStudyGroup = studyGroupRepository.findById(studyGroup.getGroupId());
        if (existingStudyGroup.isEmpty()) {
            throw new StudyGroupNotFoundException("Study group not found for groupId: " + studyGroup.getGroupId());
        }
        // Update the properties of the existing study group with the new values
        StudyGroupRecord studyGroupRecord = existingStudyGroup.get();
        studyGroupRecord.setGroupId(UUID.randomUUID().toString());
        studyGroupRecord.setGroupName(studyGroup.getGroupName());
        studyGroupRecord.setDiscussionTopic(studyGroup.getDiscussionTopic());
        studyGroupRecord.setCreationDate(studyGroup.getCreationDate());
        studyGroupRecord.setActive(studyGroup.isActive());
        studyGroupRepository.save(studyGroupRecord);
        cache.evict(studyGroup.getGroupId());
    }

    //todo

    public void deleteStudyGroup(String groupId) {
        studyGroupRepository.deleteById(groupId);
        cache.evict(groupId);
    }

}
