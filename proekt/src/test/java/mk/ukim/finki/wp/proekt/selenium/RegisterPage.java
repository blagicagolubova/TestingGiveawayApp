package mk.ukim.finki.wp.proekt.selenium;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

@Getter
public class RegisterPage extends AbstractPage {

    private WebElement username;

    private WebElement password;

    private WebElement repeatedPassword;

    private WebElement name;

    private WebElement surname;

    private WebElement email;

    private WebElement phone;

    private WebElement address;

    private WebElement submit;


    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public static LoginPage Register(WebDriver driver, RegisterPage registerPage, String username, String password,
                                    String repeatedPassword, String name, String surname, String email, String phone,
                                    String address) throws InterruptedException {
        registerPage.username.sendKeys(username);
        Thread.sleep(1000);
        registerPage.password.sendKeys(password);
        Thread.sleep(1000);
        registerPage.repeatedPassword.sendKeys(repeatedPassword);
        Thread.sleep(1000);
        registerPage.name.sendKeys(name);
        Thread.sleep(1000);
        registerPage.surname.sendKeys(surname);
        Thread.sleep(1000);
        registerPage.email.sendKeys(email);
        Thread.sleep(1000);
        registerPage.phone.sendKeys(phone);
        Thread.sleep(1000);
        registerPage.address.sendKeys(address);
        Thread.sleep(1000);
        registerPage.submit.click();
        System.out.println(driver.getCurrentUrl());
        return PageFactory.initElements(driver, LoginPage.class);
    }
}
