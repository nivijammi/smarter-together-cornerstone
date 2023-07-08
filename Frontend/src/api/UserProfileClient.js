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

    async addNewUser(firstName, lastName, email, password, creationDate, errorCallback) {
        try {
            const response = await this.client.post(`/v1/users`,
            {
                firstName: firstName,
                lastName: lastName,
                email: email,
                password: password,
                creationDate: creationDate
            });
            return response.data;
        } catch (error) {
            this.handleError("addNewUser", error, errorCallback)
        }
    }

    async updateUserProfile(email, firstName, lastName, password, creationDate, errorCallback){
        try{
            const response = await this.client.put(`/v1/{userID}`,
            {
                firstName: firstName,
                lastName: lastName,
                email: email,
                password: password,
                creationDate: creationDate
            });
            return response.data;
        } catch (error) {
            this.handleError("updateUserProfile", error, errorCallback);
        }
    }

    async getUserById(userId, errorCallback){
        try{
            const response = await this.client.get(`/v1/{userID}`);
            return response.data;
        } catch (error) {
            this.handleError("getUserById", error, errorCallback);
        }
    }

    async deleteUserById(userId, errorCallback){
        try{
            const response = await this.client.delete(`/v1/{userID}`);
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