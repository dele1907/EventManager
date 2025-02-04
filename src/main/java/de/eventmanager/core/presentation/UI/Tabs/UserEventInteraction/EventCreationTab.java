package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;

public class EventCreationTab implements Tab {
    private View view;
    private String loggedInUserID;
    private UserController userController;

    public EventCreationTab(View view, String loggedInUserID, UserController userController) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.userController = userController;
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Event Creation");

        DefaultDialogHelper.generateMenu(view,
            List.of(
                "Create new public event",
                "Create new private event",
                "Back to Event Operations"
            )
        );
        String choice = view.getUserInput();

        switch (choice) {
            case "1":
                createNewEvent(false);
                break;
            case "2":
                createNewEvent(true);
                break;
            case "3":
                break;
            default:
                view.displayErrorMessage("\nInvalid choice. Please try again.");
                start();
        }
    }

    private boolean createNewEvent(boolean isPrivateEvent) {
        view.displayUserInputMessage("Enter event name\n> ");
        String eventName = view.getUserInput();
        view.displayUserInputMessage("\nEnter event start date\n> ");
        String eventStart = view.getUserInput();
        view.displayUserInputMessage("\nEnter event end date\n> ");
        String eventEnd = view.getUserInput();
        view.displayUserInputMessage("\nEnter event category\n> ");
        String category = view.getUserInput();
        view.displayUserInputMessage("\nEnter event postal code\n> ");
        String postalCode = view.getUserInput();
        view.displayUserInputMessage("\nEnter event city\n> ");
        String city = view.getUserInput();
        view.displayUserInputMessage("\nEnter event address\n> ");
        String address = view.getUserInput();
        view.displayUserInputMessage("\nEnter event location\n> ");
        String eventLocation = view.getUserInput();
        view.displayUserInputMessage("\nEnter event description\n> ");
        String description = view.getUserInput();
        int maxCapacity = 0;
        int minimumAge = 0;

        if (!isPrivateEvent) {
            view.displayUserInputMessage("\nEnter event maximum capacity\n> ");
            String maxCapacityInputString = view.getUserInput();
            maxCapacity = maxCapacityInputString.isEmpty() ? 9999 : Integer.parseInt(maxCapacityInputString);

            view.displayUserInputMessage(
                    "\nEnter minimum age to participate on the event " +
                            "(Press Enter if there is none)\n> "
            );
            String minimumAgeString = view.getUserInput();

            minimumAge = minimumAgeString.isEmpty() ? 0 : Integer.parseInt(minimumAgeString);
        }

        return userController.createNewEvent(eventName, eventStart, eventEnd, category, postalCode, city, address,
                maxCapacity, eventLocation, description, minimumAge, isPrivateEvent, loggedInUserID);
    }
}
