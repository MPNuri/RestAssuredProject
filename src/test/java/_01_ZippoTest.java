import static io.restassured.RestAssured.*; // for the RestAssured codes
import static org.hamcrest.Matchers.*; // for the Hamscrest

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class _01_ZippoTest {

    /** Hamcrest
     * Allows you to do verification in readable language just like Assertions but more reliable
     * some example; equalTo(X) or hasSize(3).
     * to be able to use hamcrest we need to add its dependencies
     */

    @BeforeClass
    public void setUp() {
        // we specifically need a BeforeClass annotation to setUp baseURL
        // will be sent to each test auto ( do not need to type manually in each test )
        RestAssured.baseURI = "https://www.zippopotam.us";
    }
    // exact same thing in postman. will only implement after given when then part
    @Test
    public void test() {

        given() // body, header information will be provided here
                .when() // request method and URL part here
                .then(); // this is where we do assertion by error codes
    }

    @Test
    public void checkingStatusCode() {

        given() // no, body or authorization so nothing after given part
                .when()
                .get("/tr/35050") // send the request
                .then()
                .statusCode(200); // verification part
    }

    @Test
    public void loggingRequestDetails() {

        given()
                .log().all()
                .when()
                .get("/tr/35052")
                .then()
                .statusCode(200);
    }

    @Test
    public void loggingResponseDetails() {

        given()
                .when()
                .get("/tr/35030")
                .then()
                .log().all() // used log.all after then so the order is about response now
                .statusCode(200);
    }

    @Test
    public void checkContentType() {

        given()
                .when()
                .get("/us/75080")
                .then()
                .contentType(ContentType.JSON) // checking the content format
                .statusCode(200);
    }

    @Test
    public void checkCountry() {

        given()
                .when()
                .get("/us/75080")
                .then()
                .body("country",equalTo("United States")) // country field from response body and the name of the response
                .statusCode(200);
    }

    @Test
    public void validateCountryAbv() {

        given()
                .when()
                .get("/us/75080")
                .then()
                // if you have space inside your body response you should use '' single quotas inside the "" double quotas
                .body("'country abbreviation'", equalTo("US"))
                .statusCode(200);
    }

    @Test
    public void stateValidation() {

        given()
                .when()
                .get("/us/75080")
                .then()
                // if there are arrays (more than one info inside a single body response) we use array methodology
                .body("places[0].state",equalTo("Texas"))
                .statusCode(200);
    }

    // pathParam affects path we go , param affects search result simply?
    @Test
    public void usingPathParameters() {

        String country = "us"; // our value of the variable. Do not have to create String but used anyway to avoid hard coding
        String zipcode = "75080";

        given()
                .pathParam("country",country)
                .pathParam("zipCode",zipcode) // naming our variables be careful with the space and upper case
                .when()
                .get("/{country}/{zipCode}")
                .then()
                .statusCode(200);

    }

    // pathParam affects path we go , param affects search result simply?
    // if you use log().all() after given you will get request info if else after then you will get response data
    @Test
    public void queryParameters() {

        String gender = "female";
        String status = "inactive";

        given() // we used only .param because this is query parameter which is represented inside the url part in postman
                // ?gender=female&status=inactive
                .param("gender",gender)
                .param("status",status)
                .when()
                .get("https://gorest.co.in/public/v2/users")
                .then()
                .statusCode(200)
                .log().body();
    }

    @Test
    public void extractValueTest() {

        // for example, we stored ID information inside a variable in Postman here we will use extract.path

        Object extractCountry = given() // stored the country output
                .when()
                .get("/us/75080")
                .then()
                .statusCode(200)
                .extract().path("country"); // decided what to extract (after then part always)
        System.out.println(extractCountry);
    }




































}
