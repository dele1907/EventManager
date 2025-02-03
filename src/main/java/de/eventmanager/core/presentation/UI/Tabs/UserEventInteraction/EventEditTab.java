package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;
import java.util.Optional;

public class EventEditTab implements Tab {
    private View view;
    private String loggedInUserID;
    private UserController userController;
    private String eventID;
    private String newEventName;
    private String newEventStart;
    private String newEventEnd;
    private String newEventCategory;
    private String newEventPostalCode;
    private String newEventCity;
    private String newEventAddress;
    private String newEventLocation;
    private String newEventDescription;
    private int newEventMaximumCapacity;
    private int newEventMinimumAge;

    public EventEditTab(View view, String loggedInUserID, UserController userController) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.userController = userController;
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Edit Event");
        handleShowCreatedEventsForLoggedInUser();
        userController.editEvent(
                eventID, newEventName, newEventStart, newEventEnd, newEventCategory,
                newEventPostalCode, newEventCity, newEventAddress, newEventLocation,
                newEventDescription, loggedInUserID
        );
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
        String userInputEventID = view.getUserInput();

        if (userInputEventID.isEmpty()) {
            view.displayErrorMessage("\nInvalid event ID. Pleas try again.\n");

            return;
        }
        eventID = userInputEventID.trim();
        String eventInformation = userController.getEventInformationByEventID(userInputEventID);

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

        handleEventEditing();
    }

    private void handleEventEditing() {
        showEditEventAttributeDialog("event's name").ifPresent(dialog -> newEventName = dialog);
        showEditEventAttributeDialog("event's start date").ifPresent(dialog -> newEventStart = dialog);
        showEditEventAttributeDialog("event's end date").ifPresent(dialog -> newEventEnd = dialog);
        showEditEventAttributeDialog("event's category").ifPresent(dialog -> newEventCategory = dialog);
        showEditEventAttributeDialog("event location postal code").ifPresent(dialog -> newEventPostalCode = dialog);
        showEditEventAttributeDialog("event location city").ifPresent(dialog -> newEventCity = dialog);
        showEditEventAttributeDialog("event location address").ifPresent(dialog -> newEventAddress = dialog);
        showEditEventAttributeDialog("event location").ifPresent(dialog -> newEventLocation = dialog);
        showEditEventAttributeDialog("event description").ifPresent(dialog -> newEventDescription = dialog);
    }

    private Optional<String> showEditEventAttributeDialog(String prompt) {
        view.displayUserInputMessage("\nEnter new " + prompt +
                "\n" + DefaultDialogHelper.BLANK_TO_KEEP +
                "\n> ");
        String eventAttribute = view.getUserInput();

        if (eventAttribute.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(eventAttribute);
    }
}
