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
        showEditEventNameDialog();
        showEditEventStartDialog();
        showEditEventEndDialog();
        showEditEventCategoryDialog();
        showEditEventPostalCodeDialog();
        showEditEventCityDialog();
        showEditEventAddressDialog();
        showEditEventLocationDialog();
        showEditEventDescriptionDialog();
    }

    private void showEditEventNameDialog() {
        view.displayUserInputMessage("\nEnter new event name\n> ");
        String eventName = view.getUserInput();

        if (eventName.isEmpty()) {
            return;
        }

        newEventName = eventName;
    }

    private void showEditEventStartDialog() {
        view.displayUserInputMessage("\nEnter new event start date\n> ");
        String eventStart = view.getUserInput();

        if (eventStart.isEmpty()) {
            return;
        }

        newEventStart = eventStart;
    }

    private void showEditEventEndDialog() {
        view.displayUserInputMessage("\nEnter new event end date\n> ");
        String eventEnd = view.getUserInput();

        if (eventEnd.isEmpty()) {
            return;
        }

        newEventEnd = eventEnd;
    }

    private void showEditEventCategoryDialog() {
        view.displayUserInputMessage("\nEnter new event category\n> ");
        String eventCategory = view.getUserInput();

        if (eventCategory.isEmpty()) {
            return;
        }

        newEventCategory = eventCategory;
    }

    private void showEditEventPostalCodeDialog() {
        view.displayUserInputMessage("\nEnter new event postal code\n> ");
        String eventPostalCode = view.getUserInput();

        if (eventPostalCode.isEmpty()) {
            return;
        }

        newEventPostalCode = eventPostalCode;
    }

    private void showEditEventCityDialog() {
        view.displayUserInputMessage("\nEnter new event city" +
                "\n" + DefaultDialogHelper.BLANK_TO_KEEP +
                "\n> ");
        String eventCity = view.getUserInput();

        if (eventCity.isEmpty()) {
            return;
        }

        newEventCity = eventCity;
    }

    private void showEditEventAddressDialog() {
        view.displayUserInputMessage("\nEnter new event address" +
                "\n" + DefaultDialogHelper.BLANK_TO_KEEP +
                "\n> ");
        String eventAddress = view.getUserInput();

        if (eventAddress.isEmpty()) {
            return;
        }

        newEventAddress = eventAddress;
    }

    private void showEditEventLocationDialog() {
        view.displayUserInputMessage("\nEnter new event location" +
                "\n" + DefaultDialogHelper.BLANK_TO_KEEP +
                "\n> ");
        String eventLocation = view.getUserInput();

        if (eventLocation.isEmpty()) {
            return;
        }

        newEventLocation = eventLocation;
    }

    private void showEditEventDescriptionDialog() {
        view.displayUserInputMessage("\nEnter new event description" +
                "\n" + DefaultDialogHelper.BLANK_TO_KEEP +
                "\n> ");
        String eventDescription = view.getUserInput();

        if (eventDescription.isEmpty()) {
            return;
        }

        newEventDescription = eventDescription;
    }

    private void showEditEventMaximumCapacityDialog() {
        view.displayUserInputMessage("\nEnter new event maximum capacity" +
                "\n" + DefaultDialogHelper.BLANK_TO_KEEP +
                "\n> ");
        String eventMaximumCapacity = view.getUserInput();

        if (eventMaximumCapacity.isEmpty()) {
            return;
        }

        newEventMaximumCapacity = Integer.parseInt(eventMaximumCapacity);
    }

    private void showEditEventMinimumAgeDialog() {
        view.displayUserInputMessage("\nEnter new event minimum age" +
                "\n" + DefaultDialogHelper.BLANK_TO_KEEP +
                "\n> ");
        String eventMinimumAge = view.getUserInput();

        if (eventMinimumAge.isEmpty()) {
            return;
        }

        newEventMinimumAge = Integer.parseInt(eventMinimumAge);
    }
}
