package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.users.User;

public class AdminDeleteUserTab implements Tab {
    private View textView;

    public AdminDeleteUserTab(View textView) {
        this.textView = textView;
    }

    @Override
    public void start() {
        textView.displayMessage("\n===== Delete User Tab ======\n");
        textView.displayMessage("\nEnter the email of the user to delete: ");
        String email = textView.getUserInput();
        User user = UserManager.readUserByEMail(email).get();

        if (user == null) {
            textView.displayErrorMessage("User not found.\n");

            return;
        }

        textView.displayWarningMessage("\n\u2757" + "\u2757" + "WARNING" + "\u2757" + "\u2757");
        textView.displayWarningMessage("\nAre you sure you want to delete: " + user + "\n\nType 'yes'> ");
        String confirmation = textView.getUserInput();

        if ("yes".equals(confirmation.toLowerCase())) {
            boolean success = UserManager.deleteUserByEmail(email);

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