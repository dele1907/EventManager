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
    public boolean createNewUser(UserRegistrationData userRegistrationData) {

        boolean successfullyRegistered = userService.registerUser(
                userRegistrationData.getFirstName(),
                userRegistrationData.getLastName(),
                userRegistrationData.getDateOfBirth(),
                userRegistrationData.getEmail(),
                userRegistrationData.getPhoneNumber(),
                userRegistrationData.getPassword(),
                userRegistrationData.getConfirmPassword()
        );

        if (!successfullyRegistered) {
            return false;
        }

        return true;
    }

    public Optional<User> loginUser(String eMail, String password) {
        return userService.loginUser(eMail, password);
    }

    //#endregion Login & Authentication

    //#region CRUD-Operations

    public boolean editUser(String userID) {
        return userService.editUser(userID);
    }

    public boolean deleteUser(User user) {
       return userService.deleteUser(user);
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
    //#endregion eventOperations

}
