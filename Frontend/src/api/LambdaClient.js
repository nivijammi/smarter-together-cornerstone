import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class LambdaClient extends BaseClass {

    constructor(props = {}){
        super();
        this.bindClassMethods(['clientLoaded', 'addSession', 'getSessionBySessionId', 'getSessionsBySubject', 'getSessionsByUserId', 'getAllSessions', 'deleteSession'], this);
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

 /**
     * Gets the flight for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns the flight
     */
    async addSession(id, errorCallback) {
        try {
            const response = await this.client.post(`/add`);
            return response.data;
        } catch (error) {
            this.handleError("addSession", error, errorCallback)
        }
    }

/**
     * Deletes the flight for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    async getSessionBySessionId(id, errorCallback){
        try{
            const response = await this.client.get(`/session/${sessionId}`);
            return response.data;
        } catch (error) {
            this.handleError("getSessionBySessionId", error, errorCallback);
        }
    }

    async getSessionsBySubject(subject, errorCallback){
        try{
            const response = await this.client.get(`/subject/${subject}`);
            return response.data;
        } catch (error) {
            this.handleError("getSessionsBySubject", error, errorCallback);
        }
    }

    async getSessionsByUserId(userId, errorCallback){
        try{
            const response = await this.client.get(`/${userId}`);
            return response.data;
        } catch (error) {
            this.handleError("getSessionsByUserId", error, errorCallback);
        }
    }

    async getAllSessions(errorCallback){
        try{
            const response = await this.client.get(`/all`);
            return response.data;
        } catch (error) {
            this.handleError("getAllSessions", error, errorCallback);
        }
    }

    async deleteSession(id, errorCallback){
        try{
            const response = await this.client.get(`/delete/${sessionId}`);
            return response.data;
        } catch (error) {
            this.handleError("deleteSession", error, errorCallback);
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