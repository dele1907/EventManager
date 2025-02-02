package helpertests;

import helper.DateOperationsHelper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DateOperationsHelperTestDrive {
    //TODO: Rework test class so it will work again! See ticket # 89.

    @Test
    @Disabled
    void testcorrectEmail() {

        DateOperationsHelper dateOperationsHelper = new DateOperationsHelper();

        int testcase = dateOperationsHelper.getTheAgeFromDatabase("hallo");

        assertTrue(testcase == 0);
    }

    @Test
    @Disabled
    void testAgelogic() {

        DateOperationsHelper dateOperationsHelper = new DateOperationsHelper();

        int correcttestcase = dateOperationsHelper.getTheAgeFromDatabase("tisc00006@htwsaar.de");

        assertFalse(correcttestcase > 27);
        assertTrue(correcttestcase > 18);
    }
}
