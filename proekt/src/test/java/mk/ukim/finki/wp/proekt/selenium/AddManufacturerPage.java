package mk.ukim.finki.wp.proekt.selenium;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

@Getter
public class AddManufacturerPage extends AbstractPage{

    private WebElement name;

    private WebElement description;

    private WebElement submit;

    public AddManufacturerPage(WebDriver driver) {
        super(driver);
    }

    public static AddAwardPage addManufacturer(WebDriver driver, AddManufacturerPage addManufacturer, String name,
                                               String description) throws InterruptedException {
        addManufacturer.name.sendKeys(name);
        Thread.sleep(1000);
        addManufacturer.description.sendKeys(description);
        Thread.sleep(1000);
        addManufacturer.submit.click();
        Thread.sleep(1000);
        return PageFactory.initElements(driver,AddAwardPage.class);
    }
}
