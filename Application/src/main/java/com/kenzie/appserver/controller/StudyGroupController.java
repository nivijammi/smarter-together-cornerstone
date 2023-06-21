package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.AddStudyGroupRequest;
import com.kenzie.appserver.controller.model.AddStudyGroupResponse;
import com.kenzie.appserver.controller.model.StudyGroupMemberId;
import com.kenzie.appserver.service.StudyGroupService;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.StudyGroup;
import com.kenzie.appserver.service.model.StudyGroupMember;
import com.kenzie.appserver.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class StudyGroupController {

    @Autowired
    private StudyGroupService studyGroupService;

    private UserService userService;


    StudyGroupController(StudyGroupService studyGroupService){
        this.studyGroupService = studyGroupService;
    }

    // endpoint for creating a study group
    @PostMapping("/groups")
    public ResponseEntity<AddStudyGroupResponse> addNewStudyGroup(@RequestBody AddStudyGroupRequest request) {
        if (request.getGroupName() == null || request.getGroupName().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Group Name");
        }
        // setting of ids should be in the service
        // controller does not need to know how to create a study group

        // creating a study group object out of request
        StudyGroup group = convertToStudyGroupRequest(request);
        // add new study group to repo
        StudyGroup newStudyGroup = studyGroupService.addNewStudyGroup(group);

        AddStudyGroupResponse studyGroupResponse = convertToStudyGroupResponse(newStudyGroup);
        return ResponseEntity.created(URI.create("/group/" + studyGroupResponse.getGroupId())).body(studyGroupResponse);
    }



    // Endpoint for adding a user to a study group
    @PostMapping("/groups/{groupId}/{userId}" )
    public ResponseEntity<StudyGroupMember> addUserToStudyGroup(String groupId, String userId ) {
        System.out.println(groupId);
        // Create User - get userId

        // Retrieve the studyGroup from the database using the userId
        StudyGroup studyGroup = studyGroupService.findByGroupId(groupId);
        if(studyGroup == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "StudyGroup "+groupId +" not found for userId " + userId);
        }

        User user = studyGroupService.findUserById(userId);
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found for userId " + userId);
        }

        // Add the user to the study group
        StudyGroupMember studyGroupMember = studyGroupService.addUserToStudyGroup(studyGroup, userId);
        // create the Response
        return ResponseEntity.ok(studyGroupMember);
    }


    // endpoint for getting all members in a study group-???
    @GetMapping("/groups/{groupId}/users/")
    public ResponseEntity<List<StudyGroupMember>> getStudyGroupMembers(@PathVariable String groupId){

        List<StudyGroupMember> studyGroupMembers = studyGroupService.getStudyGroupMembers(groupId);
        if (studyGroupMembers == null || studyGroupMembers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        //List<String> members = new ArrayList<>();
        //members.addAll(studyGroupMembers);
        return ResponseEntity.ok(studyGroupMembers);

    }

    /*
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
        StudyGroupMembers studyGroupMembers = studyGroupService.findByGroupId(groupId);
        //todo??? if bad request
        if(studyGroupMembers == null){
            return ResponseEntity.notFound().build();
        }
        StudyGroupResponse studyGroupResponse = convertToStudyGroupResponse(studyGroupMembers);
        return ResponseEntity.ok(studyGroupResponse);
    }

    // endpoint for retrieving all study groups
    @GetMapping
    public ResponseEntity<List<StudyGroupMembers>> getAllStudyGroups() {
        List<StudyGroupMembers> studyGroupMembers = studyGroupService.getAllStudyGroups();
        // if no studyGroups found return 204
        if(studyGroupMembers == null || studyGroupMembers.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Otherwise, convert the List of StudyGroup objects into a List of StudyGroupResponse and return it
        List<StudyGroupResponse> response = new ArrayList<>();
        for (StudyGroupMembers studyGroupMembers : studyGroupMembers) {
            response.add(this.convertToStudyGroupResponse(studyGroupMembers));
        }
        return ResponseEntity.ok(studyGroupMembers);
    }

    // endpoint to update a study group
    @PutMapping("/{groupId}")
    public ResponseEntity<StudyGroupResponse> updateStudyGroup(@PathVariable String groupId, @RequestBody AddStudyGroupRequest addStudyGroupRequest) {
        if (addStudyGroupRequest.getGroupName() == null || addStudyGroupRequest.getGroupName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Group Name is required");
        }

        StudyGroupMembers existingStudyGroupMembers = studyGroupService.findByGroupId(groupId);
        if (existingStudyGroupMembers == null) {
            return ResponseEntity.notFound().build();
        }
        StudyGroupMembers updatedStudyGroupMembers = convertToStudyGroupRequest(addStudyGroupRequest);
        studyGroupService.updateStudyGroup(updatedStudyGroupMembers);

        StudyGroupResponse studyGroupResponse = convertToStudyGroupResponse(updatedStudyGroupMembers);
        return ResponseEntity.ok(studyGroupResponse);


    }
    @DeleteMapping("/{groupId}")
    public ResponseEntity deleteStudyGroup(@PathVariable String groupId) {

        StudyGroupMembers existingStudyGroupMembers = studyGroupService.findByGroupId(groupId);
        if (existingStudyGroupMembers == null) {
            return ResponseEntity.notFound().build();
        }
        studyGroupService.deleteStudyGroup(groupId);
        return ResponseEntity.noContent().build();
    }
    */


    // helper methods
    private StudyGroup convertToStudyGroupRequest(AddStudyGroupRequest request) {
        StudyGroup studyGroup = new StudyGroup(
                UUID.randomUUID().toString(),
                request.getGroupName(),
                request.getDiscussionTopic(),
                request.getCreationDate(),
                request.isActive()
        );

        return studyGroup;
    }
    private AddStudyGroupResponse convertToStudyGroupResponse(StudyGroup studyGroup) {
        AddStudyGroupResponse response = new AddStudyGroupResponse();
        response.setGroupId(studyGroup.getGroupId());
        response.setGroupName(studyGroup.getGroupName());
        response.setDiscussionTopic(studyGroup.getDiscussionTopic());
        response.setCreationDate(studyGroup.getCreationDate());
        response.setActive(studyGroup.isActive());
        return response;
    }


}
