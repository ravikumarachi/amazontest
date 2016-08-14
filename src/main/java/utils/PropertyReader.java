package utils;

import java.io.*;
import java.util.Properties;

public class PropertyReader {
    private static Properties properties = new Properties();

    public PropertyReader(String fileName) {
        loadProperties(fileName);
    }

    private void loadProperties(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream((new File(fileName)).getAbsolutePath());
            properties.load(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}