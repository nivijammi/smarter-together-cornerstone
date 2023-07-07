import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import UserLoginClient from "../api/UserLoginClient";

/**
 * Logic needed for the create an account for the website.
 */
class CreateAccountPagePage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onSubmit'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        document.getElementById('submit').addEventListener('click', this.onSubmit);

        this.client = new UserLoginClientClient();
    }

    async reserveFlight(flightId1, errorHandler1) {
        const reservedFlight = await this.client.reserveTicket(flightId1, errorHandler1);

        const ticketId = reservedFlight.ticketId;
        localStorage.setItem("reservedTicketId", ticketId);
        window.location='checkout.html';
    }

    // Helper Methods --------------------------------------------------------------------------------------------------

    async validatePwsd(pswd, retypePswd) {
        if(pswd == null || retypePswd == null) {
            return false;
        } else if(pswd != retypePswd) {
            return false;
        } else {
            return true;
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    /**
     * Method to run when the search flights submit button is pressed.
     */
    async onSubmit(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();

        // Get the values from the form inputs
        const firstName = document.getElementById('first-name').value;
        const lastName = document.getElementById('last-name').value;
        const username = document.getElementById('username').value;
        const email = document.getElementById('email').value;
        const pswd = document.getElementById('pswd').value;
        const retypePswd = document.getElementById('retype-pswd').value;

//        boolean matches = validatePwsd(pswd, retypePswd);

        //Create account if passwords match
        if(matches) {
            const user = await this.client.registerUser(email, pswd, this.errorHandler);
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