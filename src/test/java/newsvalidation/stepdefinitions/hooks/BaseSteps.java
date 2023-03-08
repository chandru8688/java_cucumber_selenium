package newsvalidation.stepdefinitions.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import newsvalidation.factory.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class BaseSteps {
    private WebDriver driver;
    private DriverFactory driverFactory;

    @Before
    public void setup() {
        // Before each Scenario
        driverFactory = new DriverFactory();
        driver = driverFactory.initDriver(System.getProperty("browser").toUpperCase());
    }

    @After
    public void tearDown(Scenario scenario) {
        // After each Scenario
        if (scenario.isFailed()) {
            // Screenshot
            String screenshotName = scenario.getName().replaceAll(" ", "_");
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", screenshotName);
        }
        driver.quit();
    }
}
