package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.AdminCreateUserTab;
import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.AdminDeleteUserTab;
import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.AdminEditUserTab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;
import de.eventmanager.core.presentation.Controller.UserController;

import java.util.List;

public class AdminOperationsTab implements Tab {
    private View textView;
    private String loggedInUserID;
    private UserController userController;

    public AdminOperationsTab(View textView, String loggedInUserID, UserController userController) {
        this.textView = textView;
        this.loggedInUserID = loggedInUserID;
        this.userController = userController;
    }

    @Override
    public void start() {
        boolean adminIsActive = true;
        
        while (adminIsActive) {
            DefaultDialogHelper.getTabOrPageHeading(textView, "Admin Operations");
            DefaultDialogHelper.generateMenu(
                textView,
                List.of("Edit user", "Delete user", "Create new user", "Back to main menu")
            );
            String choice = textView.getUserInput();

            switch (choice) {
                case "1":
                    handleEditUser();
                    break;
                case "2":
                    handleDeleteUser();
                    break;
                case "3":
                    handleCreateUser();
                    break;
                case "4":
                    adminIsActive = false;
                    break;
                default:
                    textView.displayErrorMessage("\nInvalid choice");
                    break;
            }
        }
    }

    private void handleCreateUser() {
        AdminCreateUserTab createUserTab = new AdminCreateUserTab(textView, userController, loggedInUserID);
        createUserTab.start();
    }

    private void handleDeleteUser() {
        AdminDeleteUserTab deleteUserTab = new AdminDeleteUserTab(textView, userController, loggedInUserID);
        deleteUserTab.start();
    }

    private void handleEditUser() {
        AdminEditUserTab editUserTab = new AdminEditUserTab(textView, userController, loggedInUserID);
        editUserTab.start();
    }


}