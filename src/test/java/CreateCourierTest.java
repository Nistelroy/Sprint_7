import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {
    private int randomInt = new Random().nextInt();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("courier creation check")
    public void createCourierCorrectDataStatus201() {

        Response response = ThisTestData.createCourierForTest();

        response.then().statusCode(201);
        ThisTestData.deleteCourierForTest();
    }

    @Test
    @DisplayName("checking that it is impossible to create two identical couriers")
    public void createTwoCourierSameDataStatus409() {
        ThisTestData.createCourierForTest();
        Response response = ThisTestData.createCourierForTest();
        response.then().statusCode(409);

        ThisTestData.deleteCourierForTest();
    }

    @Test
    @DisplayName("verification to create a courier, you need to submit all required fields")
    @Description("need password")
    public void needAllDataForCreateCourierNoPasswordStatus400() {
        String json = "{\"login\": \"" + ThisTestData.loginRandomUser + "\", \"firstName\": \""
                + ThisTestData.nameRandomUser + "\"}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");

        response.then().statusCode(400);
    }

    @Test
    @DisplayName("verification to create a courier, you need to submit all required fields")
    @Description("need login")
    public void needAllDataForCreateCourierNoLoginStatus400() {
        String json = "{\"password\": \"" + ThisTestData.passwordRandomUser + "\", \"firstName\": \""
                + ThisTestData.nameRandomUser + "\"}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");

        response.then().statusCode(400);
    }

    @Test
    @DisplayName("a successful request returns ok: true")
    public void createCourierCorrectDataResponseContainsTrue() {
        Response response = ThisTestData.createCourierForTest();

        response.then().assertThat().body("ok", equalTo(true));
        ThisTestData.deleteCourierForTest();
    }

    @Test
    @DisplayName("create a user with a login that you no longer have")
    public void createCourierBusyLoginStatus409() {
        ThisTestData.createCourierForTest();
        String busyLoginJson = "{\"login\": \"" + ThisTestData.loginRandomUser + "\", \"password\": \""
                + (ThisTestData.passwordRandomUser + randomInt) + "\", \"firstName\": \""
                + ThisTestData.nameRandomUser + "\"}"; //Неправильный пароль

        Response response = given()
                .header("Content-Type", "application/json")
                .body(busyLoginJson)
                .post("/api/v1/courier");

        response.then().statusCode(409);
        ThisTestData.deleteCourierForTest();
    }
}



