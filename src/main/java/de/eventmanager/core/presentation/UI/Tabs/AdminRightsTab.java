package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.users.User;

public class AdminRightsTab implements Tab {
    User loggedInUser;

    public AdminRightsTab(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void start() {
        System.out.println("======AdminRightsTab======");
    }

}
