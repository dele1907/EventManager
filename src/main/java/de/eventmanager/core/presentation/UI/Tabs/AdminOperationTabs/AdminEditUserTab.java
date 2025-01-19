package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;
import helper.DatabaseSimulation.JsonDatabaseHelper;

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
        textView.displayMessage("\n===== Edit User Tab =====");

        Optional<User> userOptional = showFindUser();

        if (userOptional.isEmpty()) {
            textView.displayErrorMessage("\nUser not found.\n");

            return;
        }

        User user = userOptional.get();

        showEditUserDialog(user);
        userController.editUser(user);
        textView.displayMessage("\nUser details updated successfully!");
    }

    private void displayUserDetails(User user) {
        textView.displayMessage("\nCurrent user details:");
        textView.displayMessage("\nFirst Name: " + user.getFirstName());
        textView.displayMessage("\nLast Name: " + user.getLastName());
        textView.displayMessage("\nEmail Address: " + user.getEMailAddress());
        textView.displayMessage("\nPhone Number: " + user.getPhoneNumber() + "\n");
    }

    private Optional<User> showFindUser() {
        textView.displayMessage("\nEnter the email of the user to edit: ");
        String email = textView.getUserInput();

        Optional<User> userOptional = JsonDatabaseHelper.getUserByEmailFromJson(email);

        return userOptional.isPresent() ? userOptional : Optional.empty();
    }

    private void showEditUserDialog(User user) {
        displayUserDetails(user);
        showEditFirstname(user);
        showEditLastname(user);
        showEditEmailAddress(user);
        showEditPhoneNumber(user);
    }

    private void showEditFirstname(User user) {
        textView.displayMessage("\nEnter new first name (leave blank to keep current): ");
        String newFirstName = textView.getUserInput();

        if (newFirstName.isEmpty()) {
            return;
        }

        user.setFirstName(newFirstName);
    }

    private void showEditLastname(User user) {
        textView.displayMessage("\nEnter new last name (leave blank to keep current): ");
        String newLastName = textView.getUserInput();

        if (newLastName.isEmpty()) {
            return;
        }

        user.setLastName(newLastName);
    }

    private void showEditEmailAddress(User user) {
        textView.displayMessage("\nEnter new email address (leave blank to keep current): ");
        String newEmail = textView.getUserInput();

        if (newEmail.isEmpty()) {
            return;
        }

        user.seteMailAddress(newEmail);
    }

    private void showEditPhoneNumber(User user) {
        textView.displayMessage("\nEnter new phone number (leave blank to keep current): ");
        String newPhoneNumberInput = textView.getUserInput();

        if (newPhoneNumberInput.isEmpty()) {
            return;
        }

        try {
            int newPhoneNumber = Integer.parseInt(newPhoneNumberInput);
            user.setPhoneNumber(newPhoneNumber);

        } catch (NumberFormatException e) {
            textView.displayErrorMessage("\nInvalid phone number format. Please enter a valid number.");
            showEditPhoneNumber(user);
        }
    }
}