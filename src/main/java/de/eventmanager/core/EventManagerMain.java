package de.eventmanager.core;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.TextView;
import de.eventmanager.core.users.User;

public class EventManagerMain {
    public static void main(String[] args) {
        TextView textView = new TextView();
        UserService userService;
        UserController userController;
        User loggedInUser;

        String choice = textView.getUserInput();
        if (choice.equals("1")) {

        }

    }
}
