package de.eventmanager.core.presentation.UI.Tabs.Inbox;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class InboxUnreadNotificationsTab implements Tab {
    private View view;
    private String loggedInUserID;
    private UserService userService;

    public InboxUnreadNotificationsTab(View view, String loggedInUserID) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.userService = new UserServiceImpl();
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Unread Notifications");

        var unreadNotifications = userService.getLoggedUsersUnreadNotifications(loggedInUserID);

        if (unreadNotifications.isEmpty()) {
            view.displayWarningMessage("No unread notifications found\n");

            return;
        }

        unreadNotifications.forEach(notification -> {
            view.displayMessage(notification);
            DefaultDialogHelper.showItemSeparator(view, DefaultDialogHelper.DEFAULT_ITEM_SEPARATOR_LENGTH);
            view.displayMessage("\n");
        });

        userService.onOpenNotificationsMarkAsRead(loggedInUserID);
    }
}
