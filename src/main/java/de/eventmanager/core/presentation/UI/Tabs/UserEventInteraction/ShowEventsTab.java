package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;

public class ShowEventsTab implements Tab{
    private View view;
    private UserController userController;
    private String loggedInUserID;

    public ShowEventsTab(View view, UserController userController, String loggedInUserID) {
        this.view = view;
        this.userController = userController;
        this.loggedInUserID = loggedInUserID;
    }

    public void start() {
        boolean eventSearchingIsActive = true;

        while (eventSearchingIsActive) {
            DefaultDialogHelper.getTabOrPageHeading(view, "Show Events");

            DefaultDialogHelper.generateMenu(view, List.of(
                    "Search Event by Name",
                    "Search Event by Location",
                    "Search Event by City",
                    "Back to Event Operations"
            ));
            String choice = view.getUserInput();

            switch (choice) {
                case "1":
                    getEventInformationByName();
                    break;
                case "2":
                    getEventInformationByLocation();
                    break;
                case "3":
                    getEventInformationByCity();
                    break;
                case "4":
                    eventSearchingIsActive = false;
                    break;
                default:
                    view.displayErrorMessage("\nPlease enter a valid option!");
                    break;
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
}
