package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.presentation.Service.Implementation.EventServiceImpl;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class EventCancelParticipationTab implements Tab {
    private View view;
    private String loggedInUserID;
    private EventService eventService;

    public EventCancelParticipationTab(View view, String loggedInUserID) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        eventService = new EventServiceImpl();
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Cancel Event Participation");
        showCancelParticipationDialog();
    }

    private void showCancelParticipationDialog() {
        view.displayUserInputMessage("Enter the event ID you want to cancel participation\n> ");
        String eventID = view.getUserInput();

        if (!eventService.getUserHasAlreadyBookedEvent(loggedInUserID, eventID)) {
            view.displayErrorMessage("You have not booked this event.");

            return;
        }

        if (!userIsSureToCancelParticipationDialog(eventID)) {
            return;
        }

        if (!eventService.userCancelParticipationForEvent(eventID, loggedInUserID)) {
            DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "event ID");

            return;
        }

        view.displaySuccessMessage("\nCancel participation successful.\n");
    }

    private boolean userIsSureToCancelParticipationDialog(String eventID) {
        view.displayWarningMessage("Are you sure you want to cancel participation for following event:\n");
        view.displayMessage(eventService.getEventInformationByID(eventID));
        view.displayUserInputMessage("\n(yes/press any key)\n> ");
        String userInput = view.getUserInput();

        if (!userInput.equalsIgnoreCase("yes")) {
            view.displayErrorMessage("\nAbort participation canceled.\n");

            return false;
        }

        return true;
    }
}
