package api;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import config.Config;
import io.restassured.response.Response;

public class LoginApiTests {
    // ---------- 200 ----------
    @Test
    public void testLoginCredenciaisValidas200() {
        String username = Config.get("user.username");
        String senhaErrada = Config.get("user.password");

        Response response = LoginApi.login(username, senhaErrada);
        System.out.println(response.statusCode());
        assertEquals(201, response.statusCode());
    }

    // ---------- 401 ----------
    @Test
    public void testLoginCredenciaisInvalidas401() {
        String username = Config.get("user.username");
        String senhaErrada = "SenhaIncorretaTeste";

        Response response = LoginApi.login(username, senhaErrada);

        assertEquals(401, response.statusCode());
        assertTrue(response.asString().toLowerCase().contains("username or password is incorrect"));
    }

    // ---------- 403 ----------
    @Test
     public void testLoginPerfilVisitor403() {
        String username = Config.get("visitor.username");
        String password = Config.get("visitor.password");

        Response response = LoginApi.login(username, password);

        assertEquals(403, response.statusCode());
        assertTrue(response.asString().toLowerCase().contains("acesso negado"));
    }

    // ---------- 423 ----------
    @Test
    public void testUsuarioBloqueado423() {
        String username = Config.get("blocked.username");
        String password = Config.get("blocked.password");

        Response response = LoginApi.login(username, password);

        assertEquals(423, response.statusCode());
        assertTrue(response.asString().toLowerCase().contains("bloque"));
    }
}
