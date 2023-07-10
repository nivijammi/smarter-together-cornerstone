import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import Toastify from "toastify-js";
import StudyGroupClient from "../api/StudyGroupClient";
import StudyGroupReviewClient from "../api/StudyGroupReviewClient";

/**
 * Logic needed for the create an account for the website.
 */
class MyGroupPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onCreate', 'onDelete', 'onAddMember', 'onRemoveMember', 'onSubmit',
        'onLoad', 'errorHandler', 'renderGroup', 'loadDropDowns'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the flight list.
     */
    mount() {
        document.getElementById('create').addEventListener('click', this.onCreate);
        document.getElementById('delete').addEventListener('click', this.onDelete);
        document.getElementById('add').addEventListener('click', this.onAddMember);
        document.getElementById('remove').addEventListener('click', this.onRemoveMember);
        document.getElementById('review').addEventListener('click', this.onSubmit);        this.onLoad();

        this.groupClient = new StudyGroupClient();
        this.reviewClient = new StudyGroupReviewClient();
    }

    //Render Method

    async renderGroup() {
//                      id=results
//                      <h3>Group Name</h3>
//                      <h4>Group Topic</h4>
//                      <p>Rating:</p>
//                      <p>Members</p>

//                      id=group-name
//                      <h3>Group Name</h3>
//                      <h4>Group Topic</h4>
        let userId = localStorage.getItem("userId");
        let fullResults = document.getElementById('results');
        let partialResults = document.getElementById('group-name');
        let group = this.dataStore.get("group");
        let fullHtml = "";
        let partialHtml = "";

        if(group) {
            fullHtml += `
                <div class="results-content">
                    <h3>${group.groupName}</h3>
                    <h4>${group.discussionTopic}</h4>
                    <p>${group.Rating} stars</p>
                    <p>Members:</p>
                    <ul>
            `
            let members = await this.groupClient.getStudyGroupMembers(group.groupId, this.errorHandler);

            if(members) {
                for(const member of members){
                    fullHtml += `
                                <li>${member.memberId}</li>
                    `
                }
            } else {
                fullHtml += `
                <li>${userId}</li>
                `
            }
            forHtml += `
                    </ul>
                </div>
            `

            partialHtml += `
                <h3>${group.groupName}</h3>
                <h4>${group.discussionTopic}</h4>
            `
        } else {
            fullHtml += `<p>No group</p>`
            partialHtml += `<p>No group</p>`
        }

        fullResults.innerHTML = fullHtml;
        partialResults.innerHTML = partialHtml;
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

    async loadDropDowns() {
        let userId = localStorage.getItem("userId");
        console.log(new Date());
        let groups = ""

        try{
            groups = await this.groupClient.getAllStudyGroups(this.errorHandler);
        } catch (error) {
            groups = null;
        }


        if(groups){
        let dropDownHtml = "";
            //Groups dropdown
            let groupDropDown = document.getElementById('my-groups');

            //Gather all groups
            for(const group of groups) {
                //get members from groups
                let members = await this.groupClient.getStudyGroupMembers(group.groupId, this.errorCallback);

                if(members){
                    //grab memberId
                    let memberId = member.memberId;
                    //select groups that current user is part off. add options for dropdown
                    if(memberId == userId){
                        dropDownHtml += `<option value="${group.groupId}">Topic: ${group.groupName}</option>`
                    }
                }
            }

            groupDropDown.innerHTML = dropDownHtml;
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onLoad(event) {
        this.loadDropDowns();
    }

    async onCreate(event) {
        event.preventDefault();

         console.log("create");

         let userId = localStorage.getItem("userId");
         let groupName = document.getElementById('name').value;
         let topic = document.getElementById('group-topic').value;
         let date = new Date();
         let createdGroup = await this.groupClient.addNewStudyGroup(name, topic, date, true, this.errorHandler);

         console.log(createdGroup);
         if(!createdGroup) {
             let errorMessage = document.getElementById("error");
             let errorHtml = '<p>Could not create group. Try again.</p>';
             errorMessage.innerHTML = errorHtml;
         } else {
             location.reload();
         }
    }

    async onDelete(event) {
        event.preventDefault();

        console.log("delete");
        let groupId = document.getElementById('my-groups').value;
        await this.groupClient.deleteStudyGroup(groupId, this.errorHandler);

        location.reload();
    }

    async onAddMember(event) {
        event.preventDefault();

        console.log("add");
        let groupId = document.getElementById('my-groups').value;
        let memberId = document.getElementById('add-member').value;
        await this.groupClient.addMemberToStudyGroup(groupId, memberId, this.errorHandler);

        location.reload();
    }

    async onRemoveMember(event) {
        event.preventDefault();

        console.log("remove");
        let groupId = document.getElementById('my-groups').value;
        let memberId = document.getElementById('remove-member').value;
        await this.groupClient.removeMemberFromStudyGroup(groupId, memberId, this.errorHandler);

        location.reload();
    }

    async onSubmit(event) {
        // Prevent the form from refreshing the page

        console.log("review");

        //Get
        let groupId = document.getElementById('my-groups').value;
        let group = await this.groupClient.getStudyGroupById(groupId, this.errorHandler);
        let groupName = group.groupName;
        let topic = group.discussionTopic;
        let rating = "";
        let review = document.getElementById('review-text').value;

        if(fiveStars = document.getElementById('star-a').checked) {
            rating = document.getElementById('star-a').value;
        } else if(document.getElementById('star-b').checked) {
            rating = document.getElementById('star-b').value;
        } else if(document.getElementById('star-c').checked) {
            rating = document.getElementById('star-c').value;
        } else if (document.getElementById('star-d').checked) {
            rating = document.getElementById('star-d').value;
        } else if (oneStar = document.getElementById('star-e').checked) {
            rating = document.getElementById('star-e').value;
        }

        if(rating == "") {
            let errorMessage = document.getElementById('review-error');
            let errorHtml = `<p>No rating. Try again</p>`;

            errorMessage.innerHTML = errorHtml;
        } else {
            let review = await this.reviewClient.submitReview(groupId, groupName, topic, rating, review, this.errorHandler);
        }
        location.reload();
    }

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const myGroupPage = new MyGroupPage();
    myGroupPage.mount();
};

window.addEventListener('DOMContentLoaded', main);