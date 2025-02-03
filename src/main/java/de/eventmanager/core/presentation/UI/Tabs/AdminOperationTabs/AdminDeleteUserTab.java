package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class AdminDeleteUserTab implements Tab {
    private View textView;
    private UserController userController;
    private String loggedInUserUserID;

    public AdminDeleteUserTab(View textView, UserController userController, String loggedInUserUserID) {
        this.textView = textView;
        this.userController = userController;
        this.loggedInUserUserID = loggedInUserUserID;
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(textView, "Delete User");
        textView.displayUserInputMessage("Enter the email of the user to delete\n> ");
        String email = textView.getUserInput();

        String userInformationUserToDelete = userController.getUserInformationByEmail(email);

        if (userInformationUserToDelete.isEmpty()) {
            textView.displayErrorMessage(DefaultDialogHelper.USER_NOT_FOUND);

            return;
        }

        if (!showConfirmationDialog(userInformationUserToDelete).equalsIgnoreCase("yes")) {
            textView.displayErrorMessage("\nUser deletion canceled.\n");

            return;
        }

        showUserDeletionSuccess(email);
    }

    private String showConfirmationDialog(String userInformationUserToDelete) {
        textView.displayWarningMessage(DefaultDialogHelper.WARNING_MESSAGE);
        textView.displayWarningMessage("\nAre you sure you want to delete: " );
        textView.displayUnderlinedSubheading("User Information: \n");
        textView.displayMessage(userInformationUserToDelete);
        textView.displayUserInputMessage("\n\nType 'yes'> ");

        return textView.getUserInput();
    }

    private void showUserDeletionSuccess(String emailUserToDelete) {
        boolean success = userController.deleteUser(emailUserToDelete, loggedInUserUserID);

        if (success) {
            textView.displaySuccessMessage("\nUser deleted successfully.\n");
        } else {
            textView.displayErrorMessage("\nFailed to delete user. User may not exist.\n");
        }
    }
}