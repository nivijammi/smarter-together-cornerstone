import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import NoteClient from "../api/NoteClient";


class NotesPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onCreate', 'onDelete', 'onUpdate', 'renderNotes', 'errorHandler'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        document.getElementById('create').addEventListener('click', this.onCreate);
        document.getElementById('delete').addEventListener('click', this.onDelete);
        document.getElementById('update').addEventListener('click', this.onUpdate);

        window.addEventListener('load', this.onLoad);
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
            if(notes[1] == null) {
            console.log(notes.noteId)
            console.log(notes[0].noteId)
                noteHtml += `
                    <div>
                        <h3>${notes[0].noteId}</h3>
                        <p>${notes[0].content}</p>
                    </div>
                `
            } else {
                for(const note of notes) {
                    noteHtml += `
                        <div>
                            <h3>${note.noteId}</h3>
                            <p>${note.content}</p>
                        </div>
                    `
                }
            }
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


    // Event Handlers --------------------------------------------------------------------------------------------------

    /**
     * Method to run when the search flights submit button is pressed.
     */

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

        let allNotes = "";
        let index = "";

        if(this.dataStore.get("notes") == null) {
            allNotes = [];
            index = 0;
        } else {
            allNotes = this.dataStore.get("notes");
            index = allNotes.length;
        }


        if(allNotes[1] == null) {
            allNotes[index] = notes;
        } else {
            allNotes[index] = notes;
        }

        this.dataStore.set("notes", allNotes);
        let form = document.getElementById('create-form');
        form.reset();
    }

    async onDelete(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();

        console.log("2");

        // Get the values from the form inputs
        let noteId = document.getElementById('notes-id').value

        let notes = await this.client.deleteNote(noteId, this.errorHandler);
        this.dataStore.set("notes", null);
        let form = document.getElementById('delete-form');
        form.reset();
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



            console.log(notes);
            let noteResults = document.getElementById("results");
            let noteHtml = "";

            if(notes){
                if(notes[1] == null) {
                    noteHtml += `
                        <div>
                            <h3>${notes.noteId}</h3>
                            <p>${notes.content}</p>
                        </div>
                    `
                } else {
                    for(const note of notes) {
                        noteHtml += `
                            <div>
                                <h3>${note.noteId}</h3>
                                <p>${note.content}</p>
                            </div>
                        `
                    }
                }
            } else {
                noteHtml += `<p>No notes.</p>`;
            }

            noteResults.innerHTML = noteHtml;
            let form = document.getElementById('update-form');
            form.reset();
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