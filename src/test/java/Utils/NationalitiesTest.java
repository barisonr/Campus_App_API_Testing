package Utils;

import Utils.Model.Nationalities;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class NationalitiesTest {
    Cookies cookies;

    @BeforeClass
    public void login(){
        cookies = Tools.login();
    }
    String nationName;
    String nationID;

    @Test
    public void createNationalities(){
        nationName = randomName();

        Nationalities nationalities = new Nationalities();
        nationalities.setName(nationName);

        nationID =
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(nationalities)

        .when()
                .post("school-service/api/nationality")

        .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @Test(dependsOnMethods = "createNationalities")
    public void createNationalitiesNegative(){

        Nationalities nationalities = new Nationalities();
        nationalities.setName(nationName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(nationalities)

        .when()
                .post("school-service/api/nationality")

        .then()
                .statusCode(400)
                .body("message",equalTo("The Nationality with Name \""+ nationName +"\" already exists."));
    }

    @Test(dependsOnMethods = "createNationalities")
    public void updateNationalities(){
        nationName = randomName();

         Nationalities nationalities = new Nationalities();
         nationalities.setName(nationName);
         nationalities.setId(nationID);

         given()
                 .cookies(cookies)
                 .contentType(ContentType.JSON)
                 .body(nationalities)

         .when()
                 .put("school-service/api/nationality")

         .then()
                 .log().body()
                 .statusCode(200)
                 .body("name",equalTo(nationName));
    }

    @Test(dependsOnMethods = "updateNationalities")
    public void deleteNationalities(){

        given()
                .cookies(cookies)
                .pathParam("nationID",nationID)

        .when()
                .delete("school-service/api/nationality/{nationID}")

        .then()
                .log().body()
                .statusCode(200);

    }

    @Test(dependsOnMethods = "deleteNationalities")
    public void deleteNationalitiesNegative(){

        given()
                .cookies(cookies)
                .pathParam("nationID",nationID)

        .when()
                .delete("school-service/api/nationality/{nationID}")

        .then()
                .statusCode(400);

    }

    @Test(dependsOnMethods = "deleteNationalities")
    public void updateNationalitiesNegative(){
        nationName = randomName();

        Nationalities nationalities = new Nationalities();
        nationalities.setName(nationName);
        nationalities.setId(nationID);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(nationalities)

        .when()
                .put("school-service/api/nationality")

        .then()
                .statusCode(400)
                .body("message",equalTo("Can't find Nationality"));
    }

    public String randomName(){
        return RandomStringUtils.randomAlphabetic(7).toUpperCase();
    }
}


