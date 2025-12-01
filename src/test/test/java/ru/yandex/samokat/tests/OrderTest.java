package ru.yandex.samokat.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import ru.yandex.samokat.config.WebDriverConfig;
import ru.yandex.samokat.pages.MainPage;
import ru.yandex.samokat.pages.OrderPage;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest {
    private WebDriver driver;
    private MainPage mainPage;
    private OrderPage orderPage;

    private final String orderButtonType;
    private final String name;
    private final String surname;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String date;
    private final String rentalPeriod;
    private final String color;
    private final String comment;

    public OrderTest(String orderButtonType, String name, String surname, String address,
                     String metroStation, String phone, String date, String rentalPeriod,
                     String color, String comment) {
        this.orderButtonType = orderButtonType;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.date = date;
        this.rentalPeriod = rentalPeriod;
        this.color = color;
        this.comment = comment;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"top", "Иван", "Петров", "ул. Ленина, д. 1", "Сокольники", "89123456789", "15.12.2024", "трое суток", "black", "Тестовый комментарий"},
                {"bottom", "Мария", "Сидорова", "пр. Мира, д. 25", "Черкизовская", "89987654321", "20.12.2024", "сутки", "grey", "Оставить у двери"}
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
    public void testOrderScooter() {
        if ("top".equals(orderButtonType)) {
            mainPage.clickOrderButtonTop();
        } else {
            mainPage.clickOrderButtonBottom();
        }

        orderPage.fillFirstPage(name, surname, address, metroStation, phone);
        orderPage.fillSecondPage(date, rentalPeriod, color, comment);
        orderPage.confirmOrder();

        assertTrue("Заказ должен быть успешно оформлен", orderPage.isOrderSuccess());
    }

    @After
    public void tearDown() {
        WebDriverConfig.quitDriver();
    }
}
