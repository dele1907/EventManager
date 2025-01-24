package de.eventmanager.core.presentation;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabaseInitializer;
import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabasePathManager;
import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.presentation.Service.Implementation.EventServiceImpl;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.AdminUserStartupRegistrationPage;
import de.eventmanager.core.presentation.UI.Tabs.LoginRegistrationPage;
import de.eventmanager.core.presentation.UI.Tabs.MainMenuTab;
import de.eventmanager.core.presentation.UI.TextView;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class EventManagerTextBasedUIInstance implements EventManagerInstance {
    private static View textView = new TextView();
    private static UserService userService = new UserServiceImpl();
    private static EventService eventService = new EventServiceImpl();
    private static UserController userController = new UserController(userService, eventService);
    private static Optional<User> loggedInUser;
    private static LoginRegistrationPage loginRegistrationPage;
    private static AdminUserStartupRegistrationPage adminUserStartupRegistrationPage;

    public void startEventManagerInstance() {
        boolean isProductiveSystem = false;
        //@TODO: remove flush before release
        boolean flushDatabasePathAfterTest = true && isProductiveSystem;

        initPages(flushDatabasePathAfterTest);

        initDatabase(isProductiveSystem);

        boolean programIsRunning = true;
        boolean adminInDatabase = userService.getAdminUserIsPresentInDatabase();

        if (!adminInDatabase) {
            adminUserStartupRegistrationPage.start();
        }

        while (programIsRunning) {
            try {
                loginRegistrationPage.start();
                loggedInUser = loginRegistrationPage.getLoggedInUser();

                if (loggedInUser.isPresent()) {
                    MainMenuTab mainMenuTab = new MainMenuTab(textView, loggedInUser.get(), loginRegistrationPage, userController, flushDatabasePathAfterTest);
                    mainMenuTab.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initDatabase(boolean isProductiveSystem) {
        String databasePath = DatabasePathManager.loadDatabasePath(isProductiveSystem);
        if (databasePath.isEmpty() || !DatabasePathManager.isValidPath(databasePath, isProductiveSystem)) {
            System.out.println("Please provide a valid database path:");
            databasePath = new Scanner(System.in).nextLine();
            DatabasePathManager.saveDatabasePath(databasePath, isProductiveSystem);
        }

        DatabaseConnector.setDatabasePath(databasePath);
        try (Connection conn = DatabaseConnector.connect()) {
            if (conn != null) {
                DatabaseInitializer.initialize(conn);
                System.out.println("Database initialized at: " + databasePath);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initPages(boolean flushDatabasePathAfterTest) {
        loginRegistrationPage = new LoginRegistrationPage(textView, userController, flushDatabasePathAfterTest);
        adminUserStartupRegistrationPage = new AdminUserStartupRegistrationPage(textView, userController);
    }
}