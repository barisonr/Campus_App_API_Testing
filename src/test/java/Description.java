import Utils.Tools;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Description {
    Cookies cookies;
    @BeforeClass
    public void login() {cookies = Tools.login();
    }

    String gID;
    String gName;
    String gCode;
    String gPriority;
    boolean gActive;

    @Test
    public void creatDescription(){
        gName=getRandomName();
        gCode=getRandomcode();
        gPriority=getRandomsale();



        Giris giris=new Giris();
        giris.setDescription(gName);
        giris.setCode(gCode);
        giris.setPriority(gPriority);
        giris.setActive(true);




        gID=
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(giris)
                        .when()
                        .post("school-service/api/discounts")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")
        ;

    }
    @Test(dependsOnMethods = "creatDescription" )
    public void updateDescription(){
        gName=getRandomName();
        gCode=getRandomcode();
        gPriority=getRandomsale();

        Giris giris=new Giris();
        giris.setId(gID);
        giris.setDescription(gName);
        giris.setCode(gCode);
        giris.setPriority(gPriority);
        giris.setActive(false);





        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(giris)
                .when()
                .put("school-service/api/discounts")

                .then()
                .log().body()
                .statusCode(200)

        ;

    }
    @Test(dependsOnMethods = "updateDescription" )
    public void deleteDescriptionById(){



        given()
                .cookies(cookies)
                .pathParam("gID",gID)

                .when()
                .delete("school-service/api/discounts/{gID}")

                .then()
                .statusCode(200)

        ;

    }







    public String getRandomName(){

        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }

    public String getRandomcode(){

        return RandomStringUtils.randomAlphabetic(3).toLowerCase();
    }
    public String getRandomsale(){

        return RandomStringUtils.randomNumeric(2).toLowerCase();
    }
    public class Giris{

        private String  description;
        private String code;
        private String id;
        private  String priority;
        private boolean active;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public String toString() {
            return "Giris{" +
                    "description='" + description + '\'' +
                    ", code='" + code + '\'' +
                    ", id='" + id + '\'' +
                    ", priority='" + priority + '\'' +
                    ", active=" + active +
                    '}';
        }
    }
    }



