import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import LambdaClient from "../api/LambdaClient";
import StudyGroupClient from "../api/StudyGroupClient";
import StudyGroupReviewClient from "../api/StudyGroupReviewClient";

/**
 * Logic needed for the create an account for the website.
 */
class StudyGroupPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onSubmit', 'onLoad', 'joinGroup', 'errorHandler', 'getByGroupId', 'getByRatings',
        'getAllGroups', 'upcomingSessions', 'renderGroups', 'sidebar'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        document.getElementById('submit').addEventListener('click', this.onSubmit);
//        document.getElementById('select').addEventListener('click', this.joinGroup);
        this.onLoad();

        this.lambda = new LambdaClient();
        this.groupClient = new StudyGroupClient();
        this.reviewClient = new StudyGroupReviewClient();
    }

    //Render Method

    async renderGroups() {
        let groupResults = document.getElementById('results');
        let groups = this.dataStore.get("groups");
        let groupHtml = "";

        if(sessions) {
            for(const group of groups) {
            let averageRating = await this.reviewClient.getAverageRatingById(group.groupId, this.errorHandler);
                groupHtml += `
                    <div class="results-content">
                        <h3>${group.groupName}</h3>
                        <h4>Date: ${group.discussionTopic}</h4>
                        <p>Rating: ${averageRating}</p>
                        <p>Members:
                `
                let members = await this.groupClient.getStudyGroupMembers(group.groupId, this.errorHandler);
                if(members){
                    for(const member of members) {
                        groupHtml += `
                            ${member.memberId}
                        `
                    }
                    groupHtml += `
                        </p>
                    `
                } else {
                    groupHtml += `
                        None</p>
                    `
                }
                groupHtml += `
                    <button id='select' value=group.groupId>Join Group<button>
                    </div>
                `
            }
        } else {
            sessionHtml += '<p>No groups...</p>'
        }

        groupResults.innerHTML = groupHtml;

        document.getElementById('select').addEventListener('click', this.joinGroup);
    }


    // Helper Methods --------------------------------------------------------------------------------------------------

    async errorHandler(error) {
        Toastify({
            text: error,
            duration: 4500,
            gravity: "top",
            position: 'right',
            close: true,
            style: {
                background: "linear-gradient(to right, rgb(255, 95, 109), rgb(255, 195, 113))"
            }
        }).showToast();
    }

    async sidebar() {
        console.log("sidebar");
        let goal = localStorage.getItem("goal");
        let topic = localStorage.getItem("topic");

        if(goal != null) {
            let goalContainer = document.getElementById("goal");
            console.log(goalContainer);
            let goalHtml = `<p>Study ${goal} hours per week</p>`;

            goalContainer.innerHTML = goalHtml;
        }

        if(topic != null) {
            let topicContainer = document.getElementById('topics');
            let topicHtml = `<p>${topic}</p>`;

            topicContainer.innerHTML = topicHtml;
        }
    }

    async getByGroupId(groupId) {
        let sessions = await this.lambda.getStudySessionBySessionId(sessionId, this.errorHandler);

        if(sessions) {
            this.dataStore.set("sessions", sessions);
            this.renderSessions();
        }
    }

    async getByRatings(topic, minRating) {
        let sessions = await this.reviewClient.getGroupsWithDesiredAvgRatingByTopic(minRating, topic, this.errorHandler);

        if(sessions) {
            this.dataStore.set("sessions", sessions);
            this.renderSessions();
        }
    }

    async getAllGroups() {
        let groups = await this.groupClient.getAllStudyGroups(this.errorHandler);

        if(groups) {
            this.dataStore.set("groups", groups);
            this.renderGroups();
        }
    }

    async upcomingSessions() {
        let userId = localStorage.getItem("userId");
        let sessions = await this.lambda.getStudySessionsByUserId(userId, this.errorHandler);
        console.log(sessions)

        if(sessions) {
            let currentDate = new Date();
            let currentYear = currentDate.getFullYear();
            let currentMonth = currentDate.getMonth() + 1;
            console.log(currentMonth);
            let currentDay = currentDate.getDate();
            console.log(currentDay);

            let sessionResults = document.getElementById('sessions');
            let sessionHtml = "";

            for(const session of sessions) {
                let sessionDate = session.date;
                let year = sessionDate.substring(0,4);
                let month = sessionDate.substring(5, 7);
                let day = sessionDate.substring(8);

                if(year == currentYear){
                    if(month == currentMonth) {
                        if(day > currentDay) {
                            sessionHtml += `
                                <div class="upcoming-sessions">
                                    <p>Subject: ${session.subject}
                                    </br>Date: ${session.date}
                                    </br>Duration: ${session.duration} minutes</p>
                                </div>
                            `
                        }
                    }
                }
            }
            sessionResults.innerHTML = sessionHtml;
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onLoad() {
        console.log("load method");
        this.sidebar();
        this.upcomingSessions();
    }

    /**
    * Session method
    */
    async onSubmit(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();

        console.log("1");

        // Get the values from the form inputs
        let idSearch = document.getElementById('search-id');
        let ratingsSearch = document.getElementById('search-rating');
        let allSearch = document.getElementById('search-all');

        let searchType = "";
                if(document.getElementById('search-id').checked) {
                    searchType = "groupId";
                } else if(document.getElementById('search-rating').checked) {
                    searchType = "rating";
                } else if(document.getElementById('search-all').checked) {
                    searchType = "all";
                }

                let groupId = document.getElementById('group-id').value;
                let topicName = document.getElementById('topic-search').value;
                let rating = document.getElementById('rating').value;

                if(searchType == "groupId") {
                    this.getByGroupId(groupId);
                } else if(searchType == "rating") {
                    this.getByRatings(topicName, rating);
                } else {
                    this.getAllGroups();
                }
    }

    async joinGroup(event) {
        event.preventDefault();

        console.log(join);
        let userId = localStorage.getItem("userId");
        let groupId = document.getElementById('select').value;
        await this.groupClient.addMemberToStudyGroup(groupId, userId, this.errorHandler);
        window.location("my-group.html");
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const studyGroupPage = new StudyGroupPage();
    studyGroupPage.mount();
};

window.addEventListener('DOMContentLoaded', main);