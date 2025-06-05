package com.opencart.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import com.opencart.utils.WaitUtils;

public class SearchPage extends BasePage {
    public SearchPage(WebDriver driver) {
        super(driver);
    }

    private By searchBox() {
        return By.name("search");
    }

    private By searchButton() {
        return By.xpath("//button[@class='btn btn-default btn-lg']");
    }

    private By resultProducts(String name) {
        return By.xpath("//h4/a[text()='" + name + "']");
    }

    public void search(String term) {
        WaitUtils.waitForElementVisible(driver, searchBox(), 10).clear();
        driver.findElement(searchBox()).sendKeys(term);
        WaitUtils.waitForElementClickable(driver, searchButton(), 10).click();
    }

    public boolean productIsVisible(String name){
        return driver.findElements(resultProducts(name)).size() > 0;
    }

    public void openProduct(String name){
        driver.findElement(resultProducts(name)).click();
    }
}
