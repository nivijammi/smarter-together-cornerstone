import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import LambdaClient from "../api/LambdaClient";

/**
 * Logic needed for the create an account for the website.
 */
class DashboardPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['renderGraph', 'errorHandler', 'onLoad', 'renderGraph', 'goal', 'renderSessions', 'renderUpcomingSessions',
        'renderResources', 'studyHours'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        window.addEventListener('load', this.onLoad());

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

    //Render Methods

    async goal() {
        let goal = localStorage.getItem("goal");
        if(goal) {
            document.getElementById("goal").innerHTML = '<p>My goal is to study ${goal} hours per week.</p>'
        }
    }

    async getAllForUser() {
        let userId = localStorage.getItem("userId");
        console.log(userId);
        let sessions = await this.lambda.getStudySessionsByUserId(userId, this.errorHandler);
        console.log(sessions);

        return sessions;
    }

    async renderSessions() {
        let sessions = this.getAllForUser();
        let allSessions = document.getElementById('my-sessions');
        let allHtml = "";

        if(sessions) {
            for(const session of sessions){
                allHtml += `
                    <div class="sessions-content">
                        <p>${session.subject}</p>
                        <p>Date: ${session.date}</p>
                        <p>Duration: ${session.duration}</p>
                        <p>Resources: ${session.notes}</p>
                    </div>
                `
            }
        }
    }

    async renderUpcomingSessions() {
        let sessions = this.getAllForUser();

        if(sessions) {
            let currentDate = new Date();
            let currentMonth = currentDate.getMonth();
            let currentDay = currentDate.getDay();

            let sessionResults = document.getElementById('sessions');
            let sessionHtml = "";

            for(session of sessions) {
                let sessionDate = new Date().of(session.date);
                let month = sessionDate.getMonth();
                let day = sessionDate.getDay();

                if(month == currentMonth) {
                    if(day > currentDay) {
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
            if(sessionHtml != ""){
                sessionResults = sessionHtml;
            }
        }
    }

    async renderResources() {
        let sessions = this.getAllForUser();
        let resources = document.getElementById("resource-list");
        let resourceHtml = "";

        if(sessions) {
            for(const session of sessions) {
                resourceHtml += `
                    <div class="results">
                        <p>Topic: ${session.subject}</p>
                        <p>Resources: ${session.note}</p>
                    </div>
                `
            }
        }
    }

    async studyHours(weekNumber) {
        //call getStudySessionsByUserId to get all their sessions.
        let sessions = this.getAllForUser();

        if(!sessions) {
            return 0;
        } else {
            //date and grab current month and year.
            let currentYear = new Date().getFullYear();
            let currentMonth = new Date().getMonth();

            let totalMinutes = 0;
            //get days of the week pertaining to the specific week ex. week 1 etc.
            for(session of sessions){
                let sessionDate = new Date().of(session.date);
                let sessionMonth = sessionDate.getMonth();
                let sessionDay = sessionDate.getDay();
                let sessionYear = sessionDate.getYear();

                if(sessionYear == currentYear){
                    if(sessionMonth == currentMonth) {
                        if(weekNumber == 1) {
                            if(sessionDay <= 7) {
                                totalMinutes += session.duration;
                            }
                        } else if(weekNumber == 2) {
                            if(sessionDay > 7 && sessionDay <= 14) {
                                totalMinutes += session.duration;
                            }
                        } else if(weekNumber == 3) {
                            if(sessionDay > 14 && sessionDay <= 21) {
                                totalMinutes += session.duration;
                            }
                        } else if(weekNumber == 4) {
                            if(sessionDay > 21) {
                                totalMinutes += session.duration;
                            }
                        }
                    }
                }
            }

            return (totalMinutes / 60);
        }



        //for loop to find sessions with matching month, year, and matching dates for the specific week
        //return total duration for matching sessions divided by 60.
    }

    async renderGraph() {
    //https://plotly.com/javascript/getting-started/
        let graph = document.getElementById('graph');

        let week1 = this.studyHours(1);
        let week2 = this.studyHours(2);
        let week3 = this.studyHours(3);
        let week4 = this.studyHours(4);

        let study = [{
          x: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
          y: [week1, week2, week3, week4],
          type: 'bar'
        }];

        Plotly.newPlot(graph, study);
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onLoad() {
        this.goal();
        this.renderSessions();
        this.renderUpcomingSessions();
        this.renderResources();
        this.renderGraph()
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const dashboardPage = new DashboardPage();
    dashboardPage.mount();
};

window.addEventListener('DOMContentLoaded', main);