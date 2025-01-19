package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

public class MainMenuTab implements Tab {
    private View textView;
    private User loggedInUser;
    private LoginTab loginTab;
    private UserController userController;

    private enum MenuType {
        ADMIN,
        NON_ADMIN
    }

    public MainMenuTab(View textView, User loggedInUser, LoginTab loginTab, UserController userController) {
        this.textView = textView;
        this.loggedInUser = loggedInUser;
        this.loginTab = loginTab;
        this.userController = userController;
    }

    @Override
    public void start() {
        if (loggedInUser.isAdmin()) {
            showMainMenu(MenuType.ADMIN);
        } else {
            showMainMenu(MenuType.NON_ADMIN);
        }
    }

    private void showMainMenu(MenuType menuType) {
        boolean userIsLoggedIn = true;

        while (userIsLoggedIn) {
            displayMainMenu(menuType);
            String choice = textView.getUserInput();
            userIsLoggedIn = handleMenuChoice(menuType, choice);
        }
    }

    private void displayMainMenu(MenuType menuType) {
        textView.displayMessage("\n===== Main Menu =====");
        textView.displayMessage("\n1. Settings\n2. Logout\n3. Logout and Exit Program");

        if (menuType == MenuType.ADMIN) {
            textView.displayMessage("\n4. AdminOperations");
        }
        textView.displayMessage("\nChoose an option: ");
    }

    private boolean handleMenuChoice(MenuType menuType, String choice) {
        switch (choice) {
            case "1":
                textView.displayMessage("\nSettings page (not implemented yet)\n");
                break;
            case "2":
                textView.displayMessage("\nLogging out...");
                loginTab.resetLoggedInUser();
                return false;
            case "3":
                textView.displayMessage("\nLogging out...");
                loginTab.resetLoggedInUser();

                addDelay(2);

                textView.displayMessage("\nExiting Program...");
                System.exit(0);
            case "4":
                if (menuType == MenuType.ADMIN) {
                    AdminOperationsTab adminOperationsTab = new AdminOperationsTab(textView, loggedInUser, userController);
                    adminOperationsTab.start();
                } else {
                    textView.displayMessage("\nInvalid choice");
                }
                break;
            default:
                textView.displayMessage("\nInvalid choice");
                break;
        }

        return true;
    }

    private void addDelay(int seconds) {
        int delay = seconds * 1000;

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}