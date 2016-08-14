package pages;

import utils.Log;
import utils.PropertyReader;

public abstract class BasePage {
    private static final String ENV_PROP_KEY = "env";
    private static final String USER_PATH = System.getProperty("user.dir");
    private static final String ENV_FILE_PATH = USER_PATH + "/src/test/resources/properties/";
    private static String ENV_FILE_NAME = "pcbtest.properties";
    private static String ENV_TYPE = "pcbtest";

    static {
        if(System.getProperty(ENV_PROP_KEY)==null){
            Log.info("Environment Type not specified, defaulting to pcbtest");
            ENV_FILE_NAME = ENV_FILE_PATH + ENV_FILE_NAME;
        } else {
            ENV_TYPE = System.getProperty(ENV_PROP_KEY);
            ENV_FILE_NAME = ENV_FILE_PATH + ENV_TYPE + ".properties";
        }
    }

    static PropertyReader properties = new PropertyReader(ENV_FILE_NAME);

    public static String getEnvType() {
        return ENV_TYPE;
    }

    public static String getProperty(String propName) {
        return properties.getProperty(propName);
    }

    public static void main(String[] args) {
        System.out.println("URL: " + getProperty("AEM_URL"));
    }
}
