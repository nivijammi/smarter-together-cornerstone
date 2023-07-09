import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import LambdaClient from "../api/LambdaClient";
import StudyGroupClient from "../api/StudyGroupClient";

/**
 * Logic needed for the create an account for the website.
 */
class DashboardPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['renderGraph', 'errorHandler', 'onLoad', 'renderGraph', 'goal', 'renderSessions', 'renderUpcomingSessions',
        'renderGroups', 'renderResources'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        this.onLoad();
        this.renderGraph();

        this.lambda = new LambdaClient();
        this.client = new StudyGroupClient();
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

    async renderSessions(userId) {

    }

    async renderUpcomingSessions(userId) {

    }

    async renderGroups(userId) {

    }

    async renderResources(userId) {

    }

    async studyHours(weekNumber) {
        let userId = localStorage.getItem("userId");
        //date and grab current month and year.
        let currentYear = new Date().getFullYear();
        let currentMonth = new Date().getMonth();

        //call getStudySessionsByUserId to get all their sessions.
        let sessions = await this.lambda.getStudySessionsByUserId(userId);

        if(!session) {
            return 0;
        } else {
            //get days of the week pertaining to the specific week ex. week 1 etc.
            if(weekNumber == 1) {

            } else if(weekNumber == 2) {

            } else if(weekNumber == 3) {

            } else if(weekNumber == 4) {

            }
        }



        //for loop to find sessions with matching month, year, and matching dates for the specific week
        //return total duration for matching sessions divided by 60.
    }

    async renderGraph(userId) {
    //https://plotly.com/javascript/getting-started/
        let graph = document.getElementById('graph');
//        Plotly.newPlot( graph, [{
//        	x: ["Week 1", "Week 2", "Week 3", "Week 4", "Week 5"],
//        	y: [5, 10, 15, 20, 25, 30, 35, 40] }], {
//        	margin: { t: 0 } } );



        let soloWeek1 = this.studyHours(1);
        let soloWeek2 =
        let soloWeek3 =
        let soloWeek4 =

        let groupWeek1 =
        let groupWeek2 =
        let groupWeek3 =
        let groupWeek4 =

        let soloStudy = {
          x: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
          y: [soloWeek1, soloWeek2, soloWeek3, soloWeek4],
          name: 'Solo Study',
          type: 'bar'
        };

        let groupStudy = {
          x: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
          y: [groupWeek1, groupWeek2, groupWeek3, groupWeek4],
          name: 'Group Study',
          type: 'bar'
        };

        let totalStudy = {
          x: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
          y: [soloWeek1 + groupWeek1, soloWeek2 + groupWeek2, soloWeek3 + groupWeek3, soloWeek4 + groupWeek4],
          name: 'Total Study',
          type: 'bar'
        };

        let data = [trace1, trace2, trace3];

        let layout = {barmode: 'group'};

        Plotly.newPlot(graph, data, layout);
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onLoad() {
        let userId = localStorage.getItem("userId");
        this.goal();
        this.renderSessions(userId);
        this.renderUpcomingSessions(userId);
        this.renderGroups(userId);
        this.renderResources(userId);
        this.renderGraph(userId);
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