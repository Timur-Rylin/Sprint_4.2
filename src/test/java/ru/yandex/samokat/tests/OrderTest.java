package ru.yandex.samokat.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import java.time.Duration;
import ru.yandex.samokat.config.WebDriverConfig;
import ru.yandex.samokat.pages.OrderPage;
import ru.yandex.samokat.pages.MainPage;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest {
    private WebDriver driver;
    private OrderPage orderPage;
    private MainPage mainPage;

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metro;
    private final String phone;
    private final String date;
    private final String rentalPeriod;
    private final String color;
    private final String comment;

    private final boolean useTopButton;

    public OrderTest(String firstName, String lastName, String address,
                     String metro, String phone, String date,
                     String rentalPeriod, String color, String comment,
                     boolean useTopButton) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.date = date;
        this.rentalPeriod = rentalPeriod;
        this.color = color;
        this.comment = comment;
        this.useTopButton = useTopButton;
    }

    @Parameterized.Parameters(name = "Заказ: {0} {1} (кнопка: {9})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                  {"Иван", "Иванов", "ул. Ленина, 1", "Сокольники", "+79991234567",
                        "15.12.2024", "сутки", "black", "Позвонить за час", true},
                {"Мария", "Петрова", "пр. Мира, 25", "Черкизовская", "+79997654321",
                        "20.12.2024", "двое суток", "grey", "Оставить у двери", false}
        });
    }

    @Before
    public void setUp() {
        driver = WebDriverConfig.getDriver();
        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);

        mainPage.open();
    }

    @Test
    public void testOrderCreation() {

        if (useTopButton) {
            mainPage.clickOrderButtonTop();
        } else {
            mainPage.clickOrderButtonBottom();
        }

        orderPage.fillFirstPage(firstName, lastName, address, metro, phone);
        orderPage.fillSecondPage(date, rentalPeriod, color, comment);
        orderPage.confirmOrder();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'Order_Modal')]")
        ));

             wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//div[contains(@class, 'Order_ModalHeader')]"),
                "Заказ оформлен"
        ));


        assertTrue("Заказ должен быть успешно оформлен", orderPage.isOrderSuccess());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}