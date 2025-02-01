package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

import java.util.List;

public class ShowEventsTab implements Tab{
    private View textView;
    private UserController userController;
    private String loggedInUserID;

    public ShowEventsTab(View textView, UserController userController, String loggedInUserID) {
        this.textView = textView;
        this.userController = userController;
        this.loggedInUserID = loggedInUserID;
    }

    public void start() {
        boolean eventSearchingIsActive = true;

        while (eventSearchingIsActive) {
            textView.displayTabOrPageHeading("\n===== ShowEventsTab =====");
            textView.displayMessage(
                    "1. Search by Name " +
                    "\n2. Search by Location " +
                    "\n3. Search by City " +
                    "\n4. Back to Event Operations"
            );
            textView.displayUserInputMessage("\n\nChoose an option\n> ");
            String choice = textView.getUserInput();

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
                    textView.displayErrorMessage("\nPlease enter a valid option!");
                    break;
            }
        }
    }

    public void getEventInformationByName() {
        textView.displayUserInputMessage("\nWhats the Name of the Event you looking for?\n> ");
        String eventName = textView.getUserInput();

        List<String> listFoundEvents = userController.getPublicEventsByName(eventName);

        if (listFoundEvents.isEmpty()) {
            textView.displayErrorMessage("\nNo Event found with the name: " + eventName);

            return;
        }

        for(var event : listFoundEvents) {
            addDelay(1);
            textView.displayMessage(event);
        }
    }

    public void getEventInformationByLocation() {
        textView.displayUserInputMessage("\nWhats the Location of the Event you looking for?\n> ");
        String eventLocation = textView.getUserInput();

        List<String> listFoundEvents = userController.getPublicEventsByLocation(eventLocation);

        if (listFoundEvents.isEmpty()) {
            textView.displayErrorMessage("\nNo Event found for provided location: " + eventLocation);

            return;
        }

        for (var event : listFoundEvents) {
            addDelay(1);
            textView.displayMessage(event);
        }
    }

    public void getEventInformationByCity() {
        textView.displayUserInputMessage("\nWhats the City of the Event you looking for?\n> ");
        String eventCity = textView.getUserInput();

        List<String> listFoundEvents = userController.getPublicEventsByCity(eventCity);

        if (listFoundEvents.isEmpty()) {
            textView.displayErrorMessage("\nNo Event for following city: " + eventCity);

            return;
        }

        for (var event : listFoundEvents) {
            addDelay(1);
            textView.displayMessage(event);
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
