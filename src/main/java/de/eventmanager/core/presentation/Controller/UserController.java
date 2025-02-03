package de.eventmanager.core.presentation.Controller;

import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationData;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.presentation.Service.UserService;

import java.util.List;

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

    public boolean deleteUser(String userEmailAddress, String loggedInUserID) {
       return userService.deleteUser(userEmailAddress, loggedInUserID);
    }

    public boolean getUserIsPresentInDatabaseByEmail(String eMailAddress) {
        return userService.getUserIsPresentInDatabaseByEmail(eMailAddress);
    }

    public String getUserInformationByEmail(String email) {
        return getUserIsPresentInDatabaseByEmail(email) ? userService.getUserInformationByEmail(email) : "";
    }

    //#endregion CRUD-Operations

    //#region eventOperations
    public List<String> getPublicEventsByName(String name) {
        return eventService.getPublicEventsByName(name);
    }

    public List<String> getPublicEventsByLocation(String location) {
        return eventService.getPublicEventsByLocation(location);
    }

    public List<String> getPublicEventsByCity(String city) {
        return eventService.getPublicEventsByCity(city);
    }

    public boolean createNewEvent(String eventName, String eventStart, String eventEnd,
                                  String category, String postalCode, String city, String address,
                                  int maxCapacity, String eventLocation, String description, int minimumAge, boolean isPublicEvent,
                                  String loggedUserID) {

        return eventService.createNewEvent(eventName, eventStart, eventEnd, category, postalCode, city, address,
                maxCapacity, eventLocation, description, minimumAge, isPublicEvent, loggedUserID);
    }

    public List<String> getCreatedEventsForLoggedInUser(String loggedInUserID) {
        return eventService.getCreatedEventsByUserID(loggedInUserID);
    }
    //#endregion eventOperations

}
