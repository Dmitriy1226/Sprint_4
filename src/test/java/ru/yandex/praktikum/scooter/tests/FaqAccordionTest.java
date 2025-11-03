package ru.yandex.praktikum.scooter.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.scooter.pages.MainPage;

import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class FaqAccordionTest extends BaseTest {

    @Parameterized.Parameters(name = "FAQ index = {0}")
    public static Object[][] data() {
        return new Object[][]{{0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}};
    }

    @Parameterized.Parameter
    public int index;

    @Test
    public void accordionOpensCorrectText() {
        MainPage main = new MainPage(driver);
        main.open();
        main.expandQuestion(index);
        String text = main.getAnswerText(index);
        assertFalse("Ответ пустой для вопроса " + index, text.trim().isEmpty());
    }
}
