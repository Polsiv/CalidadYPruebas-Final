package com.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.opencart.utils.WaitUtils;

public class LoginPage extends BasePage {
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    private By input(String id){
        return By.xpath("//*[@id='" + id + "']");
    }

    private By loginButton() {
        return By.xpath("//input[@value='Login']");
    }

    private By warningMessage() {
        return By.xpath("//div[contains(@class,'alert-danger')]");
    }

    private By accountTitle(){
        return By.xpath("//div[@id='content']/h2[contains(text(),'My Account')]");
    }

    private By myAccountMenu() {
        return By.xpath("//span[text()='My Account']");
    }

    private By logoutOption() {
        return By.xpath("//a[text()='Logout']");
    }

    public void logout() {
        try {
            driver.findElement(myAccountMenu()).click();
            driver.findElement(logoutOption()).click();
        } catch (Exception e) {
            System.out.println("No fue necesario hacer logout.");
        }
    }

    public void fillLoginForm(String email, String password) {
        WaitUtils.waitForElementVisible(driver, input("input-email"), 10).sendKeys(email);
        WaitUtils.waitForElementVisible(driver, input("input-password"), 10).sendKeys(password);
        WaitUtils.waitForElementClickable(driver, loginButton(), 10).click();
    }

    public boolean loginWasSuccessful() {
        return driver.findElements(accountTitle()).size() > 0; //Mayor a 0 significa que se encontró el elemento
    }

    public boolean loginFailedWithWarning() {
        return driver.findElements(warningMessage()).size() > 0;
    }

}



