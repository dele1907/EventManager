package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class AdminDeleteUserTab implements Tab {
    private View view;
    private UserController userController;
    private String loggedInUserUserID;

    public AdminDeleteUserTab(View view, UserController userController, String loggedInUserUserID) {
        this.view = view;
        this.userController = userController;
        this.loggedInUserUserID = loggedInUserUserID;
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Delete User");
        view.displayUserInputMessage("Enter the email of the user to delete\n> ");
        String email = view.getUserInput();

        String userInformationUserToDelete = userController.getUserInformationByEmail(email);

        if (userInformationUserToDelete.isEmpty()) {
            view.displayErrorMessage(DefaultDialogHelper.USER_NOT_FOUND);

            return;
        }

        if (!showConfirmationDialog(userInformationUserToDelete).equalsIgnoreCase("yes")) {
            view.displayErrorMessage("\nUser deletion canceled.\n");

            return;
        }

        showUserDeletionSuccess(email);
    }

    private String showConfirmationDialog(String userInformationUserToDelete) {
        view.displayWarningMessage(DefaultDialogHelper.WARNING_MESSAGE);
        view.displayWarningMessage("\nAre you sure you want to delete: " );
        view.displayUnderlinedSubheading("User Information: \n");
        view.displayMessage(userInformationUserToDelete);
        view.displayUserInputMessage("\n\nType 'yes'> ");

        return view.getUserInput();
    }

    private void showUserDeletionSuccess(String emailUserToDelete) {
        boolean success = userController.deleteUser(emailUserToDelete, loggedInUserUserID);

        if (success) {
            view.displaySuccessMessage("\nUser deleted successfully.\n");
        } else {
            view.displayErrorMessage("\nFailed to delete user. User may not exist.\n");
        }
    }
}