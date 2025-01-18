package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

public class MainMenuTab implements Tab {
    private View textView;
    private User loggedInUser;
    private LoginTab loginTab;
    private UserController userController;

    public MainMenuTab(View textView, User loggedInUser, LoginTab loginTab, UserController userController) {
        this.textView = textView;
        this.loggedInUser = loggedInUser;
        this.loginTab = loginTab;
        this.userController = userController;
    }

    @Override
    public void start() {
        if (loggedInUser.isAdmin()) {
            showAdminMainMenu();
        } else {
            showNonAdminMainMenu();
        }
    }

    private void showAdminMainMenu() {
        boolean userIsLoggedIn = true;
        while (userIsLoggedIn) {
            textView.displayMessage("\n===== Main Menu =====");
            if (loggedInUser.isAdmin()) {
                textView.displayMessage("\n1. AdminOperations");
            }
            textView.displayMessage("\n2. Settings\n3. Logout");
            textView.displayMessage("\nChoose an option: ");
            String choice = textView.getUserInput();

            switch (choice) {
                case "1":
                    AdminOperationsTab adminOperationsTab = new AdminOperationsTab(textView, loggedInUser, userController);
                    adminOperationsTab.start();
                    break;
                case "2":
                    textView.displayMessage("\nSettings page (not implemented yet)\n");
                    break;
                case "3":
                    textView.displayMessage("\nLogging out...");
                    userIsLoggedIn = false;
                    loginTab.resetLoggedInUser();
                    break;
                default:
                    textView.displayMessage("\nInvalid choice");
                    break;
            }
        }
    }

    private void showNonAdminMainMenu() {
        boolean userIsLoggedIn = true;
        while (userIsLoggedIn) {
            textView.displayMessage("\n===== Main Menu =====");
            textView.displayMessage("\n1. Settings\n2. Logout");
            textView.displayMessage("\nChoose an option: ");
            String choice = textView.getUserInput();

            switch (choice) {
                case "1":
                    textView.displayMessage("\nSettings page (not implemented yet)\n");
                    break;
                case "2":
                    textView.displayMessage("\nLogging out...");
                    userIsLoggedIn = false;
                    loginTab.resetLoggedInUser();
                    break;
                default:
                    textView.displayMessage("\nInvalid choice");
                    break;
            }
        }
    }
}