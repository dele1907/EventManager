package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.ValidationHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.Optional;

public class AdminEditUserTab implements Tab {
    private View view;
    private UserController userController;
    private String userToEditEmail;
    private String loggedInUserID;
    private String editedFirstName;
    private String editedLastName;
    private String editedEmail;
    private String editedPhoneNumber;

    public AdminEditUserTab(View view, UserController userController, String loggedInUserID) {
        this.view = view;
        this.userController = userController;
        this.loggedInUserID = loggedInUserID;
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Edit User");

        if (!showFindUser()) {
            view.displayErrorMessage(DefaultDialogHelper.USER_NOT_FOUND);

            return;
        }

        displayUserDetails();
        showEditUserDialog();

        userController.editUser(
                userToEditEmail, loggedInUserID,
                editedFirstName, editedLastName,
                editedEmail, editedPhoneNumber
        );

        view.displaySuccessMessage("\nUser details updated successfully!");
    }

    private void displayUserDetails() {
        if (!userController.getUserIsPresentInDatabaseByEmail(userToEditEmail)) {
            view.displayErrorMessage(DefaultDialogHelper.USER_NOT_FOUND);

            return;
        }

        view.displayUnderlinedSubheading("\n\u001B[4mCurrent user details:\u001B[0m");
        view.displayMessage(userController.getUserInformationByEmail(userToEditEmail));
    }

    private boolean showFindUser() {
        view.displayUserInputMessage("Enter the email of the user to edit\n> ");
        userToEditEmail = view.getUserInput();

        return userController.getUserIsPresentInDatabaseByEmail(userToEditEmail);
    }

    private void showEditUserDialog() {
        DefaultDialogHelper.showEditAttributeDialog(
                view, "first name").ifPresent(attribute -> editedFirstName = attribute);
        DefaultDialogHelper.showEditAttributeDialog(
                view, "last name").ifPresent(attribute -> editedLastName = attribute);
        DefaultDialogHelper.showEditAttributeDialog(
                view, "email address").ifPresent(attribute -> editedEmail = attribute);
        handleEditPhoneNumber().ifPresent(attribute -> editedPhoneNumber = attribute);
    }

    private Optional<String> showEditUserAttributeDialog(String prompt) {
        view.displayUserInputMessage("\nEnter new " + prompt +
                "\n" + DefaultDialogHelper.BLANK_TO_KEEP +
                "\n> ");
        var userAttribute = view.getUserInput();

        if (userAttribute.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(userAttribute);
    }

    private Optional<String> handleEditPhoneNumber() {
        var newPhoneNumber = showPhoneNumberDialog();

        if (newPhoneNumber.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(newPhoneNumber);
    }

    private String showPhoneNumberDialog() {
        view.displayUserInputMessage(
                "\nEnter new phone number " +
                DefaultDialogHelper.BLANK_TO_KEEP + "\n> "
        );
        var phoneNumber = view.getUserInput();

        if (!ValidationHelper.validatePhoneNumberInput(phoneNumber)) {
            view.displayErrorMessage("\nInvalid phone number\n");
            var tryAgainChangeNumber = showEditPhoneNumberAgainDialog();
            return tryAgainChangeNumber ? showPhoneNumberDialog() : "";
        }

        return phoneNumber;
    }

    private boolean showEditPhoneNumberAgainDialog() {
        view.displayUserInputMessage("\nDo you want to edit the phone number again? (yes/any key)\n> ");
        var userChoice = view.getUserInput();

        if (userChoice.equalsIgnoreCase("yes")) {
            return true;
        }

        return false;
    }
}