package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class AdminDeleteUserTab implements Tab {
    private View view;
    private String loggedInUserUserID;
    private UserService userService;

    public AdminDeleteUserTab(View view, String loggedInUserUserID) {
        this.view = view;
        this.loggedInUserUserID = loggedInUserUserID;
        this.userService = new UserServiceImpl();
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Delete User");
        view.displayUserInputMessage("Enter the email of the user to delete\n> ");
        String email = view.getUserInput();

        String userInformationUserToDelete = userService.getUserInformationByEmail(email);


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

        if (userService.deleteUser(emailUserToDelete, loggedInUserUserID)) {
            view.displaySuccessMessage("\nUser deleted successfully.\n");
        } else {
            view.displayErrorMessage("\nFailed to delete user. User may not exist.\n");
        }
    }
}