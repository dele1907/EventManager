package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.EnumHelper;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.*;
import de.eventmanager.core.presentation.UI.View;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdminOperationsTab implements Tab {
    private View view;
    private String loggedInUserID;
    private UserService userService;

    private enum AdminMenuChoice {
        SHOW_USER_INFORMATION,
        EDIT_USER,
        DELETE_USER,
        CREATE_USER,
        GRANT_OR_REMOVE_ADMIN_RIGHTS,
        BACK_TO_MAIN_MENU;

        public static Optional<AdminMenuChoice> fromUserInput(String userInput) {
            return EnumHelper.getMapOfStringToEnumConstant(userInput, AdminMenuChoice.class,
                Map.of(
                "1", SHOW_USER_INFORMATION,
                "2", EDIT_USER,
                "3", DELETE_USER,
                "4", CREATE_USER,
                "5", GRANT_OR_REMOVE_ADMIN_RIGHTS,
                "6", BACK_TO_MAIN_MENU
                )
            );
        }
    }

    public AdminOperationsTab(View view, String loggedInUserID) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this.userService = new UserServiceImpl();
    }

    @Override
    public void start() {
        var adminIsActive = true;

        while (adminIsActive) {
            DefaultDialogHelper.getTabOrPageHeading(view, "Admin Operations");
            DefaultDialogHelper.generateMenu(
                    view,
                    List.of(
                            "Show user information",
                            "Edit user",
                            "Delete user",
                            "Create new user",
                            "Manage admin rights for user",
                            "Back to main menu")
            );
            var adminMenuChoice = AdminMenuChoice.fromUserInput(view.getUserInput());

            if (adminMenuChoice.isEmpty()) {
                DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");
                continue;
            }

            switch (adminMenuChoice.get()) {
                case SHOW_USER_INFORMATION -> handleShowUserInformation();
                case EDIT_USER -> handleEditUser();
                case DELETE_USER -> handleDeleteUser();
                case CREATE_USER -> handleCreateUser();
                case GRANT_OR_REMOVE_ADMIN_RIGHTS -> manageAdminRights();
                case BACK_TO_MAIN_MENU -> adminIsActive = false;
            }
        }
    }

    private void handleCreateUser() {
        new AdminCreateUserTab(view, loggedInUserID).start();
    }

    private void handleDeleteUser() {
        new AdminDeleteUserTab(view, loggedInUserID).start();
    }

    private void handleEditUser() {
        new AdminEditUserTab(view, loggedInUserID).start();
    }

    private void manageAdminRights() {
        new AdminManageAdminRightsTab(view).start();
    }

    private void handleShowUserInformation() {
        view.displayUserInputMessage("\nEnter the e-Mail address of the user you want to show information about\n> ");
        var email = view.getUserInput();

        if (!userService.getUserIsPresentInDatabaseByEmail(email)) {
            view.displayErrorMessage("\n" + DefaultDialogHelper.USER_NOT_FOUND + "\n");

            return;
        }

        view.displayMessage(userService.getUserInformationByEmail(email));
        DefaultDialogHelper.showItemSeparator(view, DefaultDialogHelper.DEFAULT_ITEM_SEPARATOR_LENGTH);
    }
}