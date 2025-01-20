package helpertests;

import helper.IDGenerationHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IDGenerationHelperTestDrive {

    @Test
    void testIDIsSet() {
        String randomID = IDGenerationHelper.generateRandomUUID();

        System.out.println("\nRandom ID is empty: " + randomID.isEmpty());

        assert (!randomID.isEmpty());
    }

    @Test
    void testRandomUUID() {
        String randomUUID = IDGenerationHelper.generateRandomUUID();

        System.out.println("\nRandom UUID is empty: " + randomUUID.isEmpty() + " : " + randomUUID);

        assertFalse(randomUUID.isEmpty());
    }

    @Test
    void uuidIsRandom() {
        String randomUUIDOne = IDGenerationHelper.generateRandomUUID();
        String randomUUIDTwo = IDGenerationHelper.generateRandomUUID();
        boolean areUniqueUUIDs = !(randomUUIDOne.equals(randomUUIDTwo));

        System.out.println("\nGenerated random UUIDs are different: " + areUniqueUUIDs);
        System.out.println(randomUUIDOne);
        System.out.println(randomUUIDTwo);

        assertTrue(areUniqueUUIDs);
    }
}
