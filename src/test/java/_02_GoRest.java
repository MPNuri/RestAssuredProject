import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*; // for the RestAssured codes


public class _02_GoRest {

    private RequestSpecification reqSpec; // I fake used this in line 42
    private HashMap<String, String> requestBody; // will show cleaner coding
    private Object getID;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://gorest.co.in";

        // we created this action to be able to use shortcut method
        reqSpec = given()
                .log().uri() // to see the request body if we get any error, or exception
                .header("Authorization","Bearer d56b83869ea87c5dbd633c8993227beba2636426d8c2bea43d8b505f643dfb19")
                .contentType(ContentType.JSON);

        // we need Key Value method so HasMap will be required (best FIT)
        requestBody = new HashMap<>();
        requestBody.put("name","zaytins");
        requestBody.put("email","jonkinenscko@kuku.com");
        requestBody.put("gender","male");
        requestBody.put("status","inactive");

        System.out.println(requestBody);
    }

    @Test
    public void createUser() {

        // we gave our authorization through header and type of the text as JS0N
        getID = given() // storing the ID output
                // .spec(reqSpec) this line includes 43,44,45 because we declared in the BeforeClass
                .log().uri() // every log. is used to know if correct headers, param etc. are sent and received. (here specifically uri before the test)
                .header("Authorization","Bearer d56b83869ea87c5dbd633c8993227beba2636426d8c2bea43d8b505f643dfb19")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/public/v2/users") // instead get we use post when we need to create an object
                .then()
                .log().body() // we are bale to get all the information about the response body (id as well)
                .statusCode(201)
                .extract().path("id"); // sending us the id that will be created auto

        System.out.println(getID); // ID printer
    }

    @Test(dependsOnMethods = "createUser")
    public void editUser() {

        HashMap<String,String> updatedName = new HashMap<>();
                updatedName.put("name","Gotockssu");

                given()
                        .spec(reqSpec)
                        .body(updatedName)
                        .when()
                        .put("/public/v2/users/"+getID)
                        .then()
                        .log().body()
                        .statusCode(200);
    }

    @Test(dependsOnMethods = "editUser")
    public void deleteUser() {

        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/"+getID)
                .then()
                .log().body() // why we used log.body each time is to be able to get more information about the error code if any occurs
                .statusCode(204);

    }
















}
