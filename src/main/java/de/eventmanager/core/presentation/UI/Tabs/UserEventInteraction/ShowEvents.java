package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

public class ShowEvents implements Tab{
    private View textView;
    private UserController userController;
    private User loggedInUser;

    public ShowEvents(View textView, UserController userController, User loggedInUser) {
        this.textView = textView;
        this.userController = userController;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public void start() {
        System.out.println("===== ShowEventsTab =====");
    }
}
