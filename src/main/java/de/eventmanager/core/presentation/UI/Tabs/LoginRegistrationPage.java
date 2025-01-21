package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationData;
import de.eventmanager.core.presentation.PresentationHelpers.ValidationHelper;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

import java.util.Optional;

public class LoginRegistrationPage implements Tab {
    private View textView;
    private UserController userController;
    private Optional<User> loggedInUser;

    public LoginRegistrationPage(View textView, UserController userController) {
        this.textView = textView;
        this.userController = userController;
    }

    @Override
    public void start() {
        boolean programIsRunning = true;

        while (programIsRunning) {
            textView.displayMessage("\n===== EventManager =====\n1. Register\n2. Login\n3. Exit");
            textView.displayMessage("\nChoose an option: ");
            String choice = textView.getUserInput();

            switch (choice) {
                case "1":
                    showRegisterUserDialog();
                    break;
                case "2":
                    loggedInUser = showLoginUserDialog();
                    if (loggedInUser.isPresent()) {
                        programIsRunning = false;
                    }
                    break;
                case "3":
                    textView.displayMessage("\nExit Program...");
                    programIsRunning = false;
                    System.exit(0);
                    break;
                default:
                    textView.displayErrorMessage("\nInvalid choice");
                    break;
            }
        }
    }

    public Optional<User> getLoggedInUser() {
        return loggedInUser;
    }

    public void resetLoggedInUser() {
        loggedInUser = Optional.empty();
    }

    public void showRegisterUserDialog() {
        textView.displayMessage("\n===== Registration =====\nEnter first name: ");
        String firstName = textView.getUserInput();
        textView.displayMessage("Enter last name: ");
        String lastName = textView.getUserInput();
        textView.displayMessage("Enter date of birth: ");
        String dateOfBirth = textView.getUserInput();
        textView.displayMessage("Enter email: ");
        String email = textView.getUserInput();
        String phoneNumber = ValidationHelper.checkPhoneNumber(textView);
        textView.displayMessage("Enter password: ");
        String password = textView.getUserInput();
        textView.displayMessage("Confirm password: ");
        String confirmPassword = textView.getUserInput();

        UserRegistrationData userData = new UserRegistrationData(firstName, lastName, dateOfBirth,
                email, phoneNumber, password, confirmPassword
        );

        validateRegisterUser(userData);
    }

    private void validateRegisterUser(UserRegistrationData userRegistrationData) {

        boolean registrationSuccess = userController.createNewUser(userRegistrationData);

        if (!registrationSuccess) {
            textView.displayErrorMessage("\nUser registration failed\n");

            return;
        }

        textView.displaySuccessMessage("\nUser registered successfully\n");
    }

    public Optional<User> showLoginUserDialog() {
        textView.displayMessage("\n===== Login ======\nEnter eMail: ");
        String eMail = textView.getUserInput();
        textView.displayMessage("Enter password: ");
        String password = textView.getUserInput();

        if (eMail.isEmpty() || password.isEmpty()) {
            textView.displayErrorMessage("\nYou must enter eMail or password");

            return Optional.empty();
        }

        Optional<User> loginUser = userController.loginUser(eMail, password);

        return validateLoginUser(loginUser);
    }

    private Optional<User> validateLoginUser(Optional<User> loginUser) {
        if (loginUser.isEmpty()) {
            textView.displayErrorMessage("\nWrong eMail or password! Please try again.\n");

            return Optional.empty();
        }

        textView.displayMessage("\nLogin successful\n");

        return loginUser;
    }
}