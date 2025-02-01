package de.eventmanager.core.presentation.PresentationHelpers;

public class ValidationHelper {

    public static boolean checkPhoneNumber(String phoneNumber) {
            return !phoneNumber.isEmpty() && phoneNumber.matches("\\+?[0-9]+");
    }
}
