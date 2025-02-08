package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabaseInitializer;
import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabasePathManager;
import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction.EventOperationsTab;
import de.eventmanager.core.presentation.UI.View;
import helper.ConfigurationDataSupplierHelper;

import java.util.List;
import java.util.Optional;

public class MainMenuTab implements Tab {
    private View view;
    private String loggedInUserID;
    private LoginRegistrationPage loginRegistrationPage;
    private UserController userController;

    private enum MenuType {
        ADMIN,
        NON_ADMIN
    }
    private enum MainMenuChoice {
        SETTINGS,
        EVENT_OPERATIONS,
        LOGOUT,
        LOGOUT_AND_EXIT_PROGRAM,
        ADMIN_OPERATIONS;

        public static Optional<MainMenuChoice> fromUserInput(String choice) {
            return switch (choice) {
                case "1" -> Optional.of(SETTINGS);
                case "2" -> Optional.of(EVENT_OPERATIONS);
                case "3" -> Optional.of(LOGOUT);
                case "4" -> Optional.of(LOGOUT_AND_EXIT_PROGRAM);
                case "5" -> Optional.of(ADMIN_OPERATIONS);
                default -> Optional.empty();
            };
        }
    }

    public MainMenuTab(View view, String loggedInUserID, LoginRegistrationPage loginRegistrationPage,
                       UserController userController) {

        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.loginRegistrationPage = loginRegistrationPage;
        this.userController = userController;
    }

    @Override
    public void start() {
        if (userController.getUserIsAdminUser(loggedInUserID)) {
            showMainMenu(MenuType.ADMIN);
        } else {
            showMainMenu(MenuType.NON_ADMIN);
        }
    }

    private void showMainMenu(MenuType menuType) {
        boolean userIsLoggedIn = true;

        while (userIsLoggedIn) {
            displayMainMenu(menuType);
            String choice = view.getUserInput();
            userIsLoggedIn = handleMenuChoice(menuType, choice);
        }
    }

    private void displayMainMenu(MenuType menuType) {
        List<String> menu = menuType.equals(MenuType.ADMIN) ?
                List.of("Settings", "Event Operations", "Logout", "Logout and Exit Program", "Admin Operations") :
                List.of("Settings", "Event Operations", "Logout", "Logout and Exit Program");

        DefaultDialogHelper.getTabOrPageHeading(view, "Main Menu");
        view.displaySuccessMessage("Welcome " + userController.getLoggedInUserName(loggedInUserID) + "!\n");
        DefaultDialogHelper.generateMenu(view, menu);
    }

    private boolean handleMenuChoice(MenuType menuType, String choice) {
        var mainMenuChoice = MainMenuChoice.fromUserInput(choice);

        if (mainMenuChoice.isEmpty()) {
            DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");

            return true;
        }

        switch (mainMenuChoice.get()) {
            case SETTINGS -> view.displayErrorMessage("\nSettings page (not implemented yet)\n");
            case EVENT_OPERATIONS -> doShowEventOperationTab();
            case LOGOUT -> {
                doLogout();
                return false;
            }
            case LOGOUT_AND_EXIT_PROGRAM -> doLogoutAndExitProgram();
            case ADMIN_OPERATIONS -> doShowAdminOperations(menuType);
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
        view.displayMessage("\nLogging out...\n");
        loginRegistrationPage.resetLoggedInUser();
    }

    private void doLogoutAndExitProgram() {
        view.displayMessage("\nLogging out...\n");
        loginRegistrationPage.resetLoggedInUser();

        addDelay(2);

        view.displayMessage("\nExiting Program...\n");
        System.exit(0);
    }

    private void doShowAdminOperations(MenuType menuType) {

        if (!(menuType == MenuType.ADMIN)) {
            DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");

            return;
        }

        new AdminOperationsTab(view, loggedInUserID, userController).start();
    }

    private void doShowEventOperationTab() {
        new EventOperationsTab(view, loggedInUserID, userController).start();
    }
}