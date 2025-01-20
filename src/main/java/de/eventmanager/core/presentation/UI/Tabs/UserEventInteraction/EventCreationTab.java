package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

public class EventCreationTab implements Tab {
    private View textView;
    private User loggedInUser;
    private UserController userController;

    public EventCreationTab(View textView, User loggedInUser, UserController userController) {
        this.textView = textView;
        this.loggedInUser = loggedInUser;
        this.userController = userController;
    }

    @Override
    public void start() {
        System.out.println("===== EventCreationTab ======");
    }
}
