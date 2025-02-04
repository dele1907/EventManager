package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.EnumHelper;
import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.AdminCreateUserTab;
import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.AdminDeleteUserTab;
import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.AdminEditUserTab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.presentation.Controller.UserController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdminOperationsTab implements Tab {
    private View view;
    private String loggedInUserID;
    private UserController userController;

    private enum AdminMenuChoice {
        EDIT_USER,
        DELETE_USER,
        CREATE_USER,
        BACK_TO_MAIN_MENU;

        public static Optional<AdminMenuChoice> fromUserInput(String userInput) {
            return EnumHelper.getMapOfStringToEnumConstant(userInput, AdminMenuChoice.class,
                Map.of(
                "1", EDIT_USER,
                "2", DELETE_USER,
                "3", CREATE_USER,
                "4", BACK_TO_MAIN_MENU
                )
            );
        }
    }

    public AdminOperationsTab(View view, String loggedInUserID, UserController userController) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.userController = userController;
    }

    @Override
    public void start() {
        boolean adminIsActive = true;

        while (adminIsActive) {
            DefaultDialogHelper.getTabOrPageHeading(view, "Admin Operations");
            DefaultDialogHelper.generateMenu(
                    view,
                    List.of("Edit user", "Delete user", "Create new user", "Back to main menu")
            );
            var adminMenuChoice = AdminMenuChoice.fromUserInput(view.getUserInput());

            if (adminMenuChoice.isEmpty()) {
                DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");
                continue;
            }

            switch (adminMenuChoice.get()) {
                case EDIT_USER -> handleEditUser();
                case DELETE_USER -> handleDeleteUser();
                case CREATE_USER -> handleCreateUser();
                case BACK_TO_MAIN_MENU -> adminIsActive = false;
            }
        }
    }

    private void handleCreateUser() {
        new AdminCreateUserTab(view, userController, loggedInUserID).start();
    }

    private void handleDeleteUser() {
        new AdminDeleteUserTab(view, userController, loggedInUserID).start();
    }

    private void handleEditUser() {
        new AdminEditUserTab(view, userController, loggedInUserID).start();
    }
}