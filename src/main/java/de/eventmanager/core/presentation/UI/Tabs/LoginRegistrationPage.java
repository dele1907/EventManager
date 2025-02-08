package de.eventmanager.core.presentation.UI.Tabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.EnumHelper;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationDataPayload;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.View;
import helper.ConfigurationDataSupplierHelper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LoginRegistrationPage implements Tab {
    private View view;
    private String loggedInUserID;
    private UserService userService;

    private enum LoginRegistrationMenuChoice {
        REGISTER,
        LOGIN,
        EXIT_PROGRAM;

        public static Optional<LoginRegistrationMenuChoice> fromUserInput(String userInput) {
            return EnumHelper.getMapOfStringToEnumConstant(userInput, LoginRegistrationMenuChoice.class,
                Map.of(
                "1", REGISTER,
                "2", LOGIN,
                "3", EXIT_PROGRAM
                )
            );
        }
    }

    public LoginRegistrationPage(View view) {
        this.view = view;
        this.userService = new UserServiceImpl();
    }

    @Override
    public void start() {
        boolean programIsRunning = true;

        while (programIsRunning) {
            DefaultDialogHelper.getTabOrPageHeading(view, "Login / Registration");
            DefaultDialogHelper.generateMenu(view, List.of("Register", "Login", "Exit Program"));
            var menuChoice = LoginRegistrationMenuChoice.fromUserInput(view.getUserInput());

            if (menuChoice.isEmpty()) {
                DefaultDialogHelper.showInvalidInputMessageByAttribute(view, "choice");
                continue;
            }

            switch (menuChoice.get()) {
                case REGISTER -> showRegisterUserDialog();
                case LOGIN -> {
                    loggedInUserID = showLoginUserDialog();
                    if (!loggedInUserID.isEmpty()) {
                        programIsRunning = false;
                    };
                }
                case EXIT_PROGRAM -> {
                    view.displayMessage("\nExit Program...");
                    programIsRunning = false;

                    System.exit(0);
                }
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

    private void validateRegisterUser(UserRegistrationDataPayload userRegistrationDataPayload) {

        if (!(new UserServiceImpl().registerUser(userRegistrationDataPayload,
                ConfigurationDataSupplierHelper.REGISTER_NEW_USER_ID))) {

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

        return validateLoginUser(userService.loginUser(eMail, password));
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