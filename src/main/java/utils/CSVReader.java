package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {
    private static final String USER_PATH = System.getProperty("user.dir");
    private static final String TEST_DATA_FILE_PATH = USER_PATH + "/src/test/resources/TestData/";

    public static List<Map<String, String>> readCVS(String filename) {
        List<Map<String, String>> csvFileContent = new ArrayList<>();

        try {
            boolean headerAlreadyRead = false;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(TEST_DATA_FILE_PATH + filename));

            String[] headerFields = null;
            String line;

            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                if(!headerAlreadyRead) {
                    headerFields = line.split(",");
                    headerAlreadyRead = true;
                } else {
                    String[] fields = line.split(",");

                    if(headerFields.length == fields.length) {
                        Map<String, String> csvLine = new HashMap<String, String>();
                        csvLine.put(fields[0], fields[1]);
                        csvFileContent.add(csvLine);
                    } else {
                        Log.debug("For each of the header field, a value should be present in CSV file");
                    }
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return csvFileContent;
    }

    /*public static void main(String[] args) {
        List<Map<String, String>> csvFileContent = readCVS("BranchFinder/branchfinder.csv");

        for(Map<String, String> line: csvFileContent) {
            Log.info(Arrays.toString(line.entrySet().toArray()));
        }
    }*/
}
