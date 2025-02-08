package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.presentation.Service.Implementation.EventServiceImpl;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class EventBookingTab implements Tab {
    private View view;
    private String loggedInUserID;
    private EventService eventService;

    public EventBookingTab(View view, String loggedInUserID) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        eventService = new EventServiceImpl();
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Event Booking");
        showBookingDialog();
    }

    private void showBookingDialog() {
        view.displayUserInputMessage("Enter the event ID you want to book\n> ");
        String eventID = view.getUserInput();

        if (eventService.getUserHasAlreadyBookedEventByID(loggedInUserID, eventID)) {
            view.displayErrorMessage("You have already booked this event.");

            return;
        }

        if (!userIsSureToBookEventDialog(eventID)) {
            return;
        }

        if (!eventService.userBookEvent(eventID, loggedInUserID)) {
            DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "event ID");

            return;
        }

        view.displaySuccessMessage("\nBooking successful.\n");
    }

    private boolean userIsSureToBookEventDialog(String eventID) {
        view.displayWarningMessage("Are you sure you want to book following event:\n");
        view.displayMessage(eventService.getEventInformationByID(eventID));
        view.displayUserInputMessage("\n(yes/press any key)\n> ");

        if (!view.getUserInput().equalsIgnoreCase("yes")) {
            view.displayErrorMessage("\nBooking canceled.\n");

            return false;
        }

        return true;
    }
}
