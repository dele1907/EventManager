package HelperTests;

import Helper.IDGenerationHelper;
import org.junit.jupiter.api.Test;

public class IDGenerationHelperTestDrive {

    @Test
    void testIDIsSet() {
        String randomID = IDGenerationHelper.generateRandomIDString();

        System.out.println("\nRandom ID is empty: " + randomID.isEmpty());

        assert (!randomID.isEmpty());
    }

    @Test
    void testIDLength() {
        String randomID = IDGenerationHelper.generateRandomIDString();

        System.out.println("\nRandom ID length should be: " + IDGenerationHelper.ID_DEFAULT_LENGTH);
        System.out.println("Random ID length is: " + randomID.length() + "\n");

        assert (randomID.length() == IDGenerationHelper.ID_DEFAULT_LENGTH);
    }

    @Test
    void testIDIsUnique() {
        String randomIDOne = IDGenerationHelper.generateRandomIDString();
        String randomIDTwo = IDGenerationHelper.generateRandomIDString();
        boolean areUniqueIDs = !(randomIDOne.equals(randomIDTwo));

        System.out.println("\nGenerated random IDs are different: " + areUniqueIDs);
        System.out.println(randomIDOne);
        System.out.println(randomIDTwo);

        assert (areUniqueIDs == true);
    }
}
