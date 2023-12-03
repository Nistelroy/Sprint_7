import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateCourierTest {
    int idCourier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourierCorrectDataStatus201() {

        Response response = createCourierForTest();

        response.then().statusCode(201);
        deleteCourierForTest();
    }

    @Test
    public void loginCourierCorrectDataIdNotNullAndStatus200() {

        createCourierForTest();
        Response response = loginCourierForSvaeId();

        response.then().assertThat().body("id", notNullValue()).and().statusCode(200);
        deleteCourierForTest();
    }

    @Test
    public void createTwoCourierSameDataStatus409() {
        createCourierForTest();
        Response response = createCourierForTest();
        response.then().statusCode(409);

        deleteCourierForTest();
    }

    @Test
    public void needAllDataForCreateCourierNoPasswordStatus400() {
        String json = "{\"login\": \"dpetrov\", \"firstName\": \"madara\"}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");

        response.then().statusCode(400);
    }

    @Test
    public void needAllDataForCreateCourierNoLoginStatus400() {
        String json = "{\"password\": \"12345\", \"firstName\": \"madara\"}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");

        response.then().statusCode(400);
    }

    @Test
    public void createCourierCorrectDataResponseСontainsTrue() {
        Response response = createCourierForTest();

        response.then().assertThat().body("ok", equalTo(true));
        deleteCourierForTest();
    }

    @Test
    public void loginCourierWrongPasswordStatus404() {
        String json = "{\"login\": \"dpetrov\", \"password\": \"1234\"}";

        createCourierForTest();
        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        response.then().statusCode(404);
        deleteCourierForTest();
    }

    @Test
    public void loginCourierWrongLoginStatus404() {
        String json = "{\"login\": \"dpetro\", \"password\": \"12345\"}";

        createCourierForTest();
        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        response.then().statusCode(404);
        deleteCourierForTest();
    }

    @Test
    public void loginCourierNoLoginStatus400() {
        String json = "{\"login\": \"\", \"password\": \"12345\"}";

        createCourierForTest();
        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        response.then().statusCode(400);
        deleteCourierForTest();
    }

    @Test
    public void loginCourierNoPasswordStatus400() {
        String json = "{\"login\": \"dpetrov\", \"password\": \"\"}";

        createCourierForTest();
        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        response.then().statusCode(400);
        deleteCourierForTest();
    }

    @Test
    public void createCourierBusyLoginStatus409() {
        createCourierForTest();
        String busyLoginJson = "{\"login\": \"dpetrov\", \"password\": \"12345112\", \"firstName\": \"rock-lee\"}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(busyLoginJson)
                .post("/api/v1/courier");

        response.then().statusCode(409);
        deleteCourierForTest();
    }

    private Response createCourierForTest() {
        String json = "{\"login\": \"dpetrov\", \"password\": \"12345\", \"firstName\": \"madara\"}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");

        return response;
    }

    private Response loginCourierForSvaeId() {
        String json = "{\"login\": \"dpetrov\", \"password\": \"12345\"}";

        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        idCourier = response.then().extract().path("id");

        return response;
    }

    private void deleteCourierForTest() {
        loginCourierForSvaeId();

        given()
                .header("Content-Type", "application/json")
                .delete("/api/v1/courier/" + idCourier);
    }
}



