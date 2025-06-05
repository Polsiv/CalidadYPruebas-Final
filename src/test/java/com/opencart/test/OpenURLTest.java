package com.opencart.test;

import com.opencart.pages.BasePage;
import com.opencart.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class OpenURLTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(OpenURLTest.class);

    // Método de prueba con prioridad 1, pertenece al grupo "openUrlGroup"
    @Test(priority = 1, groups = {"openUrlGroup"})
    public void OpenUrl() {
        logger.info("Iniciando prueba: OpenUrl");

        try {
            BasePage basePage = new BasePage(driver);
            logger.info("Navegando a la URL base: {}", Constants.BASE_URL);

            basePage.navigateTo(Constants.BASE_URL);

            logger.info("Navegación exitosa a la página principal.");
            takeScreenshot("open_url_success");
        } catch (Exception e) {
            logger.error("Error al intentar abrir la URL: {}", e.getMessage(), e);
            takeScreenshot("open_url_error");
            throw e;
        }

        logger.info("Prueba OpenUrl finalizada.");
    }
}
