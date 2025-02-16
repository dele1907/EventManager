package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.ValidationHelper;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

import java.util.Optional;

public class AdminEditUserTab implements Tab {
    private View view;
    private String userToEditEmail;
    private String loggedInUserID;
    private String editedFirstName;
    private String editedLastName;
    private String editedEmail;
    private String editedPhoneNumber;
    private UserService userService;

    public AdminEditUserTab(View view, String loggedInUserID) {
        this.view = view;
        this.loggedInUserID = loggedInUserID;
        this. userService = new UserServiceImpl();
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

        userService.editUser(
                userToEditEmail, loggedInUserID,
                editedFirstName, editedLastName,
                editedEmail, editedPhoneNumber
        );

        view.displaySuccessMessage("\nUser details updated successfully!");
    }

    private void displayUserDetails() {
        if (!userService.getUserIsPresentInDatabaseByEmail(userToEditEmail)) {
            view.displayErrorMessage(DefaultDialogHelper.USER_NOT_FOUND);

            return;
        }

        view.displayUnderlinedSubheading("\n\u001B[4mCurrent user details:\u001B[0m");
        view.displayMessage(userService.getUserInformationByEmail(userToEditEmail));
    }

    private boolean showFindUser() {
        view.displayUserInputMessage("Enter the email of the user to edit\n> ");
        userToEditEmail = view.getUserInput();

        return userService.getUserIsPresentInDatabaseByEmail(userToEditEmail);
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

    private Optional<String> handleEditPhoneNumber() {
        var newPhoneNumber = showPhoneNumberDialog();

        return newPhoneNumber.isEmpty() ? Optional.empty() : Optional.of(newPhoneNumber);
    }

    private String showPhoneNumberDialog() {
        view.displayUserInputMessage(
                "\nEnter new phone number " +
                DefaultDialogHelper.BLANK_TO_KEEP + "\n> "
        );
        var phoneNumber = view.getUserInput();

        if (!ValidationHelper.validatePhoneNumberInput(phoneNumber)) {
            DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "phone number format");

            return showEditPhoneNumberAgainDialog() ? showPhoneNumberDialog() : "";
        }

        return phoneNumber;
    }

    private boolean showEditPhoneNumberAgainDialog() {
        view.displayUserInputMessage(
                "\nDo you want to edit the phone number again? " +
                DefaultDialogHelper.ACCEPT_OR_ABORT_MESSAGE
        );

        if ( view.getUserInput().equalsIgnoreCase("yes")) {
            return true;
        }

        return false;
    }
}