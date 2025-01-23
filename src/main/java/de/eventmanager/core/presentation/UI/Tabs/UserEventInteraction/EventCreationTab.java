package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

public class EventCreationTab implements Tab {
    private View textView;
    private User loggedInUser;
    private UserController userController;

    public EventCreationTab(View textView, User loggedInUser, UserController userController) {
        this.textView = textView;
        this.loggedInUser = loggedInUser;
        this.userController = userController;
    }

    @Override
    public void start() {
        System.out.println("===== Event Creation ======");
        System.out.println("1. Create new public event");
        System.out.println("2. Create new private event");
        System.out.println("3. Back to Event Operations");
        System.out.println("Please enter your choice: ");

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
                System.out.println("Invalid choice. Please try again.");
                start();
        }
    }

    private boolean createNewEvent(boolean isPublicEvent) {
        System.out.println("Enter event name: ");
        String eventName = textView.getUserInput();
        System.out.println("Enter event start date: ");
        String eventStart = textView.getUserInput();
        System.out.println("Enter event end date: ");
        String eventEnd = textView.getUserInput();
        System.out.println("Enter event category: ");
        String category = textView.getUserInput();
        System.out.println("Enter event postal code: ");
        String postalCode = textView.getUserInput();
        System.out.println("Enter event address: ");
        String address = textView.getUserInput();
        System.out.println("Enter event location: ");
        String eventLocation = textView.getUserInput();
        System.out.println("Enter event description: ");
        String description = textView.getUserInput();
        int maxCapacity = 0;

        if (isPublicEvent) {
            System.out.println("Enter event maximum capacity: ");
            maxCapacity = Integer.parseInt(textView.getUserInput());
        }

        return userController.createNewEvent(eventName, eventStart, eventEnd, category, postalCode, address, maxCapacity, eventLocation, description, isPublicEvent);
    }
}
