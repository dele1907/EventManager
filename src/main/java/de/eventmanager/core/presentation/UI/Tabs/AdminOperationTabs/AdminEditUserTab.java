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

    public AdminEditUserTab(View textView, UserController userController) {
        this.textView = textView;
        this.userController = userController;
    }

    @Override
    public void start() {
        textView.displayTabOrPageHeading("\n===== Edit User Tab =====");

        Optional<User> userOptional = showFindUser();

        if (userOptional.isEmpty()) {
            textView.displayErrorMessage(DefaultDialogHelper.USER_NOT_FOUND);

            return;
        }

        User user = userOptional.get();

        showEditUserDialog(user);
        userController.editUser(user);
        textView.displaySuccessMessage("\nUser details updated successfully!");
    }

    private void displayUserDetails(User user) {
        textView.displayUnderlinedSubheading("\n\u001B[4mCurrent user details:\u001B[0m");
        textView.displayMessage("\nFirst Name: " + user.getFirstName());
        textView.displayMessage("\nLast Name: " + user.getLastName());
        textView.displayMessage("\nEmail Address: " + user.getEMailAddress());
        textView.displayMessage("\nPhone Number: " + user.getPhoneNumber() + "\n");
    }

    private Optional<User> showFindUser() {
        textView.displayUserInputMessage("Enter the email of the user to edit\n> ");
        String email = textView.getUserInput();
        Optional<User> userOptional = userController.getUserByEmail(email);

        return userOptional.isPresent() ? userOptional : Optional.empty();
    }

    private void showEditUserDialog(User user) {
        displayUserDetails(user);
        showEditFirstname(user);
        showEditLastname(user);
        showEditEmailAddress(user);
        handleEditPhoneNumber(user);
    }

    private void showEditFirstname(User user) {
        textView.displayUserInputMessage("\nEnter new first name (leave blank to keep current)\n> ");
        String newFirstName = textView.getUserInput();

        if (newFirstName.isEmpty()) {
            return;
        }

        user.setFirstName(newFirstName);
    }

    private void showEditLastname(User user) {
        textView.displayUserInputMessage("\nEnter new last name (leave blank to keep current)\n> ");
        String newLastName = textView.getUserInput();

        if (newLastName.isEmpty()) {
            return;
        }

        user.setLastName(newLastName);
    }

    private void showEditEmailAddress(User user) {
        textView.displayUserInputMessage("\nEnter new email address (leave blank to keep current)\n> ");
        String newEmail = textView.getUserInput();

        if (newEmail.isEmpty()) {
            return;
        }

        user.seteMailAddress(newEmail);
    }

    private void handleEditPhoneNumber(User user) {
        var newPhoneNumber = showPhoneNumberDialog();

        if (newPhoneNumber.isEmpty()) {
            return;
        }

        user.setPhoneNumber(newPhoneNumber);
    }

    private String showPhoneNumberDialog() {
        textView.displayUserInputMessage("\nEnter new phone number (leave blank to keep current)\n> ");
        var phoneNumber = textView.getUserInput();

        if (!ValidationHelper.checkPhoneNumber(phoneNumber)) {
            textView.displayErrorMessage("\nInvalid phone number\n");
            var tryAgainChangeNumber = showEditPhoneNumberAgainDialog();
            return tryAgainChangeNumber ? showPhoneNumberDialog() : "";
        }

        return phoneNumber;
    }

    private boolean showEditPhoneNumberAgainDialog() {
        textView.displayUserInputMessage("\nDo you want to edit the phone number again? (yes/no)\n> ");
        String userChoice = textView.getUserInput();

        if (userChoice.equalsIgnoreCase("yes")) {
            return true;
        }

        return false;
    }
}