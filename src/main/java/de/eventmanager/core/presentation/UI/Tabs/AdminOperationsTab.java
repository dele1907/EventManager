package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.AdminCreateUserTab;
import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.AdminDeleteUserTab;
import de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs.AdminEditUserTab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;
import de.eventmanager.core.presentation.Controller.UserController;

public class AdminOperationsTab implements Tab {
    private View textView;
    private User loggedInUser;
    private UserController userController;

    public AdminOperationsTab(View textView, User loggedInUser, UserController userController) {
        this.textView = textView;
        this.loggedInUser = loggedInUser;
        this.userController = userController;
    }

    @Override
    public void start() {
        boolean adminIsActive = true;
        while (adminIsActive) {
            textView.displayMessage("\n====== Admin Operations ======");
            textView.displayMessage("\n1. Edit user");
            textView.displayMessage("\n2. Delete user");
            textView.displayMessage("\n3. Create new user");
            textView.displayMessage("\n4. Back to main menu");
            textView.displayMessage("\nChoose an option: ");
            String choice = textView.getUserInput();

            switch (choice) {
                case "1":
                    AdminEditUserTab editUserTab = new AdminEditUserTab();
                    editUserTab.start();
                    break;
                case "2":
                    AdminDeleteUserTab deleteUserTab = new AdminDeleteUserTab(textView);
                    deleteUserTab.start();
                    break;
                case "3":
                    AdminCreateUserTab createUserTab = new AdminCreateUserTab(textView, userController);
                    createUserTab.start();
                    break;
                case "4":
                    adminIsActive = false;
                    break;
                default:
                    textView.displayMessage("\nInvalid choice");
                    break;
            }
        }
    }
}