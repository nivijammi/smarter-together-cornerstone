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
        this.bindClassMethods(['renderGraph', 'errorHandler', 'onLoad', 'goal', 'renderSessions', 'renderUpcomingSessions',
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

    async renderGraph(userId) {
            // Prevent the form from refreshing the page
            event.preventDefault();

            console.log("1");



        }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onLoad() {
        let userId = localStorage.getItem("userId");
        this.goal();
        this.renderSessions(userId);
        renderUpcomingSessions(userId);
        renderGroups(userId);
        renderResources(userId);
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