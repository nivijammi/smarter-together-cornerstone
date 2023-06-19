package com.kenzie.appserver.service;


import com.kenzie.appserver.repositories.StudyGroupRepository;
import com.kenzie.appserver.repositories.model.StudyGroupRecord;
import com.kenzie.appserver.service.model.StudyGroup;
import com.kenzie.appserver.service.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudyGroupService {
    private StudyGroupRepository studyGroupRepository;
    List<String> members;
    List<StudyGroup> groups;

    public StudyGroupService(StudyGroupRepository studyGroupRepository) {
        this.studyGroupRepository = studyGroupRepository;
        this.members = new ArrayList<>();
        this.groups = new ArrayList<>();
    }

    public StudyGroup addNewStudyGroup(StudyGroup studyGroup) {
        // add to the repo
        StudyGroupRecord record = getStudyGroupRecord(studyGroup);
        //studyGroupRepository.save(record);
        StudyGroup newStudyGroup = buildStudyGroup(record);
        return newStudyGroup;
    }

    // helper method
    private StudyGroup buildStudyGroup(StudyGroupRecord record) {
        StudyGroup newStudyGroup = new StudyGroup(record.getGroupId(),
                record.getGroupName(), record.getMembers(),
                record.getSubject(), record.getTopic(), record.getCreationDate(),
                record.getEndDate(), record.getMeetingTime(), record.getDuration(),
                true);
        return newStudyGroup;
    }

    // helper method
    private StudyGroupRecord getStudyGroupRecord(StudyGroup studyGroup) {
        StudyGroupRecord record = new StudyGroupRecord();
        record.setGroupId(UUID.randomUUID().toString());
        record.setGroupName(studyGroup.getGroupName());
        record.setMembers(studyGroup.getMembers());
        record.setSubject(studyGroup.getSubject());
        record.setTopic(studyGroup.getTopic());
        record.setCreationDate(studyGroup.getCreationDate());
        record.setEndDate(studyGroup.getEndDate());
        record.setMeetingTime(studyGroup.getMeetingTime());
        record.setDuration(studyGroup.getDuration());
        record.setSuccessful(studyGroup.isSuccessful());

        return record;
    }

    public StudyGroup findByGroupId(String groupId) {
        Optional<StudyGroupRecord> byId = studyGroupRepository.findById(groupId);

        if(byId.isPresent()){
            StudyGroupRecord studyGroupRecord = byId.get();
            return buildStudyGroup(studyGroupRecord);
        }
        return null;
    }


    public User addUserToStudyGroup(String groupID, com.kenzie.appserver.controller.model.User user) {

        // generate userId and add user to the db

        //return user
        User record = new User(UUID.randomUUID().toString(), user.email, user.userName, null);
        return record;
    }

    public void updateStudyGroup(StudyGroup studyGroup) {
    }

    public void removeUserFromStudyGroup(String groupId, String userId) {
        boolean groupExist = studyGroupRepository.existsById(groupId);
        if(groupExist){
            // bring in the userRepo


        }

    }

    public void deleteStudyGroup(String groupId) {
    }

    public List<StudyGroup> getAllStudyGroups() {
        return null;
    }

    public List<String> getStudyGroupMembers(String groupId) {

        members.add("johnDoe");
        members.add("janeDoe");
        return members;
    }

}
