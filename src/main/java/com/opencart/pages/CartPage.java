package com.opencart.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getAllProductsInCart() {
        List<String> products = new ArrayList<>();

        // Tomar todos los <a> dentro del carrito que tengan un href con route=product/product
        var links = driver.findElements(By.xpath("//div[@id='content']//a[contains(@href, 'route=product/product')]"));

        for (var link : links) {
            String name = link.getText().trim();
            if (!name.isEmpty()) {
                products.add(name);
            }
        }

        return products;
    }
}
