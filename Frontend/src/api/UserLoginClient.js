import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class UserLoginClient extends BaseClass {

    constructor(props = {}){
        super();
        this.bindClassMethods(['clientLoaded', 'loginAsync', 'registerUser'], this);
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

    async loginAsync(email, password, errorCallback) {
        try {
            const response = await this.client.post(`/v2/users/login`,
            {
                "email": email,
                "password": password
            }
            );
            return response.data;
        } catch (error) {
            this.handleError("loginAsync", error, errorCallback)
        }
    }

    async registerUser(email, password, errorCallback){
    console.log(email, password);
        try{
            const response = await this.client.post(`/v1/users/register`,
            {
                "email": email,
                "password": password
            }
            );
            return response.data;
        } catch (error) {
            this.handleError("registerUser", error, errorCallback);
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