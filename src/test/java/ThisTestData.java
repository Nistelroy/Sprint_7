import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ThisTestData {
    private static int idCourier;
    public static String loginRandomUser = "dpetrovf";
    public static String passwordRandomUser = "123456";
    public static String nameRandomUser = "madaraa";

    public static Response createCourierForTest() {
        String json = "{\"login\": \"" + loginRandomUser + "\", \"password\": \"" + passwordRandomUser
                + "\", \"firstName\": \"" + nameRandomUser + "\"}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/api/v1/courier");

        return response;
    }

    public static Response loginCourierForSaveId() {
        String json = "{\"login\": \"" + loginRandomUser + "\", \"password\": \"" + passwordRandomUser + "\"}";

        Response response = given()
                .contentType("application/json")
                .body(json)
                .post("/api/v1/courier/login");

        idCourier = response.then().extract().path("id");

        return response;
    }

    public static void deleteCourierForTest() {
        loginCourierForSaveId();

        given()
                .header("Content-Type", "application/json")
                .delete("/api/v1/courier/" + idCourier);
    }
}
