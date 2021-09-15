package mk.ukim.finki.wp.proekt.selenium;

import lombok.Getter;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class DetailsGiveawayPage extends AbstractPage{

    private WebElement back;

    private WebElement winner;

    @FindBy(className = "message")
    private WebElement message;

    public DetailsGiveawayPage(WebDriver driver) {
        super(driver);
    }

    public void AssertMessage(String messageExpected){
        Assert.assertEquals(messageExpected,message.getText());
    }

    public GiveawayPage BackToGiveaways(WebDriver driver, DetailsGiveawayPage detailsGiveawayPage){
        detailsGiveawayPage.back.click();
        return PageFactory.initElements(driver, GiveawayPage.class);
    }

    public ChooseWinnerPage chooseWinner(WebDriver driver,DetailsGiveawayPage detailsGiveawayPage){
        detailsGiveawayPage.winner.click();
        return PageFactory.initElements(driver, ChooseWinnerPage.class );
    }
}
