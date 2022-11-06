import PojoClass._01_GoRestUserCreatinguser;
import PojoClass._02_GoRestCreatingPost;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.groovy.parser.antlr4.GroovyParser;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*; // for the RestAssured codes
import static org.hamcrest.Matchers.*; // for the Hamcrest, gotta work on this

public class _05_GoRestCreatingPost {

    _01_GoRestUserCreatinguser userData;
    _02_GoRestCreatingPost postData;
    RequestSpecification reqSpec;

    @BeforeClass(alwaysRun = true)
    public void setU() {

        RestAssured.baseURI="https://gorest.co.in";

        reqSpec = given()
                .header("Authorization", "Bearer d56b83869ea87c5dbd633c8993227beba2636426d8c2bea43d8b505f643dfb19")
                .log().body()
                .contentType(ContentType.JSON);

        userData = new _01_GoRestUserCreatinguser();
        userData.setName("Zokora");
        userData.setEmail("lassana@serjan.com");
        userData.setGender("male");
        userData.setStatus("inactive");

        postData = new _02_GoRestCreatingPost();
        //postData.setUserid(userData.getId()); // do not forget to add the id when user is created
        postData.setTitle("thinking of learning new variety of Accents");
        postData.setBody("hello, today we will be learning how to talk like Indian accent");

    }

    @Test(priority = 1)
    public void createUser() {

        userData.setId(given()
                .spec(reqSpec)
                .body(userData)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(201)
                .body("name",equalTo(userData.getName()))
                .extract().jsonPath().getString("id")); // we must use a type of way to get id ti field and deploy into UserdataSetId variable
    }

    @Test(dependsOnMethods = "createUser")
    public void createUserNegativeTest() {

        given()
                .spec(reqSpec)
                .body(userData)
                .when()
                .post("public/v2/users")
                .then()
                .log().body()
                .statusCode(422);
    }

    @Test(dependsOnMethods = "createUserNegativeTest")
    public void createPost() {

        postData.setPostID(given()
                .spec(reqSpec)
                .body(postData)
                .when()
                .post("/public/v2/users/"+ userData.getId() +"/posts")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id"));
    }

    @Test(dependsOnMethods = "createPost")
    public void editPost() {

        HashMap<String,String> newTitle = new HashMap<>();
        newTitle.put("title","learning different variety of Accents from an Arab");

        given()
                .spec(reqSpec)
                .body(newTitle)
                .when()
                .put("/public/v2/users/" + postData.getPostID())
                .then()
                .statusCode(200); //.body("title",equalTo(postData.getTitle())
    }

    @Test(dependsOnMethods = "editPost")
    public void deletePost() {

        given()
                .spec(reqSpec)
                .when()
                .delete("public/v2/users/"+postData.getPostID())
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deletePost")
    public void deletePostNegative() {

        given()
                .spec(reqSpec)
                .when()
                .delete("public/v2/users/"+postData.getPostID())
                .then()
                .log().body()
                .statusCode(404);
    }

    @Test(dependsOnMethods = "deletePostNegative")
    public void deleteUser() {

        given()
                .spec(reqSpec)
                .when()
                .delete("public/v2/users/"+userData.getId())
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegative() {

        given()
                .spec(reqSpec)
                .when()
                .delete("public/v2/users/"+userData.getId())
                .then()
                .log().body()
                .statusCode(404);
    }

}
