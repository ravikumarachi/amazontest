package setpdefs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertyReader;

public class ScenarioHooks {
    private static final Logger Log = LoggerFactory.getLogger(ScenarioHooks.class.getName());
    private static final String ENV_PROP_KEY = "env";
    private static final String USER_PATH = System.getProperty("user.dir");
    private static final String ENV_FILE_PATH = USER_PATH + "/src/test/resources/properties/";
    private static PropertyReader properties;
    private static String ENV_FILE_NAME = "pcbtest.properties";

    static {
        if(System.getProperty(ENV_PROP_KEY) == null){
            Log.info("Environment Type not specified, defaulting to pcbtest");
            ENV_FILE_NAME = ENV_FILE_PATH + ENV_FILE_NAME;
        } else {
            ENV_FILE_NAME = ENV_FILE_PATH + System.getProperty(ENV_PROP_KEY) + ".properties";
            Log.info("Using properties file: " + ENV_FILE_NAME);
        }
        properties = new PropertyReader(ENV_FILE_NAME);
    }

    public static String getProperty(String propName) {
        return properties.getProperty(propName);
    }

    public static void main(String[] args) {
    }
}
