package mk.ukim.finki.wp.proekt.selenium;

import lombok.Getter;
import mk.ukim.finki.wp.proekt.model.Giveaway;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Getter
public class GiveawayPage extends AbstractPage{

    @FindBy(className = "card")
    private List<WebElement> Cards;

    @FindBy(className = "details")
    private List<WebElement> DetailsButtons;

    @FindBy(className = "participate")
    private List<WebElement> ParticipateButtons;

    @FindBy(className = "myProfile")
    private WebElement myProfile;

    private WebElement addGiveaway;

    public GiveawayPage(WebDriver driver) {
        super(driver);
    }

    public void assertElements(int cardsNumber, int detailsButtons, int participateButtons) {
        Assert.assertEquals("cards do not match", cardsNumber, this.getCards().size());
        Assert.assertEquals("details Buttons do not match", detailsButtons, this.getDetailsButtons().size());
        Assert.assertEquals("edit Buttons do not match", participateButtons, this.getParticipateButtons().size());
    }

    public DetailsGiveawayPage ParticipateInAGiveaway(WebDriver driver, GiveawayPage giveawayPage){
        giveawayPage.getParticipateButtons().get(0).click();
        return PageFactory.initElements(driver, DetailsGiveawayPage.class);
    }

    public static AddGiveawayPage goToAddGiveawayPage(WebDriver driver, GiveawayPage giveawayPage){
        giveawayPage.addGiveaway.click();
        return PageFactory.initElements(driver,AddGiveawayPage.class);
    }

    public static MyProfilePage goToMyProfile(WebDriver driver, GiveawayPage giveawayPage){
        giveawayPage.myProfile.click();
        System.out.println(driver.getCurrentUrl());
        return PageFactory.initElements(driver, MyProfilePage.class);
    }

}
