package ru.yandex.praktikum.scooter.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.scooter.pages.MainPage;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FaqAccordionTest extends BaseTest {

    // Параметры для теста — индекс вопроса и ожидаемый ответ
    @Parameterized.Parameters(name = "Вопрос {0}")
    public static Object[][] data() {
        return new Object[][]{
                {0, "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},
                {1, "Пока что у нас один самокат на одного. Но скоро всё изменится."},
                {2, "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. " +
                        "Отсчёт времени аренды начнётся с момента, когда вы оплатите заказ курьеру. " +
                        "Если вы оплатили заказ в 20:30, суточная аренда закончится в 20:30 следующего дня."},
                {3, "Только начиная с завтрашнего дня. Но скоро станем расторопнее."},
                {4, "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку " +
                        "по красивому номеру 1010."},
                {5, "Самокат приезжает к вам с полным зарядом. Этого хватает на восемь суток — даже если " +
                        "будете кататься без передышки и во сне. Зарядку мы с собой не привозим."},
                {6, "Да, пока самокат не привезли. Штрафа не будет, объяснительную записку тоже не попросим :)"},
                {7, "Да, обязательно. Всем самокатов! И Москве, и Московской области."}
        };
    }

    @Parameterized.Parameter(0)
    public int index;

    @Parameterized.Parameter(1)
    public String expectedAnswer;

    @Test
    public void checkFaqAnswerText() {
        MainPage main = new MainPage(driver);
        main.open();

        main.expandQuestion(index);
        String actual = main.getAnswerText(index).trim();

        // Сравниваем ожидаемый и фактический текст
        assertEquals("Некорректный текст ответа для вопроса №" + index,
                expectedAnswer,
                actual);
    }
}