import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import LambdaClient from "../api/LambdaClient";

/**
 * Logic needed for the create an account for the website.
 */
class CreateSessionsPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onCreate', 'onSubmit', 'onDelete', 'onLoad', 'errorHandler', 'getBySessionId', 'getBySubject', 'upcomingSessions',
        'getAllForUser', 'renderSessions', 'loadDropDowns'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        document.getElementById('topic-submit').addEventListener('click', this.onCreate);
        document.getElementById('update-submit').addEventListener('click', this.onSubmit);
        document.getElementById('delete-submit').addEventListener('click', this.onDelete);
        this.onLoad;

        this.lambda = new LambdaClient();
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

        return sessions;
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

        return sessions;
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

    async loadDropDowns() {
        let sessions = this.getAllForUser();

        if(sessions){
            let dropDownHtml = "";

            //Update
            let updateDropDown = document.getElementById('update');

            //Delete
            let deleteDropDown = document.getElementById('delete');

            //Gather sessions
            for(session of sessions) {
                dropDownHtml += '<option value="${session.sessionId}">Topic: ${session.subject} ${session.date}</option>'
            }

            updateDropDown.innerHTML = dropDownHtml;
            deleteDropDown.innerHTML = dropDownHtml;
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

    async onCreate(event) {
        event.preventDefault();

                 console.log("create");

                 let userId = localStorage.getItem("userId");
                 let topicName = document.getElementById('topic-name').value;
                 let duration = document.getElementById('duration').value;
                 let date = document.getElementById('date').value;
                 let resources = document.getElementById('resources').value;

                 let createdSession = await this.lambda.addStudySession(user, topicName, duration, date, resources, this.errorHandler);

                 if(!createdSession) {
                     let errorMessage = document.getElementById("create-error");
                     let errorHtml = '<p>Could not create session. Try again.</p>';
                     errorMessage.innerHTML = errorMessage;
                 }
    }

    async onSubmit(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();

        console.log("update");

        //Get
        let sessionId = document.getElementById('update').value;
        let session = this.getBySessionId(sessionId);

        if(session) {

        }

        let sessionName = session.subject;
        let sessionDuration = session.duration;
        let sessionDate = session.date;
        let sessionNotes = session.notes;

        //Delete

        //Create
        let userId = localStorage.getItem("userId");
        let topicName = document.getElementById('topic-name').value;
        let duration = document.getElementById('duration').value;
        let date = document.getElementById('date').value;
        let resources = document.getElementById('resources').value;

        let createdSession = await this.lambda.addStudySession(user, topicName, duration, date, resources, this.errorHandler);

        if(!createdSession) {
            let errorMessage = document.getElementById("create-error");
            let errorHtml = '<p>Could not create session. Try again.</p>';
            errorMessage.innerHTML = errorMessage;
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const createSessionsPage = new CreateSessionsPage();
    createSessionsPage.mount();
};

window.addEventListener('DOMContentLoaded', main);