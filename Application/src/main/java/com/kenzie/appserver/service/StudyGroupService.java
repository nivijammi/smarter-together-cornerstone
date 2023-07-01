package com.kenzie.appserver.service;


import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.exception.StudyGroupNotFoundException;
import com.kenzie.appserver.exception.MemberNotFoundException;
import com.kenzie.appserver.repositories.StudyGroupMemberRepository;
import com.kenzie.appserver.repositories.StudyGroupRepository;
import com.kenzie.appserver.repositories.MemberRepository;
import com.kenzie.appserver.repositories.model.MemberRecord;
import com.kenzie.appserver.repositories.model.StudyGroupMemberId;
import com.kenzie.appserver.repositories.model.StudyGroupMemberRecord;
import com.kenzie.appserver.repositories.model.StudyGroupRecord;
import com.kenzie.appserver.service.model.Member;
import com.kenzie.appserver.service.model.StudyGroup;
import com.kenzie.appserver.service.model.StudyGroupMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * The application caches the study groups to improve performance and reduce the load on your backend database.
 * The study groups are frequently accessed and queried by multiple members,
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
    @Autowired
    private CacheStore cache;

    @Autowired
    private MemberRepository memberRepository;

    public StudyGroupService(StudyGroupRepository studyGroupRepository, StudyGroupMemberRepository studyGroupMemberRepository,MemberRepository memberRepository,CacheStore cache) {
        this.studyGroupRepository = studyGroupRepository;
        this.studyGroupMemberRepository = studyGroupMemberRepository;
        this.memberRepository = memberRepository;
        this.cache = cache;
    }

    /** addNewStudyGroup
     *
     * made it idempotent
     */
    public StudyGroup addNewStudyGroup(StudyGroup group) {

        // Check if the study group already exists
        StudyGroup existingGroup = getExistingStudyGroup(group);
        if (existingGroup != null) {
            // If the study group exists, return it
            return existingGroup;
        }
        // add to the repo
        StudyGroupRecord record = buildStudyGroupRecord(group);
        studyGroupRepository.save(record);
        return buildStudyGroup(record);
    }

    public StudyGroup getExistingStudyGroup(StudyGroup group) {
        Iterable<StudyGroupRecord> records = studyGroupRepository.findAll();
        List<StudyGroup> allStudyGroups = new ArrayList<>();
        if (records != null) {
            for (StudyGroupRecord record : records) {
                StudyGroup studyGroup = buildStudyGroup(record);
                allStudyGroups.add(studyGroup);
            }
        }
        for (StudyGroup existingGroup : allStudyGroups) {
            if (existingGroup.getGroupName().equals(group.getGroupName())
                    && existingGroup.getDiscussionTopic().equals(group.getDiscussionTopic())) {
                return existingGroup;
            }
        }
        return null;
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
        record.setGroupId(studyGroup.getGroupId());
        record.setGroupName(studyGroup.getGroupName());
        record.setDiscussionTopic(studyGroup.getDiscussionTopic());
        record.setCreationDate(studyGroup.getCreationDate());
        record.setActive(true);
        return record;
    }


    // addMemberToStudyGroup
