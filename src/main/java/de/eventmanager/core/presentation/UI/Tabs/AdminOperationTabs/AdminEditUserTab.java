package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.ValidationHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;

import java.util.Optional;

public class AdminEditUserTab implements Tab {
    private View textView;
    private UserController userController;
    private String userToEditEmail;
    private String loggedInUserID;
    private String editedFirstName;
    private String editedLastName;
    private String editedEmail;
    private String editedPhoneNumber;

    public AdminEditUserTab(View textView, UserController userController, String loggedInUserID) {
        this.textView = textView;
        this.userController = userController;
        this.loggedInUserID = loggedInUserID;
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(textView, "Edit User");

        if (!showFindUser()) {
            textView.displayErrorMessage(DefaultDialogHelper.USER_NOT_FOUND);

            return;
        }
        showEditUserDialog();
        userController.editUser(
                userToEditEmail, loggedInUserID,
                editedFirstName, editedLastName,
                editedEmail, editedPhoneNumber
        );

        textView.displaySuccessMessage("\nUser details updated successfully!");
    }

    private void displayUserDetails() {
        if (!userController.getUserIsPresentInDatabaseByEmail(userToEditEmail)) {
            textView.displayErrorMessage(DefaultDialogHelper.USER_NOT_FOUND);

            return;
        }

        textView.displayUnderlinedSubheading("\n\u001B[4mCurrent user details:\u001B[0m");
        textView.displayMessage(userController.getUserInformationByEmail(userToEditEmail));
    }

    private boolean showFindUser() {
        textView.displayUserInputMessage("Enter the email of the user to edit\n> ");
        userToEditEmail = textView.getUserInput();

        return userController.getUserIsPresentInDatabaseByEmail(userToEditEmail);
    }

    private void showEditUserDialog() {
        displayUserDetails();
        showEditFirstname();
        showEditLastname();
        showEditEmailAddress();
        handleEditPhoneNumber();
    }

    private void showEditFirstname() {
        textView.displayUserInputMessage(
                "\nEnter new first name " +
                "(" + DefaultDialogHelper.BLANK_TO_KEEP + ")\n> "
        );
        String newFirstName = textView.getUserInput();

        if (newFirstName.isEmpty()) {
            return;
        }

        editedFirstName = newFirstName;
    }

    private void showEditLastname() {
        textView.displayUserInputMessage(
                "\nEnter new last name " +
                "(" + DefaultDialogHelper.BLANK_TO_KEEP + ")\n> "
        );

        String newLastName = textView.getUserInput();

        if (newLastName.isEmpty()) {
            return;
        }

        editedLastName = newLastName;
    }

    private void showEditEmailAddress() {
        textView.displayUserInputMessage(
                "\nEnter new email address " +
                "(" + DefaultDialogHelper.BLANK_TO_KEEP + ")\n> "
        );

        String newEmail = textView.getUserInput();

        if (newEmail.isEmpty()) {
            return;
        }

        editedEmail = newEmail;
    }

    private void handleEditPhoneNumber() {
        var newPhoneNumber = showPhoneNumberDialog();

        if (newPhoneNumber.isEmpty()) {
            return;
        }

        editedPhoneNumber = newPhoneNumber;
    }

    private String showPhoneNumberDialog() {
        textView.displayUserInputMessage(
                "\nEnter new phone number " +
                "(" + DefaultDialogHelper.BLANK_TO_KEEP + ")\n> "
        );
        var phoneNumber = textView.getUserInput();

        if (!ValidationHelper.validatePhoneNumberInput(phoneNumber)) {
            textView.displayErrorMessage("\nInvalid phone number\n");
            var tryAgainChangeNumber = showEditPhoneNumberAgainDialog();
            return tryAgainChangeNumber ? showPhoneNumberDialog() : "";
        }

        return phoneNumber;
    }

    private boolean showEditPhoneNumberAgainDialog() {
        textView.displayUserInputMessage("\nDo you want to edit the phone number again? (yes/any key)\n> ");
        String userChoice = textView.getUserInput();

        if (userChoice.equalsIgnoreCase("yes")) {
            return true;
        }

        return false;
    }
}