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
        'errorHandler', 'renderGroup', 'loadDropDowns', 'selectGroup'], this);
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
        document.getElementById('review').addEventListener('click', this.onSubmit);
        window.addEventListener('load', this.loadDropDowns);
        document.getElementById('my-groups').addEventListener('click', this.renderGroup);

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
        let addResults = document.getElementById('results-add');
        let removeResults = document.getElementById('results-remove');
        let partialResults = document.getElementById('group-name');
        let fullHtml = "";
        let partialHtml = "";

        let groupId = document.getElementById('my-groups').value;
        console.log(groupId)
        if(groupId != "Please select a group") {

            let group = await this.groupClient.getStudyGroupById(groupId, this.errorHandler);
            let averageRating = await this.reviewClient.getAverageRatingById(group.groupId, this.errorHandler);
            let rounded = parseFloat(averageRating).toFixed(2);


            if(group) {
            console.log(group)
            console.log(group.groupId);
                fullHtml += `
                    <div class="results-content">
                        <h3>${group.groupName}</h3>
                        <h4>Studying ${group.discussionTopic}</h4>
                        <p>Rating: ${averageRating} stars<p>
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
                fullHtml += `
                        </ul>
                    </div>
                `

                partialHtml += `
                    <h3>${group.groupName}</h3>
                    <h4>Studying ${group.discussionTopic}</h4>
                `
            }
        }  else {
            fullHtml += `<p>No group</p>`
            partialHtml += `<p>No group</p>`
        }

        fullResults.innerHTML = fullHtml;
        addResults.innerHTML = fullHtml;
        removeResults.innerHTML = fullHtml;
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

    async selectGroup() {
        let group = document.getElementById('my-groups').value;
        this.dataStore.set("group", group);
    }

    async loadDropDowns() {
        console.log("load");
        let userId = localStorage.getItem("userId");
        let groups = await this.groupClient.getAllStudyGroups(this.errorHandler);
        console.log(groups)


        if(groups){
            let dropDownHtml = "";
            //Groups dropdown
            let groupDropDown = document.getElementById('my-groups');

            //Gather all groups
            for(const group of groups) {
                //get members from groups
                let members = await this.groupClient.getStudyGroupMembers(group.groupId, this.errorCallback);
                console.log(members)
                if(members){
                    for(const member of members){
                        //grab memberId
                        let memberId = member.memberId;
                        //select groups that current user is part off. add options for dropdown
                        if(memberId == userId){
                            dropDownHtml += `<option value="${group.groupId}">Group Name: ${group.groupName}</option>`
                        }
                    }
                }
            }

            if(dropDownHtml == "") {
                dropDownHtml = `<option>`
            }
            groupDropDown.innerHTML = dropDownHtml;
        }
        this.renderGroup();
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onCreate(event) {
        event.preventDefault();

         console.log("create");

         let userId = localStorage.getItem("userId");
         let groupName = document.getElementById('name').value;
         let topic = document.getElementById('group-topic').value;
         let date = new Date()

         let year = date.getFullYear();
         let month = date.getMonth() + 1;
         let day = date.getDate();

         if(month < 10) {
            month = "0" + month;
         }

         let dateString = year + "-" + month + "-" + day;
         console.log(dateString);

         let createdGroup = await this.groupClient.addNewStudyGroup(groupName, topic, dateString, true, this.errorHandler);
         await this.groupClient.addMemberToStudyGroup(createdGroup.groupId, userId, this.errorHandler);

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

        console.log(groupId);
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
        event.preventDefault();

        console.log("review");

        //Get
        let groupId = document.getElementById('my-groups').value;
        let group = await this.groupClient.getStudyGroupById(groupId, this.errorHandler);
        let groupName = group.groupName;
        let topic = group.discussionTopic;
        let rating = "";
        let content = document.getElementById('review-text').value;
        console.log(1);

        if(document.getElementById('star-a').checked) {
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

        console.log(rating);

        if(rating == "") {
            let errorMessage = document.getElementById('review-error');
            let errorHtml = `<p>No rating. Try again</p>`;

            errorMessage.innerHTML = errorHtml;
        } else {
            let review = await this.reviewClient.submitReview(groupId, groupName, topic, rating, content, this.errorHandler);
            console.log(review);
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