package utils;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import pages.BasePage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RestService extends BasePage {
    private static final String JSON_URL = "/content/securecontent/en/%s/jcr:content.secure.json";
    private static final String USER_PATH = System.getProperty("user.dir");
    private static final String JSON_FILE_PATH = USER_PATH + "/src/test/resources/ExpectedJSON/";
    private static final String ENV_PROP_KEY = "env";

    /**
     * Use this method to read the JSON content for a page
     * @param pageName  Name of the page for which JSON content is needed
     * @return          Return the JSON response
     */
    public static Response getJsonForPage(String pageName) {
        String jsonPageUrl = String.format(JSON_URL, pageName);

        RestAssured.baseURI = getProperty("AEM_BASE_URI");
        RestAssured.basePath = jsonPageUrl;

        if(!StringUtils.contains(getEnvType(), "local")) {
            Log.info("Using proxy setting for REST call");
            RestAssured.proxy(getProperty("PROXY_HOST"), Integer.parseInt(getProperty("PROXY_PORT")));
        }

        Log.info("Getting JSON for page name: " + pageName);
        Log.info("Using URL: " + RestAssured.baseURI + RestAssured.basePath);

        Response response = given().accept(ContentType.JSON).authentication().basic(getProperty("AEMUsername"), getProperty("AEMUsernamePassword")).when().get("/");
        Log.info("Response: " + response.prettyPrint());
        return response;
    }

    /**
     * Use this method to compare 2 JSON file for equality
     * @param expectedJsonFileName  Name of the JSON file to compare
     * @param actualJson            Actual JSON content to compare
     */
    public static void isBothJsonSame(String expectedJsonFileName, String actualJson) {
        try {
            JSONAssert.assertEquals(getFileContentAsString(expectedJsonFileName), actualJson, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use this method to read the file content into a string
     * @param jsonFilename      Name of the file
     * @return                  file conent in string
     */
    public static String getFileContentAsString(String jsonFilename) {
        String fileContentAsString = null;

        try {
            fileContentAsString = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH + jsonFilename + ".json")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContentAsString;
    }

    public static void main(String[] args) {
        //Response response = RestService.getJsonForPage("BuyingACar_final");
        Response response = RestService.getJsonForPage("MomentsAuto");
        assertThat("Status Code not 200", response.getStatusCode(), is(200));
        JsonPath jsonOutput = new JsonPath(response.getBody().asString().replace("jcr:content", "jcr_content"));
        RestService.isBothJsonSame("momentsPageBaseJson", response.getBody().asString());
    }
}
