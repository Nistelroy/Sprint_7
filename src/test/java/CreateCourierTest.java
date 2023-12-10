import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.models.Courier;
import ru.yandex.practicum.models.CourierGenerator;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.practicum.ThisTestData.BASE_URI;
import static ru.yandex.practicum.ThisTestData.createCourierInApi;
import static ru.yandex.practicum.ThisTestData.deleteCourierInApi;

public class CreateCourierTest {
    private Courier courier;
    private boolean needToDeleteCourier = false;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        courier = CourierGenerator.randomCourier();
    }

    @Test
    @DisplayName("courier creation check")
    public void createCourierCorrectDataStatus201() {
        Response response = createCourierInApi(courier);

        response.then().statusCode(SC_CREATED);

        needToDeleteCourier = true;
    }

    @Test
    @DisplayName("checking that it is impossible to create two identical couriers")
    public void createTwoCourierSameDataStatus409() {
        createCourierInApi(courier);
        Response response = createCourierInApi(courier);

        response.then().statusCode(SC_CONFLICT);

        needToDeleteCourier = true;

    }

    @Test
    @DisplayName("verification to create a courier, you need to submit all required fields")
    @Description("need password")
    public void needAllDataForCreateCourierNoPasswordStatus400() {
        courier.withPassword("");

        Response response = createCourierInApi(courier);

        response.then().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("verification to create a courier, you need to submit all required fields")
    @Description("need login")
    public void needAllDataForCreateCourierNoLoginStatus400() {
        courier.withLogin("");

        Response response = createCourierInApi(courier);

        response.then().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("a successful request returns ok: true")
    public void createCourierCorrectDataResponseContainsTrue() {
        Response response = createCourierInApi(courier);

        response.then().assertThat().body("ok", equalTo(true));

        needToDeleteCourier = true;
    }

    @Test
    @DisplayName("create a user with a login that you no longer have")
    public void createCourierBusyLoginStatus409() {
        Faker faker = new Faker();
        String savePassForDelete = courier.getPassword(); //сохраняю пароль

        createCourierInApi(courier); //создаю курьера

        courier.withPassword(faker.name().lastName()); //меняю пароль
        Response response = createCourierInApi(courier); //пробую создать с тем же логином и новым паролем

        response.then().statusCode(SC_CONFLICT);

        courier.withPassword(savePassForDelete); // возвращаю старый пароль, для удаления
        needToDeleteCourier = true;
    }

    @After
    public void deleteCourier() {
        if (needToDeleteCourier) {
            deleteCourierInApi(courier);
            needToDeleteCourier = false;
        }
    }
}



