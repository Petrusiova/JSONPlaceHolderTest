import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import model.Comment;
import model.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.regex.Pattern;

public class PostsTest {

    @Test
    @Description("Check all comments to all user with userName 'Delphine' contains correct ids (/posts?userId=id)")
    public void onlyUserPosts() {
        Post[] posts = Util.sendRequestAndGetResponse("/posts?userId=9").as((Type) Post[].class);

        Allure.step("Check the quantity of posts by 'Delphine' != 0", () ->
                Assertions.assertNotEquals(0, posts.length,
                        "No posts by user with userName 'Delphine'. Add some data and try again"));

        long count = Arrays.stream(posts).filter(item -> item.userId != 9).count();

        Allure.step("Check the response do not contain other users' posts", () ->
                Assertions.assertEquals(0, count, "Response contains other users' posts"));
    }

    @Test
    @Description("Check all comments to all user with userName 'Delphine' contains correct ids (/users/id/posts)")
    public void onlyUserPosts2() {
        Post[] posts = Util.sendRequestAndGetResponse("/users/9/posts").as((Type) Post[].class);

        Allure.step("Check the quantity of posts by 'Delphine' != 0", () ->
                Assertions.assertNotEquals(0, posts.length,
                        "No posts by user with userName 'Delphine'. Add some data and try again"));

        long count = Arrays.stream(posts).filter(item -> item.userId != 9).count();

        Allure.step("Check the response do not contain other users' posts", () ->
                Assertions.assertEquals(0, count, "Response contains other users' posts"));
    }

    @Test
    @Description("Check all comments from all user with userName 'Delphine' contains correct emails (posts/id/comments)")
    public void searchEmailTest() {
        Post[] posts = Util.sendRequestAndGetResponse("/users/9/posts").as((Type) Post[].class);

        Arrays.stream(posts).forEach(item -> {
            int postId = item.id;
            Comment[] comments =
                    Util.sendRequestAndGetResponse("/posts/" + postId + "/comments").as((Type) Comment[].class);
            Arrays.stream(comments).forEach(comment ->
                    validate(comment.email));
        });
    }

    @Test
    @Description("Check all comments from all user with userName 'Delphine' contains correct emails (/comments?postId=)")
    public void searchEmailTest2() {
        Post[] posts = Util.sendRequestAndGetResponse("/users/9/posts").as((Type) Post[].class);

        Arrays.stream(posts).forEach(item -> {
            int postId = item.id;
            Comment[] comments =
                    Util.sendRequestAndGetResponse("/comments?postId=" + postId).as((Type) Comment[].class);
            Arrays.stream(comments).forEach(comment ->
                    validate(comment.email));
        });
    }

    @Step("Check email is valid: {0}")
    public boolean validate(final String email) {
        return Pattern.compile(Util.EMAIL_PATTERN).matcher(email).matches();
    }
}
