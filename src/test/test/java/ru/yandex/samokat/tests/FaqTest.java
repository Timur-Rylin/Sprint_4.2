package ru.yandex.samokat.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import ru.yandex.samokat.config.WebDriverConfig;
import ru.yandex.samokat.pages.MainPage;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class FaqTest {
    private WebDriver driver;
    private MainPage mainPage;

    private final int questionIndex;
    private final String expectedAnswerContains;

    public FaqTest(int questionIndex, String expectedAnswerContains) {
        this.questionIndex = questionIndex;
        this.expectedAnswerContains = expectedAnswerContains;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {0, "Сутки — 400 рублей"},
                {1, "покататься"},
                {2, "Допустим, вы оформляете заказ на 8 мая"},
                {3, "Только начиная с завтрашнего дня"},
                {4, "Пока что нет"},
                {5, "Самокат приезжает к вам с полной зарядкой"},
                {6, "Да, пока самокат не привезли"},
                {7, "Москв"}
        });
    }

    @Before
    public void setUp() {
        driver = WebDriverConfig.getDriver();
        mainPage = new MainPage(driver);
        mainPage.open();
    }

    @Test
    public void testFaqQuestionAnswer() {
        mainPage.clickQuestion(questionIndex);
        String answerText = mainPage.getAnswerText(questionIndex);
        assertNotNull("Ответ не должен быть null", answerText);
        assertTrue("Ответ должен содержать: " + expectedAnswerContains + ", но получили: " + answerText,
                answerText.contains(expectedAnswerContains));
    }

    @After
    public void tearDown() {
        WebDriverConfig.quitDriver();
    }
}
