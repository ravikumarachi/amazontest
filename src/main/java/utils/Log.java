package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
    private static final Logger Log = LoggerFactory.getLogger(Log.class.getName());

    public static void info(String message)
    {
        Log.info(message);
    }

    public static void debug(String message)
    {
        Log.debug(message);
    }

    public static void error(String message)
    {
        Log.error(message);
    }

    /*public static void main(String[] args) {
        Log.info("This is INFO");
        Log.debug("This is DEBUG");
    }*/
}
