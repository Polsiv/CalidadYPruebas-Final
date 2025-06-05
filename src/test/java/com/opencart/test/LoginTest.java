package com.opencart.test;

import com.opencart.pages.LoginPage;
import com.opencart.utils.Constants;
import com.opencart.utils.Excel;
import com.opencart.utils.Verify;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;
import static com.opencart.utils.Verify.verify;
import static org.testng.Assert.assertTrue;

public class LoginTest extends BaseTest {

    // Depende del grupo "openUrlGroup"
    @Test(priority = 3, dependsOnGroups = {"openUrlGroup"})
    public void loginUsersFromExcel() {

        // Lee la hoja "LoginData" del archivo Excel como una lista de mapas
        // Cada mapa representa un usuario con campos como "Email", "Password", "ResultadoEsperado"
        List<Map<String, String>> users = Excel.readSheet("LoginData");

        // Itera sobre cada usuario leído del Excel
        for (Map<String, String> user : users) {

            // Navega a la página de login
            driver.get(Constants.BASE_URL + Constants.LOGIN_ROUTE);

            // Crea una instancia de la página de login
            LoginPage loginPage = new LoginPage(driver);

            // Llena y envía el formulario de login con los datos del usuario
            loginPage.fillLoginForm(user.get("Email"), user.get("Password"));

            // Obtiene el resultado esperado del Excel
            String resultadoEsperado = user.get("ResultadoEsperado").toLowerCase();

            // Si se espera que el login sea exitoso
            if (resultadoEsperado.equals("éxito") || resultadoEsperado.equals("exito")) {
                // Verifica que el login haya sido exitoso
                verify(() -> assertTrue(loginPage.loginWasSuccessful(), "El login debería haber sido exitoso"));

                // Toma una captura de pantalla indicando éxito
                takeScreenshot("login_exito_" + user.get("Email"));

                // Cierra sesión para preparar el siguiente intento
                loginPage.logout();
            } else {
                // Verifica que el login haya fallado mostrando un mensaje de advertencia
                verify(() -> assertTrue(loginPage.loginFailedWithWarning(), "El login debería haber fallado"));

                // Toma una captura de pantalla indicando fallo
                takeScreenshot("login_fallo_" + user.get("Email"));
            }

            // Vuelve a cargar la página de login para el siguiente usuario
            driver.get(Constants.BASE_URL + Constants.LOGIN_ROUTE);

            // Ejecuta todas las verificaciones acumuladas hasta el momento
            Verify.verifyAll();
        }
    }
}
