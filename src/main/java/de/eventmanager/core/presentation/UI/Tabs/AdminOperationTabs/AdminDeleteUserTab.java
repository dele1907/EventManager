package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

public class AdminDeleteUserTab implements Tab {
    private View textView;
    private UserController userController;

    public AdminDeleteUserTab(View textView, UserController userController) {
        this.textView = textView;
        this.userController = userController;
    }

    @Override
    public void start() {
        textView.displayMessage("\n===== Delete User Tab ======\n");
        textView.displayMessage("\nEnter the email of the user to delete: ");
        String email = textView.getUserInput();
        User user = userController.getUserByEmail(email).get();

        if (user == null) {
            textView.displayErrorMessage("User not found.\n");

            return;
        }

        textView.displayWarningMessage("\n\u2757" + "\u2757" + "WARNING" + "\u2757" + "\u2757");
        textView.displayWarningMessage("\nAre you sure you want to delete: " + user + "\n\nType 'yes'> ");
        String confirmation = textView.getUserInput();

        if ("yes".equals(confirmation.toLowerCase())) {
            boolean success = userController.deleteUser(userController.getUserByEmail(email).get());

            if (success) {
                textView.displaySuccessMessage("\nUser deleted successfully.\n");
            } else {
                textView.displayErrorMessage("\nFailed to delete user. User may not exist.\n");
            }
        } else {
            textView.displayErrorMessage("\nUser deletion canceled.\n");
        }
    }
}