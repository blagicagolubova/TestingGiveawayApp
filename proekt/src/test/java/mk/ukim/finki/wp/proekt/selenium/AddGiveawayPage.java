package mk.ukim.finki.wp.proekt.selenium;

import lombok.Getter;
import org.junit.experimental.theories.Theories;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

@Getter
public class AddGiveawayPage extends AbstractPage{

    private WebElement name;

    private WebElement startdate;

    private WebElement enddate;

    private WebElement award;

    private WebElement region;

    private WebElement country;

    private WebElement category;

    private WebElement user_type;

    private WebElement submit;


    public AddGiveawayPage(WebDriver driver) {
        super(driver);
    }

    public static GiveawayPage makeAGiveaway(WebDriver driver, AddGiveawayPage addGiveawayPage, String name,
                                             String startdate, String enddate, String award, String region,
                                             String country, String category, String user_type) throws InterruptedException {
        addGiveawayPage.name.sendKeys(name);
        Thread.sleep(500);
        addGiveawayPage.startdate.sendKeys(startdate);
        Thread.sleep(500);
        addGiveawayPage.enddate.sendKeys(enddate);
        Thread.sleep(500);
        Select awardSelect = new Select(addGiveawayPage.award);
        awardSelect.selectByVisibleText(award);
        Thread.sleep(500);
        Select regionSelect = new Select(addGiveawayPage.region);
        regionSelect.selectByVisibleText(region);
        Thread.sleep(500);
        Select countrySelect = new Select(addGiveawayPage.country);
        countrySelect.selectByVisibleText(country);
        Thread.sleep(500);
        Select categorySelect = new Select(addGiveawayPage.category);
        categorySelect.selectByVisibleText(category);
        Thread.sleep(500);
        Select userTypeSelect = new Select(addGiveawayPage.user_type);
        userTypeSelect.selectByVisibleText(user_type);
        Thread.sleep(500);
        addGiveawayPage.submit.click();
        Thread.sleep(1000);
        return PageFactory.initElements(driver,GiveawayPage.class);
    }
}
