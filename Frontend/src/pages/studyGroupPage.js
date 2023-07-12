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
        window.addEventListener('load', this.onLoad);

        this.lambda = new LambdaClient();
        this.groupClient = new StudyGroupClient();
        this.reviewClient = new StudyGroupReviewClient();
    }

    //Render Method

    async renderGroups() {
        let groupResults = document.getElementById('results');
        let groups = this.dataStore.get("groups");
        let groupHtml = "";
        console.log(groups);

        let buttonIndex = 0;

        if(groups) {
            if(groups[1] == null) {
            console.log(groups);
             let groupIdTest = groups.groupId;
                console.log(groupIdTest);
                let averageRating = await this.reviewClient.getAverageRatingById(groupIdTest, this.errorHandler);

                groupHtml += `
                    <div class="results-content">
                        <h3>${groups.groupName}</h3>
                        <h4>Topic: ${groups.discussionTopic}</h4>
                        <p>Rating: ${averageRating}</p>
                        <p>Members:</p>
                        <ul>
                `
                let members = await this.groupClient.getStudyGroupMembers(groupIdTest, this.errorHandler);
                if(members){
                    for(const member of members) {
                        groupHtml += `
                            <li>${member.memberId}</li>
                        `
                    }
                    groupHtml += `
                        </ul>
                    `
                } else {
                    groupHtml += `
                            <li>None</li>
                        <ul>
                    `
                }
                groupHtml += `
                        <button id="${buttonIndex}" value="${group.groupId}">Join Group</button>
                    </div>
                `
            } else {

                for(const group of groups) {
                let averageRating = await this.reviewClient.getAverageRatingById(group.groupId, this.errorHandler);

                    groupHtml += `
                        <div class="results-content">
                            <h3>${group.groupName}</h3>
                            <h4>Topic: ${group.discussionTopic}</h4>
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
                                <button id="${buttonIndex}" value="${group.groupId}">Join Group</button>
                            </div>
                    `
                }
            }
        } else {
            groupHtml += '<p>No groups...</p>'
        }

        groupResults.innerHTML = groupHtml;

        setTimeout(() => {
        console.log("in timemout")
            if(buttonIndex == 0) {
                const button = document.getElementById(`0`)
                console.log(button)
                console.log("1 waiting")
                if(button){
                    button.addEventListener('click', this.joinGroup);
                    console.log("1 listning")
                }
            } else {
                for (let i = 0; i < buttonIndex; i++) {
                    console.log(`${i}`)
                    const button = document.getElementById(`${i}`);
                    console.log(button)
                    console.log("waiting")

                    if(button){
                        button.addEventListener('click', this.joinGroup);
                        console.log("listning")
                    }
                }
            }
        }, 100);
        console.log("completed")
        let form = document.getElementById('form');
        form.reset();
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
        let goal = localStorage.getItem("goal");
        let topic = localStorage.getItem("topic");

        if(goal != null) {
            let goalContainer = document.getElementById("goal");
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
        let groups = await this.groupClient.getStudyGroupById(groupId, this.errorHandler);

        if(groups) {
            this.dataStore.set("groups", groups);
            this.renderGroups();
        }
    }

    async getByRatings(topic, minRating) {
        let groupsByRating = await this.reviewClient.getGroupsWithDesiredAvgRatingByTopic(minRating, topic, this.errorHandler);
        console.log(groupsByRating);
        let index = 0;
        let buttonIndex = 0;


        let groups = [];

        if(groupsByRating) {
            if(groupsByRating[1] == null) {
                let keys = ""
                for(var key in groupsByRating) {
                    keys = key;
                }

                groups[index] = (await this.groupClient.getStudyGroupById(keys, this.errorHandler));

            } else {
                let keys = ""
                for(var key in groupsByRating) {
                    keys = key;

                    for(const group of groupsByRating) {
                        groups[index] = (await this.groupClient.getStudyGroupById(keys, this.errorHandler));
                        index += 1;
                        console.log(index);
                    }
                }


            }

            index = 0;
            let groupResults = document.getElementById('results');
            let groupHtml = "";

            if(groups) {
                if(groups[1] == null) {
                console.log(groups);
                let groupIdTest = groups[0].groupId;
                console.log(groupIdTest);
                let averageRating = await this.reviewClient.getAverageRatingById(groups[0].groupId, this.errorHandler);

                    groupHtml += `
                        <div class="results-content">
                            <h3>${groups[0].groupName}</h3>
                            <h4>Topic: ${groups[0].discussionTopic}</h4>
                            <p>Rating: ${averageRating}</p>
                            <p>Members:</p>
                            <ul>
                    `
                    let members = await this.groupClient.getStudyGroupMembers(groups[0].groupId, this.errorHandler);
                    if(members){
                        for(const member of members) {
                            groupHtml += `
                                <li>${member.memberId}</li>
                            `
                        }
                        groupHtml += `
                            </ul>
                        `
                    } else {
                        groupHtml += `
                                <li>None</li>
                            </ul>
                        `
                    }
                    groupHtml += `
                            <button id="${buttonIndex}" value="${groupIdTest}">Join Group</button>
                        </div>
                    `
                } else {

                    for(const group of groups) {
                    let averageRating = await this.reviewClient.getAverageRatingById(group.groupId, this.errorHandler);

                        groupHtml += `
                            <div class="results-content" id="results-content">
                                <h3>${group.groupName}</h3>
                                <h4>Topic: ${group.discussionTopic}</h4>
                                <p>Rating: ${averageRating}</p>
                                <p>Members:</p>
                                <ul>
                        `
                        let members = await this.groupClient.getStudyGroupMembers(group.groupId, this.errorHandler);
                        if(members){
                            for(const member of members) {
                                groupHtml += `
                                   <li>${member.memberId}</li>
                                `
                            }
                            groupHtml += `
                                </ul>
                            `
                        } else {
                            groupHtml += `
                                    <li>None</li>
                                </ul>
                            `
                        }


                        groupHtml += `
                                <button id="${buttonIndex}" value="${group.groupId}">Join Group</button>
                            </div>
                        `

                        buttonIndex ++;
                    }
                }
            } else {
                groupHtml += '<p>No groups...</p>'
            }

            groupResults.innerHTML = groupHtml;

            setTimeout(() => {
            console.log("in timemout")
                if(buttonIndex == 0) {
                    const button = document.getElementById(`0`)
                    console.log(button)
                    console.log("1 waiting")
                    if(button){
                        button.addEventListener('click', this.joinGroup);
                        console.log("1 listning")
                    }
                } else {
                    for (let i = 0; i < buttonIndex; i++) {
                        console.log(`${i}`)
                        const button = document.getElementById(`${i}`);
                        console.log(button)
                        console.log("waiting")

                        if(button){
                            button.addEventListener('click', this.joinGroup);
                            console.log("listning")
                        }
                    }
                }
            }, 100);
            console.log("completed")
        }
        let form = document.getElementById('form');
        form.reset();
    }

    async getAllGroups() {
        let groups = await this.groupClient.getAllStudyGroups(this.errorHandler);
        let index = 0;
        let buttonIndex = 0;

        index = 0;
        buttonIndex = 0;
        let groupResults = document.getElementById('results');
        let groupHtml = "";

        if(groups) {
            if(groups[1] == null) {
            console.log(groups);
             let groupIdTest = groups[0].groupId;
                console.log(groupIdTest);
                let averageRating = await this.reviewClient.getAverageRatingById(groups[0].groupId, this.errorHandler);

                groupHtml += `
                    <div class="results-content">
                        <h3>${groups[0].groupName}</h3>
                        <h4>Topic: ${groups[0].discussionTopic}</h4>
                        <p>Rating: ${averageRating}</p>
                        <p>Members:</p>
                        <ul>
                `
                let members = await this.groupClient.getStudyGroupMembers(groups[0].groupId, this.errorHandler);
                if(members){
                    for(const member of members) {
                        groupHtml += `
                            <li>${member.memberId}</li>
                        `
                    }
                    groupHtml += `
                        </ul>
                    `
                } else {
                    groupHtml += `
                            <li>None</li>
                        </ul>
                    `
                }
                groupHtml += `
                        <button id="${buttonIndex}" value="${groupIdTest}">Join Group</button>
                    </div>
                `
            } else {

                for(const group of groups) {
                let averageRating = await this.reviewClient.getAverageRatingById(group.groupId, this.errorHandler);

                    groupHtml += `
                        <div class="results-content" id="results-content">
                            <h3>${group.groupName}</h3>
                            <h4>Topic: ${group.discussionTopic}</h4>
                            <p>Rating: ${averageRating}</p>
                            <p>Members:</p>
                            <ul>
                    `
                    let members = await this.groupClient.getStudyGroupMembers(group.groupId, this.errorHandler);
                    if(members){
                        for(const member of members) {
                            groupHtml += `
                               <li>${member.memberId}</li>
                            `
                        }
                        groupHtml += `
                            </ul>
                        `
                    } else {
                        groupHtml += `
                                <li>None</li>
                            </ul>
                        `
                    }


                    groupHtml += `
                            <button id="${buttonIndex}" value="${group.groupId}">Join Group</button>
                        </div>
                    `

                    buttonIndex ++;
                }
            }
        } else {
            groupHtml += '<p>No groups...</p>'
        }
        groupResults.innerHTML = groupHtml;

        setTimeout(() => {
        console.log("in timemout")
            if(buttonIndex == 0) {
                const button = document.getElementById(`0`)
                console.log(button)
                console.log("1 waiting")
                if(button){
                    button.addEventListener('click', this.joinGroup);
                    console.log("1 listning")
                }
            } else {
                for (let i = 0; i < buttonIndex; i++) {
                    console.log(`${i}`)
                    const button = document.getElementById(`${i}`);
                    console.log(button)
                    console.log("waiting")

                    if(button){
                        button.addEventListener('click', this.joinGroup);
                        console.log("listning")
                    }
                }
            }
        }, 100);
        console.log("completed")
        let form = document.getElementById('form');
        form.reset();

    }

    async upcomingSessions() {
        let userId = localStorage.getItem("userId");
        let sessions = await this.lambda.getStudySessionsByUserId(userId, this.errorHandler);

        if(sessions) {
            let currentDate = new Date();
            let currentYear = currentDate.getFullYear();
            let currentMonth = currentDate.getMonth() + 1;
            let currentDay = currentDate.getDate();

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
        this.sidebar();
        this.upcomingSessions();
    }

    /**
    * Session method
    */
    async onSubmit(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();


        // Get the values from the form inputs
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
        console.log("join")

        let clickedButtonId = event.target.id;
        console.log(clickedButtonId);
        let userId = localStorage.getItem("userId");
        let groupId = document.getElementById(clickedButtonId).value;
        console.log(groupId)
        let added = await this.groupClient.addMemberToStudyGroup(groupId, userId, this.errorHandler);

        window.location='my-group.html';
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