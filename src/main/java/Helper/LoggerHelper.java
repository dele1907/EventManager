package Helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerHelper {

    /*
    * This helper should provide basic methods for logging information.
    * Simply use provided methods in a class like for example:
    * (in class User)
    * 'LoggerHelper.getErrorMessage(User.class, "User has not the permission to execute this action")';
    * */

    //#region new logger
    private static Logger getLogger(Class<?> objectClass) {
        Logger logger = LogManager.getLogger(objectClass);

        return logger;
    }
    //#endregion new logger

    //#region logger methods
    public static void getErrorMessage(Class<?> objectClass, String errorMessage) {
        getLogger(objectClass).error(errorMessage);
    }

    public static void getInfoMessage(Class<?> objectClass, String infoMessage) {
        getLogger(objectClass).info(infoMessage);
    }

    public static void getDebugMessage(Class<?> objectClass, String debugMessage) {
        getLogger(objectClass).debug(debugMessage);
    }

    public static void getTraceMessage(Class<?> objectClass, String traceMessage) {
        getLogger(objectClass).trace(traceMessage);
    }
    //#endregion logger methods
}
