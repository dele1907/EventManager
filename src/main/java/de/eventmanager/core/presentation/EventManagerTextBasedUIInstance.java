package de.eventmanager.core.presentation;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.LoginTab;
import de.eventmanager.core.presentation.UI.Tabs.MainMenuTab;
import de.eventmanager.core.presentation.UI.TextView;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;
import helper.LoggerHelper;

import java.util.Optional;

public class EventManagerTextBasedUIInstance implements EventManagerInstance {
    private static View textView = new TextView();
    private static UserService userServiceImpl = new UserServiceImpl();
    private static UserController userController = new UserController(textView, userServiceImpl);
    private static Optional<User> loggedInUser;
    private static LoginTab loginTab = new LoginTab(textView, userController);


    public void startEventManagerInstance() {
        initDatabase();

        boolean programIsRunning = true;

        while (programIsRunning) {
            try {
                loginTab.start();
                loggedInUser = loginTab.getLoggedInUser();

                if (loggedInUser.isPresent()) {
                    MainMenuTab mainMenuTab = new MainMenuTab(textView, loggedInUser.get(), loginTab, userController);
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
