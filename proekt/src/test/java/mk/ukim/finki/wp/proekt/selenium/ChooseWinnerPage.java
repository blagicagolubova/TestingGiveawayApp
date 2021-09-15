package mk.ukim.finki.wp.proekt.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ChooseWinnerPage extends AbstractPage{

    private WebElement back;

    public ChooseWinnerPage(WebDriver driver) {
        super(driver);
    }

    public MyProfilePage BackToMyProfile(WebDriver driver, ChooseWinnerPage chooseWinnerPage){
        chooseWinnerPage.back.click();
        return PageFactory.initElements(driver, MyProfilePage.class);
    }
}
