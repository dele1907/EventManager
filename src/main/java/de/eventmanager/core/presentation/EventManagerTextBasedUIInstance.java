package de.eventmanager.core.presentation;

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
import helper.LoggerHelper;

import java.util.Optional;

public class EventManagerTextBasedUIInstance implements EventManagerInstance {
    private static View textView = new TextView();
    private static UserService userService = new UserServiceImpl();
    private static EventService eventService = new EventServiceImpl();
    private static UserController userController = new UserController(userService, eventService);
    private static Optional<User> loggedInUser;
    private static LoginRegistrationPage loginRegistrationPage = new LoginRegistrationPage(textView, userController);
    private static AdminUserStartupRegistrationPage adminUserStartupRegistrationPage = new AdminUserStartupRegistrationPage(textView, userController);


    public void startEventManagerInstance() {
        initDatabase();

        boolean programIsRunning = true;
        boolean adminInDatabase = userService.getAdminUserIsPresentInDatabase();

        while (programIsRunning) {
            try {
                if (adminInDatabase) {
                    adminUserStartupRegistrationPage.start();
                }

                loginRegistrationPage.start();
                loggedInUser = loginRegistrationPage.getLoggedInUser();

                if (loggedInUser.isPresent()) {
                    MainMenuTab mainMenuTab = new MainMenuTab(textView, loggedInUser.get(), loginRegistrationPage, userController);
                    mainMenuTab.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initDatabase() {
        /**
         * TODO init database
         * */
        LoggerHelper.logInfoMessage(EventManagerTextBasedUIInstance.class, "Database initialized");
    }
}
