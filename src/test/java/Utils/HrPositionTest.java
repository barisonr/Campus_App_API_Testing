package Utils;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HrPositionTest {
    Cookies cookies;

    @BeforeClass
    public void login(){
        cookies = Tools.login();
    }
    String positionName;
    String positionShortName;
    String positionID;
    static String tenantId = "5fe0786230cc4d59295712cf";

    @Test
    public void createPosition(){
        positionName = randomName();
        positionShortName = randomShortName();

        Map<String, String> hrPosition = new HashMap<>();
        hrPosition.put("name",positionName);
        hrPosition.put("shortName",positionShortName);
        hrPosition.put("tenantId",tenantId);

        positionID =
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(hrPosition)

                .when()
                        .post("school-service/api/employee-position")

                .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id");
    }

    @Test(dependsOnMethods = "createPosition")
    public void createPositionNegative(){

        Map<String, String> hrPosition = new HashMap<>();
        hrPosition.put("name",positionName);
        hrPosition.put("shortName",positionShortName);
        hrPosition.put("tenantId",tenantId);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(hrPosition)

        .when()
                .post("school-service/api/employee-position")

        .then()
                .statusCode(400)
                .body("message",equalTo("The Position with Name \"" + positionName + "\" already exists."));

    }

    @Test(dependsOnMethods = "createPosition")
    public void updatePosition(){
        positionName = randomName();

        Map<String, String> hrPosition = new HashMap<>();
        hrPosition.put("name",positionName);
        hrPosition.put("shortName",positionShortName);
        hrPosition.put("tenantId",tenantId);
        hrPosition.put("id",positionID);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(hrPosition)

        .when()
                .put("school-service/api/employee-position")

        .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(positionName));

    }

    @Test(dependsOnMethods = "updatePosition")
    public void deletePosition(){

        given()
                .cookies(cookies)
                .pathParam("positionID", positionID)

        .when()
                .delete("school-service/api/employee-position/{positionID}")

        .then()
                .log().body()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deletePosition")
    public void updatePositionNegative(){
        positionName = randomName();
        positionShortName = randomShortName();

        Map<String, String> hrPosition = new HashMap<>();
        hrPosition.put("name",positionName);
        hrPosition.put("shortName", positionShortName);
        hrPosition.put("tenantId",tenantId);
        hrPosition.put("id",positionID);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(hrPosition)

                .when()
                .put("school-service/api/employee-position")

                .then()
                .statusCode(400)
                .body("message",equalTo("Can't find Position"));
    }

    public String randomName(){
        return RandomStringUtils.randomAlphabetic(7).toLowerCase(); }
    public String randomShortName(){
        return RandomStringUtils.randomAlphabetic(3).toLowerCase();
    }

}
