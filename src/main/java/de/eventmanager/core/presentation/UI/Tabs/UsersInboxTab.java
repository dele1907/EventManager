package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.EnumHelper;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.Inbox.InboxAllNotificationsTab;
import de.eventmanager.core.presentation.UI.Tabs.Inbox.InboxUnreadNotificationsTab;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UsersInboxTab implements Tab {
    private View view;
    private String loggedInUserID;
    private UserService userService;

    private enum InboxMenuOptions {
        SHOW_UNREAD_NOTIFICATIONS,
        SHOW_ALL_NOTIFICATIONS,
        EMPTY_INBOX,
        BACK;

        public static Optional<InboxMenuOptions> fromUserInput(String userInput) {
            return EnumHelper.getMapOfStringToEnumConstant(userInput, InboxMenuOptions.class,
                Map.of(
                "1", SHOW_UNREAD_NOTIFICATIONS,
                "2", SHOW_ALL_NOTIFICATIONS,
                "3", EMPTY_INBOX,
                "4", BACK
                )
            );
        }
    }

    public UsersInboxTab(View view, String loggedInUserID) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        userService = new UserServiceImpl();
    }

    @Override
    public void start() {
        var inboxIsActive = true;

        while (inboxIsActive) {
            DefaultDialogHelper.getTabOrPageHeading(view, "Inbox");
            DefaultDialogHelper.generateMenu(view, List.of(
                "Show Unread Notifications",
                "Show All Notifications",
                "Empty Inbox",
                "Back"
            ));
            var inboxMenuOption = InboxMenuOptions.fromUserInput(view.getUserInput());

            if (inboxMenuOption.isEmpty()) {
                DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");
                continue;
            }

            switch (inboxMenuOption.get()) {
                case SHOW_UNREAD_NOTIFICATIONS -> handleShowUnreadNotifications();
                case SHOW_ALL_NOTIFICATIONS -> handleShowAllNotifications();
                case EMPTY_INBOX -> handleEmptyInbox();
                case BACK -> inboxIsActive = false;
            }
        }
    }

    private void handleShowUnreadNotifications() {
        new InboxUnreadNotificationsTab(view, loggedInUserID).start();
    }

    private void handleShowAllNotifications() {
        new InboxAllNotificationsTab(view, loggedInUserID).start();
    }

    private void handleEmptyInbox() {
        view.displayUserInputMessage("Are you sure you want to empty your inbox? (yes/any key)\n> ");

        if (!view.getUserInput().equalsIgnoreCase("yes")) {
            view.displayWarningMessage("\nInbox not emptied\n");

            return;
        }

        userService.emptyUsersNotifications(loggedInUserID);

        view.displaySuccessMessage("\nInbox emptied\n");
    }
}
