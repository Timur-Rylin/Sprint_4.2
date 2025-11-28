package ru.yandex.samokat.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By[] questionLocators = {
            By.id("accordion__heading-0"),
            By.id("accordion__heading-1"),
            By.id("accordion__heading-2"),
            By.id("accordion__heading-3"),
            By.id("accordion__heading-4"),
            By.id("accordion__heading-5"),
            By.id("accordion__heading-6"),
            By.id("accordion__heading-7")
    };

    private final By[] answerLocators = {
            By.id("accordion__panel-0"),
            By.id("accordion__panel-1"),
            By.id("accordion__panel-2"),
            By.id("accordion__panel-3"),
            By.id("accordion__panel-4"),
            By.id("accordion__panel-5"),
            By.id("accordion__panel-6"),
            By.id("accordion__panel-7")
    };

    private final By orderButtonTop = By.className("Button_Button__ra12g");
    private final By orderButtonBottom = By.xpath("//button[@class='Button_Buttonra12g Button_Middle1CSJM']");
    private final By allOrderButtons = By.xpath("//button[text()='Заказать']");
    private final By samokatLogo = By.className("Header_LogoScooter__3lsAR");
    private final By yandexLogo = By.className("Header_LogoYandex__3TSOI");
    private final By orderStatusButton = By.className("Header_Link__1TAG7");
    private final By orderNumberInput = By.xpath("//input[@placeholder='Введите номер заказа']");
    private final By goButton = By.xpath("//button[text()='Go!']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void open() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
    }

    public void clickQuestion(int index) {
        WebElement question = wait.until(ExpectedConditions.elementToBeClickable(questionLocators[index]));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", question);
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        question.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocators[index]));
    }

    public String getAnswerText(int index) {
        WebElement answer = wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocators[index]));
        return answer.getText();
    }

    public void clickOrderButtonTop() {
        List<WebElement> buttons = driver.findElements(allOrderButtons);
        if (!buttons.isEmpty()) buttons.get(0).click();
    }

    public void clickOrderButtonBottom() {
        List<WebElement> buttons = driver.findElements(allOrderButtons);
        if (buttons.size() > 1) {
            WebElement button = buttons.get(1);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", button);
            wait.until(ExpectedConditions.elementToBeClickable(button)).click();
        } else if (!buttons.isEmpty()) {
            WebElement button = buttons.get(0);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", button);
            wait.until(ExpectedConditions.elementToBeClickable(button)).click();
        }
    }

    public void clickSamokatLogo() {
        driver.findElement(samokatLogo).click();
    }

    public void clickYandexLogo() {
        driver.findElement(yandexLogo).click();}

    public void checkOrderStatus(String orderNumber) {
        driver.findElement(orderStatusButton).click();
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(orderNumberInput));
        input.sendKeys(orderNumber);
        driver.findElement(goButton).click();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void switchToNewWindow() {
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        String originalWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.equals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }
}