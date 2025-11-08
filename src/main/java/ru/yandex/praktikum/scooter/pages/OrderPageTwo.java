package ru.yandex.praktikum.scooter.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPageTwo {

    private final WebDriver driver;

    // Локаторы полей
    private final By dateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodDropdown = By.className("Dropdown-control");
    private final By colorBlack = By.id("black");
    private final By colorGrey = By.id("grey");
    private final By commentField = By.xpath("//input[@placeholder='Комментарий для курьера']");

    // Локаторы для оформления заказа
    private final By orderButton = By.xpath("//button[contains(@class,'Button_Middle') and text()='Заказать']");
    private final By yesButton = By.xpath("//button[text()='Да']");
    private final By successPopup = By.xpath("//div[contains(text(),'Заказ оформлен')]");

    public OrderPageTwo(WebDriver driver) {
        this.driver = driver;
    }

    // Установка даты
    public void setDate(String date) {
        driver.findElement(dateField).sendKeys(date);
        driver.findElement(dateField).submit();
    }

    // Выбор срока аренды
    public void setPeriod(String period) {
        driver.findElement(rentalPeriodDropdown).click();
        driver.findElement(By.xpath("//div[@class='Dropdown-option' and text()='" + period + "']")).click();
    }

    // Выбор цвета самоката
    public void chooseColor(String color) {
        if (color.equalsIgnoreCase("black")) {
            driver.findElement(colorBlack).click();
        } else if (color.equalsIgnoreCase("grey")) {
            driver.findElement(colorGrey).click();
        }
    }

    // Добавление комментария
    public void setComment(String comment) {
        driver.findElement(commentField).sendKeys(comment);
    }

    // Клик по кнопке "Заказать" + подтверждение "Да" + проверка окна успеха
    public boolean submitAndConfirm() {
        // нажимаем кнопку "Заказать" на шаге аренды
        driver.findElement(orderButton).click();

        // ждем появления окна подтверждения и кликаем "Да"
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(yesButton))
                .click();

        // ждём появления окна "Заказ оформлен"
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(successPopup));

        // возвращаем true, если окно появилось
        return !driver.findElements(successPopup).isEmpty();
    }
}