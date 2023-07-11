import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import LambdaClient from "../api/LambdaClient";

/**
 * Logic needed for the create an account for the website.
 */
class AnalyticsPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['renderGraph', 'errorHandler', 'onLoad', 'analytics',], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        window.addEventListener('load', this.onLoad);

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

    async analytics() {
        let userId = localStorage.getItem("userId");
        let sessions = await this.lambda.getStudySessionsByUserId(userId, this.errorHandler);

        //Get total hours studied
        //Month + 1 because months start at 0;
        let totalMinutes = 0;
        let currentYear = new Date().getFullYear();
        let currentMonth = new Date().getMonth() + 1;

        for(const session of sessions){
            let sessionDate = session.date;
            let sessionYear = sessionDate.substring(0,4);
            let sessionMonth = sessionDate.substring(5, 7);

            if(sessionYear == currentYear){
                if(sessionMonth == currentMonth) {
                    totalMinutes += session.duration;
                }
            }
        }

        let totalHours = (totalMinutes / 60);
        let goal = localStorage.getItem("goal");

        let hoursPrint = document.getElementById('total-hours');
        let goalPrint = document.getElementById('goal');
        let goalMetPrint = document.getElementById('goal-met');

        let hourHtml = `<p>${totalHours}</p>`;
        let goalHtml = `<p>${goal}</p>`;
        let goalMetHtml = "";

        if((goal / totalHours) <= 1) {
            goalMetHtml = `<p><b>has</b></p>`;
        } else {
            goalMetHtml = `<p><b>has not</b></p>`;
        }

        hoursPrint.innerHTML = hourHtml;
        goalPrint.innerHTML = goalHtml;
        goalMetPrint.innerHTML = goalMetHtml;
    }


    async renderGraph() {
        //https://plotly.com/javascript/getting-started/
        let graph = document.getElementById("graph");
        let goal = localStorage.getItem("goal");

        //call getStudySessionsByUserId to get all their sessions.
        let userId = localStorage.getItem("userId");
        let sessions = await this.lambda.getStudySessionsByUserId(userId, this.errorHandler);
        console.log(sessions);

        if(!sessions) {
            return 0;
        } else {
            //date and grab current month and year.
            let currentYear = new Date().getFullYear();
            let currentMonth = new Date().getMonth() + 1;

            let totalMinutes1 = 0;
            let totalMinutes2 = 0;
            let totalMinutes3 = 0;
            let totalMinutes4 = 0;
            //get days of the week pertaining to the specific week ex. week 1 etc.
            for(const session of sessions){
                let sessionDate = session.date;
                let sessionYear = sessionDate.substring(0,4);
                let sessionMonth = sessionDate.substring(5, 7);
                let sessionDay = sessionDate.substring(8);

                if(sessionYear == currentYear){
                    if(sessionMonth == currentMonth) {
                        if(sessionDay <= 7) {
                            totalMinutes1 += session.duration;
                        }
                        if(sessionDay > 7 && sessionDay <= 14) {
                           totalMinutes2 += session.duration;
                        }
                        if(sessionDay > 14 && sessionDay <= 21) {
                          totalMinutes3 += session.duration;
                        }
                        if(sessionDay > 21) {
                         totalMinutes4 += session.duration;
                        }
                    }
                }
            }

            let week1 = (totalMinutes1 / 60);
            let week2 = (totalMinutes2 / 60);
            let week3 = (totalMinutes3 / 60);
            let week4 = (totalMinutes4 / 60);

        //https://plotly.com/javascript/bar-charts/
        var trace1 = {
          x: ["Week 1", "Week 2", "Week 3", "Week 4"],
          y: [week1, week2, week3, week4],
          name: 'Hours Studied',
          marker: {color: '#02bd7f'},
          type: 'bar'
        };

        var trace2 = {
          x: ["Week 1", "Week 2", "Week 3", "Week 4"],
          y: [goal, goal, goal, goal],
          name: 'Goal Hours Studied',
          marker: {color: '#003e29'},
          type: 'bar'
        };

        var data = [trace1, trace2];

        var layout = {
          title: 'My Study Progress',
          paper_bgcolor: 'FEFEE2',
          plot_bgcolor: 'FEFEE2',
          xaxis: {tickfont: {
              title: 'Week',
              color: 'rgb(0, 0, 0)',
              size: 14,
              color: 'rgb(0, 0, 0)'
            }},
          yaxis: {
            title: 'Time (hours)',
            color: 'rgb(0, 0, 0)',
            titlefont: {
              size: 16,
              color: 'rgb(0, 0, 0)'
            },
            tickfont: {
              size: 14,
              color: 'rgb(0, 0, 0)'
            }
          },
          legend: {
            x: 0,
            y: 1.5,
            bgcolor: 'FEFEE2',
            bordercolor: '#003e29'
          },
          barmode: 'group',
          bargap: 0.15,
          bargroupgap: 0.1
        };

        Plotly.newPlot(graph, data, layout);

        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onLoad() {
        event.preventDefault();
        this.analytics();
        this.renderGraph();
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const analyticsPage = new AnalyticsPage();
    analyticsPage.mount();
};

window.addEventListener('DOMContentLoaded', main);