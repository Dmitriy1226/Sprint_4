package ru.yandex.praktikum.scooter.tests;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;

    @Before
    public void setUp() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();

        switch (browser) {
            case "firefox": {
                FirefoxOptions ff = new FirefoxOptions();
                // ff.addArguments("-headless"); // если нужно без окна
                driver = new FirefoxDriver(ff);   // Selenium Manager сам найдёт geckodriver
                break;
            }
            case "chrome":
            default: {
                ChromeOptions ch = new ChromeOptions();
                // ch.addArguments("--headless=new"); // если нужно без окна
                driver = new ChromeDriver(ch);     // Selenium Manager сам найдёт chromedriver
                break;
            }
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // только явные ожидания
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}