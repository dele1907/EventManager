package de.eventmanager.core.presentation.PresentationHelpers;

import de.eventmanager.core.presentation.UI.View;

public class ValidationHelper {

    public static boolean checkPhoneNumber(View view, String phoneNumber) {
        final String INVALID_PHONE_NUMBER_INPUT_MESSAGE = "Invalid input! Please enter a valid phone number\n";

            try {

                if (!phoneNumber.isEmpty()) {

                    if (phoneNumber.matches(".*[-äöüÄÖÜß?!.,<>a-zA-Z].*")) {
                        view.displayErrorMessage(INVALID_PHONE_NUMBER_INPUT_MESSAGE);

                        return false;
                    }
                }

            }catch (Exception e) {
                view.displayErrorMessage(INVALID_PHONE_NUMBER_INPUT_MESSAGE);
            }


        return true;
    }
}
