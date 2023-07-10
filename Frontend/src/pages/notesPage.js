import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import NoteClient from "../api/NoteClient";

/**
 * Logic needed for the create an account for the website.
 */
class NotesPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onCreate', 'onDelete', 'onUpdate', 'onLoad', 'upcomingSessions',
        'sidebar', 'renderNotes', 'errorHandler'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        document.getElementById('create').addEventListener('click', this.onCreate);
        document.getElementById('delete').addEventListener('click', this.onDelete);
        document.getElementById('update').addEventListener('click', this.onUpdate);

        this.onLoad();
        this.dataStore.addChangeListener(this.renderNotes);

        this.client = new NoteClient();

    }


    // Helper Methods --------------------------------------------------------------------------------------------------

    async renderNotes() {
        let notes = this.dataStore.get("notes");
        console.log(notes);
        let noteResults = document.getElementById("results");
        let noteHtml = "";

        if(notes){
            noteHtml += `
                <div>
                    <h3>${notes.noteId}</h3>
                    <p>${notes.content}</p>
                </div>
            `
        } else {
            noteHtml += `<p>No notes.</p>`;
        }

        noteResults.innerHTML = noteHtml;
    }

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
    async onLoad() {
        this.sidebar();
        this.upcomingSessions();
        this.renderNotes();
    }

    async onCreate(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();
        let userId = localStorage.getItem("userId");

        console.log("1");

        // Get the values from the form inputs
        let noteId = document.getElementById('notes-topic').value
        let content = document.getElementById('notes').value;
        let date = new Date();

        let notes = await this.client.createNote(noteId, userId, content, date, date, this.errorHandler);
        document.getElementById('notes-topic').innerHTML = "";
        document.getElementById('notes').innerHTML = "";

        this.dataStore.set("notes", notes);
    }

    async onDelete(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();

        console.log("2");

        // Get the values from the form inputs
        let noteId = document.getElementById('notes-id').value

        let notes = await this.client.deleteNote(noteId, this.errorHandler);
        this.dataStore.remove("notes", notes);
    }

    async onUpdate(event) {
        event.preventDefault();
        let userId = localStorage.getItem("userId");

        console.log(3);


        //Get the values from the form inputs
        let noteId = document.getElementById('update-note-id').value
        let content = document.getElementById('update-notes').value;
        let date = new Date();

        //Get existing note
        let oldNote = await this.client.getNoteById(noteId, this.errorHandler);

        let createDate = oldNote.createdDateTime;
        console.log(createDate);

        //delete the old note
        await this.client.deleteNote(noteId, this.errorHandler);

        //Create a new note
        let notes = await this.client.createNote(noteId, userId, content, createDate, date, this.errorHandler);
        this.dataStore.set("notes", notes);
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const notesPage = new NotesPage();
    notesPage.mount();
};

window.addEventListener('DOMContentLoaded', main);