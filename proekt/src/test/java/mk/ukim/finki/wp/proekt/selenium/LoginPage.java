package mk.ukim.finki.wp.proekt.selenium;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

@Getter
public class LoginPage extends AbstractPage {

    private WebElement username;

    private WebElement password;

    private WebElement submit;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public static LoginPage openLogin(WebDriver driver) {
        driver.get("http://localhost:9999/login");
        System.out.println(driver.getCurrentUrl());
        return PageFactory.initElements(driver, LoginPage.class);

    }

    public static HomePage doLogin(WebDriver driver, LoginPage loginPage, String username, String password) throws InterruptedException {
        loginPage.username.sendKeys(username);
        Thread.sleep(1000);
        loginPage.password.sendKeys(password);
        Thread.sleep(1000);
        loginPage.submit.click();
        System.out.println(driver.getCurrentUrl());
        return PageFactory.initElements(driver, HomePage.class);
    }

    public static LoginPage logout(WebDriver driver) {
        driver .get("http://localhost:9999/logout");
        return PageFactory.initElements(driver, LoginPage.class);
    }


}
