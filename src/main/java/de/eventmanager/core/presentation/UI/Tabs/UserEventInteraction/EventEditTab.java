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
            DefaultDialogHelper.showItemSeparator(view, 55);
            }
        );

        showEventInformationByEventIDDialog();
    }

    private void showEventInformationByEventIDDialog() {
        view.displayUserInputMessage("\nEnter the event ID of the event you want to edit\n> ");
        String userInputEventID = view.getUserInput();

        if (userInputEventID.isEmpty()) {
            DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "event ID");

            return;
        }
        eventID = userInputEventID;
        String eventInformation = eventService.getEventInformationByID(userInputEventID);

        if (eventInformation.isEmpty()) {
            view.displayErrorMessage("\nNo event found with the given event ID. Please try again.\n");

            return;
        }

        showConfirmEventEditingDialog(eventInformation);
    }

    private void showConfirmEventEditingDialog(String eventInformation) {
        view.displayMessage(eventInformation);
        view.displayUserInputMessage("\nDo you want to edit this event? (yes/no)\n> ");

        if (!view.getUserInput().equalsIgnoreCase("yes")) {
            return;
        }

        handleEventEditing();
    }

    private void handleEventEditing() {
        DefaultDialogHelper.showEditAttributeDialog(
                view, "event's name").ifPresent(attribute -> newEventName = attribute);
        DefaultDialogHelper.showEditAttributeDialog(
                view, "event's start date").ifPresent(attribute -> newEventStart = attribute);
        DefaultDialogHelper.showEditAttributeDialog(
                view, "event's end date").ifPresent(attribute -> newEventEnd = attribute);
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
