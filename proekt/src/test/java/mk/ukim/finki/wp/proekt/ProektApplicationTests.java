package mk.ukim.finki.wp.proekt;

import com.fasterxml.jackson.databind.ObjectMapper;
import mk.ukim.finki.wp.proekt.model.*;
import mk.ukim.finki.wp.proekt.model.enumerations.Continent;
import mk.ukim.finki.wp.proekt.model.enumerations.Role;
import mk.ukim.finki.wp.proekt.model.enumerations.UserType;
import mk.ukim.finki.wp.proekt.repository.GiveawayRepository;
import mk.ukim.finki.wp.proekt.repository.UserRepository;
import mk.ukim.finki.wp.proekt.sevice.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProektApplicationTests {

    MockMvc mockMvc;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ManufacturerService manufacturerService;

    @Autowired
    AwardService awardService;

    @Autowired
    CountryService countryService;

    @Autowired
    RegionService regionService;

    @Autowired
    GiveawayService giveawayService;

    @Autowired
    GiveawayRepository giveawayRepository;

    @Autowired
    GiveawayRegionService giveawayRegionService;

    private static User user;
    private static User user2;
    private static User user3;
    private static Manufacturer manufacturer;
    private static Award award;
    private static Country country;
    private static Region region;
    private static Giveaway giveaway;
    private static Giveaway giveaway2;
    private static Category category;
    private static GiveawayRegion giveawayRegion;
    private static boolean dataInitialized = false;

    private void initData() throws ParseException {
        if (!dataInitialized) {
            user = this.userService.save("username", "user", "user", "user",
                    "user","user", "000", "user");
            user2 = this.userService.save("username2", "user", "user", "user",
                    "user","user", "000", "user");
            user3 = this.userService.save("username3", "user", "user", "user",
                    "user","user", "000", "user");
            manufacturer = manufacturerService.save("m1", "m1");
            award = this.awardService.save("name", (float) 15,"nekoeurl", manufacturer.getId(), user.getUsername());
            country =this.countryService.save("Macedonia","MKD",Continent.EUROPE);
            List<Integer> list = new ArrayList<>();
            list.add(country.getId());
            region = this.regionService.save("Balkan",list);
            category = this.categoryService.save("test", "test category");
            giveawayRegion = giveawayRegionService.save(list);
            giveaway = giveawayService.save("Ball Giveaway",
                    new SimpleDateFormat("dd/MM/yyyy").parse("20/08/2021"),
                    new SimpleDateFormat("dd/MM/yyyy").parse("20/11/2021"), category.getId(), award.getId(),
                    UserType.PRIVATE, user.getUsername(), giveawayRegion.getId(),null );
            giveaway2 = giveawayService.save("Giveaway",
                    new SimpleDateFormat("dd/MM/yyyy").parse("20/08/2021"),
                    new SimpleDateFormat("dd/MM/yyyy").parse("20/11/2021"), category.getId(), award.getId(),
                    UserType.PRIVATE, user.getUsername(), giveawayRegion.getId(),null );
            List<User> userList = new ArrayList<>();
            userList.add(user2);
            giveaway.setParticipants(userList);
            giveaway.setWinner(user2);
            this.giveawayRepository.save(giveaway);
            List<User> userList2 = new ArrayList<>();
            userList2.add(user3);
            giveaway2.setParticipants(userList2);
            this.giveawayRepository.save(giveaway2);
            dataInitialized = true;
        }
    }

    @BeforeEach
    public void setup(WebApplicationContext wac) throws ParseException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        initData();
    }

    @Test
    void contextLoads() {
    }


    //Award controller
    @Test
    public void testGetAwards() throws Exception {
        MockHttpServletRequestBuilder awardsRequest = MockMvcRequestBuilders.get("/awards")
                .with(request -> { request.setRemoteUser("username");
            return request;});
        this.mockMvc.perform(awardsRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("awards"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("status"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","my-awards"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testGetAddAwardPage() throws Exception {
        MockHttpServletRequestBuilder categoryRequest = MockMvcRequestBuilders.get("/awards/add-award")
                .with(request -> { request.setRemoteUser("username");
                    return request;});
        user.setRole(Role.ROLE_USER);
        this.userRepository.save(user);
        this.mockMvc.perform(categoryRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("manufacturers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","add-award"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testAddAward() throws Exception {

        MockHttpServletRequestBuilder addCategoryRequest = MockMvcRequestBuilders
                .post("/awards/add").param("name","Award name").
                        param("weight","54").param("url","Award url")
                .param("manufacturer",manufacturer.getId().toString())
                .with(request -> { request.setRemoteUser("username");
                    return request;});;

        this.mockMvc.perform(addCategoryRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/awards"));
    }

    @Test
    public void testAddEditAward() throws Exception {
        MockHttpServletRequestBuilder addAwardRequest = MockMvcRequestBuilders
                .post("/awards/add/"+ award.getId())
                .param("name","Award name")
                .param("weight","54")
                .param("url","Award url")
                .param("manufacturer",manufacturer.getId().toString())
                .with(request -> { request.setRemoteUser(user.getUsername());
                    return request;});;

        this.mockMvc.perform(addAwardRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/awards"));
    }


    @Test
    public void testGetEditAwardPage() throws Exception {
        MockHttpServletRequestBuilder editAwardRequest = MockMvcRequestBuilders
                .get("/awards/edit-form/"+award.getId())
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        this.mockMvc.perform(editAwardRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("manufacturers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("award"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","add-award"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testDeleteAward() throws Exception {
        Award award1 = this.awardService.save("award", (float) 20,"nekoeurl", manufacturer.getId(), user.getUsername());
        MockHttpServletRequestBuilder awardDeleteRequest = MockMvcRequestBuilders
                .post("/awards/delete/" + award1.getId());

        this.mockMvc.perform(awardDeleteRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/awards"));
    }


    //Category controller
    @Test
    public void testGetCategories() throws Exception {
        MockHttpServletRequestBuilder categoryRequest = MockMvcRequestBuilders.get("/category");
        this.mockMvc.perform(categoryRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","categories"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }
    @Test
    public void testGetAddCategoryPage() throws Exception {
        MockHttpServletRequestBuilder categoryRequest = MockMvcRequestBuilders.get("/category/add-category")
                .with(request -> { request.setRemoteUser("username");
                                    return request;});
        this.mockMvc.perform(categoryRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","add-category"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }


    @Test
    public void testAddCategory() throws Exception {

        MockHttpServletRequestBuilder addCategoryRequest = MockMvcRequestBuilders
                .post("/category/add").param("name","Category 1").
                        param("description","category one");

        this.mockMvc.perform(addCategoryRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/category"));
    }


    @Test
    public void testDeleteCategory() throws Exception {
        Category category = this.categoryService.save("test1", "test1 category");
        MockHttpServletRequestBuilder productDeleteRequest = MockMvcRequestBuilders
                .post("/category/delete/" + category.getId());

        this.mockMvc.perform(productDeleteRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/category"));
    }

    //Country Controller
    @Test
    public void testGetAddCountryPage() throws Exception {
        MockHttpServletRequestBuilder addCountryRequest = MockMvcRequestBuilders.get("/country/add-country")
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        this.mockMvc.perform(addCountryRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("continentList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","add-country"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }


    @Test
    public void testAddCountry() throws Exception {

        MockHttpServletRequestBuilder addCountryRequest = MockMvcRequestBuilders
                .post("/country/add").param("name","Macedonia")
                .param("code","MKD")
                .param("continent", Continent.EUROPE.toString());

        this.mockMvc.perform(addCountryRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/region/add-region"));
    }


    //Giveaway Controller
    @Test
    public void testGetGiveawayPage() throws Exception {
        MockHttpServletRequestBuilder giveawayRequest = MockMvcRequestBuilders.get("/giveaway")
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        this.mockMvc.perform(giveawayRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("categoryList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("status"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("giveawayList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","giveaway"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testGetAddGiveawayPage() throws Exception {
        MockHttpServletRequestBuilder addGiveawayPageRequest = MockMvcRequestBuilders.get("/giveaway/add-giveaway")
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        this.mockMvc.perform(addGiveawayPageRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user_types"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("regions"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("categories"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("giveawayRegion","awards",
                        "manufacturers","username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","add-giveaway"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testGetCountriesForGiveawayRegion() throws Exception {

        MockHttpServletRequestBuilder getCountriesForGiveawayRegionRequest = MockMvcRequestBuilders
                .get("/giveaway/countries").param("region_id",region.getId().toString())
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        List<Country> list = new ArrayList<>();
        list.add(country);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonListString = objectMapper.writeValueAsString(list);

        this.mockMvc.perform(getCountriesForGiveawayRegionRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(jsonListString));

    }

    @Test
    public void testGetDetailsGiveawayPage() throws Exception {
        MockHttpServletRequestBuilder detailsGiveawayPageRequest = MockMvcRequestBuilders.get("/giveaway/details/"+giveaway.getId())
                .with(request -> { request.setRemoteUser("username");
                    return request;});


        this.mockMvc.perform(detailsGiveawayPageRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("giveaway"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("isCreator"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","details-giveaway"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testGetChooseWinnerGiveawayPage() throws Exception {

        MockHttpServletRequestBuilder getChooseWinnerGiveawayPageRequest = MockMvcRequestBuilders
                .get("/giveaway/choose-winner/"+giveaway.getId())
                .flashAttr("winner", user2)
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        this.mockMvc.perform(getChooseWinnerGiveawayPageRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","choose-winner"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testAddParticipant() throws Exception{
        MockHttpServletRequestBuilder getAddParticipantRequest = MockMvcRequestBuilders
                .post("/giveaway/add-participant/"+giveaway2.getId())
                .with(request -> { request.setRemoteUser("username2");
                    return request;});

        this.mockMvc.perform(getAddParticipantRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/giveaway/details/"+giveaway2.getId()+"?message=You have successfully participate for this giveaway"));

    }

    @Test
    public void testChooseWinnerPostAction() throws Exception{
        MockHttpServletRequestBuilder getAddParticipantRequest = MockMvcRequestBuilders
                .post("/giveaway/choosewinner/"+giveaway2.getId())
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        this.mockMvc.perform(getAddParticipantRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("username","winner"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","choose-winner"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));
    }

    @Test
    public void testSaveGiveaway() throws Exception{
        MockHttpServletRequestBuilder getSaveGiveawayRequest = MockMvcRequestBuilders
                .post("/giveaway/add")
                .param("name","GiveawayName")
                .param("startdate","2021-08-20")
                .param("enddate","2021-11-20")
                .param("award",award.getId().toString())
                .param("region",region.getId().toString())
                .param("country",country.getId().toString())
                .param("category",category.getId().toString())
                .param("user_type",UserType.PRIVATE.toString())
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        this.mockMvc.perform(getSaveGiveawayRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/giveaway"));
    }

    @Test
    public void testExportPdf() throws Exception {
        MockHttpServletRequestBuilder exportPDFRequest = MockMvcRequestBuilders
                .get("/giveaway/export/pdf/")
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        this.mockMvc.perform(exportPDFRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/pdf"));
    }
    //Home Controller
    @Test
    public void testGetHomePage() throws Exception {
        MockHttpServletRequestBuilder homeRequest = MockMvcRequestBuilders.get("/home")
                .with(request -> { request.setRemoteUser("username");
                    return request;});

                    this.mockMvc.perform(homeRequest)
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","Home"))
                                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testGetMyProfilePage() throws Exception {
        MockHttpServletRequestBuilder myProfileRequest = MockMvcRequestBuilders.get("/home/my-profile")
                .with(request -> { request.setRemoteUser("username2");
                    return request;});

        this.mockMvc.perform(myProfileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("editUser"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("activeGiveawayList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("finishedGiveawayList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("giveawaysWaitingForWinner"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","my-profile"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testGetEditingMyProfilePage() throws Exception {
        MockHttpServletRequestBuilder editMyProfileRequest = MockMvcRequestBuilders
                .get("/home/my-profile/editbutton")
                .with(request -> { request.setRemoteUser("username2");
                    return request;});

        this.mockMvc.perform(editMyProfileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("editUser"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("activeGiveawayList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("finishedGiveawayList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("giveawaysWaitingForWinner"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","my-profile"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }


    @Test
    public void testEditMyProfilePage()throws Exception{
        MockHttpServletRequestBuilder editMyProfileRequest = MockMvcRequestBuilders
                .post("/home/my-profile/edit")
                .with(request -> { request.setRemoteUser("username2");
                    return request;}).param("name","test").
                        param("surname","test").param("email","test").
                        param("phone","test").param("address","test");

        this.mockMvc.perform(editMyProfileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("editUser"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("activeGiveawayList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("finishedGiveawayList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("giveawaysWaitingForWinner"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","my-profile"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));
    }

    @Test void testAccessDenied()  throws Exception{
        MockHttpServletRequestBuilder getAccessDeniedRequest = MockMvcRequestBuilders
                .get("/access_denied");

        this.mockMvc.perform(getAccessDeniedRequest).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","access_denied"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }




    //Login Controller
    @Test
    public void testGetLoginPage() throws Exception {
        MockHttpServletRequestBuilder getLoginRequest = MockMvcRequestBuilders.get("/login");
        this.mockMvc.perform(getLoginRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","login"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testLogin() throws Exception {

        MockHttpServletRequestBuilder loginRequest = MockMvcRequestBuilders.post("/login")
                .param("username",user.getUsername())
                .param("password", user.getPassword());

        this.mockMvc.perform(loginRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));

    }

    //Logout Controller
    @Test
    public void testLogout() throws Exception {
        MockHttpServletRequestBuilder logoutRequest = MockMvcRequestBuilders.get("/logout");
        this.mockMvc.perform(logoutRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));

    }

    //Manufacturer Controller
    @Test
    public void testGetAddManufacturerPage() throws Exception {
        MockHttpServletRequestBuilder addManufacturerRequest = MockMvcRequestBuilders.get("/manufacturer/add-manu")
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        this.mockMvc.perform(addManufacturerRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","add-manu"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }


    @Test
    public void testAddManufacturer() throws Exception {

        MockHttpServletRequestBuilder addCountryRequest = MockMvcRequestBuilders
                .post("/manufacturer/add").param("name","Manufacturer name")
                .param("description","M1");

        this.mockMvc.perform(addCountryRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/awards/add-award"));
    }


    //Region Controller
    @Test
    public void testGetRegionPage() throws Exception {
        MockHttpServletRequestBuilder regionRequest = MockMvcRequestBuilders.get("/region")
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        this.mockMvc.perform(regionRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("regions"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","list-region"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testGetAddRegionPage() throws Exception {
        MockHttpServletRequestBuilder addRegionRequest = MockMvcRequestBuilders.get("/region/add-region")
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        this.mockMvc.perform(addRegionRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("countries"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","add-region"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testAddRegion() throws Exception {

        MockHttpServletRequestBuilder addRegionRequest = MockMvcRequestBuilders
                    .post("/region/add").param("name","Balkan")
                .param("countries",country.getId().toString());

        this.mockMvc.perform(addRegionRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/region"));
    }


    @Test
    public void testGetEditRegionPage() throws Exception {
        MockHttpServletRequestBuilder editRegionRequest = MockMvcRequestBuilders.get("/region/details/"+region.getId())
                .with(request -> { request.setRemoteUser("username");
                    return request;});

        this.mockMvc.perform(editRegionRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("region"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("regionCountries"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("countries"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","add-region"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    //Register Controller
    @Test
    public void testGetRegisterPage() throws Exception {
        MockHttpServletRequestBuilder getRegisterRequest = MockMvcRequestBuilders.get("/register");
        this.mockMvc.perform(getRegisterRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("username"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("hasError"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","register"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testRegister() throws Exception {

        MockHttpServletRequestBuilder registerRequest = MockMvcRequestBuilders.post("/register").
                param("username","testuser").param("password","test").
                param("repeatedPassword","test").param("name","test").
                param("surname","test").param("email","test").
                param("phone","test").param("address","test");

        this.mockMvc.perform(registerRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));

    }

}
