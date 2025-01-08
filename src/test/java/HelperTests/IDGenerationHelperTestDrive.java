package HelperTests;

import Helper.IDGenerationHelper;
import org.junit.jupiter.api.Test;

public class IDGenerationHelperTestDrive {
    int RANDOM_ID_LENGHT = 15;

    @Test
    void testIDIsSet() {
        String randomID = IDGenerationHelper.generateRandomString(RANDOM_ID_LENGHT);

        System.out.println("\nRandom ID is empty: " + randomID.isEmpty());

        assert (!randomID.isEmpty());
    }

    @Test
    void testIDLength() {
        String randomID = IDGenerationHelper.generateRandomString(RANDOM_ID_LENGHT);

        System.out.println("\nRandom ID length should be: " + RANDOM_ID_LENGHT);
        System.out.println("Random ID length is: " + randomID.length() + "\n");

        assert (randomID.length() == RANDOM_ID_LENGHT);
    }

    @Test
    void testIDIsUnique() {
        String randomIDOne = IDGenerationHelper.generateRandomString(RANDOM_ID_LENGHT);
        String randomIDTwo = IDGenerationHelper.generateRandomString(RANDOM_ID_LENGHT);
        boolean areUniqueIDs = !(randomIDOne.equals(randomIDTwo));

        System.out.println("\nGenerated random IDs are different: " + areUniqueIDs);
        System.out.println(randomIDOne);
        System.out.println(randomIDTwo);

        assert (areUniqueIDs == true);
    }
}
