package api;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

public class LoginApi {

    private static final String BASE_URL = "https://fakestoreapi.com/auth/login";

    public static io.restassured.response.Response login(String username, String password) {
        return given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}")
                .when()
                .post(BASE_URL)
                .then()
                .extract()
                .response();
    }
}