package ui.Tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;         // JUnit5 imports (ver item 3)
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import config.Config;
import ui.Page.LoginPage;


public class LoginUITest {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
        loginPage.open();
    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }

    //Login válido
    @Test
    public void testLoginValido() {
        loginPage.login(
            Config.get("user.username"),
            Config.get("user.password")
        );

        // exemplo de validação de dashboard
        Assertions.assertTrue(driver.getCurrentUrl().contains("/dashboard"));
    }

    //Login com perfil sem acesso (VISITOR)
    @Test
    public void testLoginSemAcesso() {
        loginPage.login(
            Config.get("visitor.username"),
            Config.get("visitor.password")
        );

        Assertions.assertEquals("Usuário sem acesso", loginPage.getErrorMessage());
    }

    //Bloqueio após 3 tentativas inválidas
    @Test
    public void testBloqueioApos3Erros() {

        for (int i = 0; i < 3; i++) {
            loginPage.login("USER", "senhaErrada");
            Assertions.assertEquals("Credenciais inválidas", loginPage.getErrorMessage());
        }

        // 4ª tentativa o usuário deve estar bloqueado
        loginPage.login("USER", "SenhaCorreta123");
        Assertions.assertEquals("Usuário bloqueado por excesso de tentativas", 
                            loginPage.getBlockedMessage());
    }
}
