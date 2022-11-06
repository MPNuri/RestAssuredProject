package CampusMersys;

import static io.restassured.RestAssured.*; // for the RestAssured codes
import static org.hamcrest.Matchers.*; // for the Hamscrest

import CampusMersys.PojoClass.LoginPOJO;
import CampusMersys.PojoClass._07_DiscountClassPOJO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

public class _07_DiscountMersys {


    Cookies cookies;
    _07_DiscountClassPOJO disPOJO;
    RequestSpecification reqSpec;
    LoginPOJO logPOJO;
    //String discount_ID;

    @BeforeClass
    public void setUp() {

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                .contentType(ContentType.JSON)
                .log().body();

        logPOJO = new LoginPOJO();
        logPOJO.setUsername("richfield.edu");
        logPOJO.setPassword("Richfield2020!");
        logPOJO.setRememberMe(true);

        disPOJO = new _07_DiscountClassPOJO();
        disPOJO.setDescription("First Child Benefit");
        disPOJO.setCode("FCB");
        disPOJO.setPriority(String.valueOf(7));
        disPOJO.setActive(true);
    }

    @Test(priority = 1)
    public void loginPart() {
        cookies = given()
                .spec(reqSpec)
                .body(logPOJO)
                .when()
                .post("/auth/login")
                .then()
                .log().body()
                .statusCode(200)
                .body("username", equalTo(logPOJO.getUsername()))
                .extract().detailedCookies();
        System.out.println(cookies);
    }

    @Test(priority = 2)
    public void loginNegativeTest() {

        HashMap<String,String> negativeLoginInputs = new HashMap<>();
        negativeLoginInputs.put("username","murillo");
        negativeLoginInputs.put("password","x");
        negativeLoginInputs.put("rememberMe","true");



        given()
                .spec(reqSpec)
                .body(negativeLoginInputs)
                .when()
                .post("/auth/login")
                .then()
                .log().body()
                .statusCode(401);
    }

    @Test(priority = 3)
    public void creatingDiscount() {

        disPOJO.setId(given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(disPOJO)
                .when()
                .post("/school-service/api/discounts")// always doubleCheck the url
                .then()
                .log().body()
                .statusCode(201)
                .body("code", equalTo(disPOJO.getCode())) // be aware of caps, here Code will give an exception
                .extract().jsonPath().getString("id"));
    }

    @Test(priority = 4)
    public void creatingNegativeDiscountTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(disPOJO)
                .when()
                .post("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(priority = 5)
    public void getUserInfo() {
        given()
                .cookies(cookies)
                .when()
                .get("school-service/api/discounts/" + disPOJO.getId())
                .then()
                .log().body()
                .statusCode(200)
                .body("description", equalTo(disPOJO.getDescription()));
        // if you have a priority starting with 0 the expected and actual results does not match, so you get an exception
        // if you also try to give string value and get int code does not match
    }

    @Test(priority = 6)
    public void editDiscount() {

        // I have to implement everything since it is asked by Payload portion, depends on each project
        HashMap<String, String> editingTheDiscountCode = new HashMap<>();
        editingTheDiscountCode.put("active", "true");
        editingTheDiscountCode.put("code", "RM");
        editingTheDiscountCode.put("description", disPOJO.getDescription());
        editingTheDiscountCode.put("id", disPOJO.getId());
        editingTheDiscountCode.put("priority", disPOJO.getPriority());
        editingTheDiscountCode.put("translateDescription", null);

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(editingTheDiscountCode)
                .when()
                .put("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(200)
                .body("code", equalTo(editingTheDiscountCode.get("code")));
    }

    @Test(priority = 7)
    public void getEditedDiscountInformation() {

        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("description", disPOJO.getDescription());
        userInfo.put("id", disPOJO.getId());
        System.out.println(userInfo); // does not require additional body user info

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/discounts/" + disPOJO.getId())
                // the search right after the func. keyword represents the id field, line 127
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(priority = 8)
    public void deleteDiscount() {

        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/discounts/" + disPOJO.getId())
                .then()
                .statusCode(200)
                .log().body();
    }

    @Test(priority = 9)
    public void deleteNegativeTest() {

        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/discounts/" + disPOJO.getId())
                .then()
                .statusCode(400)
                .log().body();
    }
}