//    public StudyGroup findStudyGroupById(String groupId) {
//        Optional<StudyGroupRecord> groupById = studyGroupRepository.findById(groupId);
//        if(!groupById.isPresent()) {
//            return null;
//        }
//        StudyGroupRecord studyGroupRecord = groupById.get();
//        // convert from record to study group(domain object)
//        StudyGroup studyGroup = buildStudyGroup(studyGroupRecord);
//        return studyGroup;
//    }

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


    public StudyGroupMember addMemberToStudyGroup(StudyGroup studyGroup, String memberId) {
        if (studyGroup == null) {
            throw new StudyGroupNotFoundException("Study group is null...");
        }
        if (memberId == null) {
            throw new MemberNotFoundException("memberId is null...");
        }
        StudyGroupMemberId studyGroupMemberId = new StudyGroupMemberId(studyGroup.getGroupId(), memberId);
        if (isStudyGroupMemberExists(studyGroupMemberId)) {
            // If the study group member exists, retrieve and return it
            return getExistingStudyGroupMember(studyGroupMemberId);
        } else {
            StudyGroupMemberRecord studyGroupRecord = new StudyGroupMemberRecord(studyGroupMemberId, studyGroup.getGroupName(), studyGroup.getDiscussionTopic(), studyGroup.getCreationDate(), studyGroup.isActive());
            studyGroupMemberRepository.save(studyGroupRecord);
            StudyGroupMember studyGroupMember = buildStudyGroupMember(studyGroupRecord);
            return studyGroupMember;
        }
    }
    private boolean isStudyGroupMemberExists(StudyGroupMemberId studyGroupMemberId) {
        return studyGroupMemberRepository.existsById(studyGroupMemberId);
    }
    private StudyGroupMember getExistingStudyGroupMember(StudyGroupMemberId studyGroupMemberId) {
        Optional<StudyGroupMemberRecord> byMemberId = studyGroupMemberRepository.findByMemberId(studyGroupMemberId.getMemberId());
        StudyGroupMemberRecord record = byMemberId.get();
        return buildStudyGroupMember(record);
    }

    public Member findMemberById(String memberId) {
        //todo
        Optional<MemberRecord> findById = memberRepository.findById(memberId);
        if(!findById.isPresent()){
            return null;
        }
        // convert from record to study group(domain object)
        MemberRecord memberRecord = findById.get();
        return buildMember(memberRecord);
    }

    private Member buildMember(MemberRecord record) {
        Member member = new Member(record.getEmail(), record.getPassword());
        return member;
    }


    private static StudyGroupMember buildStudyGroupMember(StudyGroupMemberRecord record) {
        StudyGroupMember studyGroupMember = new StudyGroupMember(record.getGroupId(),
                record.getMemberId(), record.getGroupName(),
                record.getDiscussionTopic(),
                record.getCreationDate(),
                record.isActive());
        return studyGroupMember;
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


    public void removeMemberFromStudyGroup(String groupId, String memberId) {
        StudyGroupMemberId studyGroupMemberId = new StudyGroupMemberId(groupId, memberId);

        boolean doesExist = studyGroupMemberRepository.existsById(studyGroupMemberId);
        if(!doesExist) {
            throw new StudyGroupNotFoundException("Study group not found for groupId: " + groupId);
        }

        Optional<StudyGroupMemberRecord> studyGroupMemberRecordById = studyGroupMemberRepository.findById(studyGroupMemberId);

        StudyGroupMemberRecord studyGroupMemberRecord = studyGroupMemberRecordById.get();
        studyGroupMemberRepository.delete(studyGroupMemberRecord);
    }


    // remove all members from study group
    public void removeAllMembersFromStudyGroup(String groupId) {
        Optional<List<StudyGroupMemberRecord>> recordsOfMembers = studyGroupMemberRepository.findByGroupId(groupId);

        if (recordsOfMembers.isEmpty()) {
            throw new StudyGroupNotFoundException("Study group not found for groupId: " + groupId);
        }
        List<StudyGroupMemberRecord> membersToRemove = recordsOfMembers.get();

        for (StudyGroupMemberRecord memberRecord : membersToRemove) {
            studyGroupMemberRepository.delete(memberRecord);
        }
    }

    public List<StudyGroup> getAllStudyGroups() {
        Iterable<StudyGroupRecord> studyGroupRecords = studyGroupRepository.findAll();
        List<StudyGroup> studyGroups = new ArrayList<>();
        if(studyGroupRecords == null){
            return null;
        }
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
        studyGroupRecord.setGroupId(studyGroup.getGroupId());
        studyGroupRecord.setGroupName(studyGroup.getGroupName());
        studyGroupRecord.setDiscussionTopic(studyGroup.getDiscussionTopic());
        studyGroupRecord.setCreationDate(studyGroup.getCreationDate());
        studyGroupRecord.setActive(true);
        studyGroupRepository.save(studyGroupRecord);
        cache.evict(studyGroup.getGroupId());
    }

    public void deleteStudyGroup(String groupId) {
        studyGroupRepository.deleteById(groupId);
        removeAllMembersFromStudyGroup(groupId);
        cache.evict(groupId);
    }

}
