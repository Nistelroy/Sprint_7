import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ThisTestData {
    private static int idCourier;
    public static String loginRandomUser = "dpetrovf";
    public static String passwordRandomUser = "123456";
    public static String nameRandomUser = "madaraa";

    @Step("Create basic courier for use in tests")
    public static Response createCourierForTest() {
        String json = "{\"login\": \"" + loginRandomUser + "\", \"password\": \"" + passwordRandomUser
                + "\", \"firstName\": \"" + nameRandomUser + "\"}";

        return given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");


    }

    @Step("Login basic courier for use in tests and save id for delete")
    public static Response loginCourierForSaveId() {
        String json = "{\"login\": \"" + loginRandomUser + "\", \"password\": \"" + passwordRandomUser + "\"}";

        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        idCourier = response.then().extract().path("id");

        return response;
    }

    @Step("Delete basic courier after use in tests and save id")
    public static void deleteCourierForTest() {
        loginCourierForSaveId();

        given()
                .header("Content-Type", "application/json")
                .delete("/api/v1/courier/" + idCourier);
    }
}
