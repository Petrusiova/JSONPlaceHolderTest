import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest {

    @Test
    @Description("Searching for the user with username 'Delphine' in users list")
    public void userInListOfUsers(){
        User[] users = Util.sendRequestAndGetResponse("/users").as((Type) User[].class);
        var delphine = Arrays
                .stream(users)
                .filter(item -> item.username.equals("Delphine"))
                .collect(Collectors.toList());

        Allure.step("Check the quantity of users with userName 'Delphine' != 0", () ->
                Assertions.assertNotEquals(0, delphine.size(), "No user with userName 'Delphine'"));

        Allure.step("Check the quantity of users with userName 'Delphine' = 1", () ->
        Assertions.assertEquals(1, delphine.size(), "More than 1 user with name 'Delphine' exists"));

        Allure.step("Check the id of user with userName 'Delphine' = 9", () ->
        Assertions.assertEquals(9, delphine.get(0).id, "Actual user id does not equal to expected"));

    }

    @Test
    @Description("Searching for the user with username 'Delphine' via request with username")
    public void userByUserName(){
        User[] users = Util.sendRequestAndGetResponse("/users?username=Delphine").as((Type) User[].class);

        Allure.step("Check the quantity of users with userName 'Delphine' = 1", () ->
        Assertions.assertEquals(1, users.length, "More than 1 user with name 'Delphine' exists"));

        Allure.step("Check the id of user with userName 'Delphine' = 9", () ->
        Assertions.assertEquals(9, users[0].id, "Actual user id does not equal to expected"));
    }

    @Test
    @Description("Searching for the user with username 'Delphine' via request by id (/users?id=id)")
    public void userById(){
        User[] users = Util.sendRequestAndGetResponse("/users?id=9").as((Type) User[].class);

        Allure.step("Check the quantity of users with id 9 = 1", () ->
        Assertions.assertEquals(1, users.length, "More than 1 user with id 9 exists"));

        Allure.step("Check the actual username equals to 'Delphine'", () ->
        Assertions.assertEquals("Delphine", users[0].username, "Actual username id does not equal to expected"));
    }

    @Test
    @Description("Searching for the user with username 'Delphine' via request by id (/users/id)")
    public void userById2(){
        User user = Util.sendRequestAndGetResponse("/users/9").as((Type) User.class);

        Allure.step("Check the actual username equals to 'Delphine'", () ->
        Assertions.assertEquals("Delphine", user.username, "Actual username id does not equal to expected"));
    }
}
