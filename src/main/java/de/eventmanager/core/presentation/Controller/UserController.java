package de.eventmanager.core.presentation.Controller;

import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.AdminRightsTab;
import de.eventmanager.core.presentation.UI.View;
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
    public void registerUser() {
        int phoneNumber = 0;

        view.displayMessage("\n===== Registration =====\nEnter first name: ");
        String firstName = view.getUserInput();
        view.displayMessage("Enter last name: ");
        String lastName = view.getUserInput();
        view.displayMessage("Enter date of birth: ");
        String dateOfBirth = view.getUserInput();
        view.displayMessage("Enter email: ");
        String email = view.getUserInput();
        phoneNumber = isValidPhoneNumber();
        view.displayMessage("Enter password: ");
        String password = view.getUserInput();
        view.displayMessage("Enter confirm password: ");
        String confirmPassword = view.getUserInput();

        boolean successfullyRegistered = userService.registerUser(firstName, lastName, dateOfBirth, email, phoneNumber, password, confirmPassword);

        if (successfullyRegistered) {
            view.displayMessage("\nUser registered successfully\n");
        }else {
            view.displayMessage("\nUser registration failed\n");
        }

    }

    public Optional<User> loginUser() {
        view.displayMessage("\n===== Login ======\nEnter email: ");
        String email = view.getUserInput();
        view.displayMessage("Enter password: ");
        String password = view.getUserInput();

        if (email.isEmpty() || password.isEmpty()) {
            view.displayMessage("\nYou must enter email or password");

            return Optional.empty();
        }

        Optional<User> userToLogin = userService.loginUser(email, password);

        if (userToLogin.isPresent()) {
            view.displayMessage("\nLogin successful\n");

            return userToLogin;
        }else {
            view.displayMessage("\nWrong email or password! Please try again.\n");

            return Optional.empty();
        }
    }

    private int isValidPhoneNumber() {
        boolean validPhoneNumber = true;
        int phoneNumber = 0;

        while (validPhoneNumber) {

            try {
                view.displayMessage("Enter phone number: ");
                phoneNumber = Integer.parseInt(view.getUserInput());
                validPhoneNumber = false;

            }catch (NumberFormatException e) {
                view.displayMessage("Invalid input! Please enter a valid phone number\n");
            }
        }

        return phoneNumber;
    }

    //#endregion Login & Authentication

}
