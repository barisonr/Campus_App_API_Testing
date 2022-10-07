import Utils.Tools;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HumanAttestations {
    Cookies cookies;
    @BeforeClass
    public void login() {cookies = Tools.login();
    }

    String gID;
    String gName;
    String gCode;
    @Test
    public void creatHA(){
        gName=getRandomName();

        Giris giris=new Giris();
        giris.setName(gName);

        gID=
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(giris)
                        .when()
                        .post("school-service/api/attestation")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")
        ;

    }
    @Test(dependsOnMethods = "creatHA" )
    public void updateHA(){
        gName=getRandomName();

        Giris giris=new Giris();
        giris.setId(gID);
        giris.setName(gName);





        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(giris)
                .when()
                .put("school-service/api/attestation")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(gName))
        ;

    }
    @Test(dependsOnMethods = "updateHA" )
    public void deleteHAById(){



        given()
                .cookies(cookies)
                .pathParam("gID",gID)

                .when()
                .delete("school-service/api/attestation/{gID}")

                .then()
                .statusCode(204)

        ;

    }







    public String getRandomName(){

        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }

    public String getRandomcode(){

        return RandomStringUtils.randomAlphabetic(3).toLowerCase();
    }
    public String getRandomsale(){

        return RandomStringUtils.randomAlphabetic(2).toLowerCase();
    }
    public class Giris{

        private String  name;
        private String code;
        private String id;
        private  String priority;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        @Override
        public String toString() {
            return "Giris{" +
                    "name='" + name + '\'' +
                    ", code='" + code + '\'' +
                    ", id='" + id + '\'' +
                    ", priority='" + priority + '\'' +
                    '}';
        }
    }
}
