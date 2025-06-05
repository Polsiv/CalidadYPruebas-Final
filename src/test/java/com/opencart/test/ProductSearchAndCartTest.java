package com.opencart.test;

import com.opencart.pages.CartPage;
import com.opencart.pages.ProductPage;
import com.opencart.pages.SearchPage;
import com.opencart.utils.Constants;
import com.opencart.utils.Excel;
import com.opencart.utils.Verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.opencart.utils.Verify.verify;
import static org.testng.Assert.assertTrue;

public class ProductSearchAndCartTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductSearchAndCartTest.class);

    @Test(priority = 4, dependsOnGroups = {"openUrlGroup"})
    public void searchAddAndVerifyProductsInCart() {
        logger.info("Iniciando prueba: searchAddAndVerifyProductsInCart");

        List<Map<String, String>> products = Excel.readSheet("ProductosBusqueda");
        logger.info("Total de productos leídos desde Excel: {}", products.size());

        List<String> addedProducts = new ArrayList<>();

        for (Map<String, String> row : products) {
            String product = row.get("Producto");
            logger.info("Buscando y agregando producto: {}", product);

            try {
                driver.get(Constants.BASE_URL);
                logger.debug("Página principal cargada");

                SearchPage searchPage = new SearchPage(driver);
                searchPage.search(product);
                logger.debug("Búsqueda ejecutada para: {}", product);

                verify(() -> assertTrue(
                        searchPage.productIsVisible(product),
                        "El producto '" + product + "' no se encontró en los resultados."
                ));
                logger.info("Producto '{}' encontrado en los resultados", product);

                searchPage.openProduct(product);
                ProductPage productPage = new ProductPage(driver);

                productPage.setQuantity(1);
                productPage.addToCart();
                logger.info("Producto '{}' agregado al carrito", product);

                addedProducts.add(product);

            } catch (Exception e) {
                logger.error("Error al procesar el producto '{}': {}", product, e.getMessage(), e);
                takeScreenshot("error_producto_" + product + "_" + System.currentTimeMillis());
                throw e;
            }
        }

        try {
            driver.get(Constants.BASE_URL + Constants.CART_ROUTE);
            logger.debug("Navegación al carrito completada");

            CartPage cartPage = new CartPage(driver);
            List<String> productsInCart = cartPage.getAllProductsInCart();

            List<String> successList = new ArrayList<>();
            for (String product : addedProducts) {
                boolean exists = productsInCart.contains(product);
                verify(() -> assertTrue(exists, "El producto '" + product + "' no se encontró en el carrito."));
                if (exists) {
                    successList.add(product);
                    logger.info("Producto '{}' verificado en el carrito", product);
                } else {
                    logger.warn("Producto '{}' no se encontró en el carrito", product);
                }
            }

            Excel.writeProductsToExcel(Constants.OUTPUT_EXCEL, successList);
            logger.info("Productos exitosamente verificados escritos en el Excel de salida");

            takeScreenshot("final_carrito");
            logger.info("Captura de pantalla del carrito tomada");

        } catch (Exception e) {
            logger.error("Error al verificar productos en el carrito: {}", e.getMessage(), e);
            throw e;
        }

        Verify.verifyAll();
        logger.info("Prueba searchAddAndVerifyProductsInCart finalizada");
    }
}
