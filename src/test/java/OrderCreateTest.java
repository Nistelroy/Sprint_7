import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.orders.Color;
import ru.yandex.practicum.orders.Orders;
import ru.yandex.practicum.orders.OrdersGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.yandex.practicum.ThisTestData.BASE_URI;
import static ru.yandex.practicum.ThisTestData.createOrderInApi;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final List<String> colorList;

    public OrderCreateTest(List<String> colorList) {
        this.colorList = colorList;
    }

    @Parameterized.Parameters
    public static Object[][] getJsonForOrder() {
        return new Object[][]{
                {new ArrayList<>(List.of(new Color("BLACK")))},
                {new ArrayList<>(List.of(new Color("GREY")))},
                {new ArrayList<>(List.of(new Color("BLACK"), new Color("GREY")))},
                {new ArrayList<>()},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    public void orderCreateTest() {

        Orders orders = OrdersGenerator.randomOrdersExceptColor(colorList);

        Response response = createOrderInApi(orders);

        response.then().statusCode(SC_CREATED).and().assertThat().body("track", notNullValue());
    }
}
