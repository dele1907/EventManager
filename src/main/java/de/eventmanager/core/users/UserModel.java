package de.eventmanager.core.users;


import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.roles.Role;

import java.util.Optional;

public abstract class UserModel {
    String userID;
    String firstName;
    String lastName;
    String dateOfBirth;
    String eMailAddress;
    String password;
    String phoneNumber;
    Role role;


    public String[] showUserIDsByEventID(String eventID) {
        return null;
    }

    public String[] showEventIDsByUserID(String userID) {
        return null;
    }

    public Optional<EventModel> showEventByID(String userID){
        return null;
    };





    public void addUserToEvent(String userID, String eventID) {}

    public void removeUserFromEvent(String userID, String eventID) {}





    //#region Getter
    public Role getRole() {
        return role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUserID() {
        return userID;
    }
    //#endregion Getter

    //#region Setter

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setRoleAdmin(boolean hasAdminRole) {
        this. role = hasAdminRole ? Role.ADMIN : Role.USER;
    }

//#endregion Setter
}
