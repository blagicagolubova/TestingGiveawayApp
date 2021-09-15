package mk.ukim.finki.wp.proekt.selenium;

import lombok.Getter;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class AbstractPage {

    protected WebDriver driver;
    public final WebDriverWait wait;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
        driver.manage().window().setSize(new Dimension(1920, 1080));

    }

    //static void get(WebDriver driver, String relativeUrl) {
        //String url = System.getProperty("geb.build.baseUrl", "http://localhost:9999") + relativeUrl;
      //  driver.get(relativeUrl);
   // }

}
