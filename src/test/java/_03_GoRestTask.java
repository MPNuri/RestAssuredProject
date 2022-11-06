import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*; // for the RestAssured codes
import static org.hamcrest.Matchers.*; // for the Hamcrest



public class _03_GoRestTask {

    private HashMap<String,String> requestData;
    private RequestSpecification reSpec;
    private Object userId;



    @BeforeClass
    public void setUp() {

        RestAssured.baseURI="https://gorest.co.in";

        requestData = new HashMap<>();
        requestData.put("name","patrick");
        requestData.put("email","patrickbeatman@white.com");
        requestData.put("gender","male");
        requestData.put("status","active");

        reSpec = given()
                .log().uri()
                .header("Authorization","Bearer d56b83869ea87c5dbd633c8993227beba2636426d8c2bea43d8b505f643dfb19")
                .contentType(ContentType.JSON);

        System.out.println(requestData);
    }

    @Test(priority = 1)
    public void createUser() {

        userId = given() // userID will be stored through the given when then steps by extractPath method
                .spec(reSpec)
                .body(requestData)
                .when()
                .post("public/v2/users")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(requestData.get("name"))) // getting the name field from (response body) part and (hashMap) and comparing them if they are equal
                .extract().path("id");

        //String userID = get("/person").path("person.userId");
    }

    @Test(priority = 2)
    public void negativeCreateTest() {

        given()
                .spec(reSpec)
                .body(requestData)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(422)
                .body("messages[0]",equalTo( "has already been taken"));
    }

    @Test(priority = 3)
    public void editUser() {

        HashMap<String,String> editUserEmail = new HashMap<>();
        editUserEmail.put("email","hayrun@nisa20.com");

        given()
                .spec(reSpec)
                .body(editUserEmail)
                .when()
                .put("/public/v2/users/" + userId)
                .then()
                .log().body()
                .statusCode(200)
                .body("email",equalTo(editUserEmail.get("email")));
                         // from response body           // from ediUserEmail HashMap variable
    }

    @Test(dependsOnMethods = "editUser")
    public void deleteUser() {
        given()
                .spec(reSpec)
                .when()
                .delete("/public/v2/users/"+userId)
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegativeTest() {
        given()
                .spec(reSpec)
                .when()
                .delete("/public/v2/users/"+userId)
                .then()
                .log().body()
                .statusCode(404);
    }



















}
