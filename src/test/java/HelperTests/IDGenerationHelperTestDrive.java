package HelperTests;

import Helper.IDGenerationHelper;
import org.junit.jupiter.api.Test;

public class IDGenerationHelperTestDrive {

    @Test
    void testIDIsSet() {
        String randomID = IDGenerationHelper.generateRandomString(IDGenerationHelper.ID_DEFAULT_LENGHT);

        System.out.println("\nRandom ID is empty: " + randomID.isEmpty());

        assert (!randomID.isEmpty());
    }

    @Test
    void testIDLength() {
        String randomID = IDGenerationHelper.generateRandomString(IDGenerationHelper.ID_DEFAULT_LENGHT);

        System.out.println("\nRandom ID length should be: " + IDGenerationHelper.ID_DEFAULT_LENGHT);
        System.out.println("Random ID length is: " + randomID.length() + "\n");

        assert (randomID.length() == IDGenerationHelper.ID_DEFAULT_LENGHT);
    }

    @Test
    void testIDIsUnique() {
        String randomIDOne = IDGenerationHelper.generateRandomString(IDGenerationHelper.ID_DEFAULT_LENGHT);
        String randomIDTwo = IDGenerationHelper.generateRandomString(IDGenerationHelper.ID_DEFAULT_LENGHT);
        boolean areUniqueIDs = !(randomIDOne.equals(randomIDTwo));

        System.out.println("\nGenerated random IDs are different: " + areUniqueIDs);
        System.out.println(randomIDOne);
        System.out.println(randomIDTwo);

        assert (areUniqueIDs == true);
    }
}
