package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.presentation.Service.Implementation.EventServiceImpl;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;

public class EventEditTab implements Tab {
    private View view;
    private String loggedInUserID;
    private String eventID;
    private String newEventName;
    private String newEventStart;
    private String newEventEnd;
    private String newEventCategory;
    private String newEventPostalCode;
    private String newEventAddress;
    private String newEventLocation;
    private String newEventDescription;
    private EventService eventService;

    public EventEditTab(View view, String loggedInUserID) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.eventService = new EventServiceImpl();
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Edit Event");
        handleShowCreatedEventsForLoggedInUser();

        if (!showConfirmEventEditingDialog(showEventInformationByEventIDDialog())) {
            return;
        }

        handleEventEditing();
        eventService.editEvent(
                eventID, newEventName, newEventStart, newEventEnd, newEventCategory,
                newEventPostalCode, newEventAddress, newEventLocation,
                newEventDescription, loggedInUserID
        );
    }

    private void handleShowCreatedEventsForLoggedInUser() {
        List<String > createdEvents = eventService.getCreatedEventsByUserID(loggedInUserID);

        if (createdEvents.isEmpty()) {
            view.displayWarningMessage("\nYou have not created any events yet.\n");

            return;
        }

        view.displayUnderlinedSubheading("Your Created Events, you can edit:");

        createdEvents.forEach(eventInformation -> {
            view.displayMessage(eventInformation);
            DefaultDialogHelper.showItemSeparator(view, DefaultDialogHelper.DEFAULT_ITEM_SEPARATOR_LENGTH);
            }
        );
    }

    private String  showEventInformationByEventIDDialog() {
        view.displayUserInputMessage("\nEnter the event ID of the event you want to edit\n> ");
        String userInputEventID = view.getUserInput();

        if (userInputEventID.isEmpty()) {
            DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "event ID");

            return "";
        }
        eventID = userInputEventID;

        if (!eventService.getEventIsExistingByID(eventID)) {
            view.displayErrorMessage("\nNo event found with the given event ID. Please try again.\n");

            return "";
        }

        return eventService.getEventInformationByID(userInputEventID);
    }

    private boolean showConfirmEventEditingDialog(String eventInformation) {
        if (eventInformation.isEmpty()) {
            return false;
        }

        view.displayMessage(eventInformation);

        view.displayUserInputMessage(
                "\nDo you want to edit this event?"
                + DefaultDialogHelper.ACCEPT_OR_ABORT_MESSAGE
        );

        if (!view.getUserInput().equalsIgnoreCase(DefaultDialogHelper.CONFIRM)) {
            view.displayWarningMessage("\nAbort event editing.\n");

            return false;
        }

        return true;
    }

    private void handleEventEditing() {
        DefaultDialogHelper.showEditAttributeDialog(
                view, "event's name").ifPresent(attribute -> newEventName = attribute);
        DefaultDialogHelper.showDateInputDialog(
                view, "new event's start date", true).ifPresent(attribute -> newEventStart = attribute);
        DefaultDialogHelper.showTimeInputDialog(
                view, "new event's start time", true).ifPresent(attribute -> newEventStart += " " + attribute);
        DefaultDialogHelper.showDateInputDialog(
                view, "new event's end date", true).ifPresent(attribute -> newEventEnd = attribute);
        DefaultDialogHelper.showTimeInputDialog(
                view, "new event's end time", true).ifPresent(attribute -> newEventEnd += " " + attribute);
        DefaultDialogHelper.showEditAttributeDialog(
                view, "event's category").ifPresent(attribute -> newEventCategory = attribute);
        DefaultDialogHelper.showEditAttributeDialog(
                view, "event location postal code").ifPresent(attribute -> newEventPostalCode = attribute);
        DefaultDialogHelper.showEditAttributeDialog(
                view, "event location address").ifPresent(attribute -> newEventAddress = attribute);
        DefaultDialogHelper.showEditAttributeDialog(
                view, "event location").ifPresent(attribute -> newEventLocation = attribute);
        DefaultDialogHelper.showEditAttributeDialog(
                view, "event description").ifPresent(attribute -> newEventDescription = attribute);
    }
}
