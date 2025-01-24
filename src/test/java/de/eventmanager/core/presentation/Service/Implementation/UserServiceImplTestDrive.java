package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTestDrive {

    @Test
    @Order(1)
    @Disabled // Disabled because it is not possible to remove the user after registration
    void registerUser() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        boolean isRegistered = userServiceImpl.registerUser(
                "Dis",
                "Appear",
                "01.01.2000",
                "disappear@muster.com",
                "12345678",
                "123456",
                "123456",
                "iwbLeZWwmrg5E0oC8KIs"
        );
        System.out.println("User registered: " + isRegistered);
        assertTrue(isRegistered);

        boolean isRemoved = UserDatabaseConnector.deleteUserByID("disappear@muster.com");
        System.out.println("User removed: " + isRemoved);
        assertTrue(isRemoved);
    }

    @Test
    @Order(2)
    void loginUser() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        System.out.println(userServiceImpl.loginUser("dele0003@htwsaar.de", "Password123").get());

        assertTrue(userServiceImpl.loginUser("dele0003@htwsaar.de", "Password123").isPresent());
    }
}
