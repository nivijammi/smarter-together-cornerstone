import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import LambdaClient from "../api/LambdaClient";

/**
 * Logic needed for the create an account for the website.
 */
class MyAccountPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['topic', 'goal', 'onLoad', 'upcomingSessions', 'sidebar', 'getAllForUser', 'errorHandler'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        document.getElementById('topic-submit').addEventListener('click', this.topic);
        document.getElementById('goal-submit').addEventListener('click', this.goal);
        this.onLoad();

        this.lambda = new LambdaClient();
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

    async getAllForUser() {
        let userId = localStorage.getItem("userId");
        console.log(userId);
        let sessions = await this.lambda.getStudySessionsByUserId(userId, this.errorHandler);
        console.log(sessions);

        return sessions;
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

    async sidebar() {
        let goal = localStorage.getItem("goal");
        let topic = localStorage.getItem("topic");

        if(goal != null) {
            let goalContainer = document.getElementById("goal");
            let goalHtml = `<p>Study ${goal} hours per month</p> </br>`;

            goalContainer.innerHTML = goalHtml;
        }

        if(topic != null) {
            let topicContainer = document.getElementById('topics');
            let topicHtml = `<p>${topic}</p>`;

            topicContainer.innerHTML = topicHtml;
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    /**
     * Method to run when the search flights submit button is pressed.
     */
    async onLoad(event) {
        this.sidebar();
        this.upcomingSessions();
    }

    async topic(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();

        console.log("1");

        // Get the values from the form inputs
        const topic = document.getElementById('topic-name').value;

        localStorage.setItem("topic", topic);
        this.onLoad();
        document.getElementById('topic-name').innerHTML = "";
    }

    async goal(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();

        console.log("2");

        // Get the values from the form inputs
        const goal = document.getElementById('goal-input').value;

        localStorage.setItem("goal", goal);
        this.onLoad();
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const myAccountPage = new MyAccountPage();
    myAccountPage.mount();
};

window.addEventListener('DOMContentLoaded', main);