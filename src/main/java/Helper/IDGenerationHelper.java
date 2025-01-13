package Helper;

public class IDGenerationHelper {

    public static final int ID_DEFAULT_LENGTH = 20;

    /**
     * method to generate random IDs for every purpose an ID has to be provided
     *
     * @return an ID having 20 characters containing numbers and capital & small letters
     *
     * there is no check if a several ID already exists in the database
     * because there are 1*10^35 different possible IDs having 20 characters, so it's unlikely to have
     * the same ID twice
     * */
    public static String generateRandomIDString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        java.util.Random randomizer = new java.util.Random();


        for (int i = 0; i < ID_DEFAULT_LENGTH; i++) {
            randomString.append(characters.charAt(randomizer.nextInt(characters.length())));
        }

        return randomString.toString();
    }
}
