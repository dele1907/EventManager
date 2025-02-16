package de.eventmanager.core.presentation.UI.Tabs.Inbox;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class InboxAllNotificationsTab implements Tab {
    private View view;
    private String loggedInUserID;
    private UserService userService;

    public InboxAllNotificationsTab(View view, String loggedInUserID) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.userService = new UserServiceImpl();
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "All Notifications");

        var allNotifications = userService.getLoggedUsersNotifications(loggedInUserID);

        if (allNotifications.isEmpty()) {
            view.displayWarningMessage("No notifications found\n");

            return;
        }

        allNotifications.forEach(notification -> {
            view.displayMessage(notification);
            DefaultDialogHelper.showItemSeparator(view, DefaultDialogHelper.DEFAULT_ITEM_SEPARATOR_LENGTH);
            view.displayMessage("\n");
        });

        userService.onOpenNotificationsMarkAsRead(loggedInUserID);
    }
}
