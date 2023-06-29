package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddUserLoginResponse {

    @NotEmpty
    @JsonProperty("userEmail")
    private String userEmail;
    @NotEmpty
    @JsonProperty("registrationStatus")
    private RegistrationStatus registrationStatus;

    @JsonProperty("errorMessage")
    private String errorMessage;


    /**
     * error: Cannot construct instance of `com.kenzie.appserver.controller.model.AddUserLoginResponse`
     * (no Creators, like default constructor, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
     *  at [Source: (String)"
     * Solution: https://stackoverflow.com/questions/53191468/no-creators-like-default-construct-exist-cannot-deserialize-from-object-valu
     * @JsonCreator. This annotation tells the Jackson library which constructor to use for deserialization
     */
    @JsonCreator
    public AddUserLoginResponse(@JsonProperty("userEmail")String userEmail,  @JsonProperty("registrationStatus") RegistrationStatus registrationStatus, @JsonProperty("errorMessage") String errorMessage) {
        this.userEmail = userEmail;
        this.registrationStatus = registrationStatus;
        this.errorMessage = errorMessage;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
