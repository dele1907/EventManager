package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.presentation.Service.Implementation.EventServiceImpl;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class EventExportTab implements Tab {
    private View view;
    private String loggedInUserID;
    private EventService eventService;

    public EventExportTab(View view, String loggedInUserID) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.eventService = new EventServiceImpl();
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Event Export");

        view.displayMessage("\nYour booked and created events you can export are:");
        DefaultDialogHelper.addDelay(2);

        view.displayUnderlinedSubheading("\nYour booked events:");
        eventService.getUsersBookedEventsInformation(loggedInUserID).forEach(event -> {
            view.displayMessage(event.toString());
            DefaultDialogHelper.showItemSeparator(view, DefaultDialogHelper.DEFAULT_ITEM_SEPARATOR_LENGTH);
        });

        DefaultDialogHelper.addDelay(1);

        view.displayUnderlinedSubheading("Your created events:");
        eventService.getCreatedEventsByUserID(loggedInUserID).forEach(event -> {
            view.displayMessage(event.toString());
            DefaultDialogHelper.showItemSeparator(view, DefaultDialogHelper.DEFAULT_ITEM_SEPARATOR_LENGTH);
        });

        view.displayUserInputMessage("\nEnter event ID you want to export\n> ");
        if (!eventService.exportEventToCalendarFile(view.getUserInput())) {
            view.displayMessage("\nEvent export failed. Maybe the event does not exist.\n");

            return;
        }

        view.displaySuccessMessage("\nEvent exported successfully.\n");
    }
}
