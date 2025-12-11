package ui.Page;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import config.Config;


public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameField = By.id("username"); // podemos utilizar tambem o Xpath By.xpath("//input[@id='username']") ou assim //input[@type='text' and @id='username'] para ficar mais especifico
    private final By passwordField = By.id("password"); // podemos utilizar tambem o Xpath By.xpath("//input[@id='password']")
    private final By loginButton   = By.id("btnLogin"); // podemos utilizar tambem o Xpath By.xpath("//input[@id='btnLogin']")

    private final By errorMessage = By.id("errorMsg");
    private final By blockedMessage = By.id("blockedMsg");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(Config.get("base.url"));
    }

    public void login(String user, String pass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(user);
        driver.findElement(passwordField).sendKeys(pass);
        driver.findElement(loginButton).click();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    public String getBlockedMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(blockedMessage)).getText();
    }
}
