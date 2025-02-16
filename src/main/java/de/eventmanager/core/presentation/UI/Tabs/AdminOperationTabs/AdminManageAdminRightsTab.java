package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.EnumHelper;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdminManageAdminRightsTab implements Tab {
    private View view;
    private UserService userService;

    private enum AdminRightsMenuChoice {
        GRANT_ADMIN_RIGHTS,
        REMOVE_ADMIN_RIGHTS,
        BACK_TO_ADMIN_OPERATIONS_MENU;

        public static Optional<AdminRightsMenuChoice> fromUserInput(String userInput) {
            return EnumHelper.getMapOfStringToEnumConstant(userInput, AdminRightsMenuChoice.class,
                Map.of(
                "1", GRANT_ADMIN_RIGHTS,
                "2", REMOVE_ADMIN_RIGHTS,
                "3", BACK_TO_ADMIN_OPERATIONS_MENU
                )
            );
        }
    }

    public AdminManageAdminRightsTab(View view) {
        this.view = view;
        this.userService = new UserServiceImpl();
    }


    @Override
    public void start() {
        var adminRightsIsActive = true;

        while (adminRightsIsActive) {
            DefaultDialogHelper.getTabOrPageHeading(view, "Manage Admin Rights");
            DefaultDialogHelper.generateMenu(view, List.of(
                "Grant Admin Rights",
                "Remove Admin Rights",
                "Back to Admin Operations Menu"
            ));
            var adminRightsMenuChoice = AdminRightsMenuChoice.fromUserInput(view.getUserInput());

            if (adminRightsMenuChoice.isEmpty()) {
                DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");
                continue;
            }

            switch (adminRightsMenuChoice.get()) {
                case GRANT_ADMIN_RIGHTS -> handleGrantAdminRights();
                case REMOVE_ADMIN_RIGHTS -> handleRemoveAdminRights();
                case BACK_TO_ADMIN_OPERATIONS_MENU -> adminRightsIsActive = false;
            }
        }
    }

    private void handleGrantAdminRights() {
        new AdminGrantAdminRightsTab(view).start();
    }

    private void handleRemoveAdminRights() {
        new AdminRemoveAdminRightsTab(view).start();
    }
}
