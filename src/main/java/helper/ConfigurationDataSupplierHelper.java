package helper;

public class ConfigurationDataSupplierHelper {
    public static final String REGISTER_NEW_USER_ID = "newRegisteredUser";

    public static boolean IS_PRODUCTION_MODE = true;

    public static void setIsProductionMode(boolean isProductionMode) {
        IS_PRODUCTION_MODE = isProductionMode;
    }
}
