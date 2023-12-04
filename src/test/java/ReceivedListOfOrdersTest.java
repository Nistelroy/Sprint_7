import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ReceivedListOfOrdersTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("the response body returns a list of orders")
    public void getListOrderEmptyRequestOrderListNotNullAndStatus200() {

        Response response = given()
                .get("/api/v1/orders");

        response.then().assertThat().body("orders", notNullValue()).and().statusCode(200);
    }
}
