package Selenide.pages;

import Selenide.utils.ConfigLoader;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MainPage {
    private final SelenideElement burgerMenu = $(By.cssSelector("button[aria-label='main menu']"));
    private final SelenideElement catalogItem = $(By.xpath("//h3[contains(text(),'OTC товары')]/ancestor::a"));

    public MainPage openPage() {
        String baseUrl = ConfigLoader.get("base.url");
        open(baseUrl);
        burgerMenu.shouldBe(Condition.visible);
        return this;
    }

    public CatalogPage goToCatalogViaBurger() {
        burgerMenu.shouldBe(Condition.visible).click();
        catalogItem.shouldBe(Condition.visible).click();
        return new CatalogPage();
    }
}