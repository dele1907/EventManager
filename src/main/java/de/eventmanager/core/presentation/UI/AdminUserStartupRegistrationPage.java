package de.eventmanager.core.presentation.UI;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationData;
import de.eventmanager.core.presentation.PresentationHelpers.ValidationHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import helper.ConfigurationDataSupplierHelper;

public class AdminUserStartupRegistrationPage implements Tab {
    private View textView;
    private UserController userController;

    public AdminUserStartupRegistrationPage(View textView, UserController userController) {
        this.textView = textView;
        this.userController = userController;
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(textView, "Admin User Registration");

        textView.displayMessage(
                "There is no admin user present for your system." +
                "\nWould you like to create one?"
        );
        textView.displayUserInputMessage("\n\nType 'yes' to create an admin user or 'no' to exit\n> ");
        String choice = textView.getUserInput();

        if ("yes".equals(choice.toLowerCase())) {
            showCreateAdminUserDialog();
        } else {
            textView.displayErrorMessage("\nChanging to login and registration page...");
        }
    }

    private void showCreateAdminUserDialog() {
        String[] prompts = getPromptsForDialogs();
        String[] userInputs = new String[prompts.length];

        for (int i = 0; i < prompts.length; i++) {
            userInputs[i] = getUserInputWithValidation(prompts[i], i);

            if (userInputs[i].isEmpty()) {
                textView.displayErrorMessage("\n Aborting user creation\n Changing to login and registration page...");
                return;
            }
        }

        UserRegistrationData userRegistrationData = new UserRegistrationData(
                userInputs[0], userInputs[1], userInputs[2],
                userInputs[3], userInputs[4], userInputs[5], userInputs[6]
        );

        userController.createNewAdminUser(userRegistrationData, ConfigurationDataSupplierHelper.REGISTER_NEW_USER_ID);
    }

    private String getUserInputWithValidation(String prompt, int index) {
        while (true) {
            String input = getUserInput(prompt);

            if (input.isEmpty()) {
                return "";
            }

            if (isValidInput(index, input)) {
                return input;
            }
        }
    }

    private boolean isValidInput(int index, String input) {
        switch (index) {
            case 2:
                if (!isValidBirthDate(input)) {
                    return false;
                }
                break;
            case 3:
                if (!ValidationHelper.validateEmailInput(input)) {
                    textView.displayErrorMessage("\nInvalid email format. Please try again.\n");

                    return false;
                }
                break;
            case 4:
                if (!ValidationHelper.validatePhoneNumberInput(input)) {
                    textView.displayErrorMessage("\nInvalid phone number format. Please try again.\n");

                    return false;
                }
                break;
        }
        return true;
    }

    private boolean isValidBirthDate(String birthDate) {
        if (!ValidationHelper.validateDateInput(birthDate)) {
            textView.displayErrorMessage("Invalid date format. Please try again.\n");

            return false;
        }

        if (!ValidationHelper.validateAge(birthDate)) {
            textView.displayErrorMessage("" +
                    "Invalid age!\n" +
                    "Your age must be between 12 and 130 years.\n"
            );

            return false;
        }

        return true;
    }

    private String getUserInput(String prompt) {
        textView.displayUserInputMessage(prompt);
        return textView.getUserInput();
    }

    private String[] getPromptsForDialogs() {
        return new String[]{
                "\nEnter first name (leave blank to cancel Account creation)\n> ",
                "Enter last name (leave blank to cancel Account creation)\n> ",
                "Enter date of birth (provide format yyyy-mm-dd OR leave blank to cancel Account creation)\n> ",
                "Enter email (leave blank to cancel Account creation)\n> ",
                "Enter phone number (leave blank to cancel Account creation)\n> ",
                "Enter password (leave blank to cancel Account creation)\n> ",
                "Confirm password (leave blank to cancel Account creation)\n> "
        };
    }
}