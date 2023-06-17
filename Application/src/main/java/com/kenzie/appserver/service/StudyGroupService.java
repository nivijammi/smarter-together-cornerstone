package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.User;
import com.kenzie.appserver.service.model.StudyGroup;

import java.util.List;

public class StudyGroupService {

    public StudyGroup addNewStudyGroup(StudyGroup studyGroup) {
        return studyGroup;
    }



    public StudyGroup findByGroupId(String groupId) {
        return null;
    }

    public StudyGroup getStudyGroup(String groupId) {
        return null;
    }

    public StudyGroup addUserToStudyGroup(String groupID, User userID) {
        return null;
    }

    public void updateStudyGroup(StudyGroup studyGroup) {
    }

    public void removeUserFromStudyGroup(String groupId, String userId) {

    }

    public void deleteStudyGroup(String groupId) {
    }

    public List<StudyGroup> getAllStudyGroups() {
        return null;
    }

    public List<String> getStudyGroupMembers(String groupId) {
        return null;
    }
}
