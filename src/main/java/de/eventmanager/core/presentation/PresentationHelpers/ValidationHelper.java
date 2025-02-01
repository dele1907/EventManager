package de.eventmanager.core.presentation.PresentationHelpers;

import de.eventmanager.core.presentation.UI.View;
import helper.LoggerHelper;

public class ValidationHelper {

    public static boolean checkPhoneNumber(String phoneNumber) {
            return !phoneNumber.isEmpty() && phoneNumber.matches("\\+?[0-9]+");
    }
}
