import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
    private int randomInt = new Random().nextInt();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("To authorize you need to specify the correct login")
    public void loginCourierWrongLoginStatus404() {
        String json = "{\"login\": \"" + (ThisTestData.loginRandomUser + randomInt) + "\", \"password\": \""
                + ThisTestData.passwordRandomUser + "\"}"; //неправильный логин

        ThisTestData.createCourierForTest();
        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        response.then().statusCode(404);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    @DisplayName("To authorize you need to specify the correct password")
    public void loginCourierWrongPasswordStatus404() {
        String json = "{\"login\": \"" + ThisTestData.loginRandomUser + "\", \"password\": \""
                + (ThisTestData.passwordRandomUser + randomInt) + "\"}"; //неправильный пароль

        ThisTestData.createCourierForTest();
        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        response.then().statusCode(404);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    @DisplayName("For authorization you need to specify a login")
    public void loginCourierNoLoginStatus400() {
        String json = "{\"login\": \"\", \"password\": \"" + ThisTestData.passwordRandomUser + "\"}"; //пустой логин

        ThisTestData.createCourierForTest();
        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        response.then().statusCode(400);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    @DisplayName("For authorization you need to specify a password")
    public void loginCourierNoPasswordStatus400() {
        String json = "{\"login\": \"" + ThisTestData.loginRandomUser + "\", \"password\": \"\"}"; //пустой пароль

        ThisTestData.createCourierForTest();
        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        response.then().statusCode(400);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    @DisplayName("the courier can log in")
    public void loginExistingCourierCode200() {

        ThisTestData.createCourierForTest();
        Response response = ThisTestData.loginCourierForSaveId();

        response.then().statusCode(200);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    @DisplayName("successful login returns id")
    public void loginCourierCorrectDataIdNotNull() {

        ThisTestData.createCourierForTest();
        Response response = ThisTestData.loginCourierForSaveId();

        response.then().assertThat().body("id", notNullValue());
        ThisTestData.deleteCourierForTest();
    }
}
