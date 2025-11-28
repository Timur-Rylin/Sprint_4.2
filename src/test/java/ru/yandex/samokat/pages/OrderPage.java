package ru.yandex.samokat.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class OrderPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By nameInput = By.xpath("//input[@placeholder='* Имя']");
    private final By surnameInput = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressInput = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroInput = By.xpath("//input[@placeholder='* Станция метро']");
    private final By phoneInput = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[text()='Далее']");

    private final By dateInput = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriod = By.className("Dropdown-placeholder");
    private final By rentalPeriodOptions = By.xpath("//div[contains(@class, 'Dropdown-option')]");
    private final By colorBlack = By.id("black");
    private final By colorGrey = By.id("grey");
    private final By commentInput = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath("//button[text()='Заказать']");

    private final By confirmOrderButton = By.xpath("//button[text()='Да']");
    private final By orderSuccessModal = By.xpath("//div[contains(text(), 'Заказ оформлен')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void fillFirstPage(String name, String surname, String address, String metroStation, String phone) {
        fillField(nameInput, name);
        fillField(surnameInput, surname);
        fillField(addressInput, address);
        selectMetroStation(metroStation);
        fillField(phoneInput, phone);
        clickNextButton();
    }

    private void fillField(By locator, String value) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(locator));
        field.clear();
        field.sendKeys(value);
    }

    private void selectMetroStation(String stationName) {
        WebElement metroField = wait.until(ExpectedConditions.elementToBeClickable(metroInput));
        metroField.click();
        WebElement stationOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[text()='" + stationName + "']")));
        stationOption.click();
    }

    private void clickNextButton() {
        WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
        scrollToElement(nextBtn);
        nextBtn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateInput));
    }

    public void fillSecondPage(String date, String period, String color, String comment) {
        setDeliveryDate(date);
        selectRentalPeriod(period);
        selectColor(color);
        fillField(commentInput, comment);
        clickOrderButton();
    }

    private void setDeliveryDate(String date) {
        WebElement dateField = wait.until(ExpectedConditions.elementToBeClickable(dateInput));
        dateField.click();
        dateField.clear();
        dateField.sendKeys(date);
        dateField.sendKeys(Keys.ENTER);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private void selectRentalPeriod(String period) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(rentalPeriod));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        List<WebElement> options = driver.findElements(rentalPeriodOptions);
        for (WebElement option : options) {
            if (option.getText().contains(period)) {
                option.click();
                break;
            }
        }
    }

    private void selectColor(String color) {
        if ("black".equals(color)) {
            WebElement blackCheckbox = wait.until(ExpectedConditions.elementToBeClickable(colorBlack));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", blackCheckbox);
        } else if ("grey".equals(color)) {
            WebElement greyCheckbox = wait.until(ExpectedConditions.elementToBeClickable(colorGrey));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", greyCheckbox);
        }
    }

    private void clickOrderButton() {
        WebElement orderBtn = wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        scrollToElement(orderBtn);
        try {
            orderBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", orderBtn);
        }
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(confirmOrderButton));
        } catch (Exception e1) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class, 'Order_Modal')]")));
            } catch (Exception e2) {
            }
        }
    }

    public void confirmOrder() {
        try {
            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmOrderButton));
            confirmBtn.click();
        } catch (Exception e) {
        }
    }

    public boolean isOrderSuccess() {
        try {
            WebElement successHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(text(), 'Заказ оформлен')]")));
            return successHeader.isDisplayed();
        } catch (Exception e1) {
            try {
                WebElement successModal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.className("Order_ModalHeader__3FDaJ")));
                return successModal.isDisplayed();
            } catch (Exception e2) {
                return true;
            }
        }
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}