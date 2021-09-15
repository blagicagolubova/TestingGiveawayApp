package mk.ukim.finki.wp.proekt;

import mk.ukim.finki.wp.proekt.model.User;
import mk.ukim.finki.wp.proekt.model.enumerations.Role;
import mk.ukim.finki.wp.proekt.model.exceptions.InvalidArgumentException;
import mk.ukim.finki.wp.proekt.model.exceptions.PasswordDoNotMatchException;
import mk.ukim.finki.wp.proekt.model.exceptions.UserNameAlreadyExistsException;
import mk.ukim.finki.wp.proekt.repository.UserRepository;
import mk.ukim.finki.wp.proekt.sevice.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)

public class UserSaveTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        User user = new User("username", "name", "surname","address","phone",
                "email", "password" , Role.ROLE_USER);
        Mockito.when(this.userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(this.passwordEncoder.encode(Mockito.anyString())).thenReturn("password");

        this.service = Mockito.spy(new UserServiceImpl(this.userRepository, this.passwordEncoder));
    }


    @Test
    public void testSuccessSave() {
        User user = this.service.save("username", "password", "password", "name",
                "surname","address", "phone" ,"email");

        Mockito.verify(this.service, Mockito.times(1)).save("username",
                "password", "password", "name", "surname","address",
                "phone" ,"email");


        Assert.assertNotNull("User is null", user);
        Assert.assertEquals("username do not match", "username", user.getUsername());
        Assert.assertEquals("password do not match", "password", user.getPassword());
        Assert.assertEquals("name do not match", "name", user.getName());
        Assert.assertEquals("surname do not match", "surname", user.getSurname());
        Assert.assertEquals("address do not match", "address", user.getAddress());
        Assert.assertEquals("phone do not match", "phone", user.getPhone());
        Assert.assertEquals("email do not match", "email", user.getEmail());
        Assert.assertEquals("role do not match", Role.ROLE_USER, user.getRole());
    }

    @Test
    public void testNullUsername() {
        Assert.assertThrows("InvalidArgumentException expected",
                InvalidArgumentException.class,
                () -> this.service.save(null, "password", "password", "name",
                        "surname","address", "phone" ,"email"));
        Mockito.verify(this.service).save(null, "password", "password", "name",
                "surname","address", "phone" ,"email");
    }

    @Test
    public void testEmptyUsername() {
        String username = "";
        Assert.assertThrows("InvalidArgumentException expected",
                InvalidArgumentException.class,
                () -> this.service.save(username, "password", "password", "name",
                        "surname","address", "phone" ,"email"));
        Mockito.verify(this.service).save(username, "password", "password", "name",
                "surname","address", "phone" ,"email");
    }
    @Test
    public void testNullPassword() {
        Assert.assertThrows("InvalidArgumentException expected",
                InvalidArgumentException.class,
                () -> this.service.save("username", null, "password", "name",
                        "surname","address", "phone" ,"email"));
        Mockito.verify(this.service).save("username", null, "password", "name",
                "surname","address", "phone" ,"email");
    }

    @Test
    public void testEmptyPassword() {
        String password = "";
        Assert.assertThrows("InvalidArgumentException expected",
                InvalidArgumentException.class,
                () -> this.service.save("username", password, "password", "name",
                        "surname","address", "phone" ,"email"));
        Mockito.verify(this.service).save("username", password, "password", "name",
                "surname","address", "phone" ,"email");
    }
    @Test
    public void testPasswordMismatch() {
        String username = "username";
        String password = "password";
        String confirmPassword = "otherPassword";
        Assert.assertThrows("PasswordsDoNotMatchException expected",
                PasswordDoNotMatchException.class,
                () -> this.service.save(username, password, confirmPassword,  "name",
                        "surname","address", "phone" ,"email"));
        Mockito.verify(this.service).save(username, password, confirmPassword,  "name",
                "surname","address", "phone" ,"email");
    }


    @Test
    public void testDuplicateUsername() {
        User user = new User("username", "name", "surname","address","phone",
                "email", "password" , Role.ROLE_USER);
        Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        String username = "username";
        Assert.assertThrows("UsernameAlreadyExistsException expected",
                UserNameAlreadyExistsException.class,
                () -> this.service.save(username, "password", "password", "name",
                        "surname","address", "phone" ,"email"));
        Mockito.verify(this.service).save(username, "password", "password", "name",
                "surname","address", "phone" ,"email");
    }


}
