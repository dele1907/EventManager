package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabaseInitializer;
import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabasePathManager;
import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction.EventOperationsTab;
import de.eventmanager.core.presentation.UI.View;
import helper.ConfigurationDataSupplierHelper;

import java.util.List;

public class MainMenuTab implements Tab {
    private View view;
    private String loggedInUserID;
    private LoginRegistrationPage loginRegistrationPage;
    private UserController userController;
    //@TODO: remove flush before release
    private  boolean flushTestDatabase;

    private enum MenuType {
        ADMIN,
        NON_ADMIN
    }

    public MainMenuTab(View view, String loggedInUserID, LoginRegistrationPage loginRegistrationPage,
                       UserController userController, boolean flushTestDatabase) {

        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.loginRegistrationPage = loginRegistrationPage;
        this.userController = userController;
        //@TODO: remove flush before release
        this.flushTestDatabase = flushTestDatabase;
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
        switch (choice) {
            case "1":
                view.displayErrorMessage("\nSettings page (not implemented yet)\n");
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
                DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");
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
        view.displayMessage("\nLogging out...\n");
        loginRegistrationPage.resetLoggedInUser();
    }

    private void doLogoutAndExitProgram() {
        view.displayMessage("\nLogging out...\n");
        loginRegistrationPage.resetLoggedInUser();

        addDelay(2);

        view.displayMessage("\nExiting Program...\n");
        //TODO @Dennis: remove flush before release
        DatabasePathManager.flushDatabasePath(flushTestDatabase);
        //TODO @Dennis: remove following line before release
        DatabaseInitializer.deInit(ConfigurationDataSupplierHelper.IS_PRODUCTION_MODE);
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