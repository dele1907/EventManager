package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.EnumHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ShowEventsTab implements Tab{
    private View view;
    private UserController userController;
    private String loggedInUserID;

    private enum ShowEventsMenuChoice {
        SEARCH_EVENT_BY_NAME,
        SEARCH_EVENT_BY_LOCATION,
        SEARCH_EVENT_BY_CITY,
        BACK_TO_EVENT_OPERATIONS;

        public static Optional<ShowEventsMenuChoice> fromUserInput(String userInput) {
            return EnumHelper.getMapOfStringToEnumConstant(userInput, ShowEventsMenuChoice.class,
                Map.of(
                "1", SEARCH_EVENT_BY_NAME,
                "2", SEARCH_EVENT_BY_LOCATION,
                "3", SEARCH_EVENT_BY_CITY,
                "4", BACK_TO_EVENT_OPERATIONS
                )
            );
        }
    }

    public ShowEventsTab(View view, UserController userController, String loggedInUserID) {
        this.view = view;
        this.userController = userController;
        this.loggedInUserID = loggedInUserID;
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
                case SEARCH_EVENT_BY_NAME -> getEventInformationByName();
                case SEARCH_EVENT_BY_LOCATION -> getEventInformationByLocation();
                case SEARCH_EVENT_BY_CITY -> getEventInformationByCity();
                case BACK_TO_EVENT_OPERATIONS -> eventSearchingIsActive = false;
            }
        }
    }

    public void getEventInformationByName() {
        view.displayUserInputMessage("\nWhats the Name of the Event you looking for?\n> ");
        String eventName = view.getUserInput();

        List<String> listFoundEvents = userController.getPublicEventsByName(eventName);

        if (listFoundEvents.isEmpty()) {
            view.displayErrorMessage("\nNo Event found with the name: " + eventName);

            return;
        }

        for(var event : listFoundEvents) {
            addDelay(1);
            view.displayMessage(event);
        }
    }

    public void getEventInformationByLocation() {
        view.displayUserInputMessage("\nWhats the Location of the Event you looking for?\n> ");
        String eventLocation = view.getUserInput();

        List<String> listFoundEvents = userController.getPublicEventsByLocation(eventLocation);

        if (listFoundEvents.isEmpty()) {
            view.displayErrorMessage("\nNo Event found for provided location: " + eventLocation);

            return;
        }

        for (var event : listFoundEvents) {
            addDelay(1);
            view.displayMessage(event);
        }
    }

    public void getEventInformationByCity() {
        view.displayUserInputMessage("\nWhats the City of the Event you looking for?\n> ");
        String eventCity = view.getUserInput();

        List<String> listFoundEvents = userController.getPublicEventsByCity(eventCity);

        if (listFoundEvents.isEmpty()) {
            view.displayErrorMessage("\nNo Event for following city: " + eventCity);

            return;
        }

        for (var event : listFoundEvents) {
            addDelay(1);
            view.displayMessage(event);
            DefaultDialogHelper.showItemSeparator(view, 55);
        }
    }

    private void addDelay(int seconds) {
        int delay = seconds * 1000;

        try{
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleMenuGeneration() {
        DefaultDialogHelper.generateMenu(view, List.of(
                "Search Event by Name",
                "Search Event by Location",
                "Search Event by City",
                "Back to Event Operations"
        ));
    }
}
