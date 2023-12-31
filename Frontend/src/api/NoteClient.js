import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class NoteClient extends BaseClass {

    constructor(props = {}){
        super();
        this.bindClassMethods(['clientLoaded', 'createNote', 'getNoteById', 'updateNote', 'deleteNote'], this);
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

    async createNote(noteId, userId, content, createdDateTime, updatedDateTime, errorCallback) {
        try {
            const response = await this.client.post(`/v1/notes/create`,
            {
                noteId: noteId,
                userId: userId,
                content: content,
                createdDateTime: createdDateTime,
                updatedDateTime: updatedDateTime
            });
            return response.data;
        } catch (error) {
            this.handleError("createNote", error, errorCallback)
        }
    }

    async getNoteById(noteId, errorCallback){
        try{
            const response = await this.client.get(`/v1/notes/${noteId}`);
            return response.data;
        } catch (error) {
            this.handleError("getNoteById", error, errorCallback);
        }
    }

    async updateNote(noteId, userId, content, createdDateTime, updatedDateTime, errorCallback){
        try{
            const response = await this.client.put(`/v1/notes/${noteId}`,
                {
                    userId: userId,
                    content: content,
                    createdDateTime: createdDateTime,
                    updatedDateTime: updatedDateTime
                });
            return response.data;
        } catch (error) {
            this.handleError("updateNote", error, errorCallback);
        }
    }

    async deleteNote(noteId, errorCallback){
        try{
            const response = await this.client.delete(`/v1/notes/${noteId}`);
            return response.data;
        } catch (error) {
            this.handleError("deleteStudyGroup", error, errorCallback);
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