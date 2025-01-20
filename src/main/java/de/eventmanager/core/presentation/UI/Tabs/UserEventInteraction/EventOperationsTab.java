package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

public class EventOperationsTab implements Tab {
    private View textView;
    private User loggedInUser;
    private UserController userController;

    public EventOperationsTab(View textView, User loggedInUser, UserController userController) {
        this.textView = textView;
        this.loggedInUser = loggedInUser;
        this.userController = userController;
    }

    @Override
    public void start() {
        boolean eventOperationIsActive = true;

        while (eventOperationIsActive) {
            textView.displayMessage("===== EventOperationsTab =====");

            textView.displayMessage(
                    "\n1. Create Event" +
                    "\n2. Show Events" +
                    "\n3. Back to main menu"
            );
            
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
        EventCreationTab eventCreationTab = new EventCreationTab(textView, loggedInUser, userController);
        eventCreationTab.start();
    }

    private void handleShowEvents() {
        ShowEventsTab showEventsTab = new ShowEventsTab(textView, userController, loggedInUser);
        showEventsTab.displaySearchOptions();
    }
}
