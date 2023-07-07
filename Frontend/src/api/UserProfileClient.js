import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class UserProfileClient extends BaseClass {

    constructor(props = {}){
        super();
        this.bindClassMethods(['clientLoaded', 'addNewUser', 'updateUserProfile', 'getUserById', 'deleteUserById'], this);
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
    async addNewUser(firstName, lastName, email, username, pswd, creationDate, errorCallback) {
        try {
            const response = await this.client.post(`/add`);
            return response.data;
        } catch (error) {
            this.handleError("addNewUser", error, errorCallback)
        }
    }

/**
     * Deletes the flight for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    async updateUserProfile(id, errorCallback){
        try{
            const response = await this.client.get(`/session/${sessionId}`);
            return response.data;
        } catch (error) {
            this.handleError("updateUserProfile", error, errorCallback);
        }
    }

    async getUserById(id, errorCallback){
        try{
            const response = await this.client.get(`/subject/${subject}`);
            return response.data;
        } catch (error) {
            this.handleError("getUserById", error, errorCallback);
        }
    }

    async deleteUserById(id, errorCallback){
        try{
            const response = await this.client.delete(`/delete/${userId}`);
            return response.data;
        } catch (error) {
            this.handleError("deleteUserById", error, errorCallback);
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