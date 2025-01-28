package helper;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHelper {

    /**
     * TODO @Dennis: Comment in when change to Argon2, also add to pom.xml:
     *  <dependency>
     *      <groupId>org.bouncycastle</groupId>
     *      <artifactId>bcpkix-jdk15on</artifactId>
     *      <version>1.70</version>
     *  </dependency>
     * */
    //private static final PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}
