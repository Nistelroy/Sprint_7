import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.models.Courier;
import ru.yandex.practicum.models.CourierGenerator;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.yandex.practicum.ThisTestData.BASE_URI;
import static ru.yandex.practicum.ThisTestData.createCourierInApi;
import static ru.yandex.practicum.ThisTestData.deleteCourierInApi;
import static ru.yandex.practicum.ThisTestData.loginCourierInApi;
import static ru.yandex.practicum.ThisTestData.loginCourierInApiAndSaveId;

public class LoginCourierTest {
    private Courier courier;
    private boolean needToDeleteCourier = false;
    private Faker faker = new Faker();

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        courier = CourierGenerator.randomCourier();
    }

    @Test
    @DisplayName("To authorize you need to specify the correct login")
    public void loginCourierWrongLoginStatus404() {
        Response response = loginCourierInApi(courier);

        response.then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("To authorize you need to specify the correct password")
    public void loginCourierWrongPasswordStatus404() {
        String savePassForDeleteCourier = courier.getPassword();

        createCourierInApi(courier);

        courier.withPassword(faker.name().lastName()); //меняю пароль

        Response response = loginCourierInApi(courier); //пробую залогинить с тем же логином и новым паролем

        response.then().statusCode(SC_NOT_FOUND);

        courier.withPassword(savePassForDeleteCourier); // возвращаю старый пароль, для удаления
        needToDeleteCourier = true;
    }

    @Test
    @DisplayName("For authorization you need to specify a login")
    public void loginCourierNoLoginStatus400() {
        courier.withLogin("");

        Response response = loginCourierInApi(courier);

        response.then().statusCode(400);
    }

    @Test
    @DisplayName("For authorization you need to specify a password")
    public void loginCourierNoPasswordStatus400() {
        courier.withPassword("");

        Response response = loginCourierInApi(courier);

        response.then().statusCode(400);
    }

    @Test
    @DisplayName("the courier can log in")
    public void loginExistingCourierCode200() {
        createCourierInApi(courier);
        Response response = loginCourierInApiAndSaveId(courier);

        response.then().statusCode(200);

        needToDeleteCourier = true;
    }

    @Test
    @DisplayName("successful login returns id")
    public void loginCourierCorrectDataIdNotNull() {
        createCourierInApi(courier);
        Response response = loginCourierInApiAndSaveId(courier);

        response.then().assertThat().body("id", notNullValue());

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
