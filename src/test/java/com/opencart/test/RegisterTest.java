package com.opencart.test;

import com.opencart.pages.RegisterPage;
import static com.opencart.utils.Verify.verify;
import static com.opencart.utils.Verify.verifyAll;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import com.opencart.utils.Constants;
import com.opencart.utils.Excel;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;

public class RegisterTest extends BaseTest {

    // Este test tiene prioridad 2 y depende del grupo "openUrlGroup"
    @Test(priority = 2, dependsOnGroups = {"openUrlGroup"})
    public void registerUsersFromExcel() {

        // Lee los datos de la hoja "UsuariosRegistro" del Excel
        // Cada fila se convierte en un Map con claves como "FirstName", "LastName", etc.
        List<Map<String, String>> users = Excel.readSheet("UsuariosRegistro");

        // Itera sobre cada usuario del Excel
        for (Map<String, String> user : users) {

            // Navega a la página de registro
            driver.get(Constants.BASE_URL + Constants.REGISTER_ROUTE);

            // Crea una instancia de la página de registro
            RegisterPage registerPage = new RegisterPage(driver);

            // Rellena el formulario de registro con los datos del usuario
            registerPage.fillForm(
                    user.get("FirstName"),
                    user.get("LastName"),
                    user.get("Email"),
                    user.get("Telephone"),
                    user.get("Password"),
                    user.get("ConfirmPassword")
            );

            // Envía el formulario
            registerPage.submit();

            // Verifica que el título de la página sea "Account"
            verify(() -> assertEquals(registerPage.getPageTitle(), "Account", "El título del h1 no es correcto"));

            // Verifica que el párrafo de éxito tenga el mensaje esperado
            verify(() -> assertEquals(
                    registerPage.getSuccessParagraph(),
                    "Congratulations! Your new account has been successfully created!",
                    "El mensaje de éxito no coincide"
            ));

            // Verifica que el botón "Continue" esté visible
            verify(() -> assertTrue(registerPage.isContinueButtonDisplayed(), "El botón Continue no está visible"));

            // Verifica que la URL contenga "account/success", indicando éxito en el registro
            verify(() -> assertTrue(driver.getCurrentUrl().contains("account/success"),
                    "La URL no termina en account/success"));

            // Ejecuta todas las verificaciones acumuladas
            verifyAll();

            // Toma una captura de pantalla con el nombre del usuario y un timestamp
            takeScreenshot("registro_" + user.get("FirstName") + "_" + System.currentTimeMillis());

            // Cierra sesión para limpiar el estado antes del siguiente registro
            registerPage.logout();

            // Recarga la página de registro para el siguiente usuario
            driver.get(Constants.BASE_URL + "index.php?route=account/register");
        }
    }
}
