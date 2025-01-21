package de.eventmanager.core.presentation.PresentationHelpers;

import de.eventmanager.core.presentation.UI.TextView;
import de.eventmanager.core.presentation.UI.View;

public class ValidationHelper {

    public static String checkPhoneNumber(View view) {
        boolean inValidPhoneNumber = true;
        String phoneNumber = "";
        final String INVALID_PHONE_NUMBER_INPUT_MESSAGE = "Invalid input! Please enter a valid phone number\n";
        while (inValidPhoneNumber) {

            try {
                view.displayMessage("Enter phone number: ");
                phoneNumber = view.getUserInput();
                if (!phoneNumber.isEmpty()) {

                    if (!phoneNumber.matches(".*[-äöüÄÖÜß?!.,<>a-zA-Z].*")) {

                        inValidPhoneNumber = false;
                    }else {

                        view.displayErrorMessage(INVALID_PHONE_NUMBER_INPUT_MESSAGE);
                    }
                }

            }catch (Exception e) {
                view.displayErrorMessage(INVALID_PHONE_NUMBER_INPUT_MESSAGE);
            }
        }

        return phoneNumber;
    }
}
