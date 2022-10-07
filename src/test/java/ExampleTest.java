import Utils.Tools;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ExampleTest {
    Cookies cookies;

    @BeforeClass
    public void login() {
        cookies = Tools.login();
    }


    @Test(testName = "Create a Country")
    public void createCountry() {

        Map<String, String> country = new HashMap<>();
        country.put("name", "testdeneme12");
        country.put("code", "1234");


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)

        .when()
                .post("school-service/api/countries")

        .then()
                .log().body()
                .statusCode(201);

    }
}
