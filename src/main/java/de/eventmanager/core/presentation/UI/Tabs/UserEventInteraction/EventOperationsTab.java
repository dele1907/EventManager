package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

public class EventOperationsTab implements Tab {
    private View textView;
    private String loggedInUserID;
    private UserController userController;

    public EventOperationsTab(View textView, String loggedInUserID, UserController userController) {
        this.textView = textView;
        this.loggedInUserID = loggedInUserID;
        this.userController = userController;
    }

    @Override
    public void start() {
        boolean eventOperationIsActive = true;

        while (eventOperationIsActive) {
            textView.displayTabOrPageHeading("\n===== EventOperationsTab =====");

            textView.displayMessage(
                    "1. Create Event" +
                    "\n2. Show Events" +
                    "\n3. Back to main menu"
            );
            textView.displayUserInputMessage("\n\nChoose a Option\n> ");
            
            String choice = textView.getUserInput();

            switch (choice) {
                case "1":
                    handleCreateEvent();
                    break;
                case "2":
                    handleShowEvents();
                    break;
                case "3":
                    eventOperationIsActive = false;
                    break;
                default:
                    textView.displayErrorMessage("\nInvalid choice");
                    break;
            }
        }
    }

    private void handleCreateEvent() {
        EventCreationTab eventCreationTab = new EventCreationTab(textView, loggedInUserID, userController);
        eventCreationTab.start();
    }

    private void handleShowEvents() {
        ShowEventsTab showEventsTab = new ShowEventsTab(textView, userController, loggedInUserID);
        showEventsTab.start();
    }
}
