package helpertests;

import helper.PasswordHelper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Tag("parallelHelperTests")
public class PasswordHelperTestDrive {
    @Test
    void testHashPassword() {
        String password = "password";

        String hashedPassword = PasswordHelper.hashPassword(password);

        assertFalse(hashedPassword.equals(password));
    }

    @Test
    void testVerifyPassword() {
        String hashedPassword = PasswordHelper.hashPassword("password");

        assertTrue(PasswordHelper.verifyPassword("password", hashedPassword));
    }
}
