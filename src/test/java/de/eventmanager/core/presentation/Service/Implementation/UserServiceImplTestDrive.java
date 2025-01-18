package de.eventmanager.core.presentation.Service.Implementation;

import helper.DatabaseSimulation.JsonDatabaseHelper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTestDrive {

    @Test
    @Order(1)
    void registerUser() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        boolean isRegistered = userServiceImpl.registerUser(
                "Dis",
                "Appear",
                "01.01.2000",
                "disappear@muster.com",
                12345678,
                "123456",
                "123456"
        );
        System.out.println("User registered: " + isRegistered);
        assertTrue(isRegistered);

        boolean isRemoved = JsonDatabaseHelper.removeUserByEmailFromJson("disappear@muster.com");
        System.out.println("User removed: " + isRemoved);
        assertTrue(isRemoved);
    }

    @Test
    @Order(2)
    void loginUser() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        System.out.println(userServiceImpl.loginUser("max.mustermann@testmail.com", "password").get());

        assertTrue(userServiceImpl.loginUser("max.mustermann@testmail.com", "password").isPresent());
    }
}
