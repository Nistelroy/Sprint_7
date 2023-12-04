import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
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
    public void loginExistingCourierCode200() {

        ThisTestData.createCourierForTest();
        Response response = ThisTestData.loginCourierForSaveId();

        response.then().statusCode(200);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    public void loginCourierCorrectDataIdNotNull() {

        ThisTestData.createCourierForTest();
        Response response = ThisTestData.loginCourierForSaveId();

        response.then().assertThat().body("id", notNullValue());
        ThisTestData.deleteCourierForTest();
    }
}
