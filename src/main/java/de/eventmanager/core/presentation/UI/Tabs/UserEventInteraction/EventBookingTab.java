package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.presentation.Service.Implementation.EventServiceImpl;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class EventBookingTab implements Tab {
    private View view;
    private String loggedInUserID;
    private EventService eventService;

    public EventBookingTab(View view, String loggedInUserID, UserController userController) {
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

        if (eventService.getUserHasAlreadyBookedEvent(loggedInUserID, eventID)) {
            view.displayErrorMessage("You have already booked this event.");

            return;
        }

        if (!userIsSureToBookEventDialog()) {
            return;
        }

        if (!eventService.userBookEvent(eventID, loggedInUserID)) {
            DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "event ID");

            return;
        }

        view.displaySuccessMessage("\nBooking successful.\n");
    }

    private boolean userIsSureToBookEventDialog() {
        view.displayUserInputMessage("Are you sure you want to book following event(yes/press any key)\n> ");
        String userInput = view.getUserInput();

        if (!userInput.equalsIgnoreCase("yes")) {
            view.displayErrorMessage("\nBooking canceled.\n");

            return false;
        }

        return true;
    }
}
