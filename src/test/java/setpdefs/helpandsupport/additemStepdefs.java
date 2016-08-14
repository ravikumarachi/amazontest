package setpdefs.helpandsupport;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import pages.aem.classic.view.HomePageView;
import setpdefs.ScenarioHooks;
import utils.Browser;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class additemStepdefs {

    @Given("^I am on Amazon site$")
    public void iAmOnAmazonSite() {
        Browser.loadPage(ScenarioHooks.getProperty("AMAZON_URL"));
    }


    @When("^I search for \"([^\"]*)\"$")
    public void iSearchFor(String item)   {
        HomePageView homepageview = new HomePageView();
        Browser.sendKeys(homepageview.searchBox,item);
        List<WebElement> elements = homepageview.ListofItems;
        Browser.waitForElement(elements.get(0));
    }

    @And("^add the cheapest item to the basket$")
    public void addTheCheapestIPhoneToTheBasket()     {
        HomePageView homepageview = new HomePageView();
        Select dropdown = new Select(Browser.getCurrentDriver().findElement(By.id("sort")));
        dropdown.selectByVisibleText("Price: Low to High");
        List<WebElement> elements = homepageview.ListofItems;
        Browser.click(elements.get(0),elements.get(0));

        Browser.click(homepageview.cartButton,homepageview.cartButton);

        Browser.waitForElement(homepageview.proceedtoCheckoutBtn);
    }

    @Then("^\"([^\"]*)\" should be added to the basket$")
    public void shouldBeAddedToTheBasket(String item)     {
        HomePageView homepageview = new HomePageView();
        Browser.click(homepageview.basket);

        List<WebElement> elements = homepageview.productList;
        assertTrue(elements.get(0).getText().contains(item));
    }


    @Then("^I should see total price in the basket$")
    public void totalPriceOfTheBasketIs()     {
        HomePageView homepageview = new HomePageView();
        List<WebElement> elements = homepageview.productList;
        assertEquals(elements.size(),2);
        assertTrue(homepageview.checkoutPrice.isDisplayed());
    }
}
