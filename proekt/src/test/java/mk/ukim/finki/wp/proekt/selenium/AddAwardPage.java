package mk.ukim.finki.wp.proekt.selenium;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

@Getter
public class AddAwardPage extends AbstractPage{

    private WebElement name;
    private WebElement weight;
    private WebElement url;
    private WebElement manufacturer;
    private WebElement addManufacturer;
    private WebElement submit;

    public AddAwardPage(WebDriver driver) {
        super(driver);
    }

    public static AddManufacturerPage goToAddManufacturer(WebDriver driver, AddAwardPage addAwardPage){
        addAwardPage.addManufacturer.click();
        return PageFactory.initElements(driver, AddManufacturerPage.class);
    }
    public static MyAwardsPage AddAward(WebDriver driver, AddAwardPage addAwardPage, String name, String weight,
                                        String url, String manufacturer) throws InterruptedException {
        addAwardPage.name.sendKeys(name);
        Thread.sleep(1000);
        addAwardPage.weight.sendKeys(weight);
        Thread.sleep(1000);
        addAwardPage.url.sendKeys(url);
        Thread.sleep(1000);
        Select manufacturerSelect = new Select(addAwardPage.manufacturer);
        manufacturerSelect.selectByVisibleText(manufacturer);
        Thread.sleep(1000);
        addAwardPage.submit.click();
        Thread.sleep(1000);
        return PageFactory.initElements(driver,MyAwardsPage.class);
    }


}
