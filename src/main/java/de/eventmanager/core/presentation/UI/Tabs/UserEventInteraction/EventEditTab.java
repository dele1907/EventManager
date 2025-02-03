package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;

public class EventEditTab implements Tab {
    private View view;
    private String loggedInUserID;
    private UserController userController;

    public EventEditTab(View view, String loggedInUserID, UserController userController) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.userController = userController;
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Edit Event");

        List<String > createdEvents = userController.getCreatedEventsForLoggedInUser(loggedInUserID);

        if (createdEvents.isEmpty()) {
            view.displayMessage("You have not created any events yet.");

            return;
        }

        createdEvents.forEach(view::displayMessage);
    }
}
