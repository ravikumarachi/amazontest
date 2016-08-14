package utils;

import constants.Browsers;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import java.awt.*;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class BrowserFactory {
    private static final String BROWSER_PROP_KEY = "browser";
    private static final String PROFILE_PROP_KEY = "profile";
    private static final String USER_HOME = System.getProperty("user.home");
    private static final String USER_PATH = System.getProperty("user.dir");
    private static final String IE_DRIVER_WIN32 = USER_PATH + "/src/main/resources/drivers/IEDriverServer32.exe";
    private static final String IE_DRIVER_WIN64 = USER_PATH + "/src/main/resources/drivers/IEDriverServer64.exe";
    private static final String CHROME_DRIVER_WIN = USER_PATH + "/src/main/resources/drivers/chromedriver.exe";
    private static final String CHROME_DRIVER_MAC = USER_PATH + "/src/main/resources/drivers/chromedriver";
    private static final String PHANTOM_DRIVER_WIN = USER_PATH + "/src/main/resources/drivers/phantomjs.exe";
    private static final String PHANTOM_DRIVER_MAC = USER_PATH + "/src/main/resources/drivers/phantomjs";

    /**
     * Use this to create a browser based on the system property "browser"
     * If no property is set then a chrome browser is created
     * The allowed browser types are firefox, chrome, safari and IE
     * e.g. to execute tests with firefox, pass the option -Dbrowser=chrome at runetime
     * @return WebDriver for the specified browser
     */
    public static WebDriver getBrowser() {
        Browsers browser;
        WebDriver driver = null;

        if(System.getProperty(BROWSER_PROP_KEY)==null){
            Log.info("Browser Type not specified, defaulting to Chrome");
            browser = Browsers.CHROME;
        }else{
            browser = Browsers.browserForName(System.getProperty(BROWSER_PROP_KEY));
        }

        switch(browser) {
            case CHROME:
                driver = getChromeBrowser();
                break;
            case SAFARI:
                driver = getSafariBrowser();
                break;
            case FIREFOX:
                driver = getFirefoxBrowser();
                break;
            case IE:
                driver = getIEBrowser();
                break;
            default:
                Log.debug("Unable to determine browser type to create browser instance.");
        }

        setBrowserSettings(driver);
        return driver;
    }

    private static WebDriver getFirefoxBrowser() {
        Log.info("Getting firefox webdriver");
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability(FirefoxDriver.PROFILE, getFirefoxProfile());
        return new FirefoxDriver(capabilities);
    }

    private static FirefoxProfile getFirefoxProfile() {
        Log.info("Creating firefox profile");
        FirefoxProfile firefoxProfile = null;

        //TODO: Need to fix the profile path, so it can work for everyone
        String OSName = OSChecker.getOS();
        if("windows".equalsIgnoreCase(OSName)) {
            if(System.getProperty(PROFILE_PROP_KEY) != null && System.getProperty(PROFILE_PROP_KEY).equalsIgnoreCase("default")) {
                Log.info("Firefox with profile " + System.getProperty(PROFILE_PROP_KEY) + " will be created");
                File fp = new File("/Users/g01083731/Library/Application Support/Firefox/Profiles/qig8gawg.default");
                firefoxProfile = new FirefoxProfile(fp);
            }

        } else if ("mac".equalsIgnoreCase(OSName)) {
            if(System.getProperty(PROFILE_PROP_KEY) != null && System.getProperty(PROFILE_PROP_KEY).equalsIgnoreCase("default")) {
                Log.info("Firefox with " + System.getProperty(PROFILE_PROP_KEY) + " profile will be created");
                File fp = new File("/Users/g01083731/Library/Application Support/Firefox/Profiles/qig8gawg.default");
                firefoxProfile = new FirefoxProfile(fp);
            }
        } else {
            Log.debug("Unable to find the correct chrome driver for your OS.");
        }

        /*try {
            firefoxProfile.addExtension(FileUtils.getFile("firebug/firebug-1.9.2.xpi"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e){
            e.printStackTrace();
        }

        //See http://getfirebug.com/wiki/index.php/Firebug_Preferences
        firefoxProfile.setPreference("extensions.firebug.currentVersion", "1.9.2");  // Avoid startup screen
        firefoxProfile.setPreference("extensions.firebug.script.enableSites", true);
        firefoxProfile.setPreference("extensions.firebug.console.enableSites", true);
        firefoxProfile.setPreference("extensions.firebug.allPagesActivation", true);
        firefoxProfile.setPreference("extensions.firebug.delayLoad", false);*/
        return firefoxProfile;
    }

    private static WebDriver getSafariBrowser() {
        Log.info("Getting safari webdriver");
        DesiredCapabilities capabilities = DesiredCapabilities.safari();
        return new SafariDriver(capabilities);
    }

    private static WebDriver getChromeBrowser() {
        Log.info("Getting chrome webdriver");
        ChromeOptions chromeOptions = new ChromeOptions();

        String OSName = OSChecker.getOS();
        if("windows".equalsIgnoreCase(OSName)) {
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_WIN);

            if(System.getProperty(PROFILE_PROP_KEY) != null && System.getProperty(PROFILE_PROP_KEY).equalsIgnoreCase("default")) {
                Log.info("Chrome with profile " + System.getProperty(PROFILE_PROP_KEY) + " will be created");
                chromeOptions.addArguments("user-data-dir=" + USER_HOME + "\\AppData\\Local\\Google\\Chrome\\User Data");
            }

        } else if ("mac".equalsIgnoreCase(OSName)) {
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_MAC);

            if(System.getProperty(PROFILE_PROP_KEY) != null && System.getProperty(PROFILE_PROP_KEY).equalsIgnoreCase("default")) {
                Log.info("Chrome with " + System.getProperty(PROFILE_PROP_KEY) + " profile will be created");
                chromeOptions.addArguments("user-data-dir=" + USER_HOME + "/Library/Application Support/Google/Chrome");
            }
        } else {
            Log.debug("Unable to find the correct chrome driver for your OS.");
        }

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return new ChromeDriver(capabilities);
    }

    private static WebDriver getIEBrowser() {
        Log.info("Getting IE webdriver");
        System.setProperty("webdriver.ie.driver", IE_DRIVER_WIN64);
        return new InternetExplorerDriver();
    }

    private static void setBrowserSettings(WebDriver driver) {
        Log.info("Set webdriver properties ");
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        driver.manage().window().setPosition(new Point(0, 0));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        org.openqa.selenium.Dimension dim = new org.openqa.selenium.Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
        driver.manage().window().setSize(dim);
    }

    public static void main(String[] args) {
        System.out.println(USER_PATH);
        System.out.println(System.getProperty("user.home"));
    }
}
