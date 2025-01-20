package helper;

import java.util.UUID;

public class IDGenerationHelper {

    public static final int ID_DEFAULT_LENGTH = 20;

    /**
     * generates a random UUID
     * */
    public static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
