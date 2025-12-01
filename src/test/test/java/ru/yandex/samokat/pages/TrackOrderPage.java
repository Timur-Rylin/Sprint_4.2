package ru.yandex.samokat.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class TrackOrderPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By notFoundImage = By.xpath("//img[@alt='Not found']");

    public TrackOrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isNotFoundDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(notFoundImage));
            return driver.findElement(notFoundImage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}