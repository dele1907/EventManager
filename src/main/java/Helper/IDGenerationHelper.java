package Helper;

public class IDGenerationHelper {

    public static final int ID_DEFAULT_LENGHT = 20;

    /**
     * method to generate random IDs for every purpose an ID has to be provided
     * */
    public static String generateRandomIDString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        java.util.Random randomizer = new java.util.Random();


        for (int i = 0; i < ID_DEFAULT_LENGHT; i++) {
            randomString.append(characters.charAt(randomizer.nextInt(characters.length())));
        }

        return randomString.toString();
    }
}
