package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.EnumHelper;
import de.eventmanager.core.presentation.Service.Implementation.EventServiceImpl;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EventCreationTab implements Tab {
    private View view;
    private String loggedInUserID;

    private enum EventCreationMenuChoice {
        CREATE_PUBLIC_EVENT,
        CREATE_PRIVATE_EVENT,
        BACK_TO_EVENT_OPERATIONS;

        public static Optional<EventCreationMenuChoice> fromUserInput(String userInput) {
            return EnumHelper.getMapOfStringToEnumConstant(userInput, EventCreationMenuChoice.class,
                Map.of(
                "1", CREATE_PUBLIC_EVENT,
                "2", CREATE_PRIVATE_EVENT,
                "3", BACK_TO_EVENT_OPERATIONS
                )
            );
        }
    }

    public EventCreationTab(View view, String loggedInUserID) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
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
        var eventCreationMenuChoice = EventCreationMenuChoice.fromUserInput(view.getUserInput());

        if (eventCreationMenuChoice.isEmpty()) {
            DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");
            start();
        }

        switch (eventCreationMenuChoice.get()) {
            case CREATE_PUBLIC_EVENT -> createNewEvent(false);
            case CREATE_PRIVATE_EVENT -> createNewEvent(true);
            case BACK_TO_EVENT_OPERATIONS -> view.displayMessage("Returning to Event Operations...");
        }
    }

    private boolean createNewEvent(boolean isPrivateEvent) {
        view.displayUserInputMessage("Enter event name\n> ");
        String eventName = view.getUserInput();
        String eventStartDate = DefaultDialogHelper.showDateInputDialog(view, "\nEnter event start date", false).get();
        String eventStartTime = DefaultDialogHelper.showTimeInputDialog(view, "\nEnter event start time", false).get();
        String eventEndDate = DefaultDialogHelper.showDateInputDialog(view, "\nEnter event end date", false).get();
        String eventEndTime = DefaultDialogHelper.showTimeInputDialog(view, "\nEnter event end time", false).get();
        view.displayUserInputMessage("\nEnter event category\n> ");
        String category = view.getUserInput();
        view.displayUserInputMessage("\nEnter event postal code\n> ");
        String postalCode = view.getUserInput();
        view.displayUserInputMessage("\nEnter event address\n> ");
        String address = view.getUserInput();
        view.displayUserInputMessage("\nEnter event location\n> ");
        String eventLocation = view.getUserInput();
        view.displayUserInputMessage("\nEnter event description\n> ");
        String description = view.getUserInput();
        int maxCapacity = 0;
        int minimumAge = 0;

        if (!isPrivateEvent) {
            String enterToNotSetASpecificValue = "(Press Enter if there is none)";
            view.displayUserInputMessage("\nEnter event maximum capacity" +
                    enterToNotSetASpecificValue +
                    "\n> "
            );
            String maxCapacityInputString = view.getUserInput();
            maxCapacity = maxCapacityInputString.isEmpty() ? 9999 : Integer.parseInt(maxCapacityInputString);

            view.displayUserInputMessage(
                    "\nEnter minimum age to participate on the event " +
                            enterToNotSetASpecificValue +
                            "\n> "
            );
            String minimumAgeString = view.getUserInput();

            minimumAge = minimumAgeString.isEmpty() ? 0 : Integer.parseInt(minimumAgeString);
        }

        String eventStart = eventStartDate.concat(" ".concat(eventStartTime));
        String eventEnd = eventEndDate.concat(" ".concat(eventEndTime));

        return new EventServiceImpl().createNewEvent(eventName, eventStart, eventEnd, category, postalCode, address,
                maxCapacity, eventLocation, description, minimumAge, isPrivateEvent, loggedInUserID);
    }
}
