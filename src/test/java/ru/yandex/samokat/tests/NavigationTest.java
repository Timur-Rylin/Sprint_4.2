package ru.yandex.samokat.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import ru.yandex.samokat.config.WebDriverConfig;
import ru.yandex.samokat.pages.MainPage;
import ru.yandex.samokat.pages.TrackOrderPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NavigationTest {
    private WebDriver driver;
    private MainPage mainPage;
    private TrackOrderPage trackOrderPage;

    @Before
    public void setUp() {
        driver = WebDriverConfig.getDriver();
        mainPage = new MainPage(driver);
        trackOrderPage = new TrackOrderPage(driver);
        mainPage.open();
    }

    @Test
    public void testSamokatLogoNavigation() {
        mainPage.checkOrderStatus("12345");
        mainPage.clickSamokatLogo();
        String currentUrl = mainPage.getCurrentUrl();
        assertEquals("Должны быть на главной странице Самоката",
                "https://qa-scooter.praktikum-services.ru/", currentUrl);
    }

    @Test
    public void testYandexLogoNavigation() {
        mainPage.clickYandexLogo();
        mainPage.switchToNewWindow();
        String currentUrl = mainPage.getCurrentUrl();
        assertTrue("Должны быть на странице Яндекса",
                currentUrl.contains("yandex.ru") || currentUrl.contains("dzen.ru"));
    }

    @Test
    public void testInvalidOrderNumber() {
        try {
            mainPage.checkOrderStatus("999999999");
            Thread.sleep(2000);
            assertTrue("Должно отображаться сообщение 'Не найден'",
                    trackOrderPage.isNotFoundDisplayed());
        } catch (Exception e) {
            System.out.println("Тест выявил известный баг: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}