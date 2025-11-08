package ru.yandex.praktikum.scooter.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Assert;
import ru.yandex.praktikum.scooter.pages.MainPage;
import ru.yandex.praktikum.scooter.pages.OrderPageOne;
import ru.yandex.praktikum.scooter.pages.OrderPageTwo;

@RunWith(Parameterized.class)
public class OrderScooterTest extends BaseTest {

    @Parameterized.Parameters(name = "{index}: entry={0}, user={1} {2}, metro={4}")
    public static Object[][] data() {
        return new Object[][]{
                // entry, firstName, lastName, address, metro, phone, date, period, color, comment
                {"top",    "Иван",  "Иванов", "Москва, Тверская 1",  "Арбатская",
                        "+79990000001", "20.12.2025", "двое суток", "black", "позвонить"},
                {"bottom", "Пётр",  "Петров", "Москва, Красная 3",   "Черкизовская",
                        "+79990000002", "21.12.2025", "сутки",      "grey",  "код 1234"},
        };
    }

    @Parameterized.Parameter(0)
    public String entry;          // "top" | "bottom"
    @Parameterized.Parameter(1)
    public String firstName;
    @Parameterized.Parameter(2)
    public String lastName;
    @Parameterized.Parameter(3)
    public String address;
    @Parameterized.Parameter(4)
    public String metroStation;
    @Parameterized.Parameter(5)
    public String phoneNumber;
    @Parameterized.Parameter(6)
    public String date;
    @Parameterized.Parameter(7)
    public String rentalPeriod;
    @Parameterized.Parameter(8)
    public String scooterColor;
    @Parameterized.Parameter(9)
    public String courierComment;

    @Test
    public void makeOrder_PositiveFlow() {

        // 1. Открываем главную
        MainPage mainPage = new MainPage(driver);
        mainPage.open();

        // 2. Жмём "Заказать" (верхняя или нижняя кнопка)
        if ("top".equalsIgnoreCase(entry)) {
            mainPage.clickOrderTop();
        } else {
            mainPage.clickOrderBottom();
        }

        // 3. Шаг "Для кого самокат"
        OrderPageOne customerInfoPage = new OrderPageOne(driver);
        customerInfoPage.fillCustomer(firstName, lastName, address, metroStation, phoneNumber);
        customerInfoPage.clickNext();

        // 4. Шаг "Про аренду"
        OrderPageTwo rentDetailsPage = new OrderPageTwo(driver);
        rentDetailsPage.setDate(date);
        rentDetailsPage.setPeriod(rentalPeriod);
        rentDetailsPage.chooseColor(scooterColor);
        rentDetailsPage.setComment(courierComment);

        // 5. Оформляем заказ: нажать "Заказать" и подтвердить
        boolean orderCreated = rentDetailsPage.submitAndConfirm();

        // Тест должен падать, если окно подтверждения не появилось
        Assert.assertTrue("Не появилось окно подтверждения заказа.", orderCreated);
    }
}