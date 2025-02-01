package de.eventmanager.core.presentation.PresentationHelpers;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class ValidationHelper {

    public static boolean validatePhoneNumberInput(String phoneNumber) {
            return !phoneNumber.isEmpty() && phoneNumber.matches("\\+?[0-9]+");
    }

    public static boolean validateEmailInput(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    public static boolean validateDateInput(String date) {
        return date.matches("^(19[0-9]{2}|20[0-2][0-9]|2030)-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
    }

    public static boolean validateTimeInput(String time) {
        return time.matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
    }

    private static int getAgeFromBirthdate(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    public static boolean validateAge(String birthdate) {
        return getAgeFromBirthdate(birthdate) >= 12 && getAgeFromBirthdate(birthdate) <= 130;
    }
}
