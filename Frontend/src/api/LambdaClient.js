import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class LambdaClient extends BaseClass {

    constructor(props = {}){
        super();
        this.bindClassMethods(['clientLoaded', 'addStudySession', 'getStudySessionBySessionId', 'getStudySessionsBySubject',
        'getStudySessionsByUserId', 'getAllStudySessions', 'deleteStudySessionBySessionId'], this);
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

    async addStudySession(userId, subject, duration, date, notes, errorCallback) {
        try {
            const response = await this.client.post(`/v1/studysession/add`,
            {
                userId: userId,
                subject: subject,
                duration: duration,
                date: date,
                notes: notes
            });
            return response.data;
        } catch (error) {
            this.handleError("addStudySession", error, errorCallback)
        }
    }

    async getStudySessionBySessionId(sessionId, errorCallback){
        try{
            const response = await this.client.get(`/v1/session/{sessionId}`);
            return response.data;
        } catch (error) {
            this.handleError("getStudySessionBySessionId", error, errorCallback);
        }
    }

    async getStudySessionsBySubject(subject, errorCallback){
        try{
            const response = await this.client.get(`/v1/studysession/subject/{subject}`);
            return response.data;
        } catch (error) {
            this.handleError("getStudySessionsBySubject", error, errorCallback);
        }
    }

    async getStudySessionsByUserId(userId, errorCallback){
        try{
            const response = await this.client.get(`/v1/studysession/{userId}`);
            return response.data;
        } catch (error) {
            this.handleError("getStudySessionsByUserId", error, errorCallback);
        }
    }

    async getAllStudySessions(errorCallback){
        try{
            const response = await this.client.get(`/v1/studysession/all`);
            return response.data;
        } catch (error) {
            this.handleError("getAllStudySessions", error, errorCallback);
        }
    }

    async deleteStudySessionBySessionId(sessionId, errorCallback){
        try{
            const response = await this.client.delete(`/v1/studysession/delete/{sessionId}`);
            return response.data;
        } catch (error) {
            this.handleError("deleteStudySessionBySessionId", error, errorCallback);
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