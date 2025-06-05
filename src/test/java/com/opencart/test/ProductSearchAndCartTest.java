package com.opencart.test;

import com.opencart.pages.CartPage;
import com.opencart.pages.ProductPage;
import com.opencart.pages.SearchPage;
import com.opencart.utils.Constants;
import com.opencart.utils.Excel;
import com.opencart.utils.Verify;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.opencart.utils.Verify.verify;
import static org.testng.Assert.assertTrue;

public class ProductSearchAndCartTest extends BaseTest {

    // Este test tiene prioridad 4 y depende de que se haya abierto la URL base primero
    @Test(priority = 4, dependsOnGroups = {"openUrlGroup"})
    public void searchAddAndVerifyProductsInCart() {

        // Lee los productos a buscar desde la hoja "ProductosBusqueda" del Excel
        List<Map<String, String>> products = Excel.readSheet("ProductosBusqueda");

        // Lista para guardar los productos que fueron agregados al carrito con éxito
        List<String> addedProducts = new ArrayList<>();

        // PASOS 1-3: Buscar y agregar cada producto al carrito
        for (Map<String, String> row : products) {
            String product = row.get("Producto");

            // Abre la página principal
            driver.get(Constants.BASE_URL);

            // Instancia de la página de búsqueda
            SearchPage searchPage = new SearchPage(driver);

            // Realiza la búsqueda del producto
            searchPage.search(product);

            // Verifica que el producto esté visible en los resultados
            verify(() -> assertTrue(
                    searchPage.productIsVisible(product),
                    "El producto '" + product + "' no se encontró en los resultados."
            ));

            // Abre el detalle del producto
            searchPage.openProduct(product);

            // Instancia de la página del producto
            ProductPage productPage = new ProductPage(driver);

            // Establece la cantidad y lo agrega al carrito
            productPage.setQuantity(1);
            productPage.addToCart();

            // Agrega el nombre del producto a la lista de éxitos
            addedProducts.add(product);
        }

        // PASO 4: Verificar los productos en el carrito
        driver.get(Constants.BASE_URL + Constants.CART_ROUTE); // Abre la página del carrito
        CartPage cartPage = new CartPage(driver);

        // Obtiene todos los productos visibles en el carrito
        List<String> productsInCart = cartPage.getAllProductsInCart();

        // Lista para guardar los productos verificados con éxito
        List<String> successList = new ArrayList<>();

        for (String product : addedProducts) {
            boolean exists = productsInCart.contains(product);
            verify(() -> assertTrue(exists, "El producto '" + product + "' no se encontró en el carrito."));
            if (exists) {
                successList.add(product);
            }
        }

        // PASO 5: Escribir los productos exitosamente agregados en un Excel de salida
        Excel.writeProductsToExcel(Constants.OUTPUT_EXCEL, successList);

        // Captura de pantalla final del carrito
        takeScreenshot("final_carrito");

        // Ejecuta todas las verificaciones acumuladas
        Verify.verifyAll();
    }
}

