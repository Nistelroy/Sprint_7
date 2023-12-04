package оrdorcreate;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final List<String> colorJson;
    private final int responseCode;

    public OrderCreateTest(List<String> colorJson, int responseCode) {
        this.colorJson = colorJson;
        this.responseCode = responseCode;
    }

    @Parameterized.Parameters
    public static Object[][] getJsonForOrder() {
        return new Object[][]{
                {new ArrayList<>(List.of(new Color("BLACK"))), 201},
                {new ArrayList<>(List.of(new Color("GREY"))), 201},
                {new ArrayList<>(List.of(new Color("BLACK"), new Color("GREY"))), 201},
                {new ArrayList<>(), 201},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void orderCreateTest() {
        Orders ordersJson = new Orders("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35",
                "5", "2020-06-06", "Saske, come back to Konoha", List.of(colorJson.toString()));

        Response response = given()
                .header("Content-Type", "application/json")
                .body(ordersJson)
                .post("/api/v1/orders");

        response.then().statusCode(responseCode).and().assertThat().body("track", notNullValue());
    }
}
