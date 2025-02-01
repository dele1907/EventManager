package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

public class EventCreationTab implements Tab {
    private View textView;
    private String loggedInUserID;
    private UserController userController;

    public EventCreationTab(View textView, String loggedInUserID, UserController userController) {
        this.textView = textView;
        this.loggedInUserID = loggedInUserID;
        this.userController = userController;
    }

    @Override
    public void start() {
        textView.displayTabOrPageHeading("\n===== Event Creation ======");
        textView.displayMessage("1. Create new public event");
        textView.displayMessage("\n2. Create new private event");
        textView.displayMessage("\n3. Back to Event Operations");
        textView.displayUserInputMessage("\n\nPlease enter your choice\n> ");

        String choice = textView.getUserInput();

        switch (choice) {
            case "1":
                createNewEvent(true);
                break;
            case "2":
                createNewEvent(false);
                break;
            case "3":
                break;
            default:
                textView.displayErrorMessage("\nInvalid choice. Please try again.");
                start();
        }
    }

    private boolean createNewEvent(boolean isPublicEvent) {
        textView.displayUserInputMessage("Enter event name\n> ");
        String eventName = textView.getUserInput();
        textView.displayUserInputMessage("\nEnter event start date\n> ");
        String eventStart = textView.getUserInput();
        textView.displayUserInputMessage("\nEnter event end date\n> ");
        String eventEnd = textView.getUserInput();
        textView.displayUserInputMessage("\nEnter event category\n> ");
        String category = textView.getUserInput();
        textView.displayUserInputMessage("\nEnter event postal code\n> ");
        String postalCode = textView.getUserInput();
        textView.displayUserInputMessage("\nEnter event city\n> ");
        String city = textView.getUserInput();
        textView.displayUserInputMessage("\nEnter event address\n> ");
        String address = textView.getUserInput();
        textView.displayUserInputMessage("\nEnter event location\n> ");
        String eventLocation = textView.getUserInput();
        textView.displayUserInputMessage("\nEnter event description\n> ");
        String description = textView.getUserInput();
        int maxCapacity = 0;

        if (isPublicEvent) {
            textView.displayUserInputMessage("\nEnter event maximum capacity\n> ");
            maxCapacity = Integer.parseInt(textView.getUserInput());
        }

        return userController.createNewEvent(eventName, eventStart, eventEnd, category, postalCode, city, address, maxCapacity, eventLocation, description, isPublicEvent);
    }
}
