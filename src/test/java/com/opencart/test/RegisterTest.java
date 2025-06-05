package com.opencart.test;

import com.opencart.pages.RegisterPage;
import com.opencart.utils.Constants;
import com.opencart.utils.Excel;

import static com.opencart.utils.Verify.verify;
import static com.opencart.utils.Verify.verifyAll;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class RegisterTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(RegisterTest.class);

    @Test(priority = 2, dependsOnGroups = {"openUrlGroup"})
    public void registerUsersFromExcel() {
        logger.info("Iniciando prueba: registerUsersFromExcel");

        List<Map<String, String>> users = Excel.readSheet("UsuariosRegistro");
        logger.info("Total de usuarios leídos desde Excel: {}", users.size());

        for (Map<String, String> user : users) {
            String email = user.get("Email");
            String nombre = user.get("FirstName");
            logger.info("Iniciando registro para: {} ({})", nombre, email);

            try {
                driver.get(Constants.BASE_URL + Constants.REGISTER_ROUTE);
                logger.debug("Navegación a página de registro realizada");

                RegisterPage registerPage = new RegisterPage(driver);

                logger.debug("Llenando formulario con datos del usuario");
                registerPage.fillForm(
                        user.get("FirstName"),
                        user.get("LastName"),
                        email,
                        user.get("Telephone"),
                        user.get("Password"),
                        user.get("ConfirmPassword")
                );

                logger.debug("Enviando formulario");
                registerPage.submit();

                logger.debug("Ejecutando verificaciones");
                verify(() -> assertEquals(registerPage.getPageTitle(), "Account", "El título del h1 no es correcto"));
                verify(() -> assertEquals(
                        registerPage.getSuccessParagraph(),
                        "Congratulations! Your new account has been successfully created!",
                        "El mensaje de éxito no coincide"
                ));
                verify(() -> assertTrue(registerPage.isContinueButtonDisplayed(), "El botón Continue no está visible"));
                verify(() -> assertTrue(driver.getCurrentUrl().contains("account/success"),
                        "La URL no termina en account/success"));
                verifyAll();

                takeScreenshot("registro_" + nombre + "_" + System.currentTimeMillis());
                logger.info("Registro exitoso para el usuario: {}", email);

                registerPage.logout();
                logger.debug("Logout realizado para el usuario: {}", email);

                driver.get(Constants.BASE_URL + Constants.REGISTER_ROUTE);
                logger.debug("Página de registro recargada para el siguiente usuario");

            } catch (Exception e) {
                logger.error("Error durante el registro del usuario {}", email);
                takeScreenshot("registro_error_" + nombre + "_" + System.currentTimeMillis());
                throw e;
            }
        }

        logger.info("Prueba registerUsersFromExcel finalizada.");
    }
}
