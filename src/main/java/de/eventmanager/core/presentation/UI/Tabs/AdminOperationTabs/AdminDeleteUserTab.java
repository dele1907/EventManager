package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import helper.DatabaseSimulation.JsonDatabaseHelper;
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
        User user = JsonDatabaseHelper.getUserByEmailFromJson(email).orElse(null);

        if (user == null) {
            textView.displayErrorMessage("User not found.\n");

            return;
        }

        textView.displayMessage("\nAre you sure you want to delete " + user + "?\nType 'yes': ");
        String confirmation = textView.getUserInput();

        if ("yes".equals(confirmation.toLowerCase())) {
            boolean success = JsonDatabaseHelper.removeUserByEmailFromJson(email);

            if (success) {
                textView.displaySuccessMessage("\nUser deleted successfully.\n");
            } else {
                textView.displayErrorMessage("\nFailed to delete user. User may not exist.\n");
            }
        } else {
            textView.displayMessage("\nUser deletion canceled.\n");
        }
    }
}