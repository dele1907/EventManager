package Helper;

public class IDGenerationHelper {

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        java.util.Random rnd = new java.util.Random();
        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(rnd.nextInt(characters.length())));
        }
        return randomString.toString();
    }
}
