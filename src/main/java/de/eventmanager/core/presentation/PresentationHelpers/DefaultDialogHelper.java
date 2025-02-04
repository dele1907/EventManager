package de.eventmanager.core.presentation.PresentationHelpers;

import de.eventmanager.core.presentation.UI.View;

import java.util.List;
import java.util.Optional;

public class DefaultDialogHelper {
    public final static String WARNING_MESSAGE = "\n\u2757" + "\u2757" + "WARNING" + "\u2757" + "\u2757";
    public final static String USER_NOT_FOUND = "\nUser not found.\n";
    public final static String BLANK_TO_KEEP = "(Leave blank to keep current)";

    public static void getTabOrPageHeading(View view, String heading) {
        view.displayTabOrPageHeading("\n===== " + heading + " =====");
    }

    public static void generateMenu(View view, List<String> menuOptions) {
        StringBuilder menu = new StringBuilder();

        for (int i = 0; i < menuOptions.size(); i++) {
            menu.append(i + 1).append(". ").append(menuOptions.get(i)).append("\n");
        }

        view.displayMessage(menu.toString());
        view.displayUserInputMessage("\nChoose an option\n> ");
    }

    public static  UserRegistrationData createNewUserDefaultDialog(View view) {
        view.displayUserInputMessage("Enter first name\n> ");
        var firstName = view.getUserInput();
        view.displayUserInputMessage("Enter last name\n> ");
        var lastName = view.getUserInput();
        var dateOfBirth = showDateOfBirthDialog(view);
        var email = showEmailAddressDialog(view);
        var phoneNumber = showPhoneNumberDialog(view);
        view.displayUserInputMessage("Enter password\n> ");
        var password = view.getUserInput();
        view.displayUserInputMessage("Confirm password\n> ");
        var confirmPassword = view.getUserInput();

        return new UserRegistrationData(firstName, lastName, dateOfBirth,
                email, phoneNumber, password, confirmPassword
        );
    }

    private static String showPhoneNumberDialog(View view) {
        String phoneNumber = "";

        view.displayUserInputMessage("Enter phone number\n> ");
        phoneNumber = view.getUserInput();

        if (!ValidationHelper.validatePhoneNumberInput(phoneNumber)) {
            view.displayErrorMessage("Invalid phone number\n");
            return showPhoneNumberDialog(view);
        }

        return phoneNumber;
    }

    private static String showEmailAddressDialog(View view) {
        String email = "";

        view.displayUserInputMessage("Enter email\n> ");
        email = view.getUserInput();

        if (!ValidationHelper.validateEmailInput(email)) {
            view.displayErrorMessage("Invalid email\n");
            return showEmailAddressDialog(view);
        }

        return email;
    }

    private static String showDateOfBirthDialog(View view) {
        String dateOfBirth = "";

        view.displayUserInputMessage("Enter date of birth\n(Format: YYYY-MM-DD)\n> ");
        dateOfBirth = view.getUserInput();

        if (!ValidationHelper.validateDateInput(dateOfBirth)) {
            view.displayErrorMessage("Invalid date of birth\n");

            return showDateOfBirthDialog(view);
        }

        if (!ValidationHelper.validateAge(dateOfBirth)) {
            view.displayErrorMessage(
                    "Invalid age!\n" +
                    "Your age must be between 12 and 130 to register for the event manager!\n"
            );

            return showDateOfBirthDialog(view);
        }

        return dateOfBirth;
    }

    public static Optional<String> showEditAttributeDialog(View view, String prompt) {
        view.displayUserInputMessage("\nEnter new " + prompt + "\n" + BLANK_TO_KEEP + "\n> ");
        var userAttribute = view.getUserInput();

        if (userAttribute.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(userAttribute);
    }

    public static void showItemSeparator(View view, int lengthOfSeparator) {
        for (int i = 0; i < lengthOfSeparator; i++) {
            view.displayItemSeparatorMessage("=");
        }
    }
}
