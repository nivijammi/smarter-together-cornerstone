import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import UserLoginClient from "../api/UserLoginClient";
import UserProfileClient from "../api/UserProfileClient";

/**
 * Logic needed for the create an account for the website.
 */
class CreateAccountPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onSubmit', 'errorHandler', 'emailPasswordValidation'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        document.getElementById('submit').addEventListener('click', this.onSubmit);

        this.loginClient = new UserLoginClient();
        this.profileClient = new UserProfileClient();
    }

    async reserveFlight(flightId1, errorHandler1) {
        const reservedFlight = await this.client.reserveTicket(flightId1, errorHandler1);

        const ticketId = reservedFlight.ticketId;
        localStorage.setItem("reservedTicketId", ticketId);
        window.location='checkout.html';
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

    async emailPasswordValidation(email, pswd, retypePswd) {
        let validEmail = false;
        let validPassword = false;

        if(email == "" || email == null) {
            document.getElementById("email-error").innerHTML = `<div><p>Email not valid!</p></div>`;
        } else {
            validEmail = true;
        }

        if(pswd == "" || pswd == null || retypePswd == "" || retypePswd == null) {
            document.getElementById("pswd-error").innerHTML = `<div><p>Passwords do not match!</p></div>`;
        } else {
            if(pswd == retypePswd) {
                validPassword = true;
            }
        }

        if(validEmail && validPassword) {
            return true;
        } else {
            return false;
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    /**
     * Method to run when the search flights submit button is pressed.
     */
    async onSubmit(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();

        console.log("1");

        // Get the values from the form inputs
        const firstName = document.getElementById('first-name').value;
        const lastName = document.getElementById('last-name').value;
        const email = document.getElementById('email').value;
        const pswd = document.getElementById('pswd').value;
        const retypePswd = document.getElementById('retype-pswd').value;

        let validEntries = this.emailPasswordValidation(email, pswd, retypePswd);
        if(validEntries) {
            console.log("2");

            let user = await this.loginClient.registerUser(email, pswd, this.errorHandler);

            if(user) {
                let profile = await this.profileClient.addNewUser(firstName, lastName, email, pswd, creationDate, this.errorHandler);

                const userId = user.userId;
                localStorage.setItem("userId", userId);
                window.location='dashboard.html';
            }
        }

    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const createAccountPage = new CreateAccountPage();
    createAccountPage.mount();
};

window.addEventListener('DOMContentLoaded', main);