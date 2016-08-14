package pages.aem.classic.view;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class HomePageView {
    @FindBy(how = How.XPATH, xpath = "//*[contains(@id, 'twotabsearchtextbox')]")
    public WebElement searchBox;

    @FindBy(how = How.XPATH, xpath = "//*[contains(@class, 's-access-image cfMarker')]")
    public List<WebElement> ListofItems;

    @FindBy(how = How.XPATH, xpath = "//*[contains(@class, 'add-to-cart-button')]")
    public WebElement cartButton;

    @FindBy(how = How.XPATH, xpath = "//*[contains(@class, 'hlb-ptc-btn-native')]")
    public WebElement proceedtoCheckoutBtn;

    @FindBy(how = How.XPATH, xpath = "//*[contains(@class, 'nav-cart-icon nav-sprite')]")
    public WebElement basket;

    @FindBy(how = How.XPATH, xpath = "//*[contains(@class, '-link-normal sc-product-link')]")
    public  List<WebElement> productList;

@FindBy(how = How.XPATH, xpath = "//*[contains(@class, 'a-color-price a-text-bold')]")
    public WebElement checkoutPrice;



}
