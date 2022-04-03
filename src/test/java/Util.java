import io.qameta.allure.Allure;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class Util {

        private Util(){

        }

        private static Logger logger = LoggerFactory.getLogger(Util.class);
        public static final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public static ExtractableResponse<Response> sendRequestAndGetResponse(String url){
                String baseUri = "https://jsonplaceholder.typicode.com";

                RequestSpecification requestSpecification =
                        given()
                         .relaxedHTTPSValidation()
                        .log()
                        .all()
                        .baseUri(baseUri);

                var response = given()
                        .spec(requestSpecification)
                        .get(url)
                        .then()
                        .statusCode(200)
                        .extract();

                logger.info(response.response().getBody().prettyPrint());
                Allure.addAttachment("request", baseUri + url);
                Allure.addAttachment("response", response.response().getBody().prettyPrint());

                return response;
        }

}
