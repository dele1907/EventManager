package de.eventmanager.core;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.LoginTab;
import de.eventmanager.core.presentation.UI.Tabs.MainMenuTab;
import de.eventmanager.core.presentation.UI.TextView;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

import java.util.Optional;

public class EventManagerMain {

    private static View textView = new TextView();
    private static UserService userServiceImpl = new UserServiceImpl();
    private static UserController userController = new UserController(textView, userServiceImpl);
    private static Optional<User> loggedInUser;
    private static LoginTab loginTab = new LoginTab(textView, userController);

    public static void main(String[] args) {
        boolean programIsRunning = true;

        while (programIsRunning) {
            try {
                loginTab.start();
                loggedInUser = loginTab.getLoggedInUser();

                if (loggedInUser.isPresent()) {
                    MainMenuTab mainMenuTab = new MainMenuTab(textView, loggedInUser.get(), loginTab);
                    mainMenuTab.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}