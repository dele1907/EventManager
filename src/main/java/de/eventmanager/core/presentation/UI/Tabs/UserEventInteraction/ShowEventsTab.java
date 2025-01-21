package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.events.Management.EventManager;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.Tabs.LoginRegistrationPage;
import de.eventmanager.core.presentation.UI.Tabs.MainMenuTab;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

import java.util.Optional;

public class ShowEventsTab implements Tab{
    private View textView;
    private UserController userController;
    private User loggedInUser;

    public ShowEventsTab(View textView, UserController userController, User loggedInUser) {
        this.textView = textView;
        this.userController = userController;
        this.loggedInUser = loggedInUser;
    }

    public void start() {

        System.out.println("===== ShowEventsTab =====");
        textView.displayMessage("\n1. Search by Name \n2. Search by Location \n3. Search by City \n4. Go Back to Main Menu");

        textView.displayMessage("\nChoose an option: ");
        String choice = textView.getUserInput();
        handelSearchOption(choice);

    }

    public boolean handelSearchOption(String choice) {

        switch (choice) {
            case "1":
                textView.displayMessage("\nWhats the Name of the Event you looking for?");
                textView.displayMessage("\nType in: ");
                String eventName = textView.getUserInput();
                getEventInformationbyName(eventName);
                break;
            case "2":
                textView.displayMessage("\nWhats the Location of the Event you looking for?");
                textView.displayMessage("\nType in: ");
                String eventLocation = textView.getUserInput();
                getEventInformationbyLocation(eventLocation);
                break;
            case "3":
                textView.displayMessage("\nWhats the City of the Event you looking for?");
                textView.displayMessage("\nType in: ");
                String eventCity = textView.getUserInput();
                getEventInformationbyCity(eventCity);
                break;
            case "4":
                textView.displayMessage("\nGo Back to Main Menu");
                handelGoBackToMainMenu();
                break;
            default:
                textView.displayErrorMessage("\nPlease enter a valid option!");
                break;
        }

        return true;
    }

    public void getEventInformationbyName(String eventName) {
        EventManager readbyName = new EventManager();
        Optional<PublicEvent> optionalEvent= readbyName.readPublicEventByName(eventName);

        if (optionalEvent.isPresent()) {
            PublicEvent event = optionalEvent.get();

            textView.displayMessage("\n Event Name: " + event.getEventName());
            textView.displayMessage("\n Event Start: " + event.getEventStart());
            textView.displayMessage("\n Event End: " + event.getEventEnd());
            textView.displayMessage("\n Event Location: " + event.getEventLocation());
            textView.displayMessage("\n Event Address: " + event.getAddress());
            textView.displayMessage("\n Event Postalcode: " + event.getPostalCode());
            textView.displayMessage("\n Event Description: " + event.getDescription());
            textView.displayMessage("\n Event Category: " + event.getCategory());
            textView.displayMessage("\n Event Capacity: " + event.getMaximumCapacity());
            textView.displayMessage("\n Event Booked: " + event.getBookedUsersOnEvent());

        }
    }

    public void getEventInformationbyLocation(String eventLocation) {

    }

    public void getEventInformationbyCity(String eventCity) {

    }

    private void addDelay(int seconds) {
        int delay = seconds * 1000;

        try{
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handelGoBackToMainMenu() {
        MainMenuTab mainMenuTab = new MainMenuTab(textView, loggedInUser,
                new LoginRegistrationPage(textView, userController), userController
        );

        mainMenuTab.start();
        addDelay(2);
    }
}
