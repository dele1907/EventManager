package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.EnumHelper;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.presentation.Service.Implementation.EventServiceImpl;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ShowEventsTab implements Tab{
    private View view;
    private String loggedInUserID;
    private EventService eventService;

    private enum ShowEventsMenuChoice {
        SEARCH_EVENT_BY_NAME,
        SEARCH_EVENT_BY_LOCATION,
        SEARCH_EVENT_BY_CITY,
        SHOW_USERS_BOOKED_EVENTS,
        SHOW_USERS_CREATED_EVENTS,
        BACK_TO_EVENT_OPERATIONS;

        public static Optional<ShowEventsMenuChoice> fromUserInput(String userInput) {
            return EnumHelper.getMapOfStringToEnumConstant(userInput, ShowEventsMenuChoice.class,
                Map.of(
                "1", SEARCH_EVENT_BY_NAME,
                "2", SEARCH_EVENT_BY_LOCATION,
                "3", SEARCH_EVENT_BY_CITY,
                "4", SHOW_USERS_BOOKED_EVENTS,
                "5", SHOW_USERS_CREATED_EVENTS,
                "6", BACK_TO_EVENT_OPERATIONS
                )
            );
        }
    }

    public ShowEventsTab(View view, String loggedInUserID) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        eventService = new EventServiceImpl();
    }

    public void start() {
        boolean eventSearchingIsActive = true;

        while (eventSearchingIsActive) {
            DefaultDialogHelper.getTabOrPageHeading(view, "Show Events");
            handleMenuGeneration();
            var showEventsMenuChoice = ShowEventsMenuChoice.fromUserInput(view.getUserInput());

            if (showEventsMenuChoice.isEmpty()) {
                DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");
                continue;
            }

            switch (showEventsMenuChoice.get()) {
                case SEARCH_EVENT_BY_NAME -> showEventInformationByName();
                case SEARCH_EVENT_BY_LOCATION -> showEventInformationByLocation();
                case SEARCH_EVENT_BY_CITY -> showEventInformationByCity();
                case SHOW_USERS_BOOKED_EVENTS -> showEventInformationForUsersBookedEvents();
                case SHOW_USERS_CREATED_EVENTS -> showEventInformationForUsersCreatedEvents();
                case BACK_TO_EVENT_OPERATIONS -> eventSearchingIsActive = false;
            }
        }
    }

    public void showEventInformationByName() {
        view.displayUserInputMessage("\nWhats the Name of the Event you looking for?\n> ");
        String eventName = view.getUserInput();

        List<String> listFoundEvents = eventService.getPublicEventsByName(eventName);

        if (listFoundEvents.isEmpty()) {
            view.displayErrorMessage("\nNo Event found with the name: " + eventName);

            return;
        }

        for(var event : listFoundEvents) {
            addDelay(1);
            view.displayMessage(event);
            DefaultDialogHelper.showItemSeparator(view, DefaultDialogHelper.DEFAULT_ITEM_SEPARATOR_LENGTH);
        }

        showUserWantToBookDialog();
    }

    public void showEventInformationByLocation() {
        view.displayUserInputMessage("\nWhats the Location of the Event you looking for?\n> ");
        String eventLocation = view.getUserInput();

        List<String> listFoundEvents = eventService.getPublicEventsByLocation(eventLocation);

        if (listFoundEvents.isEmpty()) {
            view.displayErrorMessage("\nNo Event found for provided location: " + eventLocation);

            return;
        }

        for (var event : listFoundEvents) {
            addDelay(1);
            view.displayMessage(event);
            DefaultDialogHelper.showItemSeparator(view, DefaultDialogHelper.DEFAULT_ITEM_SEPARATOR_LENGTH);
        }

        showUserWantToBookDialog();
    }

    public void showEventInformationByCity() {
        view.displayUserInputMessage("\nWhats the City of the Event you looking for?\n> ");
        String eventCity = view.getUserInput();

        List<String> listFoundEvents = eventService.getPublicEventsByCity(eventCity);

        if (listFoundEvents.isEmpty()) {
            view.displayErrorMessage("\nNo Event for following city: " + eventCity);

            return;
        }

        for (var event : listFoundEvents) {
            addDelay(1);
            view.displayMessage(event);
            DefaultDialogHelper.showItemSeparator(view, DefaultDialogHelper.DEFAULT_ITEM_SEPARATOR_LENGTH);
        }

        showUserWantToBookDialog();
    }

    public void showEventInformationForUsersBookedEvents() {
        List<String> listBookedEvents = eventService.getUsersBookedEventsInformation(loggedInUserID);

        if (listBookedEvents.isEmpty()) {
            view.displayErrorMessage("\nYou have not booked any events yet.");

            return;
        }

        view.displayUnderlinedSubheading("\nYour booked events:\n");
        listBookedEvents.forEach(event -> {
            addDelay(1);
            view.displayMessage(event);
            DefaultDialogHelper.showItemSeparator(view, DefaultDialogHelper.DEFAULT_ITEM_SEPARATOR_LENGTH);
        });
    }

    public void showEventInformationForUsersCreatedEvents() {
        List<String> listCreatedEvents = eventService.getCreatedEventsByUserID(loggedInUserID);

        if (listCreatedEvents.isEmpty()) {
            view.displayErrorMessage("\nYou have not created any events yet.");

            return;
        }

        view.displayUnderlinedSubheading("\nYour created events:\n");
        listCreatedEvents.forEach(event -> {
            addDelay(1);
            view.displayMessage(event);
            DefaultDialogHelper.showItemSeparator(view, DefaultDialogHelper.DEFAULT_ITEM_SEPARATOR_LENGTH);
        });
    }

    private void addDelay(int seconds) {
        try{
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleMenuGeneration() {
        DefaultDialogHelper.generateMenu(view, List.of(
                "Search event by name",
                "Search event by location",
                "Search event by city",
                "Show my booked events",
                "Show my created events",
                "Back to event operations"
        ));
    }

    private void showUserWantToBookDialog() {
        view.displayUserInputMessage("\nDo you want to book an event? " +
                DefaultDialogHelper.ACCEPT_OR_ABORT_MESSAGE);

        if (!view.getUserInput().equalsIgnoreCase("yes")) {
            return;
        }

        new EventBookingTab(view, loggedInUserID).start();
    }
}
