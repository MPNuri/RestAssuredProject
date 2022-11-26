package CampusMersys;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*; // for the RestAssured codes
import static org.hamcrest.Matchers.*; // for the Hamscrest


public class _08_CitizenshipMersys {

    private RequestSpecification reqSpec;
    private Cookies cookies;

    private HashMap<String,String> loginUser;

    private String id;

    HashMap<String,String> newCitizenship;

    @BeforeClass
    public void setUp() {

        RestAssured.baseURI = "https://demo.mersys.io";

        loginUser = new HashMap<>();
        loginUser.put("username","richfield.edu");
        loginUser.put("rememberMe","true");
        loginUser.put("password","Richfield2020!");

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);
    }

    @Test(priority = 1)
    public void login() {

        cookies = given()
                .spec(reqSpec)
                .body(loginUser)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .log().body()
                .body("username",equalTo(loginUser.get("username")))
                .extract().detailedCookies();
    }

    @Test(dependsOnMethods = "login")
    public void negativeLogin() {

        given()
                .spec(reqSpec)
                .body(loginUser)
                .when()
                .get("/auth/login") // used wrong method 405, code will pass but does not mean positive test
                .then()
                .log().body()
                .statusCode(405);
    }

    @Test(dependsOnMethods = "negativeLogin")
    public void createCitizen() {

        newCitizenship = new HashMap<>();
        newCitizenship.put("name","Kazsirtkonzis");
        newCitizenship.put("shortName","ptrk"); // burdan devam et creating the citizen test
        newCitizenship.put("translateName",null);

        id = given() // gives the id string value
                .cookies(cookies)
                .spec(reqSpec)
                .body(newCitizenship)
                .when()
                .post("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @Test(dependsOnMethods = "createCitizen")
    public void getNewCitizenship() {
        given()
                .cookies(cookies)
                .spec(reqSpec)
                .when()
                .get("/school-service/api/citizenships/"+id)
                .then()
                .statusCode(200)
                .log().body();
                //.header(id,equalTo(id.contains("6")));
    }

    @Test(dependsOnMethods = "getNewCitizenship")
    public void createNegativeCitizenship() {
        HashMap<String,String> newCitizenshipNN = new HashMap<>();

        given()
                .cookies(cookies)
                .spec(reqSpec)
                .body(newCitizenshipNN)
                .when()
                .post("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "createNegativeCitizenship")
    public void editCitizenship() {
        HashMap<String,String> editingCitizenship = new HashMap<>();
        editingCitizenship.put("id",id);
        editingCitizenship.put("name","Mekenkuk");
        editingCitizenship.put("shortName","mkk");
        editingCitizenship.put("translateName",null);

        given()
                .cookies(cookies)
                .spec(reqSpec)
                .body(editingCitizenship)
                .when()
                .put("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "editCitizenship")
    public void deleteCitizenship() {
        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/citizenships/"+id)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteCitizenship")
    public void getNegativeCitizenship() {

        given()
                .cookies(cookies)
                .when()
                .get("/school-service/api/citizenships/"+id)
                .then()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "getNegativeCitizenship")
    public void negativeDeleteTest() {
        given()
                .cookies(cookies)
                .when() 
                .delete("/school-service/api/citizenships/"+id)
                .then()
                .log().body()
                .statusCode(400);
    }
}
