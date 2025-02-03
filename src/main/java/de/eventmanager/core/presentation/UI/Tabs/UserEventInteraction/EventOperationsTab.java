package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

import java.util.List;

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
            DefaultDialogHelper.getTabOrPageHeading(textView, "Event Operations");
            DefaultDialogHelper.generateMenu(
                textView,
                List.of(
                    "Create new Event",
                    "Show Events",
                    "Book Event",
                    "Cancel Event",
                    "Edit Event's information",
                    "Back to main menu"
                )
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
                    handleBookEvent();
                    break;
                case "4":
                    handleCancelEvent();
                    break;
                case "5":
                    handleEditEvent();
                    break;
                case "6":
                    eventOperationIsActive = false;
                    break;
                default:
                    textView.displayErrorMessage("\nInvalid choice");
                    break;
            }
        }
    }

    private void handleCreateEvent() {
      new EventCreationTab(textView, loggedInUserID, userController).start();
    }

    private void handleShowEvents() {
        new ShowEventsTab(textView, userController, loggedInUserID).start();
    }

    private void handleBookEvent() {
        new EventBookingTab(textView, loggedInUserID, userController).start();
    }

    private void handleCancelEvent() {
        new EventCancelParticipationTab(textView, loggedInUserID, userController).start();
    }

    private void handleEditEvent() {
        new EventEditTab(textView, loggedInUserID, userController).start();
    }
}
