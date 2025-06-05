package com.opencart.test;

import com.opencart.pages.BasePage;
import com.opencart.utils.Constants;
import org.testng.annotations.Test;

public class OpenURLTest extends BaseTest{

    // Método de prueba con prioridad 1, pertenece al grupo "openUrlGroup"
    // Esto significa que será ejecutado primero y otros tests pueden depender de él
    @Test(priority = 1, groups = {"openUrlGroup"})
    public void OpenUrl() {

        // Crea una instancia de la clase BasePage, que generalmente contiene acciones comunes como navegación
        BasePage basePage = new BasePage(driver);

        // Usa el método navigateTo() para abrir la URL base definida en los constantes
        basePage.navigateTo(Constants.BASE_URL);
    }
}
