package com.opencart.test;

import com.opencart.pages.LoginPage;
import com.opencart.utils.Constants;
import com.opencart.utils.Excel;
import com.opencart.utils.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.opencart.utils.Verify.verify;
import static org.testng.Assert.assertTrue;

public class LoginTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);

    @Test(priority = 3, dependsOnGroups = {"openUrlGroup"})
    public void loginUsersFromExcel() {

        logger.info("=== Inicio de la prueba de Login con múltiples usuarios desde Excel ===");

        List<Map<String, String>> users = Excel.readSheet("LoginData");

        for (Map<String, String> user : users) {

            String email = user.get("Email");
            String password = user.get("Password");
            String resultadoEsperado = user.get("ResultadoEsperado").toLowerCase();

            logger.info("Probando login con usuario: " + email);

            driver.get(Constants.BASE_URL + Constants.LOGIN_ROUTE);
            LoginPage loginPage = new LoginPage(driver);
            loginPage.fillLoginForm(email, password);

            if (resultadoEsperado.equals("éxito") || resultadoEsperado.equals("exito")) {
                verify(() -> assertTrue(loginPage.loginWasSuccessful(), "El login debería haber sido exitoso"));
                logger.info("✅ Login exitoso para el usuario: " + email);
                takeScreenshot("login_exito_" + email);
                loginPage.logout();
            } else {
                verify(() -> assertTrue(loginPage.loginFailedWithWarning(), "El login debería haber fallado"));
                logger.warn("⚠️ Login fallido (esperado) para el usuario: " + email);
                takeScreenshot("login_fallo_" + email);
            }

            driver.get(Constants.BASE_URL + Constants.LOGIN_ROUTE);
            Verify.verifyAll();
        }

        logger.info("=== Finalización de la prueba de Login ===");
    }
}
