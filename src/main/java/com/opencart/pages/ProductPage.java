package com.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage extends BasePage {

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    private By quantityInput() {
        return By.xpath("//input[@id='input-quantity']");
    }

    private By addToCartButton() {
        return By.xpath("//button[@id='button-cart']");
    }

    public void setQuantity(int qty) {
        driver.findElement(quantityInput()).clear();
        driver.findElement(quantityInput()).sendKeys(String.valueOf(qty));
    }

    public void addToCart() {
        driver.findElement(addToCartButton()).click();
    }
}
