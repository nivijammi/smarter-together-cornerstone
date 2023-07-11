import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import LambdaClient from "../api/LambdaClient";

/**
 * Logic needed for the create an account for the website.
 */
class SearchSessionsPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onSubmit', 'onLoad', 'errorHandler', 'getBySessionId', 'getBySubject', 'upcomingSessions',
        'renderSessions', 'sidebar'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        document.getElementById('submit').addEventListener('click', this.onSubmit);
        window.addEventListener('load', this.onLoad);

        this.lambda = new LambdaClient();
    }

    //Render Method

    async renderSessions() {
        console.log(2);
        let sessionResults = document.getElementById('results');
        let sessions = this.dataStore.get("sessions");
        let sessionHtml = "";

        if(sessions) {
            if(sessions[1] == null) {
                sessionHtml += `
                    <div class="results-content">
                        <h3>${sessions.subject}</h3>
                        <h4>Date: ${sessions.date}</h4>
                        <p>Duration: ${sessions.duration}</p>
                        <p>Resources: ${sessions.notes}</p>
                    </div>
                `
            } else {
                for(const session of sessions) {
                    sessionHtml += `
                        <div class="results-content">
                            <h3>${session.subject}</h3>
                            <h4>Date: ${session.date}</h4>
                            <p>Duration: ${session.duration}</p>
                            <p>Resources: ${session.notes}</p>
                        </div>
                    `
                }
            }
        } else {
            sessionHtml += '<p>No sessions...</p>'
        }

        sessionResults.innerHTML = sessionHtml;
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

    async getBySessionId(sessionId) {
        let sessions = await this.lambda.getStudySessionBySessionId(sessionId, this.errorHandler);
        console.log("by id" + sessions);
        if(sessions) {
            this.dataStore.set("sessions", sessions);

        }
        this.renderSessions();
    }

    async getBySubject(topic) {
        console.log(3)
        let sessions = await this.lambda.getStudySessionsBySubject(topic, this.errorHandler);
        console.log("subject" + sessions)
        if(sessions) {
            this.dataStore.set("sessions", sessions);

        }

        let sessionResults = document.getElementById('results');
            let sessionHtml = "";

            if(sessions) {
                if(sessions[1] == null) {
                    sessionHtml += `
                        <div class="results-content">
                            <h3>${sessions[0].subject}</h3>
                            <h4>Date: ${sessions[0].date}</h4>
                            <p>Duration: ${sessions[0].duration}</p>
                            <p>Resources: ${sessions[0].notes}</p>
                        </div>
                    `
                } else {
                    for(const session of sessions) {
                        sessionHtml += `
                            <div class="results-content">
                                <h3>${session.subject}</h3>
                                <h4>Date: ${session.date}</h4>
                                <p>Duration: ${session.duration}</p>
                                <p>Resources: ${session.notes}</p>
                            </div>
                        `
                    }
                }
            } else {
                sessionHtml += '<p>No sessions...</p>'
            }

            sessionResults.innerHTML = sessionHtml;
    }

    async getStudySessionsByUserId() {
        console.log(4);

        let userId = localStorage.getItem("userId");
        let sessions = await this.lambda.getStudySessionsByUserId(userId, this.errorHandler);

        console.log("all" + sessions);
        if(sessions) {
            this.dataStore.set("sessions", sessions);

        }
        this.renderSessions();
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
//        let searchType = "";
//        if(document.getElementById('search-id').checked) {
//            searchType = "groupId";
//        } else if(document.getElementById('search-rating').checked) {
//            searchType = "rating";
//        } else if(document.getElementById('search-all').checked) {
//            searchType = "all";
//        }
//
//        let groupId = document.getElementById('group-id').value;
//        let topicName = document.getElementById('topic-search').value;
//        let rating = document.getElementById('rating').value;
//
//        if(searchType == "groupId") {
//            this.getByGroupId(groupId);
//        } else if(searchType == "rating") {
//            this.getByRatings(topicName, rating);
//        } else {
//            this.getAllGroups();
//        }


        let searchType = "";

        if(document.getElementById('search-topic').checked) {
            searchType = "topic";
        } else if(document.getElementById('search-id').checked) {
            searchType = "sessionId";
        } else if(document.getElementById('search-all').checked) {
            searchType = "all";
        }

        let input = document.getElementById('user-input').value;
        console.log(input);


    console.log(input);
        if(searchType == "topic") {
            this.getBySubject(input);
            console.log("searching")
        } else if(searchType == "sessionId") {
            this.getBySessionId(input);
        } else if(searchType == "all"){
            this.getStudySessionsByUserId();
        }


        console.log(searchType)
        console.log(6)
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const searchSessionsPage = new SearchSessionsPage();
    searchSessionsPage.mount();
};

window.addEventListener('DOMContentLoaded', main);