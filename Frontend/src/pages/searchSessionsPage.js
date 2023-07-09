import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import LambdaClient from "../api/LambdaClient";
import StudyGroupClient from "../api/StudyGroupClient";

/**
 * Logic needed for the create an account for the website.
 */
class SearchSessionsPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onSubmit', 'onLoad', 'errorHandler', 'getBySessionId', 'getBySubject', 'upcomingSessions',
        'getAllForUser', 'renderSessions'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        document.getElementById('submit').addEventListener('click', this.onSubmit);
        this.onLoad;

        this.lambda = new LambdaClient();
        this.client = new StudyGroupClient();
    }

    //Render Method

    async renderSessions() {
        let sessionResults = document.getElementById('results');
        let sessions = this.dataStore.get("sessions");
        let sessionHtml = "";

        if(sessions) {
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

    async getBySessionId(sessionId) {
        let sessions = await this.lambda.getStudySessionBySessionId(sessionId, this.errorHandler);

        if(sessions) {
            this.dataStore.set("sessions", sessions);
            this.renderSessions();
        }
    }

    async getBySubject(subject) {
        let sessions = await this.lambda.getStudySessionsBySubject(subject, this.errorHandler);

        if(sessions) {
            this.dataStore.set("sessions", sessions);
            this.renderSessions();
        }
    }

    async getAllForUser() {
        let userId = localStorage.getItem("userId");
        let sessions = await this.lambda.getStudySessionsByUserId(userId);

        if(sessions) {
            this.dataStore.set("sessions", sessions);
            this.renderSessions();
        }
    }

    async upcomingSessions() {
        let userId = localStorage.getItem("userId");
        let sessions = await this.lambda.getStudySessionsByUserId(userId);
        let currentDate = new ZoneDateTime();
        let tilDate = new ZoneDateTime().plusWeeks(1);

        if(sessions) {
        let sessionResults = document.getElementById('sessions');
        let sessionHtml = "";

            for(session of sessions) {
                if(session.date <= tilDate) {
                    sessionHtml += `
                        <div class="upcoming-sessions">
                            <p>Subject: ${session.subject}</p>
                            <p>Date: ${session.date}</p>
                            <p>Duration: ${session.duration}</p>
                        </div>
                    `
                }
            }
        }

    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    /**
    * Fills sidebar with current data
    */
    async onLoad() {
        let goal = localStorage.getItem("goal");
        let topic = localStorage.getItem("topic");
        let upcomingSessions = this.upcomingSessions();

        if(goal != null) {
            let goalContainer = document.getElementById("goal");
            let goalHtml = `<p>My goal is to study ${goal} hours per month</p>`;

            goalContainer.innerHTML = goalHtml;
        }
    }

    /**
    * Session method
    */
    async onSubmit(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();

        console.log("1");

        // Get the values from the form inputs
        let searchType = "";
        if(document.getElementById('search-topic').checked) {
            searchType = "topic";
        } else if(document.getElementById('seach-id').checked) {
            searchType = "sessionId";
        } else if(document.getElementById('search-all').checked) {
            searchType = "all";
        }

        let input = document.getElementById('input').value;

        if(searchType == "topic") {
            this.getBySubject(input);
        } else if(searchType == "all") {
            this.getBySessionId(input);
        } else {
            this.getStudySessionsByUserId();
        }
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