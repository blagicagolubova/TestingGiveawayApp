package mk.ukim.finki.wp.proekt.selenium;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class HomePage extends AbstractPage{

    private WebElement make;

    private WebElement participate;

    private WebElement register;

    @FindBy(className = "myProfile")
    private WebElement myProfile;

    private WebElement awards;


    public HomePage(WebDriver driver) {
        super(driver);
    }

    public static GiveawayPage participateInAGiveaway(WebDriver driver, HomePage homePage) {
        homePage.participate.click();
        System.out.println(driver.getCurrentUrl());
        return PageFactory.initElements(driver, GiveawayPage.class);
    }

    public static MyProfilePage goToMyProfile(WebDriver driver, HomePage homePage){
        homePage.myProfile.click();
        System.out.println(driver.getCurrentUrl());
        return PageFactory.initElements(driver, MyProfilePage.class);
    }

    public static HomePage openHomePage(WebDriver driver) {
        driver.get("http://localhost:9999/home");
        System.out.println(driver.getCurrentUrl());
        return PageFactory.initElements(driver, HomePage.class);

    }

    public static RegisterPage goToRegisterPage(WebDriver driver, HomePage homePage){
       // homePage.icon.click();

        homePage.register.click();
        System.out.println(driver.getCurrentUrl());
        return PageFactory.initElements(driver,RegisterPage.class);
    }

    public static MyAwardsPage goToMyAwardsPage(WebDriver driver, HomePage homePage){
        homePage.awards.click();
        System.out.println(driver.getCurrentUrl());
        return PageFactory.initElements(driver,MyAwardsPage.class);
    }
}
