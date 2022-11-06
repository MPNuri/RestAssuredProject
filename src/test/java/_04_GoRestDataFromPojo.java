import PojoClass._01_GoRestUserCreatinguser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*; // for the RestAssured codes
import static org.hamcrest.Matchers.*; // for the Hamcrest

public class _04_GoRestDataFromPojo {

    private HashMap<String,String> editedName;
    private RequestSpecification reqSpec;
    private _01_GoRestUserCreatinguser user;

    @BeforeClass
    public void setUp() {

        RestAssured.baseURI = "https://gorest.co.in";

        reqSpec = given()
                .log().body()
                .header("Authorization","Bearer d56b83869ea87c5dbd633c8993227beba2636426d8c2bea43d8b505f643dfb19")
                .contentType(ContentType.JSON);

        user = new _01_GoRestUserCreatinguser();
        user.setName("murilllovski");
        user.setEmail("mpn.31@seven.com");
        user.setGender("female");
        user.setStatus("active");
    }

    @Test(priority = 1)
    public void createUser() {

        user.setId(given() // through () inside user.setID the id will be implemented inside id variable. but everything must be inside ()
                .spec(reqSpec)
                .body(user)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(201)
                .body("name",equalTo(user.getName()))
                .extract().jsonPath().getString("id"));
    }

    @Test(priority = 2)
    public void createUserNegative() {

        given()
                .spec(reqSpec)
                .body(user)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(422);
    }

    @Test(priority = 3)
    public void getUser(){

        given()
                .spec(reqSpec)
                .when()
                .get("public/v2/users/" + user.getId())
                .then()
                .log().body()
                .statusCode(200)
                // do not forget = when you do assertion use body() not header other than looks fine
                .body("name", equalTo(user.getName()))
                .body("email", equalTo(user.getEmail()))
                .body("gender", equalTo(user.getGender()))
                .body("status", equalTo(user.getStatus()));
    }

    @Test(priority = 4)
    public void editUser() {

        editedName = new HashMap<>();
        editedName.put("name","xRentgon");

        given()
                .spec(reqSpec)
                .body(editedName)
                .when()
                .put("/public/v2/users/"+user.getId())
                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(editedName.get("name")));
    }

    @Test(priority = 5)
    public void deleteUserTest() {

        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + user.getId())
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(priority = 6)
    public void deleteUserNegativeTest() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + user.getId())
                .then()
                .log().body()
                .statusCode(404);
    }







}
