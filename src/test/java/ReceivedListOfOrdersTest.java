import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static ru.yandex.practicum.ThisTestData.BASE_URI;
import static ru.yandex.practicum.ThisTestData.getAllOrder;


public class ReceivedListOfOrdersTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("the response body returns a list of orders")
    public void getListOrderEmptyRequestOrderListAboveZeroAndStatus200() {

        Response response = getAllOrder();

        List<String> orders = response.then().extract().path("orders");

        response.then().assertThat().statusCode(200);

        assertTrue("Список orders пустой", orders.size() > 0);
    }
}
