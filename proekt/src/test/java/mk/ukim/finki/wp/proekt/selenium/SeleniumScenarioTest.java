package mk.ukim.finki.wp.proekt.selenium;

import mk.ukim.finki.wp.proekt.model.*;
import mk.ukim.finki.wp.proekt.model.enumerations.Continent;
import mk.ukim.finki.wp.proekt.model.enumerations.GiveawayStatus;
import mk.ukim.finki.wp.proekt.model.enumerations.UserType;
import mk.ukim.finki.wp.proekt.repository.GiveawayRepository;
import mk.ukim.finki.wp.proekt.sevice.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

public class SeleniumScenarioTest {

    //private HtmlUnitDriver driver;

    private WebDriver webDriver;

    @Autowired
    UserService userService;

    @Autowired
    GiveawayService giveawayService;

    @Autowired
    GiveawayRepository giveawayRepository;

    @Autowired
    AwardService awardService;

    @Autowired
    CountryService countryService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    GiveawayRegionService giveawayRegionService;

    @Autowired
    ManufacturerService manufacturerService;

    @Autowired
    RegionService regionService;

    private static User regularUser1;
    private static User regularUser2;
    private static User adminUser;
    private static Award award1;
    private static Award award2;
    private static Award award3;
    private static Manufacturer manufacturer;
    private static Country country;
    private static Region region;
    private static GiveawayRegion giveawayRegion;
    public static Category category;
    private static Giveaway giveaway1;
    private static Giveaway giveaway2;
    private static Giveaway giveaway3;
    private static boolean dataInitialized = false;



    @BeforeEach
    private void setup() throws ParseException {
       // this.driver = new HtmlUnitDriver(true);
        this.webDriver = getDriver();
        initData();
    }

