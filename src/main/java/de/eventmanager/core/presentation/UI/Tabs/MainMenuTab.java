package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction.EventOperationsTab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.User;

public class MainMenuTab implements Tab {
    private View textView;
    private User loggedInUser;
    private LoginRegistrationPage loginRegistrationPage;
    private UserController userController;

    private enum MenuType {
        ADMIN,
        NON_ADMIN
    }

    public MainMenuTab(View textView, User loggedInUser, LoginRegistrationPage loginRegistrationPage, UserController userController) {
        this.textView = textView;
        this.loggedInUser = loggedInUser;
        this.loginRegistrationPage = loginRegistrationPage;
        this.userController = userController;
    }

    @Override
    public void start() {
        if (loggedInUser.getRole().equals(Role.ADMIN)) {
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
        textView.displayMessage("\n1. Settings\n2. Event Operations\n3. Logout\n4. Logout and Exit Program");

        if (menuType == MenuType.ADMIN) {
            textView.displayMessage("\n5. Admin Operations");
        }
        textView.displayMessage("\nChoose an option: ");
    }

    private boolean handleMenuChoice(MenuType menuType, String choice) {
        switch (choice) {
            case "1":
                textView.displayErrorMessage("\nSettings page (not implemented yet)\n");
                break;
            case "2":
                doShowEventOperationTab();
                break;
            case "3":
                doLogout();
                return false;
            case "4":
                doLogoutAndExitProgram();
            case "5":
                doShowAdminOperations(menuType);
                break;
            default:
                textView.displayErrorMessage("\nInvalid choice!\n");
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

    private void doLogout() {
        textView.displayMessage("\nLogging out...\n");
        loginRegistrationPage.resetLoggedInUser();
    }

    private void doLogoutAndExitProgram() {
        textView.displayMessage("\nLogging out...\n");
        loginRegistrationPage.resetLoggedInUser();

        addDelay(2);

        textView.displayMessage("\nExiting Program...\n");
        System.exit(0);
    }

    private void doShowAdminOperations(MenuType menuType) {

        if (menuType == MenuType.ADMIN) {
            AdminOperationsTab adminOperationsTab = new AdminOperationsTab(textView, loggedInUser, userController);
            adminOperationsTab.start();
        } else {
            textView.displayErrorMessage("\nInvalid choice!\n");
        }
    }

    private void doShowEventOperationTab() {
        EventOperationsTab eventOperationsTab = new EventOperationsTab(textView, loggedInUser, userController);
        eventOperationsTab.start();
    }
}