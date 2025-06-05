package com.opencart.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class WaitUtils {

    //Espera explicita para que un elemento sea visible
    public static WebElement waitForElementVisible(WebDriver driver, By locator, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    //Espera explicita para que un elemento sea clickeable
    public static WebElement waitForElementClickable(WebDriver driver, By locator, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    //Fluent Wait para esperar un elemento con polling
    public static WebElement fluentWait(WebDriver driver, By locator, int timeoutInSeconds, int pollingMillis) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingMillis))
                .ignoring(Exception.class)
                .until(d -> d.findElement(locator));
    }
}
