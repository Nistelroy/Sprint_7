package ru.yandex.practicum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.models.Courier;
import ru.yandex.practicum.orders.Orders;

import static io.restassured.RestAssured.given;

public class ThisTestData {
    private static int idCourier;
    public static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";
    public static final String URI_FOR_CREATE_COURIER = "/api/v1/courier";
    public static final String URI_FOR_LOGIN_COURIER = "/api/v1/courier/login";
    public static final String URI_FOR_DELETE_COURIER = "/api/v1/courier/";
    public static final String URI_FOR_ORDER_COURIER = "/api/v1/orders";

    @Step("Create basic courier for use in tests")
    public static Response createCourierInApi(Courier courier) {

        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(URI_FOR_CREATE_COURIER);
    }

    @Step("Login basic courier for use in tests and save id for delete")
    public static Response loginCourierInApiAndSaveId(Courier courier) {

        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(URI_FOR_LOGIN_COURIER);

        idCourier = response.then().extract().path("id");

        return response;
    }

    @Step("try login basic courier for use in tests")
    public static Response loginCourierInApi(Courier courier) {

        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(URI_FOR_LOGIN_COURIER);
    }

    @Step("Delete basic courier after use in tests and save id")
    public static void deleteCourierInApi(Courier courier) {
        loginCourierInApiAndSaveId(courier);

        given()
                .header("Content-Type", "application/json")
                .and()
                .delete(URI_FOR_DELETE_COURIER + idCourier);
    }

    @Step("a list of orders is returned in the response body")
    public static Response createOrderInApi(Orders orders) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(orders)
                .when()
                .post(URI_FOR_ORDER_COURIER);
    }

    @Step("get a list of all orders")
    public static Response getAllOrder() {
        return given()
                .get(URI_FOR_ORDER_COURIER);
    }
}
