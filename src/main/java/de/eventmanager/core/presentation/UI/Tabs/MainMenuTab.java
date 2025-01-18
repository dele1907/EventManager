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
                    if (loggedInUser.isAdmin()) {
                        AdminOperationsTab adminOperationsTab = new AdminOperationsTab(textView, loggedInUser, userController);
                        adminOperationsTab.start();
                    } else {
                        textView.displayMessage("\nInvalid choice");
                    }
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
}