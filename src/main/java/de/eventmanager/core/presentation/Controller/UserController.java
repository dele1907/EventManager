package de.eventmanager.core.presentation.Controller;

import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationData;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.users.User;

import java.util.Optional;

public class UserController {
    private View view;
    private UserService userService;

    public UserController(View view, UserService userService) {
        this.view = view;
        this.userService = userService;
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

    public void editUser(User user) {
        UserManager.updateUser(user);
    }

    //#endregion CRUD-Operations

}
