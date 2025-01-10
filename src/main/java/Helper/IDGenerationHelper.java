package Helper;

public class IDGenerationHelper {

    public static final int ID_DEFAULT_LENGTH = 20;

    /**
     * method to generate random IDs for every purpose an ID has to be provided
     * */
    public static String generateRandomIDString() {

        //Todo @DennisLeister checking if UserID already exists

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        java.util.Random randomizer = new java.util.Random();


        for (int i = 0; i < ID_DEFAULT_LENGTH; i++) {
            randomString.append(characters.charAt(randomizer.nextInt(characters.length())));
        }

        return randomString.toString();
    }
}
