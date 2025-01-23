package de.eventmanager.core.presentation.UI;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationData;
import de.eventmanager.core.presentation.UI.Tabs.Tab;

public class AdminUserStartupRegistrationPage implements Tab {
    private View textView;
    private UserController userController;

    public AdminUserStartupRegistrationPage(View textView, UserController userController) {
        this.textView = textView;
        this.userController = userController;

    }

    @Override
    public void start() {
        textView.displayMessage("\n===== Admin User Startup Registration Page ======\n");
        textView.displayMessage("\nThere is no admin user present for your system." +
                "\nWould you like to create one?" +
                "\nType 'yes' to create an admin user or 'no' to exit: ");
        String choice = textView.getUserInput();

        if ("yes".equals(choice.toLowerCase())) {
            showCreateAdminUserDialog();
        } else {
            textView.displayMessage("\nChanging to login and registration page...");

            return;
        }
    }

    private void showCreateAdminUserDialog() {
        String[] prompts = getPromptsForDialogs();

        String[] userInputs = new String[prompts.length];

        for (int i = 0; i < prompts.length; i++) {
            userInputs[i] = getUserInput(prompts[i]);

            if (userInputs[i].isEmpty()) {
                textView.displayMessage("\n Aborting user creation\n Changing to login and registration page...");

                return;
            }
        }

        UserRegistrationData userRegistrationData = new UserRegistrationData(
                userInputs[0], userInputs[1], userInputs[2],
                userInputs[3], userInputs[4], userInputs[5], userInputs[6]
        );

        userController.createNewAdminUser(userRegistrationData, true);
    }

    private String getUserInput(String prompt) {
        textView.displayMessage(prompt);
        return textView.getUserInput();
    }

    private String[] getPromptsForDialogs() {
         String[] dialogPrompts = {
                 "Enter first name \n(leave blank to cancel Account creation): ",
                 "Enter last name \n(leave blank to cancel Account creation): ",
                 "Enter date of birth \n(leave blank to cancel Account creation): ",
                 "Enter email \n(leave blank to cancel Account creation): ",
                 "Enter phone number \n(leave blank to cancel Account creation): ",
                 "Enter password \n(leave blank to cancel Account creation): ",
                 "Confirm password \n(leave blank to cancel Account creation): "
         };

        return dialogPrompts;
    }
}
