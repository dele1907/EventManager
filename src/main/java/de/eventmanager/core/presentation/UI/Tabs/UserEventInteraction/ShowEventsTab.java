package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

public class ShowEventsTab{
    private View textView;
    private UserController userController;
    private User loggedInUser;

    public ShowEventsTab(View textView, UserController userController, User loggedInUser) {
        this.textView = textView;
        this.userController = userController;
        this.loggedInUser = loggedInUser;
    }

    public void displaySearchOptions() {

        System.out.println("===== ShowEventsTab =====");
        textView.displayMessage("\n1. Search by Name \n2. Search by Location \n3. Search by City");

        textView.displayMessage("\nChoose an option: ");
        String choice = textView.getUserInput();

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
                textView.displayErrorMessage("\npage not implemented yet");
                break;
            case "3":
                textView.displayErrorMessage("\npage not implemented yet");
                break;
        }

        return true;
    }

    public void getEventInformationbyName(String eventName) {

    }
}
