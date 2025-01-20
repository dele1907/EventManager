package helper;

import java.util.UUID;

public class IDGenerationHelper {

    /**
     * generates a random UUID
     * */
    public static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
