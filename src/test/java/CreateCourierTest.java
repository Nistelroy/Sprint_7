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



