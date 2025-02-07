package de.eventmanager.core.presentation.PresentationHelpers;

public record UserRegistrationDataPayload (
    String firstName,
    String lastName,
    String dateOfBirth,
    String email,
    String phoneNumber,
    String password,
    String confirmPassword
){}