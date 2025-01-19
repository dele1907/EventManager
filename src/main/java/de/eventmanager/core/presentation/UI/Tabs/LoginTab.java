package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

import java.util.Optional;

public class LoginTab implements Tab {
    private View textView;
    private UserController userController;
    private Optional<User> loggedInUser;

    public LoginTab(View textView, UserController userController) {
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
                    userController.registerUser();
                    break;
                case "2":
                    loggedInUser = userController.loginUser();
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
}