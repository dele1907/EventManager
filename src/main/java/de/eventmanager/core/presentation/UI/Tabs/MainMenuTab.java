package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.Service.Implementation.EventServiceImpl;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.Inbox.UsersInboxTab;
import de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction.EventOperationsTab;
import de.eventmanager.core.presentation.UI.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainMenuTab implements Tab {
    private View view;
    private String loggedInUserID;
    private LoginRegistrationPage loginRegistrationPage;
    private UserService userService;

    private enum MenuType {
        ADMIN,
        NON_ADMIN
    }
    private enum MainMenuChoice {
        SETTINGS,
        INBOX,
        EVENT_OPERATIONS,
        LOGOUT,
        LOGOUT_AND_EXIT_PROGRAM,
        ADMIN_OPERATIONS;

        public static Optional<MainMenuChoice> fromUserInput(String choice) {
            return switch (choice) {
                case "1" -> Optional.of(INBOX);
                case "2" -> Optional.of(EVENT_OPERATIONS);
                case "3" -> Optional.of(LOGOUT);
                case "4" -> Optional.of(LOGOUT_AND_EXIT_PROGRAM);
                case "5" -> Optional.of(ADMIN_OPERATIONS);
                default -> Optional.empty();
            };
        }
    }

    public MainMenuTab(View view, String loggedInUserID, LoginRegistrationPage loginRegistrationPage) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.loginRegistrationPage = loginRegistrationPage;
        this.userService = new UserServiceImpl();
    }

    @Override
    public void start() {
        new EventServiceImpl().deleteAllExpiredEvents();

        if (userService.getUserIsAdminUserByID(loggedInUserID)) {
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
        var menu = List.of(getInBoxMainMenuItem(),"Event Operations", "Logout", "Logout and Exit Program");

        if (menuType.equals(MenuType.ADMIN)) {
            menu = new ArrayList<>(menu);
            menu.add("Admin Operations");
        }

        DefaultDialogHelper.getTabOrPageHeading(view, "Main Menu");
        view.displaySuccessMessage("Welcome " + userService.getLoggedInUserName(loggedInUserID) + "!\n");
        DefaultDialogHelper.generateMenu(view, menu);
    }

    private String getInBoxMainMenuItem() {
        return "Inbox(" + userService.getNumberOfUnreadNotifications(loggedInUserID)+ ")";
    }

    private boolean handleMenuChoice(MenuType menuType, String choice) {
        var mainMenuChoice = MainMenuChoice.fromUserInput(choice);

        if (mainMenuChoice.isEmpty()) {
            DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");

            return true;
        }

        switch (mainMenuChoice.get()) {
            case INBOX -> doShowInboxTab();
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
        try {
            Thread.sleep(seconds * 1000);
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

        new AdminOperationsTab(view, loggedInUserID).start();
    }

    private void doShowEventOperationTab() {
        new EventOperationsTab(view, loggedInUserID).start();
    }

    private void doShowInboxTab() {
        new UsersInboxTab(view, loggedInUserID).start();
    }
}