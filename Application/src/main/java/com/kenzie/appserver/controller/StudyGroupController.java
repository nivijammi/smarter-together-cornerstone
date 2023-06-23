package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.AddStudyGroupRequest;
import com.kenzie.appserver.controller.model.AddStudyGroupResponse;
import com.kenzie.appserver.exception.StudyGroupNotFoundException;
import com.kenzie.appserver.exception.MemberNotFoundException;
import com.kenzie.appserver.service.StudyGroupService;
import com.kenzie.appserver.service.MemberService;
import com.kenzie.appserver.service.model.StudyGroup;
import com.kenzie.appserver.service.model.StudyGroupMember;
import com.kenzie.appserver.service.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class StudyGroupController {
    @Autowired
    private StudyGroupService studyGroupService;

    @Autowired
    private MemberService memberService;
    StudyGroupController(StudyGroupService studyGroupService){
        this.studyGroupService = studyGroupService;
    }

    // Route to the groups [study groups] endpoint, for creating a new study group
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

    // Route to the groups [study groups] endpoint, for adding a member to a study group
    @PostMapping("/groups/{groupId}/members/{memberId}" ) // Pass email as the memberId to this
    public ResponseEntity<StudyGroupMember> addMemberToStudyGroup(@PathVariable String groupId, @PathVariable String memberId ) {
        // Retrieve the studyGroup from the database using the memberId
        StudyGroup studyGroup = studyGroupService.findStudyGroupById(groupId);
        if(studyGroup == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "StudyGroup "+groupId +" not found for groupId " + groupId);
        }

        Member member = studyGroupService.findMemberById(memberId);
        if(member == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found for memberId " + memberId);
        }

        // Add the member to the study group
        StudyGroupMember studyGroupMember = studyGroupService.addMemberToStudyGroup(studyGroup, memberId);
        // create the Response
        return ResponseEntity.ok(studyGroupMember);
    }


    /**
     * Endpoint to get all members in a study group
     *      /groups/                                [Returns all Groups]
     *      /groups/{groupId}                       [Returns a group]
     *
     *      /groups/{groupId}/members/                [Returns all Members]
     *      /groups/{groupId}/members/{memberId}      [Returns a member within a group]
     *
     * Note:
     * ResponseEntity can return a list of objects or a single object.
     * A list of objects will be converted to JSON Array output by ResponseEntity.
     * source: https://technicalsand.com/using-responseentity-in-spring/
     */
    @GetMapping("/groups/{groupId}/members/")
    public ResponseEntity<List<StudyGroupMember>> getStudyGroupMembers(@PathVariable String groupId){

        List<StudyGroupMember> studyGroupMembers = studyGroupService.getStudyGroupMembers(groupId);
        if (studyGroupMembers == null || studyGroupMembers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        //List<String> members = new ArrayList<>();
        //members.addAll(studyGroupMembers);
        return ResponseEntity.ok(studyGroupMembers);

    }

    /**
     * Endpoint to remove member from study Group
     * */
    @DeleteMapping("/groups/{groupId}/members/{memberId}")
    public ResponseEntity<Void> removeMemberFromStudyGroup(@PathVariable String groupId, @PathVariable String memberId) {
        try {
            studyGroupService.removeMemberFromStudyGroup(groupId, memberId);
            return ResponseEntity.ok().build();

        } catch (StudyGroupNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (MemberNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     *  endpoint for retrieving a study group by ID

     */
    @GetMapping("/groups/{groupId}")
    // CONFIRM
    public ResponseEntity<AddStudyGroupResponse> getStudyGroupById(@PathVariable String groupId) {
        StudyGroup studyGroup = studyGroupService.findByCachedGroupId(groupId);
        //todo??? if bad request
        if(studyGroup == null){
            return ResponseEntity.notFound().build();
        }
        AddStudyGroupResponse studyGroupResponse = convertToStudyGroupResponse(studyGroup);
        return ResponseEntity.ok(studyGroupResponse);
    }


    /**
     * Endpoint for retrieving all study groups
     */
    @GetMapping("/groups")
    public ResponseEntity<List<AddStudyGroupResponse>> getAllStudyGroups() {
        List<StudyGroup> studyGroups = studyGroupService.getAllStudyGroups();
        // if no studyGroups found return 204
        if(studyGroups == null || studyGroups.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Otherwise, convert the List of StudyGroup objects into a List of StudyGroupResponse and return it
        List<AddStudyGroupResponse> response = new ArrayList<>();
        for (StudyGroup studyGroup : studyGroups) {
            response.add(convertToStudyGroupResponse(studyGroup));
        }
        return ResponseEntity.ok(response);
    }



    // endpoint to update a study group
    @PutMapping("/groups/{groupId}")
    public ResponseEntity<AddStudyGroupResponse> updateStudyGroup(@PathVariable String groupId, @RequestBody AddStudyGroupRequest updateRequest) {
        if (updateRequest.getGroupName() == null || updateRequest.getGroupName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Group Name is required");
        }

        StudyGroup existingStudyGroup = studyGroupService.findByCachedGroupId(groupId);
        if (existingStudyGroup == null) {
            return ResponseEntity.notFound().build();
        }
        existingStudyGroup.setGroupName(updateRequest.getGroupName());
        existingStudyGroup.setDiscussionTopic(updateRequest.getDiscussionTopic());
        existingStudyGroup.setCreationDate(ZonedDateTime.now());
        existingStudyGroup.setActive(updateRequest.isActive());

        studyGroupService.updateStudyGroup(existingStudyGroup);

        AddStudyGroupResponse studyGroupResponse = convertToStudyGroupResponse(existingStudyGroup);
        return ResponseEntity.ok(studyGroupResponse);


    }
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity deleteStudyGroup(@PathVariable String groupId) {

        StudyGroup existingStudyGroup = studyGroupService.findByCachedGroupId(groupId);
        if (existingStudyGroup == null) {
            return ResponseEntity.notFound().build();
        }
        studyGroupService.deleteStudyGroup(groupId);
        return ResponseEntity.noContent().build();
    }


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
