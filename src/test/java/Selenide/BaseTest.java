package Selenide;

import Selenide.utils.ConfigLoader;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    @BeforeEach
    public void setUp() {
        System.setProperty("selenium.manager.enabled", "false");
        String driverPath = System.getProperty("user.dir") + "/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driverPath);
        String browserSize = ConfigLoader.get("browser.size");
        String headless = ConfigLoader.get("browser.headless");

        Configuration.browser = "chrome";
        Configuration.browserSize = browserSize != null ? browserSize : "1920x1080";
        Configuration.headless = Boolean.parseBoolean(headless);
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 60000;
        Configuration.holdBrowserOpen = false;
        Configuration.downloadsFolder = "target/downloads";
    }

    @AfterEach
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}