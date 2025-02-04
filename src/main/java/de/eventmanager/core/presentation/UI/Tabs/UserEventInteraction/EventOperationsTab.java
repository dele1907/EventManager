package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;

public class EventOperationsTab implements Tab {
    private View view;
    private String loggedInUserID;
    private UserController userController;

    public EventOperationsTab(View view, String loggedInUserID, UserController userController) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.userController = userController;
    }

    @Override
    public void start() {
        boolean eventOperationIsActive = true;

        while (eventOperationIsActive) {
            DefaultDialogHelper.getTabOrPageHeading(view, "Event Operations");
            DefaultDialogHelper.generateMenu(
                    view,
                List.of(
                    "Create new Event",
                    "Show Events",
                    "Book Event",
                    "Cancel Event",
                    "Edit Event's information",
                    "Back to main menu"
                )
            );
            String choice = view.getUserInput();

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
                    view.displayErrorMessage("\nInvalid choice");
                    break;
            }
        }
    }

    private void handleCreateEvent() {
      new EventCreationTab(view, loggedInUserID, userController).start();
    }

    private void handleShowEvents() {
        new ShowEventsTab(view, userController, loggedInUserID).start();
    }

    private void handleBookEvent() {
        new EventBookingTab(view, loggedInUserID, userController).start();
    }

    private void handleCancelEvent() {
        new EventCancelParticipationTab(view, loggedInUserID, userController).start();
    }

    private void handleEditEvent() {
        new EventEditTab(view, loggedInUserID, userController).start();
    }
}
