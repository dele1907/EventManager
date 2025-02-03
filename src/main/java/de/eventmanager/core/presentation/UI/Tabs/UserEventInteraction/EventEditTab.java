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
        handleShowCreatedEventsForLoggedInUser();
    }

    private void handleShowCreatedEventsForLoggedInUser() {
        List<String > createdEvents = userController.getCreatedEventsForLoggedInUser(loggedInUserID);

        if (createdEvents.isEmpty()) {
            view.displayWarningMessage("\nYou have not created any events yet.\n");

            return;
        }

        view.displayUnderlinedSubheading("\nYour Created Events, you can edit:\n");
        createdEvents.forEach(view::displayMessage);

        showEventInformationByEventIDDialog();
    }

    private void showEventInformationByEventIDDialog() {
        view.displayUserInputMessage("\nEnter the event ID of the event you want to edit\n> ");
        String eventID = view.getUserInput();

        if (eventID.isEmpty()) {
            view.displayErrorMessage("\nInvalid event ID. Pleas try again.\n");

            return;
        }

        String eventInformation = userController.getEventInformationByEventID(eventID);

        if (eventInformation.isEmpty()) {
            view.displayErrorMessage("\nNo event found with the given event ID. Please try again.\n");

            return;
        }

        showConfirmEventEditingDialog(eventInformation);
    }

    private void showConfirmEventEditingDialog(String eventInformation) {
        view.displayMessage(eventInformation);
        view.displayUserInputMessage("\nDo you want to edit this event? (yes/no)\n> ");
        String choice = view.getUserInput();

        if (!choice.equalsIgnoreCase("yes")) {
            return;
        }
    }
}
