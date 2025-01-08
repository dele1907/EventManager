package HelperTests;

import Helper.IDGenerationHelper;
import org.junit.jupiter.api.Test;

public class IDGenerationHelperTestDrive {

    @Test
    void testIDIsSet() {
        assert (IDGenerationHelper.generateRandomString(4).length() == 4);
    }
}
