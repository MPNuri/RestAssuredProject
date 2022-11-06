package CampusMersys;
import CampusMersys.PojoClass.LoginPOJO;
import CampusMersys.PojoClass._06_FeesCredentialsPOJO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*; // for the RestAssured codes
import static org.hamcrest.Matchers.*; // for the Hamcrest


public class _06_FeesMersys {

    _06_FeesCredentialsPOJO feesPOJO;
    LoginPOJO logPOJO;
    RequestSpecification reqSpec;

    Cookies cookie;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                 .contentType(ContentType.JSON)
                 .log().body();

        logPOJO = new LoginPOJO();
        logPOJO.setRememberMe(true);
        logPOJO.setPassword("Richfield2020!");
        logPOJO.setUsername("richfield.edu");

        feesPOJO = new _06_FeesCredentialsPOJO();
        feesPOJO.setActive(true);
        feesPOJO.setName("Enrolment Fee");
        feesPOJO.setIntegrationCode("NEF");
        feesPOJO.setCode("First Time");
        feesPOJO.setPriority("1");
        feesPOJO.setIntegrationCode(null);

    }

    @Test(priority = 1)
    public void loginPositiveTest() {
        cookie = given()
                .spec(reqSpec)
                .body(logPOJO)
                .when()
                .post("/auth/login")
                .then()
                .log().body()
                .statusCode(200)
                .body("username",equalTo(logPOJO.getUsername()))
                .extract().detailedCookies();
    }

    @Test(dependsOnMethods = "loginPositiveTest")
    public void negativeLogin() {

        given()
                .spec(reqSpec)
                .cookies(cookie)
                // body is missing will give 400 error
                .when()
                .post("/auth/login")
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "negativeLogin")
    public void creatingFees() {

        feesPOJO.setFeesID(given()
                .spec(reqSpec)
                .cookies(cookie)
                .body(feesPOJO)
                .when()
                .post("/school-service/api/fee-types")
                .then()
                .log().body()
                .statusCode(201)
                .body("name",equalTo(feesPOJO.getName()))
                .extract().jsonPath().getString("id")); // again forgot this part be careful
    }

    @Test(dependsOnMethods = "negativeLogin")
    public void creatingNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookie)
                .body(feesPOJO)
                .when()
                .post("/school-service/api")
                .then()
                .log().body()
                .statusCode(405);
    }

    @Test(dependsOnMethods = "creatingNegativeTest")
    public void getUserInformation() {
        System.out.println(feesPOJO.getFeesID());

        given()
                .cookies(cookie)
                .when()
                .get("/school-service/api/fee-types/"+feesPOJO.getFeesID())
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "getUserInformation")
    public void editCreatedFee() {

        HashMap<String, String> editFeeFunctionality = new HashMap<>();
        editFeeFunctionality.put("active","true");
        editFeeFunctionality.put("budgetAccountIntegrationCode", feesPOJO.getIntegrationCode());
        editFeeFunctionality.put("code", "NEFT");
        editFeeFunctionality.put("id", feesPOJO.getFeesID());
        editFeeFunctionality.put("name", feesPOJO.getName());
        editFeeFunctionality.put("priority",feesPOJO.getPriority());
        editFeeFunctionality.put("translateName",null);

        given()
                .spec(reqSpec)
                .cookies(cookie)
                .body(editFeeFunctionality)
                .when()
                .put("/school-service/api/fee-types")
                .then()
                .log().body()
                .statusCode(200)
                .body("code",equalTo(editFeeFunctionality.get("code")));
    }

    @Test(dependsOnMethods = "editCreatedFee")
    public void deleteUser() {

        given()
                .cookies(cookie)
                .when()
                .delete("/school-service/api/fee-types/"+feesPOJO.getFeesID())
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteNegativeTest() {

        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/fee-types/"+feesPOJO.getFeesID())
                .then()
                .log().body()
                .statusCode(401); // authentication (cookies)   is not provided so 401
    }

    @Test(dependsOnMethods = "deleteNegativeTest")
    public void getNegativeTest() {

        given()
                .cookies(cookie)
                .when()
                .get("/school-service/api/fee-types/"+feesPOJO.getFeesID())
                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Can't find Fee Type "));
    }

}