    private WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\DELL\\Downloads\\chromedriver_win32\\chromedriver.exe");
        //C:\Users\DELL\Downloads\chromedriver_win32
        return new ChromeDriver();
    }
    @AfterEach
    public void destroy() {
       // if (this.driver != null) {
       //     this.driver.close();
       // }
        webDriver.quit();
    }

    private void initData() throws ParseException {
        if (!dataInitialized) {
            manufacturer = manufacturerService.save("m1", "m1");
            regularUser1 = userService.save("user1", "user", "user", "user", "user","user", "000", "user");
            regularUser2 = userService.save("user2", "user", "user", "user", "user","user", "000", "user");
            country = countryService.save("Macedonia", "MKD", Continent.EUROPE);
            List<Integer> countriesIds = new ArrayList<>();
            countriesIds.add(country.getId());
            region = regionService.save("Balkan",countriesIds);
            List<Country> countries = region.getCountries();

            giveawayRegion = giveawayRegionService.save(countriesIds);
            award1 = awardService.save("Ball", (float) 70,
                    "https://www.bcf.com.au/dw/image/v2/BBRV_PRD/on/demandware.static/-/Sites-srg-internal-master-catalog/default/dw20bfd25f/images/520209/BCF_520209_hi-res.jpg?sw=558&sh=558&sm=fit&q=90",
                    manufacturer.getId(), regularUser2.getUsername());
            award2 = awardService.save("Beach ball", (float) 50,
                    "https://www.bcf.com.au/dw/image/v2/BBRV_PRD/on/demandware.static/-/Sites-srg-internal-master-catalog/default/dw20bfd25f/images/520209/BCF_520209_hi-res.jpg?sw=558&sh=558&sm=fit&q=90",
                    manufacturer.getId(), regularUser2.getUsername());
            category = categoryService.save("sport", "sport category");
            giveaway1 = giveawayService.save("Ball Giveaway",
                    new SimpleDateFormat("dd/MM/yyyy").parse("20/08/2021"),
                    new SimpleDateFormat("dd/MM/yyyy").parse("20/11/2021"), category.getId(), award1.getId(),
                    UserType.PRIVATE, regularUser2.getUsername(), giveawayRegion.getId(),null );
            giveaway2 = giveawayService.save("Beach Ball Giveaway",
                    new SimpleDateFormat("dd/MM/yyyy").parse("20/08/2021"),
                    new SimpleDateFormat("dd/MM/yyyy").parse("20/11/2021"), category.getId(), award2.getId(),
                    UserType.PRIVATE, regularUser2.getUsername(), giveawayRegion.getId(),null );
            giveaway3 = giveawayService.save("Beach Ball",
                    new SimpleDateFormat("dd/MM/yyyy").parse("20/08/2021"),
                    new SimpleDateFormat("dd/MM/yyyy").parse("5/9/2021"), category.getId(), award2.getId(),
                    UserType.PRIVATE, regularUser2.getUsername(), giveawayRegion.getId(),null );
            List<User> participants = new ArrayList<>();
            participants.add(regularUser1);
            giveaway3.setParticipants(participants);
            giveaway3.setStatus(GiveawayStatus.FINISHED);
            giveawayRepository.save(giveaway3);
            giveaway3 = new Giveaway();
            //  adminUser = userService.register(admin, admin, admin, admin, admin, Role.ROLE_ADMIN);
            dataInitialized = true;
       }
    }

    @Test
    public void testTheFirstScenario() throws Exception {

        LoginPage loginPage = LoginPage.openLogin(this.webDriver);
        Thread.sleep(2500);
        HomePage homePage = LoginPage.doLogin(this.webDriver, loginPage , regularUser1.getUsername(), "user");
        Thread.sleep(2500);
        GiveawayPage giveawayPage = HomePage.participateInAGiveaway(this.webDriver, homePage);
        Thread.sleep(4500);


        giveawayPage.assertElements(2,2,2);


        DetailsGiveawayPage detailsGiveawayPage = giveawayPage.ParticipateInAGiveaway(this.webDriver, giveawayPage);
        Thread.sleep(5000);


        detailsGiveawayPage.AssertMessage("You have successfully participate for this giveaway");


        giveawayPage = detailsGiveawayPage.BackToGiveaways(this.webDriver, detailsGiveawayPage);
        Thread.sleep(4500);
        detailsGiveawayPage = giveawayPage.ParticipateInAGiveaway(this.webDriver, giveawayPage);
        Thread.sleep(5500);


        detailsGiveawayPage.AssertMessage("You have already set your participation for this giveaway");


        giveawayPage = detailsGiveawayPage.BackToGiveaways(this.webDriver, detailsGiveawayPage);
        Thread.sleep(3000);
        loginPage=  LoginPage.logout(this.webDriver);
        Thread.sleep(3000);
        homePage = LoginPage.doLogin(this.webDriver, loginPage , regularUser2.getUsername(), "user");
        Thread.sleep(3000);
        MyProfilePage myProfilePage = HomePage.goToMyProfile(this.webDriver, homePage);
        Thread.sleep(3000);
        myProfilePage.activeTab(this.webDriver,myProfilePage);
        Thread.sleep(3000);
        myProfilePage.finishedTab(this.webDriver,myProfilePage);
        Thread.sleep(3000);
        myProfilePage.winnerTab(this.webDriver,myProfilePage);


        myProfilePage.assertGiveaways(1,2,0);


        Thread.sleep(3000);
        DetailsGiveawayPage detailsGiveawayPage1 = myProfilePage.chooseWinner(this.webDriver,myProfilePage);
        Thread.sleep(3000);
        ChooseWinnerPage chooseWinnerPage=detailsGiveawayPage1.chooseWinner(this.webDriver,detailsGiveawayPage1);
        Thread.sleep(3000);
        myProfilePage=chooseWinnerPage.BackToMyProfile(this.webDriver,chooseWinnerPage);


        myProfilePage.assertGiveaways(0,2,1);


        Thread.sleep(1500);
        myProfilePage.activeTab(this.webDriver,myProfilePage);
        Thread.sleep(1500);
        myProfilePage.finishedTab(this.webDriver,myProfilePage);
        Thread.sleep(1500);
        myProfilePage.winnerTab(this.webDriver,myProfilePage);
        Thread.sleep(1500);
        loginPage=  LoginPage.logout(this.webDriver);
        Thread.sleep(4000);

    }

    @Test
    public void testWWTheSecondScenario() throws InterruptedException {
        HomePage homePage = HomePage.openHomePage(this.webDriver);
        RegisterPage registerPage = HomePage.goToRegisterPage(this.webDriver,homePage);
        LoginPage loginPage = RegisterPage.Register(this.webDriver,registerPage,"user3","user",
                "user", "user3","user3","user3@user3.com","000111222",
                "user3");
        Thread.sleep(2000);
        homePage = LoginPage.doLogin(this.webDriver,loginPage,"user3","user");
        Thread.sleep(1500);
        MyProfilePage myProfilePage = HomePage.goToMyProfile(this.webDriver, homePage);
        Thread.sleep(1500);
        myProfilePage.activeTab(this.webDriver,myProfilePage);
        Thread.sleep(1500);
        myProfilePage.finishedTab(this.webDriver,myProfilePage);
        Thread.sleep(1500);
        myProfilePage.winnerTab(this.webDriver,myProfilePage);
        Thread.sleep(1500);


        myProfilePage.assertGiveaways(0,0,0);


        homePage = HomePage.openHomePage(this.webDriver);
        MyAwardsPage myAwardsPage=HomePage.goToMyAwardsPage(this.webDriver,homePage);
        Thread.sleep(2000);
        AddAwardPage addAwardPage = myAwardsPage.goToAddAwardPage(this.webDriver,myAwardsPage);
        Thread.sleep(2000);
        AddManufacturerPage addManufacturerPage = AddAwardPage.goToAddManufacturer(this.webDriver,addAwardPage);
        addAwardPage = AddManufacturerPage.addManufacturer(this.webDriver,addManufacturerPage, "Babolat","Tennis equipment");
        Thread.sleep(2000);
        myAwardsPage = AddAwardPage.AddAward(this.webDriver, addAwardPage, "Babolat Drive Racket", "305",
                "https://www.tennispro.eu/media/catalog/product/cache/5/thumbnail/1200x/9df78eab33525d08d6e5fb8d27136e95/1/0/101431_1_11.jpg","Babolat");
        Thread.sleep(2000);
        GiveawayPage giveawayPage = MyAwardsPage.goToGiveawayPage(this.webDriver,myAwardsPage);
        Thread.sleep(2000);
        AddGiveawayPage addGiveawayPage = GiveawayPage.goToAddGiveawayPage(this.webDriver,giveawayPage);
        Thread.sleep(2000);
        giveawayPage = AddGiveawayPage.makeAGiveaway(this.webDriver,addGiveawayPage,"Babolat giveaway",
                "08092021","15102021","Babolat Drive Racket","Balkan", "Macedonia",
                "sport","PRIVATE");
        Thread.sleep(1000);
        myProfilePage = GiveawayPage.goToMyProfile(this.webDriver,giveawayPage);
        Thread.sleep(1000);
        myProfilePage.activeTab(this.webDriver,myProfilePage);
        Thread.sleep(1000);
        myProfilePage.finishedTab(this.webDriver,myProfilePage);
        Thread.sleep(1000);
        myProfilePage.winnerTab(this.webDriver,myProfilePage);
        Thread.sleep(1000);


        myProfilePage.assertGiveaways(0,1,0);


        Thread.sleep(1000);
        loginPage=  LoginPage.logout(this.webDriver);
        Thread.sleep(4000);

    }

}
