import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import UserLoginClient from "../api/UserLoginClient";

/**
 * Logic needed for the create an account for the website.
 */
class SigninPage extends BaseClass {
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

    async emailPasswordValidation(email, pswd) {
        let errorHtml = "";
        let validEmail = false;
        let validPassword = false;

        if(email == "" || email == " " || pswd == "" || pswd == " ") {
            document.getElementById("error").innerHTML = errorHtml += `<div><p>Email and/or password invalid!</p></div>`;
        } else {
            validEmail = true;
            validPassword = true;
        }

        if(validEmail && validPassword) {
            return true;
        } else {
            return false;
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onSubmit(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();

        console.log("1");

        // Get the values from the form inputs
        const email = document.getElementById('email').value;
        const pswd = document.getElementById('pswd').value;

        console.log(email);
        console.log(pswd);

            let user = await this.loginClient.loginAsync(email, pswd, this.errorHandler);

            if(user) {
                localStorage.setItem("userId", email);
                window.location='dashboard.html';
            }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const signinPage = new SigninPage();
    signinPage.mount();
};

window.addEventListener('DOMContentLoaded', main);