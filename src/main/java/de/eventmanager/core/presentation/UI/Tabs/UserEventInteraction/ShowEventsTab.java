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

    public ShowEventsTab(View textView, UserController userController, User loggedInUser) {
        this.textView = textView;
        this.userController = userController;
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

            textView.displayMessage("\nChoose an option: ");
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

    public void displaySearchResult(PublicEvent event) {
        textView.displayMessage("\n Event Name: " + event.getEventName());
        textView.displayMessage("\n Event Start: " + event.getEventStart());
        textView.displayMessage("\n Event End: " + event.getEventEnd());
        textView.displayMessage("\n Event Location: " + event.getEventLocation());
        textView.displayMessage("\n Event Address: " + event.getAddress());
        textView.displayMessage("\n Event Postcode: " + event.getPostalCode());
        textView.displayMessage("\n Event Description: " + event.getDescription());
        textView.displayMessage("\n Event Category: " + event.getCategory());
        textView.displayMessage("\n Event Capacity: " + event.getMaximumCapacity());
        textView.displayMessage("\n Event Booked: " + event.getBookedUsersOnEvent());
        textView.displayMessage("\n");
    }


    public void getEventInformationByName() {
        textView.displayMessage("\nWhats the Name of the Event you looking for?");
        textView.displayMessage("\nType in: ");
        String eventName = textView.getUserInput();

        List<PublicEvent> listFoundEvents = userController.getPublicEventsByName(eventName);

        if (listFoundEvents.isEmpty()) {
            textView.displayErrorMessage("\nNo Event found with the name: " + eventName);

            return;
        }

        for(var event : listFoundEvents) {
            addDelay(1);
            displaySearchResult(event);
        }
    }

    public void getEventInformationByLocation() {
        textView.displayMessage("\nWhats the Location of the Event you looking for?");
        textView.displayMessage("\nType in: ");
        String eventLocation = textView.getUserInput();

        List<PublicEvent> listFoundEvents = userController.getPublicEventsByLocation(eventLocation);

        if (listFoundEvents.isEmpty()) {
            textView.displayErrorMessage("\nNo Event found with the location: " + eventLocation);

            return;
        }

        for (PublicEvent event : listFoundEvents) {
            addDelay(1);
            displaySearchResult(event);
        }
    }

    public void getEventInformationByCity() {
        textView.displayMessage("\nWhats the City of the Event you looking for?");
        textView.displayMessage("\nType in: ");
        String eventCity = textView.getUserInput();

        List<PublicEvent> listFoundEvents = userController.getPublicEventsByCity(eventCity);

        if (listFoundEvents.isEmpty()) {
            textView.displayErrorMessage("\nNo Event found with the city: " + eventCity);

            return;
        }

        for (PublicEvent event : listFoundEvents) {
            addDelay(1);
            displaySearchResult(event);
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
