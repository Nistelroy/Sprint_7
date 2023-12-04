import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourierCorrectDataStatus201() {

        Response response = ThisTestData.createCourierForTest();

        response.then().statusCode(201);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    public void loginCourierCorrectDataIdNotNullAndStatus200() {

        ThisTestData.createCourierForTest();
        Response response = ThisTestData.loginCourierForSaveId();

        response.then().assertThat().body("id", notNullValue()).and().statusCode(200);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    public void createTwoCourierSameDataStatus409() {
        ThisTestData.createCourierForTest();
        Response response = ThisTestData.createCourierForTest();
        response.then().statusCode(409);

        ThisTestData.deleteCourierForTest();
    }

    @Test
    public void needAllDataForCreateCourierNoPasswordStatus400() {
        String json = "{\"login\": \"" + ThisTestData.loginRandomUser + "\", \"firstName\": \"" + ThisTestData.nameRandomUser + "\"}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");

        response.then().statusCode(400);
    }

    @Test
    public void needAllDataForCreateCourierNoLoginStatus400() {
        String json = "{\"password\": \"" + ThisTestData.passwordRandomUser + "\", \"firstName\": \"" + ThisTestData.nameRandomUser + "\"}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");

        response.then().statusCode(400);
    }

    @Test
    public void createCourierCorrectDataResponseContainsTrue() {
        Response response = ThisTestData.createCourierForTest();

        response.then().assertThat().body("ok", equalTo(true));
        ThisTestData.deleteCourierForTest();
    }

    @Test
    public void loginCourierWrongPasswordStatus404() {
        String json = "{\"login\": \"" + ThisTestData.loginRandomUser + "\", \"password\": \"1234\"}"; //неправильный пароль

        ThisTestData.createCourierForTest();
        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        response.then().statusCode(404);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    public void loginCourierWrongLoginStatus404() {
        String json = "{\"login\": \"dpetro\", \"password\": \"" + ThisTestData.passwordRandomUser + "\"}"; //неправильный логин

        ThisTestData.createCourierForTest();
        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        response.then().statusCode(404);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    public void loginCourierNoLoginStatus400() {
        String json = "{\"login\": \"\", \"password\": \"12345\"}"; //пустой логин

        ThisTestData.createCourierForTest();
        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        response.then().statusCode(400);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    public void loginCourierNoPasswordStatus400() {
        String json = "{\"login\": \"dpetrov\", \"password\": \"\"}"; //пустой пароль

        ThisTestData.createCourierForTest();
        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        response.then().statusCode(400);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    public void createCourierBusyLoginStatus409() {
        ThisTestData.createCourierForTest();
        String busyLoginJson = "{\"login\": \"" + ThisTestData.loginRandomUser + "\", \"password\": \"12345112\", \"firstName\": \""
                + ThisTestData.nameRandomUser + "\"}"; //Неправильный пароль

        Response response = given()
                .header("Content-Type", "application/json")
                .body(busyLoginJson)
                .post("/api/v1/courier");

        response.then().statusCode(409);
        ThisTestData.deleteCourierForTest();
    }


}



