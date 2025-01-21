package de.eventmanager.core.presentation.Controller;

import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.users.User;

import java.util.Optional;

public class UserController {
    private View view;
    private UserService userService;
    private final char[] restrictedCaractersForPhonenumber = {' ', };


    public UserController(View view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

    //#region Login & Authentication
    public void registerUser() {
        String phoneNumber;

        view.displayMessage("\n===== Registration =====\nEnter first name: ");
        String firstName = view.getUserInput();
        view.displayMessage("Enter last name: ");
        String lastName = view.getUserInput();
        view.displayMessage("Enter date of birth: ");
        String dateOfBirth = view.getUserInput();
        view.displayMessage("Enter email: ");
        String email = view.getUserInput();
        phoneNumber = checkPhoneNumber();
        view.displayMessage("Enter password: ");
        String password = view.getUserInput();
        view.displayMessage("Confirm password: ");
        String confirmPassword = view.getUserInput();

        boolean successfullyRegistered = userService.registerUser(firstName, lastName, dateOfBirth, email, phoneNumber, password, confirmPassword);

        if (successfullyRegistered) {
            view.displaySuccessMessage("\nUser registered successfully\n");
        }else {
            view.displayErrorMessage("\nUser registration failed\n");
        }

    }

    public Optional<User> loginUser() {
        view.displayMessage("\n===== Login ======\nEnter email: ");
        String email = view.getUserInput();
        view.displayMessage("Enter password: ");
        String password = view.getUserInput();

        if (email.isEmpty() || password.isEmpty()) {
            view.displayErrorMessage("\nYou must enter email or password");

            return Optional.empty();
        }

        Optional<User> userToLogin = userService.loginUser(email, password);

        if (userToLogin.isPresent()) {
            view.displayMessage("\nLogin successful\n");

            return userToLogin;
        }else {
            view.displayErrorMessage("\nWrong email or password! Please try again.\n");

            return Optional.empty();
        }
    }

    private String checkPhoneNumber() {
        boolean inValidPhoneNumber = true;
        String phoneNumber = "";
        String INVALID_PHONE_NUMBER_INPUT_MESSAGE = "Invalid input! Please enter a valid phone number\n";
        while (inValidPhoneNumber) {

            try {
                view.displayMessage("Enter phone number: ");
                phoneNumber = view.getUserInput();
                if (!phoneNumber.isEmpty()) {

                    if (!phoneNumber.matches(".*[-äöüÄÖÜß?!.,<>a-zA-Z].*")) {

                        inValidPhoneNumber = false;
                    }else {

                        System.out.println(INVALID_PHONE_NUMBER_INPUT_MESSAGE);
                    }
                }

            }catch (Exception e) {
                view.displayErrorMessage(INVALID_PHONE_NUMBER_INPUT_MESSAGE);
            }
        }

        return phoneNumber;
    }

    //#endregion Login & Authentication

    //#region CRUD-Operations

    public void editUser(User user) {
        UserManager.updateUser(user);
    }

    //#endregion CRUD-Operations

}
