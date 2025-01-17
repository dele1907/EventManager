package de.eventmanager.core;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.TextView;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

import java.util.Optional;

public class EventManagerMain {

    private static View textView = new TextView();
    private static UserService userServiceImpl = new UserServiceImpl();
    private static UserController userController = new UserController(textView, userServiceImpl);
    private static Optional<User> loggedInUser;

    public static void main(String[] args) {

        try {
            startUI();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void startUI() {
        boolean programIsRunning = true;
        while (programIsRunning) {

            textView.displayMessage("\n===== EventManager =====\n1. Register\n2. Login\n3. Exit");
            textView.displayMessage("\nChoose an option: ");
            String choice = textView.getUserInput();

            switch (choice) {
                case "1":
                    userController.registerUser();
                    startUI();
                    programIsRunning = false;
                    break;
                case "2":
                    loggedInUser =  userController.loginUser();
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
                    System.out.println("\nInvalid choice");
                    break;
            }
        }
    }
}
