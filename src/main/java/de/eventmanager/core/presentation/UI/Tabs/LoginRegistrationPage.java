package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabaseInitializer;
import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabasePathManager;
import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationData;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;
import helper.ConfigurationDataSupplierHelper;

import java.util.Optional;

public class LoginRegistrationPage implements Tab {
    private View textView;
    private UserController userController;
    //@TODO: remove flush before release
    private boolean flushTestDatabase;
    private String loggedInUserID;

    public LoginRegistrationPage(View textView, UserController userController, boolean flushTestDatabase) {
        this.textView = textView;
        this.userController = userController;
        //@TODO: remove flush before release
        this.flushTestDatabase = flushTestDatabase;
    }

    @Override
    public void start() {
        boolean programIsRunning = true;

        while (programIsRunning) {
            DefaultDialogHelper.getTabOrPageHeading(textView, "Login / Registration");

            textView.displayMessage(
                    "1. Register" +
                    "\n2. Login" +
                    "\n3. Exit Program"
            );
            textView.displayUserInputMessage("\n\nChoose an option\n> ");
            String choice = textView.getUserInput();

            switch (choice) {
                case "1":
                    showRegisterUserDialog();
                    break;
                case "2":
                    loggedInUserID = showLoginUserDialog();
                    if (!loggedInUserID.isEmpty()) {
                        programIsRunning = false;
                    }
                    break;
                case "3":
                    textView.displayMessage("\nExit Program...");
                    programIsRunning = false;

                    //TODO @Dennis: remove flush before release
                    DatabasePathManager.flushDatabasePath(flushTestDatabase);
                    //TODO @Dennis: remove following line before release
                    DatabaseInitializer.deInit(ConfigurationDataSupplierHelper.IS_PRODUCTION_MODE);
                    System.exit(0);
                    break;
                default:
                    textView.displayErrorMessage("\nInvalid choice");
                    break;
            }
        }
    }

    public String getLoggedInUser() {
        return loggedInUserID;
    }

    public void resetLoggedInUser() {
        loggedInUserID = "";
    }

    public void showRegisterUserDialog() {
        textView.displayTabOrPageHeading("\n===== Registration =====");
        validateRegisterUser(DefaultDialogHelper.createNewUserDefaultDialog(textView));
    }

    private void validateRegisterUser(UserRegistrationData userRegistrationData) {

        boolean registrationSuccess = userController.createNewUser(userRegistrationData,
                ConfigurationDataSupplierHelper.REGISTER_NEW_USER_ID);

        if (!registrationSuccess) {
            textView.displayErrorMessage("\nUser registration failed\n");

            return;
        }

        textView.displaySuccessMessage("\nUser registered successfully\n");
    }

    public String showLoginUserDialog() {
        textView.displayTabOrPageHeading("\n===== Login ======");
        textView.displayUserInputMessage("Enter eMail\n> ");
        String eMail = textView.getUserInput();

        textView.displayUserInputMessage("\nEnter password\n> ");
        String password = textView.getUserInput();

        if (eMail.isEmpty() || password.isEmpty()) {
            textView.displayErrorMessage("\nYou must enter eMail or password");

            return "";
        }

        String loginUserID = userController.loginUser(eMail, password);

        return validateLoginUser(loginUserID);
    }

    private String validateLoginUser(String loginUserID) {
        if (loginUserID.isEmpty()) {
            textView.displayErrorMessage("\nWrong eMail or password! \nPlease try again.\n");

            return "";
        }

        textView.displayMessage("\nLogin successful\n");

        return loginUserID;
    }
}