package mk.ukim.finki.wp.proekt.selenium;

import lombok.Getter;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Getter
public class MyProfilePage extends AbstractPage{

    @FindBy(className = "winner")
    private WebElement winner;

    @FindBy(className = "active1")
    private WebElement active;

    @FindBy(className = "finished1")
    private WebElement finished;

    @FindBy(className = "winnerWaitingDetails")
    private List<WebElement> winnerWaitingDetails;

    @FindBy(className = "activeDetails")
    private List<WebElement> activeDetails;

    @FindBy(className = "finishedDetails")
    private List<WebElement> finishedDetails;

    public MyProfilePage(WebDriver driver) {
        super(driver);
    }

    public void assertGiveaways(int winnerWaiting, int active, int finished) {
        Assert.assertEquals("winner waiting giveaways do not match", winnerWaiting, this.winnerWaitingDetails.size());
        Assert.assertEquals("active giveaways do not match", active, this.activeDetails.size());
        Assert.assertEquals("finished giveaways do not match", finished, this.finishedDetails.size());
    }

    public void winnerTab(WebDriver driver, MyProfilePage myProfilePage){
        myProfilePage.winner.click();
    }

    public void activeTab(WebDriver driver, MyProfilePage myProfilePage){
        myProfilePage.active.click();
    }

    public void finishedTab(WebDriver driver, MyProfilePage myProfilePage){
        myProfilePage.finished.click();
    }

    public DetailsGiveawayPage chooseWinner(WebDriver driver, MyProfilePage myProfilePage){
        myProfilePage.winnerWaitingDetails.get(0).click();
        return PageFactory.initElements(driver, DetailsGiveawayPage.class);
    }

}
