package Selenide.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CatalogPage {
    private final SelenideElement citySelector = $(By.cssSelector(".SeoRegionSelector-module__yZsavW__selector"));
    private final SelenideElement searchInput = $(By.cssSelector(".SearchInput-module__xN-moW__selectInput"));
    private final SelenideElement searchButton = $(By.xpath("//button[.//span[text()='Найти']]"));
    private final SelenideElement nextPageButton = $(By.cssSelector(".PaginationBlock-module__U4yK7G__control:not([data-disabled]) svg[style*='rotate(-90deg)']")).parent();

    private static final String PRODUCT_CARD_CSS = ".ProductCard-module__AiMWEW__wrapper";
    private static final String PRODUCT_NAME_CSS = ".ProductCard-module__AiMWEW__link";
    private static final String PRODUCT_PRICE_CSS = "h3[itemprop='price']";

    public String getCurrentCity() {
        return citySelector.shouldBe(Condition.visible).getText().trim();
    }

    public CatalogPage selectCity(String cityName) {
        String currentCity = getCurrentCity();
        if (currentCity.equalsIgnoreCase(cityName) || currentCity.equalsIgnoreCase("г. " + cityName)) {
            return this;
        }
        citySelector.click();
        SelenideElement modal = $(By.cssSelector(".mantine-Modal-root .mantine-Modal-body"));
        modal.shouldBe(Condition.visible, Duration.ofSeconds(5));
        SelenideElement cityInput = modal.$(By.cssSelector("input[placeholder='Найти город']"));
        cityInput.shouldBe(Condition.visible, Duration.ofSeconds(5)).setValue(cityName);
        cityInput.pressEnter();
        String labelText = "г. " + cityName;
        String xpathCheckbox = String.format(
                ".//label[contains(.,'%s')]/ancestor::div[contains(@class,'Checkbox-module__fmEfMG__body')]//input[@type='checkbox']",
                labelText
        );
        SelenideElement checkboxCity = modal.$(By.xpath(xpathCheckbox));
        checkboxCity.shouldBe(Condition.visible, Duration.ofSeconds(5));
        if (!checkboxCity.isSelected()) {
            checkboxCity.click();
        }

        String xpathMoscow = ".//label[contains(.,'г. Москва')]/ancestor::div[contains(@class,'Checkbox-module__fmEfMG__body')]//input[@type='checkbox']";
        SelenideElement checkboxMoscow = modal.$(By.xpath(xpathMoscow));
        if (checkboxMoscow.exists() && checkboxMoscow.isSelected()) {
            checkboxMoscow.click();
        }

        SelenideElement applyButton = modal.$(By.xpath(".//button[.//span[text()='Применить']]"));
        applyButton.shouldBe(Condition.visible).click();
        modal.shouldNotBe(Condition.visible, Duration.ofSeconds(5));
        return this;
    }

    public CatalogPage enterSearchQuery(String query) {
        searchInput.shouldBe(Condition.visible).setValue(query);
        return this;
    }

    public CatalogPage clickSearch() {
        searchButton.shouldBe(Condition.visible).click();
        $$(PRODUCT_CARD_CSS).first().shouldBe(Condition.visible, Duration.ofSeconds(10));
        return this;
    }

    public List<Map<String, String>> getProductsFromCurrentPage() {
        List<Map<String, String>> products = new ArrayList<>();
        ElementsCollection cards = $$(PRODUCT_CARD_CSS);
        cards.first().shouldBe(Condition.visible, Duration.ofSeconds(5));

        for (SelenideElement card : cards) {
            try {
                String name = card.$(PRODUCT_NAME_CSS).getText().trim();
                String price = card.$(PRODUCT_PRICE_CSS).getText().trim();
                Map<String, String> product = new HashMap<>();
                product.put("name", name);
                product.put("price", price);
                products.add(product);
            } catch (Exception e) {
                continue;
            }
        }
        return products;
    }

    public boolean isResultsDisplayed() {
        return $$(PRODUCT_CARD_CSS).size() > 0;
    }

    public boolean hasNextPage() {
        return nextPageButton.exists() && nextPageButton.isDisplayed();
    }

    public CatalogPage goToNextPage() {
        nextPageButton.shouldBe(Condition.visible).click();
        $$(PRODUCT_CARD_CSS).first().shouldBe(Condition.visible, Duration.ofSeconds(10));
        return this;
    }
}