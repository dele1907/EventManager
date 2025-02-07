package de.eventmanager.core.presentation.UI.Tabs.UserEventInteraction;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.EnumHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EventOperationsTab implements Tab {
    private View view;
    private String loggedInUserID;
    private UserController userController;

    private enum EventOperationsChoice {
        CREATE_NEW_EVENT,
        SHOW_EVENTS,
        BOOK_EVENT,
        CANCEL_EVENT,
        ADD_USER_TO_EVENT,
        EDIT_EVENT,
        BACK_TO_MAIN_MENU;

        public static Optional<EventOperationsChoice> fromUserInput(String userInput) {
            return EnumHelper.getMapOfStringToEnumConstant(userInput, EventOperationsChoice.class,
                Map.of(
                "1", CREATE_NEW_EVENT,
                "2", SHOW_EVENTS,
                "3", BOOK_EVENT,
                "4", CANCEL_EVENT,
                "5", ADD_USER_TO_EVENT,
                "6", EDIT_EVENT,
                "7", BACK_TO_MAIN_MENU
                )
            );
        }
    }

    public EventOperationsTab(View view, String loggedInUserID, UserController userController) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.userController = userController;
    }

    @Override
    public void start() {
        boolean eventOperationIsActive = true;

        while (eventOperationIsActive) {
            DefaultDialogHelper.getTabOrPageHeading(view, "Event Operations");
            handleMenuGeneration();
            var eventOperationsChoice = EventOperationsChoice.fromUserInput(view.getUserInput());

            if (eventOperationsChoice.isEmpty()) {
                DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");
                continue;
            }

            switch (eventOperationsChoice.get()) {
                case CREATE_NEW_EVENT -> handleCreateEvent();
                case SHOW_EVENTS -> handleShowEvents();
                case BOOK_EVENT -> handleBookEvent();
                case CANCEL_EVENT -> handleCancelEvent();
                case ADD_USER_TO_EVENT -> view.displayErrorMessage("Not implemented yet.");
                case EDIT_EVENT -> handleEditEvent();
                case BACK_TO_MAIN_MENU -> eventOperationIsActive = false;
            }
        }
    }

    private void handleCreateEvent() {
      new EventCreationTab(view, loggedInUserID, userController).start();
    }

    private void handleShowEvents() {
        new ShowEventsTab(view, userController, loggedInUserID).start();
    }

    private void handleBookEvent() {
        new EventBookingTab(view, loggedInUserID, userController).start();
    }

    private void handleCancelEvent() {
        new EventCancelParticipationTab(view, loggedInUserID, userController).start();
    }

    private void handleEditEvent() {
        new EventEditTab(view, loggedInUserID, userController).start();
    }

    private void handleMenuGeneration() {
        DefaultDialogHelper.generateMenu(
                view,
                List.of(
                        "Create new event",
                        "Show events",
                        "Book event",
                        "Cancel participation in event",
                        "Add a user to an event",
                        "Edit event's information",
                        "Back to main menu"
                )
        );
    }
}
