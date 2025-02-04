package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.AdminCreateUserTab;
import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.AdminDeleteUserTab;
import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.AdminEditUserTab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.presentation.Controller.UserController;

import java.util.List;
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
            return switch (userInput) {
                case "1" -> Optional.of(EDIT_USER);
                case "2" -> Optional.of(DELETE_USER);
                case "3" -> Optional.of(CREATE_USER);
                case "4" -> Optional.of(BACK_TO_MAIN_MENU);
                default -> Optional.empty();
            };
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
            String choice = view.getUserInput();
            Optional<AdminMenuChoice> adminMenuChoice = AdminMenuChoice.fromUserInput(choice);

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