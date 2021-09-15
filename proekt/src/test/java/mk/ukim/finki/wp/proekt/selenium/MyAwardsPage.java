package mk.ukim.finki.wp.proekt.selenium;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

@Getter
public class MyAwardsPage extends AbstractPage{

    private WebElement addAward;

    private WebElement activeGiveaways;

    public MyAwardsPage(WebDriver driver) {
        super(driver);
    }

    public AddAwardPage goToAddAwardPage(WebDriver driver,MyAwardsPage myAwardsPage){
        myAwardsPage.addAward.click();
        System.out.println(driver.getCurrentUrl());
        return PageFactory.initElements(driver, AddAwardPage.class);
    }

    public static GiveawayPage goToGiveawayPage(WebDriver driver,MyAwardsPage myAwardsPage){
        myAwardsPage.activeGiveaways.click();
        return PageFactory.initElements(driver,GiveawayPage.class);
    }


}
