package ru.yandex.praktikum.scooter.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPageOne {

    private final WebDriver driver;

    public OrderPageOne(WebDriver driver) {
        this.driver = driver;
    }

    /* ===================== Locators ===================== */

    private final By firstName = By.xpath("//input[@placeholder='* Имя']");
    private final By lastName  = By.xpath("//input[@placeholder='* Фамилия']");
    private final By address   = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metro     = By.xpath("//input[@placeholder='* Станция метро']");
    private final By phone     = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextBtn   = By.xpath("//button[contains(@class,'Button_Button') and text()='Далее']");

    private By metroOption(String name) {
        return By.xpath("//div[contains(@class,'select-search__select')]//div[text()='" + name + "']");
    }

    /* ===================== Helpers ===================== */

    private WebDriverWait wdWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    /* ===================== Public API ===================== */

    /** Заполняет поля формы первого шага. */
    public void fillCustomer(String f, String l, String addr, String metroName, String phoneNumber) {
        wdWait().until(ExpectedConditions.visibilityOfElementLocated(firstName));
        driver.findElement(firstName).clear();
        driver.findElement(firstName).sendKeys(f);

        driver.findElement(lastName).clear();
        driver.findElement(lastName).sendKeys(l);

        driver.findElement(address).clear();
        driver.findElement(address).sendKeys(addr);

        WebElement metroInput = driver.findElement(metro);
        scrollIntoView(metroInput);
        metroInput.click();
        metroInput.sendKeys(metroName);

        wdWait().until(ExpectedConditions.visibilityOfElementLocated(metroOption(metroName)));
        driver.findElement(metroOption(metroName)).click();

        driver.findElement(phone).clear();
        driver.findElement(phone).sendKeys(phoneNumber);
    }

    /** Нажимает кнопку «Далее». */
    public void clickNext() {
        WebElement btn = wdWait().until(ExpectedConditions.elementToBeClickable(nextBtn));
        scrollIntoView(btn);
        btn.click();
    }
}