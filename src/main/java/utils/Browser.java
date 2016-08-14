package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Browser {
    private static WebDriver driver;
    public static int WAIT_SECONDS = 5;
    public static int WAIT_FOR_SECONDS = 15;

    /**
     * This method will get a browser session for executing your tests
     * If the -Dbrowser is not passed at run time, it gives you Chrome webdriver session
     * @return Returns a specific web driver browser
     */
    public synchronized static WebDriver getCurrentDriver() {
        if (driver == null) {
            try {
                driver = BrowserFactory.getBrowser();
            } catch (UnreachableBrowserException e) {
                driver = BrowserFactory.getBrowser();
            } catch (WebDriverException e) {
                Log.debug("Something went wrong, here is the exception message: " + e.getMessage());
                driver = BrowserFactory.getBrowser();
            }finally{
                Runtime.getRuntime().addShutdownHook(new Thread(new BrowserCleanup()));
            }
        }

        return driver;
    }

    /**
     * Use this method to close the browser
     */
    public static void close() {
        try {
            getCurrentDriver().quit();
            driver = null;
            Log.debug("closing the browser");
        } catch (UnreachableBrowserException e) {
            Log.debug("cannot close browser: unreachable browser");
        }
    }

    public static String getText(WebElement element) {
        return element.getText();
    }

    public static String getAttribute(WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    /**
     * Use this method to click on desired webelement
     * @param element   Name of the element to be clicked
     */
    public static void click(WebElement element) {
        waitForElementToBeClickable(element, null);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView()", element);

        Actions action = new Actions(driver);
        action.moveToElement(element).click().perform();
        Log.debug("clicked element: " + element);
    }

    public static void click(String xpath) {
        Actions action = new Actions(driver);

        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            waitForElementToBeClickable(element, null);
            action.moveToElement(element).click().perform();
            Log.debug("clicked element: " + element);
        } catch (StaleElementReferenceException e) {
            Log.debug("Element is not attached to the page document " + e.getMessage());
        } catch (NoSuchElementException e) {
            Log.debug("Element was not found in DOM " + e.getMessage());
        } catch (Exception e) {
            Log.debug("Element was not clickable " + e.getMessage());
        }
    }

    /*public static void clickJS(String xpath) {
        WebElement element = driver.findElement(By.xpath(xpath));
        waitForElementToBeClickable(element, null);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        //js.executeScript("arguments[0].scrollIntoView()", element);
        js.executeScript("arguments[0].click();", element);
        //js.executeScript("var evt = document.createEvent('MouseEvents');" + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);" + "arguments[0].dispatchEvent(evt);", element);
    }*/

    /**
     * Use this method to click on desired webelement and wait after clicking the element
     * @param element           Name of the element to be clicked
     * @param waitForElement    Name of the element to wait for after clicking
     */
    public static void click(WebElement element, WebElement waitForElement) {
        waitForElementToBeClickable(element, null);
        Actions action = new Actions(driver);
        action.moveToElement(element).click().perform();
        Log.debug("clicked element: " + element);
        waitForElementToBeClickable(waitForElement, null);
    }

    /**
     * Use this method to double click on an element
     * @param element     Webelement to double click
     */
    public static void doubleClick(WebElement element) {
        try {
            Actions action = new Actions(driver).moveToElement(element).doubleClick(element);
            action.build().perform();
            waitFor(4000);
            Log.debug("Double clicked element: " + element);
        } catch (StaleElementReferenceException e) {
            Log.debug("Element is not attached to the page document " + e.getMessage());
        } catch (NoSuchElementException e) {
            Log.debug("Element was not found in DOM " + e.getMessage());
        } catch (Exception e) {
            Log.debug("Element was not clickable " + e.getMessage());
        }
    }

    /**
     * Use this method to double click on an element
     * @param elementXpath     element xpath as string
     */
    public static void doubleClick(String elementXpath) {
        try {
            WebElement element = driver.findElement(By.xpath(elementXpath));
            Actions action = new Actions(driver);
            action.moveToElement(element).doubleClick(element).build().perform();
            waitFor(4000);
            Log.debug("Double clicked element: " + element);
        } catch (StaleElementReferenceException e) {
            Log.debug("Element is not attached to the page document " + e.getMessage());
        } catch (NoSuchElementException e) {
            Log.debug("Element was not found in DOM " + e.getMessage());
        } catch (Exception e) {
            Log.debug("Element was not clickable " + e.getMessage());
        }
    }

    public static void mouseOver(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().build().perform();
    }

    public static void moveMouseTo(String xpath) {
        try {
            Actions action = new Actions(driver);
            WebElement element = driver.findElement(By.xpath(xpath));
            Point coordinates = element.getLocation();
            action.moveByOffset(coordinates.getX(), coordinates.getY()).build().perform();
            waitFor(4000);
            action.doubleClick();
            Log.debug("Mouse moved to: " + element);
        } catch (NoSuchElementException e) {
            Log.debug("Element was not found in DOM " + e.getMessage());
        } catch (Exception e) {
            Log.debug("Something went wrong " + e.getStackTrace().toString());
        }
    }

    /*public static void doubleClickForTouch() {
        Actions action = new Actions(driver);
        Robot robot = null;
        try {
            robot = new Robot();
            //Point coordinates = driver.findElement(By.xpath(xpath)).getLocation();
            //robot.mouseMove(coordinates.getX(),coordinates.getY()+150);
            //waitFor(3000);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            waitFor(10);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            //robot.mousePress(InputEvent.BUTTON1_MASK);
            //waitFor(10);
            //robot.mouseRelease(InputEvent.BUTTON1_MASK);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Use this method to right click on a element to open the content menu
     * @param element   Name of the element on which right click to be performed
     */
    public static void rightClick(WebElement element) {
        try {
            click(element);
            Actions action = new Actions(driver).contextClick(element);
            action.build().perform();
            waitFor(4000);
            Log.debug("Right clicked on element: " + element);
        } catch (StaleElementReferenceException e) {
            Log.debug("Stale Element " + e.getMessage());
        } catch (NoSuchElementException e) {
            Log.debug("Element " + element + " was not found " + e.getMessage());
        } catch (Exception e) {
            Log.debug("Element " + element + " was not clickable " + e.getMessage());
        }
    }

    public static void selectAll(By by) {
        try {
            WebElement element = driver.findElement(by);
            element.sendKeys(Keys.HOME);
            int contentLength = element.getText().length();

            for(int i=0; i<contentLength; i++) {
                element.sendKeys(Keys.LEFT_SHIFT, Keys.ARROW_RIGHT);
            }
        } catch (NoSuchElementException e) {
            Log.debug("Element not found: " + e.getMessage());
        }
    }

    private static class BrowserCleanup implements Runnable {
        public void run() {
            Log.debug("BrowserCleanup");
            close();
        }
    }

    /**
     * Use this method to load a particular URL
     * @param url Name of the URL to load
     */
    public static void loadPage(String url){
        Log.debug("Directing browser to:" + url);
        Log.debug("Load page: [" + url + "]");
        getCurrentDriver().get(url);
    }

    /**
     * Use this method to reopen the current browser session and load the URL
     * @param url Name of the URL to load
     */
    public static void reopenAndLoadPage(String url) {
        driver = null;
        getCurrentDriver();
        loadPage(url);
    }

    public static WebElement waitForElement(WebElement elementToWaitFor){
        return waitForElement(elementToWaitFor, null);
    }

    public static WebElement waitForElement(WebElement elementToWaitFor, Integer waitTimeInSeconds) {
        Log.debug("waitForElement: " + elementToWaitFor);
        if (waitTimeInSeconds == null) {
            waitTimeInSeconds = WAIT_FOR_SECONDS;
        }

        WebDriverWait wait = new WebDriverWait(getCurrentDriver(), waitTimeInSeconds);
        return wait.until(ExpectedConditions.visibilityOf(elementToWaitFor));
    }

    /**
     * Use this method to wait until the element can be clicked
     * @param elementToWaitFor      element to wait for
     * @param waitTimeInSeconds     wait in seconds
     * @return                      Returns an web element
     */
    public static WebElement waitForElementToBeClickable(WebElement elementToWaitFor, Integer waitTimeInSeconds) {
        Log.debug("Waiting for element to be clickable: " + elementToWaitFor);
        Log.debug("elementToWaitFor: " + elementToWaitFor.toString());
        if (waitTimeInSeconds == null) {
            waitTimeInSeconds = WAIT_FOR_SECONDS;
        }

        WebDriverWait wait = new WebDriverWait(getCurrentDriver(), waitTimeInSeconds);
        wait.withMessage("Waiting for the element to be clickable");
        return wait.until(ExpectedConditions.elementToBeClickable(elementToWaitFor));
    }

    public static WebElement getParent(WebElement element) {
        return element.findElement(By.xpath(".."));
    }

    /**
     * Get a node's previous node (equivalent to DOM's "previousSibling" attribute)
     */
    public static WebElement getPreviousSibling(WebElement element) {
        Object response = ((JavascriptExecutor) driver).executeScript("return arguments[0].previousSibling", element);
        if (response instanceof WebElement) {
            return (WebElement) response;
        } else {
            return null;
        }
    }

    public static List<WebElement> getDropDownOptions(WebElement webElement) {
        Select select = new Select(webElement);
        return select.getOptions();
    }

    public static WebElement getDropDownOption(WebElement webElement, String value) {
        WebElement option = null;
        List<WebElement> options = getDropDownOptions(webElement);
        for (WebElement element : options) {
            if (element.getAttribute("value").equalsIgnoreCase(value)) {
                option = element;
                break;
            }
        }
        return option;
    }

    public static String getPageHTML() {
        return getCurrentDriver().getPageSource();
    }

    /*public static void click(WebElement element) {
        waitForElementToBeClickable(element, null);
        element.click();
        Log.debug("clicked element");
    }*/

    public static void sendKeys(By by, String text) {
        try {
            WebElement element = driver.findElement(by);
            element.clear();
            Log.info("Typing text: " + text);
            typeChar(element, text);
        } catch (NoSuchElementException e) {
            Log.debug("Element not found: " + e.getMessage());
        }
    }

    /*public static void sendKeys(WebElement element, String text) {
        waitForElement(element);
        JavascriptExecutor jsExecutor = ((JavascriptExecutor) driver);
        jsExecutor.executeScript("arguments[0].value='"+text+"';", element);
        waitFor(200);
    }*/

    public static void sendKeys(WebElement element, String text) {
        Log.info("Typing text: " + text);
        waitForElement(element);
        click(element);
        element.clear();
        typeChar(element, text);
    }

    private static void typeChar(WebElement element, String text) {
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            String s = new StringBuilder().append(c).toString();
            element.sendKeys(s);
            waitFor(5);
        }
    }

    /**
     * Use this method to press a desired key
     * @param key
     */
    public static void press(Keys key){
        new Actions(getCurrentDriver()).keyDown(key).keyUp(key).perform();
    }

    /**
     * Use this method to switch the focus to iFrame
     * @param element       frame webelement
     */
    public static void switchToFrame(WebElement element) {
        Log.debug("Switching to frame: " + element);
        try {
            waitForElement(element);
            driver.switchTo().frame(element);
            Log.debug("Switched to frame: " + element);
        } catch (NoSuchFrameException e) {
            Log.debug("Unable to locate frame with element " + element + e.getStackTrace());
        } catch (StaleElementReferenceException e) {
            Log.debug("Element with " + element + "is not attached to the page document" + e.getStackTrace());
        } catch (Exception e) {
            Log.debug("Unable to navigate to frame with element " + element + e.getStackTrace());
        }
    }

    public static void switchToParentFrame() {
        Log.debug("Switching to Parent Frame");
        driver.switchTo().parentFrame();
        Log.debug("Switched to Parent Frame");
    }

    /**
     * Use this method to wait for specified milliseconds
     * @param millis Milliseconds to wait for
     */
    public static void waitFor(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Use this method to get a XPATH of the desired webelement
     * @param element       Name of the element for which XPATH is needed
     * @return              Returns the XPATH as string
     */
    public static String getXPath(WebElement element) {
        String jscript = "function getPathTo(node){" +
                " var stack = [];" +
                " while(node.parentNode !== null) {" +
                "  stack.unshift(node.tagName + '[id=' + node.id + ',class=' + node.className + ']');" +
                "  node = node.parentNode;" +
                " }" +
                " return stack.join('/');" +
                "}" +
                "return getPathTo(arguments[0]);";
        return (String) ((JavascriptExecutor) driver).executeScript(jscript, element);
    }

    /**
     * For some reason, getting children using By.CssSelector is very slow with WebElement... this is so much faster
     */
    public static List<WebElement> getChildren(WebElement element) {
        String jscript = "return arguments[0].childNodes";
        List<Object> children = (List<Object>) ((JavascriptExecutor) driver).executeScript("return arguments[0].childNodes", element);
        List<WebElement> elements = new ArrayList<WebElement>();
        for (Object child : children) {
            if (child instanceof WebElement) {
                elements.add((WebElement) child);
            }
        }
        return elements;
    }

    public static boolean isDisplayed(WebElement element){
        int numberOfIterations = WAIT_SECONDS*5;
        for(int i= 0; i<numberOfIterations; i++){
            if(element.isDisplayed()){
                return true;
            }else{
                waitFor(200);
            }
        }
        return false;
    }

    public static boolean isDisplayed(String elementStr) {
        try {
            WebElement element = driver.findElement(By.xpath(elementStr));
            return isDisplayed(element);
        } catch (NoSuchElementException e) {
            Log.debug("Unable to locate the element " + elementStr + e.getStackTrace());
        }
        return false;
    }

    public static boolean isNotDisplayed(WebElement element){
        Log.debug("isNotDisplayed: " + element);
        int numberOfIterations = WAIT_SECONDS*5;
        //if element is deleted return true (i.e catch noSuchElementException)
        try{
            for(int i= 0; i<numberOfIterations; i++){
                if(element.isDisplayed()){
                    Log.debug("Waiting for 200ms");
                    waitFor(200);
                }else{
                    return true;
                }
            }
            return false;
        }catch(NoSuchElementException e){
            return true;
        }catch(StaleElementReferenceException e){
            return true;
        }
    }

    /**
     * Use this method to wait for emelemt to disappear from the screen
     * @param xpath     XPATH to the element
     * @return          true if the element is present even after 15 secs, otherwise false
     */
    public static boolean waitForElementToDisappear(String xpath) {
        Log.debug("waitForElementToDisappear: " + xpath);

        try {
            WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);

            boolean elementStatus = webDriverWait
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(NoSuchElementException.class)
                    .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));

            return elementStatus;
        } catch (Exception e) {
            Log.debug("Either element still visible on page or something went wrong: " + e.getMessage());
            return false;
        }
    }

    /**
     * Use this method to wait for a particular element on screen
     * @param xpath     XPATH string to the element on screen
     */
    public static void waitForElement(String xpath) {
        Log.debug("waitForElement: " + xpath);

        WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);

        webDriverWait
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .withMessage("Waiting for the element to be clickable")
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    /**
     * Use this method to wait for a particular text to appear on screen
     * @param element       Webelement where text will be shown
     * @param text          Text to be matched
     */
    public static void waitForText(final WebElement element, String text) {
        Log.debug("waitForText: " + text);
        WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
        webDriverWait
                .withMessage("Waiting for the text: " + text)
                .until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    /**
     * Use this method to wait for a particular text to appear on screen
     * @param text      Text to be matched
     */
    public static boolean waitForText(final String text) {
        Log.debug("waitForText: " + text);
            WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
        webDriverWait
                .withMessage("Waiting for the text: " + text)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'" + text + "')]")));
        return true;
    }




    /**
     * This method will wait for both Javascript and jQuery to finish loading
     * @return
     */
    public static boolean waitForPageLoad() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_SECONDS);

        wait.withMessage("Wait for loading... to disappear").until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'x-mask-loading']")));

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long)((JavascriptExecutor)driver).executeScript("return jQuery.active") == 0);
                }
                catch (Exception e) {
                    // no jQuery present
                    return true;
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor)driver).executeScript("return document.readyState").toString().equals("complete");
            }
        };

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }

    public static void waitForTitle(String title) {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
        wait.until(ExpectedConditions.titleContains(title));
    }

    /**
     * USe this method to execute JavaScript
     * @param jsCode JavaScript snippet to be executed by web driver
     */
    public static void executeJS(String jsCode){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(jsCode);
    }

    public static void openNewTab() {
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
    }

    /**
     * Use this method to switch to tab next to current one
     */
    public static void switchToTab() {
        //Switching between tabs using CTRL + tab keys.
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"\t");

        Log.debug("Switched to TAB with its Title: " + driver.getTitle());

        //Switch to current selected tab's content.
        driver.switchTo().defaultContent();
    }
    /**
     * Use this method to click YES on the confirmation popup
     */
    public static void confirm() {
        /*try {
            WebDriverWait webDriverWait = new WebDriverWait(getCurrentDriver(),2);
            webDriverWait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            Log.debug("Alert text is " + alert.getText());
            alert.accept();
        } catch (NoAlertPresentException e) {
            Log.debug("");
        } catch (Exception e) {
            Log.debug("Something went wrong: " + e.getMessage());
        }*/
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).build().perform();
    }

    public static int getFrameCount() {
        int totalFrames = Browser.getCurrentDriver().findElements(By.xpath("//iframe")).size();
        return totalFrames;
    }

    public static WebElement getCurrentFrameElement() {
        WebElement element = (WebElement) ((JavascriptExecutor) driver).executeScript("return window.frameElement");
        return element;
    }

    //((JavascriptExecutor) Browser.getCurrentDriver()).executeScript("return self.location.toString()")

    /**
     * Use this method to compare two lists contains Map
     * @param l1 List of Values read from CSV
     * @param l2 List of Values read from Contact Details page
     * @return true/false if both equals/not equals
     */
    public static boolean equalsTwoList(List<Map<String, String>> l1, List<Map<String, String>> l2) {
        if (l1.size() != l2.size()) {
            return false;
        }
        if (!l1.equals(l2)) return false;
        return true;
    }

    /**
     * Use this method to compare two lists of String
     * @param l1 List of Values read from CSV
     * @param l2 List of Values read from Contact Details page
     * @return true/false if both equals/not equals
     */
    public static boolean isTwoListEquals(List<String> l1, List<String> l2) {
        if (l1.size() != l2.size()) {
            return false;
        }
        Collections.sort(l1);
        Collections.sort(l2);
        if (!l1.equals(l2)) return false;
        return true;
    }

    public static WebElement getWebElement(String elementStr ) {
        try {
            return driver.findElement(By.xpath(elementStr));
        } catch (NoSuchElementException e) {
            Log.debug("Unable to locate frame with element " + elementStr + e.getStackTrace());
            return null;
        }
    }
}
