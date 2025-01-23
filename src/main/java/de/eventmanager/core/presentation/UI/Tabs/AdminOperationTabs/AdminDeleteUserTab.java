package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultMessagesHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.Optional;

public class AdminDeleteUserTab implements Tab {
    private View textView;
    private UserController userController;

    public AdminDeleteUserTab(View textView, UserController userController) {
        this.textView = textView;
        this.userController = userController;
    }

    @Override
    public void start() {
        textView.displayTabOrPageHeading("\n===== Delete User Tab ======");
        textView.displayMessage("Enter the email of the user to delete: ");
        String email = textView.getUserInput();

        var userOptional = userController.getUserByEmail(email);

        if (userOptional.isEmpty()) {
            textView.displayErrorMessage(DefaultMessagesHelper.USER_NOT_FOUND);

            return;
        }

        textView.displayWarningMessage(DefaultMessagesHelper.WARNING_MESSAGE);
        textView.displayWarningMessage("\nAre you sure you want to delete: " + userOptional + "\n\nType 'yes'> ");
        String confirmation = textView.getUserInput();

        if ("yes".equals(confirmation.toLowerCase())) {
            boolean success = userController.deleteUser(userOptional.get());

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