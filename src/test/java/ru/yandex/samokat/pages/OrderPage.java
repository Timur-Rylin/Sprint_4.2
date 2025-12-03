package ru.yandex.samokat.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class OrderPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы для первой страницы
    private final By firstNameField = By.xpath(".//input[@placeholder='* Имя']");
    private final By lastNameField = By.xpath(".//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath(".//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroField = By.xpath(".//input[@placeholder='* Станция метро']");
    private final By phoneField = By.xpath(".//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath(".//button[text()='Далее']");

    // Локаторы для второй страницы
    private final By dateField = By.xpath(".//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodField = By.className("Dropdown-placeholder");
    private final By rentalPeriodOptions = By.xpath(".//div[@class='Dropdown-option']");
    private final By blackCheckbox = By.id("black");
    private final By greyCheckbox = By.id("grey");
    private final By commentField = By.xpath(".//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath(".//button[text()='Заказать']");
    private final By confirmOrderButton = By.xpath(".//button[text()='Да']");
    private final By successModal = By.className("Order_ModalHeader");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillFirstPage(String firstName, String lastName, String address, String metro, String phone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(addressField).sendKeys(address);

        // Выбор станции метро
        driver.findElement(metroField).click();
        WebElement metroOption = driver.findElement(By.xpath(".//div[text()='" + metro + "']"));
        metroOption.click();

        driver.findElement(phoneField).sendKeys(phone);
        driver.findElement(nextButton).click();
    }

    public void fillSecondPage(String date, String rentalPeriod, String color, String comment) {
        // Заполнение даты
        WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(dateField));
        dateInput.sendKeys(date);

        // ЗАКРЫВАЕМ КАЛЕНДАРЬ - нажимаем ESC
        dateInput.sendKeys(Keys.ESCAPE);

        // Небольшая пауза для закрытия календаря
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // ВЫБОР СРОКА АРЕНДЫ
        WebElement rentalPeriodElement = driver.findElement(rentalPeriodField);

        // Прокручиваем к элементу
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", rentalPeriodElement
        );

        // Ждем и кликаем
        wait.until(ExpectedConditions.elementToBeClickable(rentalPeriodElement)).click();

        // Выбираем нужный срок
        List<WebElement> periodOptions = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(rentalPeriodOptions));

        for (WebElement option : periodOptions) {
            if (option.getText().equals(rentalPeriod)) {
                option.click();
                break;
            }
        }

        // Выбор цвета
        if ("black".equals(color)) {
            driver.findElement(blackCheckbox).click();
        } else if ("grey".equals(color)) {
            driver.findElement(greyCheckbox).click();
        }

        // Комментарий
        if (comment != null && !comment.isEmpty()) {
            driver.findElement(commentField).sendKeys(comment);
        }

        // Нажатие кнопки "Заказать"
        driver.findElement(orderButton).click();
    }

    public void confirmOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmOrderButton)).click();
    }

    public boolean isOrderSuccess() {
        try {
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(successModal));
            return modal.getText().contains("Заказ оформлен");
        } catch (Exception e) {
            return false;
        }
    }

    // Метод для ожидания модального окна
    public void waitForOrderSuccessModal() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                successModal,
                "Заказ оформлен"
        ));
    }
}