package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabaseInitializer;
import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabasePathManager;
import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationData;
import de.eventmanager.core.presentation.UI.View;
import helper.ConfigurationDataSupplierHelper;

import java.util.List;

public class LoginRegistrationPage implements Tab {
    private View view;
    private UserController userController;
    //@TODO: remove flush before release
    private boolean flushTestDatabase;
    private String loggedInUserID;

    public LoginRegistrationPage(View view, UserController userController, boolean flushTestDatabase) {
        this.view = view;
        this.userController = userController;
        //@TODO: remove flush before release
        this.flushTestDatabase = flushTestDatabase;
    }

    @Override
    public void start() {
        boolean programIsRunning = true;

        while (programIsRunning) {
            DefaultDialogHelper.getTabOrPageHeading(view, "Login / Registration");
            DefaultDialogHelper.generateMenu(view, List.of("Register", "Login", "Exit Program"));
            String choice = view.getUserInput();

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
                    view.displayMessage("\nExit Program...");
                    programIsRunning = false;

                    //TODO @Dennis: remove flush before release
                    DatabasePathManager.flushDatabasePath(flushTestDatabase);
                    //TODO @Dennis: remove following line before release
                    DatabaseInitializer.deInit(ConfigurationDataSupplierHelper.IS_PRODUCTION_MODE);
                    System.exit(0);
                    break;
                default:
                    DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");
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
        view.displayTabOrPageHeading("\n===== Registration =====");
        validateRegisterUser(DefaultDialogHelper.createNewUserDefaultDialog(view));
    }

    private void validateRegisterUser(UserRegistrationData userRegistrationData) {

        boolean registrationSuccess = userController.createNewUser(userRegistrationData,
                ConfigurationDataSupplierHelper.REGISTER_NEW_USER_ID);

        if (!registrationSuccess) {
            view.displayErrorMessage("\nUser registration failed\n");

            return;
        }

        view.displaySuccessMessage("\nUser registered successfully\n");
    }

    public String showLoginUserDialog() {
        view.displayTabOrPageHeading("\n===== Login ======");
        view.displayUserInputMessage("Enter eMail\n> ");
        String eMail = view.getUserInput();

        view.displayUserInputMessage("\nEnter password\n> ");
        String password = view.getUserInput();

        if (eMail.isEmpty() || password.isEmpty()) {
            view.displayErrorMessage("\nYou must enter eMail or password");

            return "";
        }

        String loginUserID = userController.loginUser(eMail, password);

        return validateLoginUser(loginUserID);
    }

    private String validateLoginUser(String loginUserID) {
        if (loginUserID.isEmpty()) {
            view.displayErrorMessage("\nWrong eMail or password! \nPlease try again.\n");

            return "";
        }

        view.displayMessage("\nLogin successful\n");

        return loginUserID;
    }
}