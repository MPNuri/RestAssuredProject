import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestLogSpecification;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;

import java.util.HashMap;

import static io.restassured.RestAssured.*; // for the RestAssured codes
import static org.hamcrest.Matchers.*; // for the Hamcrest,


public class _06_MersysNationalityTest {

    RequestSpecification reqSpec;
    HashMap<String,String> userInfo;
    Cookies cookie;
    String nation_Id;

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                .contentType(ContentType.JSON)
                .log().body();

        userInfo = new HashMap<>();
        userInfo.put("username","richfield.edu");
        userInfo.put("password","Richfield2020!");
        userInfo.put("rememberMe","true");
    }

    @Test(priority = 1)
    public void loginTest() {

        cookie = given()
        // instead of an id we need to locate the cookie to be able to automate the website. and my Variable will be Cookies instead of String
        // after this, my cookie variable will store COOKIES information from the Campus Website
                .spec(reqSpec)
                .body(userInfo)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("username",equalTo(userInfo.get("username")))
                .extract().detailedCookies();
        System.out.println(cookie);
    }

    @Test(priority = 2)
    public void createNationalityTest() {

        HashMap<String,String> reqBody = new HashMap<>();
        reqBody.put("name","cano environment");

        nation_Id = given()
                .spec(reqSpec)
                .cookies(cookie)
                .body(reqBody)
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(201)
                .body("name",equalTo(reqBody.get("name")))
                .extract().jsonPath().getString("id"); // now id is added just like I explained in lind 38

    }

    @Test(priority = 3)
    public void createNationalityNegativeTest() {
        HashMap<String,String> reqBody = new HashMap<>();
        reqBody.put("cano","hagan environment");

        given()
                .spec(reqSpec)
                .cookies(cookie)
                .body(reqBody)
                //.body(userInfo.get("name"))
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(400); // status code must be 400 when already exist

    }

    @Test(priority = 4)
    public void getNationalityTest() {

        given()
                .spec(reqSpec)
                .cookies(cookie)
                .when()
                .get("/school-service/api/nationality/" + nation_Id)
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(priority = 5)
    public void updateNationalityTest() {

        HashMap<String,String> updatingReqBody = new HashMap<>();
        updatingReqBody.put("id",nation_Id); // this os very important
        updatingReqBody.put("name","canCano environments");
        updatingReqBody.put("translatename",null);

        given()
                .spec(reqSpec)
                .cookies(cookie)
                .body(updatingReqBody)
                .when()
                .put("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(updatingReqBody.get("name")));
    }

    @Test(priority = 6)
    public void deleteNationalityTest() {

        given()
                .spec(reqSpec)
                .cookies(cookie)
                .when()
                .delete("/school-service/api/nationality/"+ nation_Id)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(priority = 7)
    public void getNationalityNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookie)
                .when()
                .get("/school-service/api/nationality/"+ nation_Id)
                .then()
                .log().body()
                .statusCode(400);
                //.body("name",null);
    }

    @Test(priority = 8)
    public void deleteNationalityNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookie)
                .when()
                .delete("/school-service/api/nationality/"+ nation_Id)
                .then()
                .log().body()
                .statusCode(400);
    }

}
