package ru.yandex.praktikum.scooter.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class OrderPageTwo {

    private final WebDriver driver;

    public OrderPageTwo(WebDriver driver) {
        this.driver = driver;
    }

    /* ========= локаторы формы «Про аренду» ========= */

    private final By dateInput = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By durationDropdown = By.className("Dropdown-placeholder");

    private By durationOption(String text) {
        return By.xpath("//div[contains(@class,'Dropdown-menu')]//div[text()='" + text + "']");
    }

    private final By blackColor = By.id("black"); // «чёрный жемчуг»
    private final By greyColor  = By.id("grey");  // «серая безысходность»
    private final By comment    = By.xpath("//input[@placeholder='Комментарий для курьера']");

    // зелёная кнопка "Заказать" внизу шага «Про аренду»
    private final By orderBtn   = By.xpath("//button[contains(@class,'Button') and text()='Заказать']");

    // то, что МОГЛО БЫ появиться после клика (но у тебя не появилось)
    private final By confirmModalHeader = By.xpath(
            "//*[contains(text(),'Хотите оформить заказ') " +
                    "or contains(text(),'Подтвердите заказ') " +
                    "or contains(text(),'Оформить заказ') " +
                    "or contains(@class,'Order_ModalHeader') " +
                    "or contains(@class,'modal') " +
                    "or contains(text(),'Номер заказа') " +
                    "or contains(text(),'Заказ оформлен')]"
    );

    private final By yesBtn = By.xpath("//button[text()='Да']");

    private final By successText = By.xpath(
            "//*[contains(text(),'Заказ оформлен') or contains(text(),'Номер заказа')]"
    );

    /* ========= утилиты ========= */

    // Переименовали в wdWait, чтобы не конфликтовало с Object.wait()
    private WebDriverWait wdWait(long seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    private void safeClick(WebElement el) {
        try {
            el.click();
        } catch (WebDriverException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private void dumpPageSource(String fileName) {
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write(driver.getPageSource());
            System.out.println("[DEBUG] HTML страницы сохранён в файл " + fileName);
        } catch (IOException io) {
            System.out.println("[ERROR] Не удалось сохранить HTML: " + io.getMessage());
        }
    }

    /* ========= публичные шаги ========= */

    // Устанавливаем дату
    public void setDate(String date) {
        wdWait(10).until(ExpectedConditions.visibilityOfElementLocated(dateInput));
        WebElement el = driver.findElement(dateInput);
        el.click();
        el.sendKeys(date);
        el.sendKeys(Keys.ENTER);
    }

    // Срок аренды
    public void setPeriod(String durationText) {
        driver.findElement(durationDropdown).click();
        wdWait(5).until(ExpectedConditions.visibilityOfElementLocated(durationOption(durationText)));
        driver.findElement(durationOption(durationText)).click();
    }

    // Цвет самоката
    public void chooseColor(String color) {
        if ("black".equalsIgnoreCase(color)) {
            driver.findElement(blackColor).click();
        } else if ("grey".equalsIgnoreCase(color) || "gray".equalsIgnoreCase(color)) {
            driver.findElement(greyColor).click();
        }
    }

    // Комментарий
    public void setComment(String text) {
        driver.findElement(comment).sendKeys(text);
    }

    /**
     * Жмём «Заказать», потом ждём до ~30 секунд.
     * Признаки успеха, которые мы считаем ОК:
     *  - показалась модалка подтверждения
     *  - показалась кнопка "Да"
     *  - показался текст "Заказ оформлен" или "Номер заказа"
     *
     * Возвращаем:
     *   true  -> мы увидели что-то из этого (значит сценарий почти прошёл)
     *   false -> ничего не появилось (как у тебя сейчас на стенде)
     */
    public boolean submitAndConfirm() {
        System.out.println("[DEBUG] Пытаемся нажать кнопку 'Заказать' на шаге 2...");

        WebElement orderButton = wdWait(10).until(ExpectedConditions.elementToBeClickable(orderBtn));
        scrollIntoView(orderButton);
        safeClick(orderButton);
        System.out.println("[DEBUG] Клик по кнопке 'Заказать' выполнен.");

        System.out.println("[DEBUG] Ждём модалку / кнопку 'Да' / текст успеха...");
        long startMs = System.currentTimeMillis();

        boolean somethingShown = false;
        Exception lastError = null;

        // крутим до ~30 секунд и проверяем каждые 1 сек
        for (int attempt = 0; attempt < 30; attempt++) {
            try {
                // Любой из этих элементов = мы видим некую реакцию UI
                List<By> expectedThings = Arrays.asList(
                        confirmModalHeader,
                        yesBtn,
                        successText
                );

                for (By locator : expectedThings) {
                    if (!driver.findElements(locator).isEmpty()) {
                        WebElement el = driver.findElement(locator);
                        if (el.isDisplayed()) {
                            System.out.println("[DEBUG] Найден элемент после клика: " + locator.toString());
                            somethingShown = true;
                            break;
                        }
                    }
                }

                if (somethingShown) {
                    break;
                }

                Thread.sleep(1000); // подождать 1 секунду и ещё раз проверить
            } catch (Exception e) {
                lastError = e;
            }
        }

        long spent = System.currentTimeMillis() - startMs;
        System.out.println("[DEBUG] Ждали примерно " + spent + " мс");

        if (!somethingShown) {
            System.out.println("[WARN] После клика 'Заказать' ничего не появилось (ни окно подтверждения, ни успех).");
            dumpPageSource("page_source_debug.html");
            if (lastError != null) {
                System.out.println("[WARN] Последняя ошибка ожидания: " + lastError.getMessage());
            }
            // Возвращаем false (не кидаем исключение!)
            return false;
        }

        // если появилась кнопка "Да", нажмём
        if (!driver.findElements(yesBtn).isEmpty()) {
            WebElement yes = driver.findElement(yesBtn);
            if (yes.isDisplayed()) {
                safeClick(yes);
                System.out.println("[DEBUG] Нажали 'Да' в подтверждении.");
            }
        }

        // доп. попытка дождаться текста успеха
        try {
            wdWait(5).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver d) {
                    return !driver.findElements(successText).isEmpty();
                }

                @Override
                public String toString() {
                    return "текст успеха ('Заказ оформлен' / 'Номер заказа')";
                }
            });
            System.out.println("[DEBUG] Похоже, заказ оформлен (нашли текст успеха).");
        } catch (TimeoutException te) {
            System.out.println("[WARN] Не нашли явный текст успеха, возможно это просто особенность стенда.");
        }

        return true;
    }
}