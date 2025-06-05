package com.opencart.pages;

import com.opencart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class RegisterPage extends BasePage {

    private WebDriverWait wait;

    public RegisterPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Espera de hasta 10s
    }

    private By input(String fieldId) {
        return By.xpath("//*[@id='" + fieldId + "']");
    }

    private By checkbox(String name) {
        return By.xpath("//*[@name='" + name + "']");
    }

    private By submitButton() {
        return By.xpath("//input[@type='submit' and @value='Continue']");
    }

    private By pageTitle() {
        return By.xpath("//div[@id='content']/h1");
    }

    private By successParagraph() {
        return By.xpath("//div[@id='content']/p");
    }

    private By continueButton() {
        return By.xpath("//a[contains(@class,'btn-primary') and contains(text(),'Continue')]");
    }

    private By myAccountMenu() {
        return By.xpath("//span[text()='My Account']");
    }

    private By logoutOption() {
        return By.xpath("//a[text()='Logout']");
    }

    public void logout() {
        driver.findElement(myAccountMenu()).click();
        driver.findElement(logoutOption()).click();
    }

    public void fillForm(String fn, String ln, String email, String tel, String pwd, String confirmPwd) {
        WaitUtils.fluentWait(driver, input("input-firstname"), 15, 1000).sendKeys(fn);
        WaitUtils.fluentWait(driver, input("input-lastname"), 15, 1000).sendKeys(ln);
        WaitUtils.fluentWait(driver, input("input-email"), 15, 300).sendKeys(email);
        WaitUtils.fluentWait(driver, input("input-telephone"), 15, 300).sendKeys(tel);
        WaitUtils.fluentWait(driver, input("input-password"), 15, 300).sendKeys(pwd);
        WaitUtils.fluentWait(driver, input("input-confirm"), 15, 300).sendKeys(confirmPwd);
        WaitUtils.fluentWait(driver, checkbox("agree"), 15, 300).click();
    }

    public void submit() {
        driver.findElement(submitButton()).click();
    }

    public String getPageTitle() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle()));
        return driver.findElement(pageTitle()).getText();
    }

    public String getSuccessParagraph() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(successParagraph()));
        return driver.findElement(successParagraph()).getText();
    }

    public boolean isContinueButtonDisplayed() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(continueButton()));
        return driver.findElement(continueButton()).isDisplayed();
    }

}
