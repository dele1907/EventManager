package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabaseInitializer;
import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabasePathManager;
import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationData;
import de.eventmanager.core.presentation.PresentationHelpers.ValidationHelper;
import de.eventmanager.core.presentation.UI.View;
import de.eventmanager.core.users.User;
import helper.ConfigurationDataSupplierHelper;

import java.util.Optional;

public class LoginRegistrationPage implements Tab {
    private View textView;
    private UserController userController;
    private Optional<User> loggedInUser;
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
            textView.displayTabOrPageHeading("\n===== EventManager =====");
            textView.displayMessage("1. Register\n2. Login\n3. Exit Program");
            textView.displayUserInputMessage("\n\nChoose an option\n> ");
            String choice = textView.getUserInput();

            switch (choice) {
                case "1":
                    showRegisterUserDialog();
                    break;
                case "2":
                    loggedInUser = showLoginUserDialog();
                    if (loggedInUser.isPresent()) {
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

    public Optional<User> getLoggedInUser() {
        return loggedInUser;
    }

    public String getLoggedInUserID() {
        return loggedInUserID;
    }

    public String resetLoggedInUserID() {
        return loggedInUserID = "";
    }

    public void resetLoggedInUser() {
        loggedInUser = Optional.empty();
    }

    public void showRegisterUserDialog() {
        boolean validPhoneNumber = true;
        String phoneNumber = "";

        textView.displayTabOrPageHeading("\n===== Registration =====");
        textView.displayUserInputMessage("Enter first name\n> ");
        String firstName = textView.getUserInput();
        textView.displayUserInputMessage("Enter last name\n> ");
        String lastName = textView.getUserInput();
        textView.displayUserInputMessage("Enter date of birth\n> ");
        String dateOfBirth = textView.getUserInput();
        textView.displayUserInputMessage("Enter email\n> ");
        String email = textView.getUserInput();
        while (validPhoneNumber) {
            textView.displayMessage("Enter phone number: ");
            phoneNumber = textView.getUserInput();
            validPhoneNumber = !ValidationHelper.checkPhoneNumber(textView, phoneNumber); //If phoneNumber is valid, the loop ends
        }
        textView.displayUserInputMessage("Enter password\n> ");
        String password = textView.getUserInput();
        textView.displayUserInputMessage("Confirm password\n> ");
        String confirmPassword = textView.getUserInput();

        UserRegistrationData userData = new UserRegistrationData(firstName, lastName, dateOfBirth,
                email, phoneNumber, password, confirmPassword
        );

        validateRegisterUser(userData);
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

    public Optional<User> showLoginUserDialog() {
        textView.displayTabOrPageHeading("\n===== Login ======");
        textView.displayUserInputMessage("Enter eMail\n> ");
        String eMail = textView.getUserInput();

        textView.displayUserInputMessage("\nEnter password\n> ");
        String password = textView.getUserInput();

        if (eMail.isEmpty() || password.isEmpty()) {
            textView.displayErrorMessage("\nYou must enter eMail or password");

            return Optional.empty();
        }

        Optional<User> loginUser = userController.loginUser(eMail, password);

        return validateLoginUser(loginUser);
    }

    private Optional<User> validateLoginUser(Optional<User> loginUser) {
        if (loginUser.isEmpty()) {
            textView.displayErrorMessage("\nWrong eMail or password! \nPlease try again.\n");

            return Optional.empty();
        }

        textView.displayMessage("\nLogin successful\n");

        return loginUser;
    }
}