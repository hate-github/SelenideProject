package Selenide;

import Selenide.pages.MainPage;
import Selenide.pages.CatalogPage;
import Selenide.utils.ConfigLoader;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OTCTest extends BaseTest {
    @Test
    public void searchAndSaveProducts() throws IOException {
        String searchQuery = ConfigLoader.get("search.query");
        String searchCity = ConfigLoader.get("search.city");

        MainPage mainPage = new MainPage().openPage();
        CatalogPage catalogPage = mainPage.goToCatalogViaBurger();
        catalogPage.selectCity(searchCity)
                .enterSearchQuery(searchQuery)
                .clickSearch();

        assertTrue(catalogPage.isResultsDisplayed(), "Результаты не отображаются");

        List<Map<String, String>> allProducts = new ArrayList<>();
        allProducts.addAll(catalogPage.getProductsFromCurrentPage());

        if (catalogPage.hasNextPage()) {
            catalogPage.goToNextPage();
            assertTrue(catalogPage.isResultsDisplayed(), "Вторая страница не загрузилась");
            allProducts.addAll(catalogPage.getProductsFromCurrentPage());
        }

        String outputFile = "products_output.txt";
        try (FileWriter writer = new FileWriter(outputFile)) {
            for (Map<String, String> product : allProducts) {
                writer.write(product.get("name") + ", " + product.get("price") + "\n");
            }
        }

        assertTrue(Files.exists(Paths.get(outputFile)), "Файл не создан");
        List<String> lines = Files.readAllLines(Paths.get(outputFile));
        assertFalse(lines.isEmpty(), "Файл пуст");
        assertEquals(allProducts.size(), lines.size(), "Количество строк не совпадает");
    }
}