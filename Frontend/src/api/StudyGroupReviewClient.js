import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class StudyGroupReviewClient extends BaseClass {

    constructor(props = {}){
        super();
        this.bindClassMethods(['clientLoaded', 'submitReview', 'getReviewByReviewId', 'getStudyGroupReviewsByTopic',
        'getGroupsWithDesiredAvgRatingByTopic', 'getAverageRatingById'], this);
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

    async submitReview(groupId, groupName, discussionTopic, rating, reviewComment, errorCallback) {
        try {
            const response = await this.client.post(`/v1/studygroup/reviews`,
            {
                groupId: groupId,
                groupName: groupName,
                discussionTopic: discussionTopic,
                rating: rating,
                reviewComment: reviewComment
            });
            return response.data;
        } catch (error) {
            this.handleError("submitReview", error, errorCallback)
        }
    }

    async getReviewByReviewId(reviewId, errorCallback){
        try{
            const response = await this.client.get(`/v1/studygroup/reviews/{reviewId}`);
            return response.data;
        } catch (error) {
            this.handleError("getReviewByReviewId", error, errorCallback);
        }
    }

    async getStudyGroupReviewsByTopic(discussionTopic, errorCallback){
        try{
            const response = await this.client.get(`/v1/studygroup/reviews/discussionTopic/{discussionTopic}`);
            return response.data;
        } catch (error) {
            this.handleError("getStudyGroupReviewsByTopic", error, errorCallback);
        }
    }

    async getGroupsWithDesiredAvgRatingByTopic(averageRating, discussionTopic, errorCallback){
        try{
            const response = await this.client.get(`/v1/studygroup/ratings/{averageRating}/{discussionTopic}`);
            return response.data;
        } catch (error) {
            this.handleError("getGroupsWithDesiredAvgRatingByTopic", error, errorCallback);
        }
    }

    async getAverageRatingById(groupId, errorCallback){
        try{
            const response = await this.client.get(`/v1/studygroup/averageRating/{groupId}`);
            return response.data;
        } catch (error) {
            this.handleError("getAverageRatingById", error, errorCallback);
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