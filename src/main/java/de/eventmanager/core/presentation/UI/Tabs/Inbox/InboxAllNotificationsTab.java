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
    private boolean showOnlyUnreadNotifications;

    public InboxAllNotificationsTab(View view, String loggedInUserID, boolean showOnlyUnreadNotifications) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.userService = new UserServiceImpl();
        this.showOnlyUnreadNotifications = showOnlyUnreadNotifications;
    }

    @Override
    public void start() {
        var heading = showOnlyUnreadNotifications ? "Unread Notifications" : "All Notifications";
        DefaultDialogHelper.getTabOrPageHeading(view, heading);

        var notifications = showOnlyUnreadNotifications ?
                userService.getLoggedUsersUnreadNotifications(loggedInUserID) :
                userService.getLoggedUsersNotifications(loggedInUserID);

        if (notifications.isEmpty()) {
            view.displayWarningMessage("No notifications found\n");

            return;
        }

        notifications.forEach(notification -> {
            view.displayMessage(notification);
            DefaultDialogHelper.showItemSeparator(view, DefaultDialogHelper.DEFAULT_ITEM_SEPARATOR_LENGTH);
            view.displayMessage("\n");
        });

        userService.onOpenNotificationsMarkAsRead(loggedInUserID);
    }
}
