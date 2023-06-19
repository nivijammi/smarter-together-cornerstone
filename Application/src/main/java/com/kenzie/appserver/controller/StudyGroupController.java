package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.StudyGroupRequest;
import com.kenzie.appserver.controller.model.StudyGroupResponse;
import com.kenzie.appserver.exception.StudyGroupNotFoundException;
import com.kenzie.appserver.exception.UserNotFoundException;
import com.kenzie.appserver.service.StudyGroupService;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.StudyGroup;
import com.kenzie.appserver.service.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StudyGroupController {

    private StudyGroupService studyGroupService;
    private UserService userService;

    StudyGroupController(StudyGroupService studyGroupService){
        this.studyGroupService = studyGroupService;
    }

    // endpoint for creating a study group
    @PostMapping("/groups")
    public ResponseEntity<StudyGroupResponse> addNewStudyGroup(@RequestBody StudyGroupRequest studyGroupRequest) {
        System.out.println("request" + studyGroupRequest);
        if (studyGroupRequest.getGroupName() == null || studyGroupRequest.getGroupName().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Group Name");
        }
        // setting of ids should be in the service
        // controller does not need to know how to create a study group

        // creating a study group object out of request
        StudyGroup studyGroup = convertToStudyGroupRequest(studyGroupRequest);
        // add new study group to repo
        StudyGroup newStudyGroup = studyGroupService.addNewStudyGroup(studyGroup);

        StudyGroupResponse studyGroupResponse = convertToStudyGroupResponse(newStudyGroup);
        return ResponseEntity.created(URI.create("/studyGroup/" + studyGroupResponse.getGroupId())).body(studyGroupResponse);
    }


    // Endpoint for adding a user to a study group
    //todo: ?????
    @PostMapping("/groups/{groupId}/users" )
    public ResponseEntity<User> addUserToStudyGroup(@PathVariable String groupId, @RequestBody com.kenzie.appserver.controller.model.User user) {
        System.out.println(groupId +" " +user);
        // Create User - get userId

        // Retrieve the studyGroup from the database using the userId
        StudyGroup studyGroup = studyGroupService.findByGroupId(groupId);

        // Add the user to the study group
        User newUser = studyGroupService.addUserToStudyGroup(groupId, user);
        // create the Response
        //StudyGroupResponse studyGroupResponse = convertToStudyGroupResponse(studyGroup);
        return ResponseEntity.ok(newUser);
    }

    // endpoint for getting all members in a study group-???
    @GetMapping("/groups/{groupId}/users/")
    public ResponseEntity<List<String>> getStudyGroupMembers(@PathVariable String groupId){

        List<String> memberList = studyGroupService.getStudyGroupMembers(groupId);
        if (memberList == null || memberList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<String> members = new ArrayList<>();
        members.addAll(memberList);
        return ResponseEntity.ok(members);

    }

    // endpoint to remove user from study Group
    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromStudyGroup(@PathVariable String groupId, @PathVariable String userId) {
        try {
            studyGroupService.removeUserFromStudyGroup(groupId, userId);
            return ResponseEntity.ok().build();
        } catch (StudyGroupNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // endpoint for retrieving a study group by ID
    @GetMapping("/{groupId}")
    public ResponseEntity<StudyGroupResponse> getStudyGroupById(@PathVariable String groupId) {
        StudyGroup studyGroup = studyGroupService.findByGroupId(groupId);
        //todo??? if bad request
        if(studyGroup == null){
            return ResponseEntity.notFound().build();
        }
        StudyGroupResponse studyGroupResponse = convertToStudyGroupResponse(studyGroup);
        return ResponseEntity.ok(studyGroupResponse);
    }

    // endpoint for retrieving all study groups
    @GetMapping
    public ResponseEntity<List<StudyGroup>> getAllStudyGroups() {
        List<StudyGroup> studyGroups = studyGroupService.getAllStudyGroups();
        // if no studyGroups found return 204
        if(studyGroups == null || studyGroups.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Otherwise, convert the List of StudyGroup objects into a List of StudyGroupResponse and return it
        List<StudyGroupResponse> response = new ArrayList<>();
        for (StudyGroup studyGroup : studyGroups) {
            response.add(this.convertToStudyGroupResponse(studyGroup));
        }
        return ResponseEntity.ok(studyGroups);
    }

    // endpoint to update a study group
    @PutMapping("/{groupId}")
    public ResponseEntity<StudyGroupResponse> updateStudyGroup(@PathVariable String groupId, @RequestBody StudyGroupRequest studyGroupRequest) {
        if (studyGroupRequest.getGroupName() == null || studyGroupRequest.getGroupName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Group Name is required");
        }

        StudyGroup existingStudyGroup = studyGroupService.findByGroupId(groupId);
        if (existingStudyGroup == null) {
            return ResponseEntity.notFound().build();
        }
        StudyGroup updatedStudyGroup = convertToStudyGroupRequest(studyGroupRequest);
        studyGroupService.updateStudyGroup(updatedStudyGroup);

        StudyGroupResponse studyGroupResponse = convertToStudyGroupResponse(updatedStudyGroup);
        return ResponseEntity.ok(studyGroupResponse);


    }
    @DeleteMapping("/{groupId}")
    public ResponseEntity deleteStudyGroup(@PathVariable String groupId) {

        StudyGroup existingStudyGroup = studyGroupService.findByGroupId(groupId);
        if (existingStudyGroup == null) {
            return ResponseEntity.notFound().build();
        }
        studyGroupService.deleteStudyGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    // helper methods
    private static StudyGroup convertToStudyGroupRequest(StudyGroupRequest studyGroupRequest) {
        StudyGroup studyGroup = new StudyGroup(studyGroupRequest.getGroupName(),
                studyGroupRequest.getSubject(),
                studyGroupRequest.getTopic(),
                studyGroupRequest.getCreationDate(),
                studyGroupRequest.getEndDate(),
                studyGroupRequest.getMeetingTime(),
                studyGroupRequest.getDuration());
        return studyGroup;
    }
    private StudyGroupResponse convertToStudyGroupResponse(StudyGroup studyGroup) {
        StudyGroupResponse response = new StudyGroupResponse();
        response.setGroupId(studyGroup.getGroupId());//
        response.setGroupName(studyGroup.getGroupName());
        response.setMembers(studyGroup.getMembers());
        response.setSubject(studyGroup.getSubject());
        response.setTopic(studyGroup.getTopic());
        response.setCreationDate(studyGroup.getCreationDate());
        response.setEndDate(studyGroup.getEndDate());
        response.setMeetingTime(studyGroup.getMeetingTime());
        response.setDuration(studyGroup.getDuration());
        response.setSuccessful(studyGroup.isSuccessful());
        return response;
    }


}
