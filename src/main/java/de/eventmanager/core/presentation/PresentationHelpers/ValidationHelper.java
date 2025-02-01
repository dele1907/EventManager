package de.eventmanager.core.presentation.PresentationHelpers;

public class ValidationHelper {

    public static boolean validatePhoneNumberInput(String phoneNumber) {
            return !phoneNumber.isEmpty() && phoneNumber.matches("\\+?[0-9]+");
    }

    public static boolean validateEmailInput(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    public static boolean validateDateInput(String date) {
        return date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }
}
