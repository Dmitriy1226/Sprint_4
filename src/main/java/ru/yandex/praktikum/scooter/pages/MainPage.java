package ru.yandex.praktikum.scooter.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MainPage {

    private final WebDriver driver;
    private static final String URL = "https://qa-scooter.praktikum-services.ru/";

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    /* ===================== helpers ===================== */

    private WebDriverWait wdWait(long seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    // Безопасный клик: пробуем обычный клик → если блокируется, кликаем через JS
    private void safeClick(WebElement el) {
        scrollIntoView(el);
        try {
            wdWait(5).until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (WebDriverException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    /* ===================== cookies ===================== */

    private final By cookieBtnClass = By.cssSelector(
            ".App_CookieButton__3cvqF, .cookie__button, .button_button__ra12g");
    private final By cookieBtnText = By.xpath("//button[contains(.,'Принять') or contains(.,'Да')]");

    private void acceptCookiesIfAny() {
        try {
            WebElement b = driver.findElement(cookieBtnClass);
            if (b.isDisplayed()) b.click();
        } catch (NoSuchElementException ignored) {}
        try {
            WebElement b = driver.findElement(cookieBtnText);
            if (b.isDisplayed()) b.click();
        } catch (NoSuchElementException ignored) {}
    }

    /* ===================== FAQ ===================== */

    private final By faqRoot = By.xpath("//*[@id='accordion' or contains(@class,'accordion')]");
    private By question(int i) { return By.id("accordion__heading-" + i); }
    private By answer(int i)   { return By.id("accordion__panel-" + i); }

    /* ===================== Order buttons ===================== */

    private final By allOrderButtons =
            By.xpath("//button[contains(@class,'Button') and contains(.,'Заказать')]");

    /* ===================== public API ===================== */

    public void open() {
        driver.get(URL);
        wdWait(20).until(d ->
                "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
        acceptCookiesIfAny();
        WebElement root = wdWait(20).until(ExpectedConditions.visibilityOfElementLocated(faqRoot));
        scrollIntoView(root);
    }

    public void expandQuestion(int index) {
        WebElement q = wdWait(10).until(ExpectedConditions.elementToBeClickable(question(index)));
        scrollIntoView(q);
        q.click();
        wdWait(10).until(ExpectedConditions.visibilityOfElementLocated(answer(index)));
    }

    public String getAnswerText(int index) {
        return driver.findElement(answer(index)).getText();
    }

    public void clickOrderTop() {
        wdWait(10).until(ExpectedConditions.numberOfElementsToBeMoreThan(allOrderButtons, 0));
        List<WebElement> buttons = driver.findElements(allOrderButtons);
        WebElement top = buttons.get(0);
        safeClick(top);
    }

    public void clickOrderBottom() {
        wdWait(10).until(ExpectedConditions.numberOfElementsToBeMoreThan(allOrderButtons, 0));
        List<WebElement> buttons = driver.findElements(allOrderButtons);
        WebElement bottom = buttons.get(buttons.size() - 1);
        safeClick(bottom);
    }
}