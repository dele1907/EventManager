package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.presentation.Service.Implementation.EventServiceImpl;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class EventCreatorAddUserToEventTab implements Tab {
    View view;
    String loggedInUserID;
    UserService userService;
    EventService eventService;

    public EventCreatorAddUserToEventTab(View view, String loggedInUserID) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.userService = new UserServiceImpl();
        this.eventService = new EventServiceImpl();
    }

    @Override
    public void start() {
        showAddUserToEventDialog();
    }

    private void showAddUserToEventDialog() {
        if (!showCreatorsAvailableEventsToAddUserToDialog()) {
            return;
        }

        view.displayUserInputMessage("\nEnter eventID of the event you want to add a user to\n> ");
        String eventID = view.getUserInput();

        if (eventService.getEventInformationByID(eventID).isEmpty()) {
            view.displayErrorMessage("Event does not exist.");

            return;
        }

        view.displayUserInputMessage("\nEnter the email of the user you want to add to the event\n> ");
        String userEmail = view.getUserInput();

        if (!userService.getUserIsPresentInDatabaseByEmail(userEmail)) {
            view.displayErrorMessage("\nUser does not exist.\n");

            return;
        }

        if (eventService.getUserHasAlreadyBookedEventByEMail(userEmail, eventID)) {
            view.displayErrorMessage("\nUser is already booked for this event.\n");

            return;
        }

        eventService.addUserToEventByUserEmail(eventID, userEmail, loggedInUserID);

        view.displaySuccessMessage("\nUser added to event successfully.\n");
    }

    private boolean showCreatorsAvailableEventsToAddUserToDialog() {
        var loggedInUsersCreatedEvents = eventService.getCreatedEventsByUserID(loggedInUserID);

        if (loggedInUsersCreatedEvents.isEmpty()) {
            view.displayErrorMessage("\nYou have no created events to add users to.\n");

            return false;
        }

        loggedInUsersCreatedEvents.forEach(event -> {
            DefaultDialogHelper.showItemSeparator(view, 55);
            view.displayMessage(event);
        });

        return true;
    }
}
