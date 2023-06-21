package com.kenzie.appserver.service;


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

@Service
public class StudyGroupService {
    @Autowired
    private StudyGroupRepository studyGroupRepository;
    @Autowired
    private StudyGroupMemberRepository studyGroupMemberRepository;

    public StudyGroupService(StudyGroupRepository studyGroupRepository, StudyGroupMemberRepository studyGroupMemberRepository) {
        this.studyGroupRepository = studyGroupRepository;
        this.studyGroupMemberRepository = studyGroupMemberRepository;
    }

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

    // TODO: WORKED ON BY NIVI
    public void updateStudyGroup(StudyGroupMember updatedStudyGroupMember) {
        Optional<StudyGroupRecord> existingStudyGroup = studyGroupRepository.findById(updatedStudyGroupMember.getGroupId());
        if (existingStudyGroup.isEmpty()) {
            throw new StudyGroupNotFoundException("Study group not found for groupId: " + updatedStudyGroupMember.getGroupId());
        }
        StudyGroupRecord studyGroupRecord = existingStudyGroup.get();
        studyGroupRecord.setGroupId(UUID.randomUUID().toString());
        studyGroupRecord.setGroupName(updatedStudyGroupMember.getGroupName());
        studyGroupRecord.setDiscussionTopic(updatedStudyGroupMember.getDiscussionTopic());
        studyGroupRecord.setCreationDate(updatedStudyGroupMember.getCreationDate());
        studyGroupRecord.setActive(updatedStudyGroupMember.isActive());
        studyGroupRepository.save(studyGroupRecord);
    }


     //TODO
    public void removeUserFromStudyGroup(String groupId, String userId) {

        if(!studyGroupMemberRepository.existsById(groupId)){
            throw new StudyGroupNotFoundException("Study group not found for groupId: " + groupId);
        }else if(!studyGroupMemberRepository.existsById(userId)){
            throw new UserNotFoundException("User not found for the group " + userId);
        }
        Optional<StudyGroupMemberRecord> userById = studyGroupMemberRepository.findById(userId);
        StudyGroupMemberRecord studyGroupMemberRecord = userById.get();

        studyGroupMemberRepository.delete(studyGroupMemberRecord);

    }

    //todo
    public void deleteStudyGroup(String groupId) {
        studyGroupRepository.deleteById(groupId);
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

    public List<StudyGroupMember> getStudyGroupMembers(String groupId) {
        Iterable<String> groupIds =  Arrays.asList(groupId);

        StudyGroupMemberId studyGroupMemberId = new StudyGroupMemberId(groupId, "1234");
        //StudyGroupMemberRecord studyGroupRecord = new StudyGroupMemberRecord(studyGroupMemberId, studyGroup.getGroupName(), studyGroup.getDiscussionTopic(), studyGroup.getCreationDate(), studyGroup.isActive());

        Optional<StudyGroupMemberRecord> studyGroupMembers = studyGroupMemberRepository.findById(studyGroupMemberId);
        //if (studyGroupOptional.isEmpty()) {
        //    return Collections.emptyList();
        //}
        //StudyGroupRecord studyGroupRecord = studyGroupOptional.get();

        // TODO: Convert to List<StudyGroupMember>
        List<StudyGroupMember> members = new ArrayList<>();
        //for(StudyGroupMemberRecord groupMemberRecord: studyGroupMembers){
        //    StudyGroupMember member = buildStudyGroupMember(groupMemberRecord);
        //    members.add(member);
        //}
        return members;
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
}
