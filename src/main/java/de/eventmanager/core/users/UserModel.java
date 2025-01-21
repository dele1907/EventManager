package de.eventmanager.core.users;


import de.eventmanager.core.events.EventModel;
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

    //#region CRUD-Operations

    public abstract boolean createNewUser(
            String firstName,
            String lastName,
            String dateOfBirth,
            String eMailAddress,
            String password,
            String phoneNumber,
            boolean isAdmin
    );

    public abstract void editUser(String userID, String firstName,
                                  String lastName, String dateOfBirth,
                                  String eMailAddress, String password,
                                  String phoneNumber);

    public abstract boolean deleteUser(String userID);

    public abstract Optional<User> getUserByID(String userID);

    public abstract Optional<User> getUserByEmail(String eMailAddress);

    public abstract Optional<EventModel> createPrivateEvent(String eventName, String eventStart, String eventEnd, String category, String postalCode,
                                                            String address, String eventLocation, String description);
    public abstract Optional<EventModel> createPublicEvent(String eventName, String eventStart, String eventEnd, String category,String postalCode,
                                                           String address, String eventLocation, String description, int maxParticipants);

    public abstract Optional<EventModel> editEvent(String eventID);

    public abstract boolean deleteEvent(String eventID);

    public String[] showUserIDsByEventID(String eventID) {
        return null;
    }

    public String[] showEventIDsByUserID(String userID) {
        return null;
    }

    public Optional<EventModel> showEventByID(String userID){
        return null;
    };

    //#endregion Crud-Operations

    //#region Event-Operations

    public void bookEvent(String eventID) {}

    public void addUserToEvent(String userID, String eventID) {}

    public void removeUserFromEvent(String userID, String eventID) {}

    //#endregion Event-Operations

    //#region Permission-Operations
    public void addAdminStatusToUser(User user){
        user.setRoleAdmin(true);
    }

    public void removeAdminStatusFromUser(User user) {
        user.setRoleAdmin(false);
    }
    //#endregion Permission-Operations

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
