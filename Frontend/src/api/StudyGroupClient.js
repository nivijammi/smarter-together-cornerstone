import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class StudyGroupClient extends BaseClass {

    constructor(props = {}){
        super();
        this.bindClassMethods(['clientLoaded', 'addNewStudyGroup', 'getStudyGroupById', 'getAllStudyGroups',
        'updateStudyGroup', 'deleteStudyGroup', 'addMemberToStudyGroup', 'getStudyGroupMembers',
        'removeMemberFromStudyGroup', 'removeAllMemberFromStudyGroup'], this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    async addNewStudyGroup(groupName, discussionTopic, creationDate, active, errorCallback) {
        try {
            const response = await this.client.post(`/v1/groups`,
            {
                groupName: groupName,
                discussionTopic: discussionTopic,
                creationDate: creationDate,
                active: active
            });
            return response.data;
        } catch (error) {
            this.handleError("addNewStudyGroup", error, errorCallback)
        }
    }

    async getStudyGroupById(groupId, errorCallback){
        try{
            const response = await this.client.get(`/v1/groups/{groupId}`);
            return response.data;
        } catch (error) {
            this.handleError("getStudyGroupById", error, errorCallback);
        }
    }

    async getAllStudyGroups(errorCallback){
        try{
            const response = await this.client.get(`/v1/groups`);
            return response.data;
        } catch (error) {
            this.handleError("getAllStudyGroups", error, errorCallback);
        }
    }

    async updateStudyGroup(groupId, groupName, discussionTopic, creationDate, active, errorCallback){
        try{
            const response = await this.client.put(`/v1/groups/{groupId}`,
            {
                groupName: groupName,
                discussionTopic: discussionTopic,
                creationDate: creationDate,
                active: active
            });
            return response.data;
        } catch (error) {
            this.handleError("updateStudyGroup", error, errorCallback);
        }
    }

    async deleteStudyGroup(groupId, errorCallback){
        try{
            const response = await this.client.delete(`/v1/groups/{groupId}`);
            return response.data;
        } catch (error) {
            this.handleError("deleteStudyGroup", error, errorCallback);
        }
    }

    //Member Methods
    async addMemberToStudyGroup(groupId, memberId, errorCallback){
        try{
            const response = await this.client.post(`/v1/groups/{groupId}/members/{memberId}`);
            return response.data;
        } catch (error) {
            this.handleError("addMemberToStudyGroup", error, errorCallback);
        }
    }

    async getStudyGroupMembers(groupId, errorCallback){
        try{
            const response = await this.client.get(`/v1/groups/{groupId}/members/`);
            return response.data;
        } catch (error) {
            this.handleError("getStudyGroupMembers", error, errorCallback);
        }
    }

    async removeMemberFromStudyGroup(groupId, memberId, errorCallback){
        try{
            const response = await this.client.delete(`/v1/groups/{groupId}/members/{memberId}`);
            return response.data;
        } catch (error) {
            this.handleError("removeMemberFromStudyGroup", error, errorCallback);
        }
    }

    async removeAllMemberFromStudyGroup(groupId, errorCallback){
        try{
            const response = await this.client.delete(`/v1/groups/{groupId}/members`);
            return response.data;
        } catch (error) {
            this.handleError("removeAllMemberFromStudyGroup", error, errorCallback);
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}