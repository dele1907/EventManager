package helpertests;

import helper.DateOperationsHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DateOperationsHelperTestDrive {

    @Test
    void testValidateTheAge() {

        DateOperationsHelper dateOperationsHelper = new DateOperationsHelper();

        int correcttestcase = dateOperationsHelper.validateTheAge("tisc00006@htwsaar.de");
        int notcorrecttestcase = dateOperationsHelper.validateTheAge("hallo");

        assertTrue(notcorrecttestcase == 0);
        assertNotNull(correcttestcase);
    }
}
