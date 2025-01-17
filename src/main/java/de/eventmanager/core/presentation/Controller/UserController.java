package de.eventmanager.core.presentation.Controller;

import de.eventmanager.core.presentation.Service.UserService;
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

    public void registerUser() {
        view.displayMessage("===== Registration =====\n\nEnter first name: ");
        String firstName = view.getUserInput();
        view.displayMessage("\nEnter last name: ");
        String lastName = view.getUserInput();
        view.displayMessage("Enter date of birth:");
        String dateOfBirth = view.getUserInput();
        view.displayMessage("\nEnter email: ");
        String email = view.getUserInput();
        view.displayMessage("\nEnter phone number: ");
        int phoneNumber = Integer.parseInt(view.getUserInput());
        view.displayMessage("\nEnter password: ");
        String password = view.getUserInput();
        view.displayMessage("\nEnter confirm password: ");
        String confirmPassword = view.getUserInput();

        boolean successfullyRegistered = userService.registerUser(firstName, lastName, dateOfBirth, email, phoneNumber, password, confirmPassword);
        if (successfullyRegistered) {
            view.displayMessage("\nUser registered successfully");
        }else {
            view.displayMessage("\nUser registration failed");
        }


    }

    public Optional<User> loginUser() {
        view.displayMessage("===== Login ======\n\nEnter email: ");
        String email = view.getUserInput();
        view.displayMessage("\nEnter password: ");
        String password = view.getUserInput();

        if (email.isEmpty() || password.isEmpty()) {
            view.displayMessage("\nYou must enter email or password");
            return Optional.empty();
        }

        Optional<User> userToLogin = userService.loginUser(email, password);

        if (userToLogin.isPresent()) {
            view.displayMessage("\nLogin successful");
            return userToLogin;
        }else {
            view.displayMessage("\nLogin failed");
            return Optional.empty();
        }
    }

}
