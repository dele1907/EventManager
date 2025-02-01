package de.eventmanager.core.presentation.Controller;

import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationData;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.users.User;

import java.util.List;
import java.util.Optional;

public class UserController {
    private UserService userService;
    private EventService eventService;

    public UserController(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    //#region Login & Authentication
    public boolean createNewUser(UserRegistrationData userRegistrationData, String loggedInUserUserID) {

        boolean successfullyRegistered = userService.registerUser(
                userRegistrationData.getFirstName(),
                userRegistrationData.getLastName(),
                userRegistrationData.getDateOfBirth(),
                userRegistrationData.getEmail(),
                userRegistrationData.getPhoneNumber(),
                userRegistrationData.getPassword(),
                userRegistrationData.getConfirmPassword(),
                loggedInUserUserID
        );

        if (!successfullyRegistered) {
            return false;
        }

        return true;
    }

    public boolean createNewAdminUser(UserRegistrationData userRegistrationData, String loggedInUserUserID) {

        boolean successfullyRegistered = userService.registerAdminUser(
                userRegistrationData.getFirstName(),
                userRegistrationData.getLastName(),
                userRegistrationData.getDateOfBirth(),
                userRegistrationData.getEmail(),
                userRegistrationData.getPhoneNumber(),
                userRegistrationData.getPassword(),
                userRegistrationData.getConfirmPassword(),
                loggedInUserUserID
        );

        if (!successfullyRegistered) {
            return false;
        }

        return true;
    }

    public String loginUser(String eMail, String password) {
        return userService.loginUser(eMail, password);
    }

    public boolean getUserIsAdminUser(String userID) {
        return userService.getUserIsAdminUser(userID);
    }

    public String getLoggedInUserName(String userID) {
        return userService.getLoggedInUserName(userID);
    }

    //#endregion Login & Authentication

    //#region CRUD-Operations

    public void editUser(String userEmailAddress, String loggedInUserID, String newFirstName, String newLastName,
                         String newEmailAddress, String newPhoneNumber) {

        userService.editUser(userEmailAddress, loggedInUserID, newFirstName, newLastName, newEmailAddress, newPhoneNumber);
    }

    public boolean deleteUser(String userEmailAddress, String loggeddInUserID) {
       return userService.deleteUser(userEmailAddress, loggeddInUserID);
    }

    public boolean getUserIsPresentInDatabaseByEmail(String eMailAddress) {
        return userService.getUserIsPresentInDatabaseByEmail(eMailAddress);
    }

    public String getUserInformationByEmail(String email) {
        return getUserIsPresentInDatabaseByEmail(email) ? userService.getUserInformationByEmail(email) : "";
    }

    //#endregion CRUD-Operations
    public Optional<User> getUserByEmail(String email) {
        return userService.readUserByEmail(email);
    }

    //#region eventOperations
    public List<PublicEvent> getPublicEventsByName(String name) {
        return eventService.getPublicEventsByName(name);
    }

    public List<PublicEvent> getPublicEventsByLocation(String location) {
        return eventService.getPublicEventsByLocation(location);
    }

    public List<PublicEvent> getPublicEventsByCity(String city) {
        return eventService.getPublicEventsByCity(city);
    }

    public boolean createNewEvent(String eventName, String eventStart, String eventEnd,
                                  String category, String postalCode, String city, String address,
                                  int maxCapacity, String eventLocation, String description, boolean isPublicEvent) {

        return eventService.createNewEvent(
                eventName, eventStart, eventEnd,
                category, postalCode, city, address,
                maxCapacity, eventLocation, description, isPublicEvent
        );
    }
    //#endregion eventOperations

}
