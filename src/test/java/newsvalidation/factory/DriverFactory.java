package newsvalidation.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class DriverFactory {
    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public WebDriver initDriver(String browser){
        browser = browser.toUpperCase();
        switch (browser) {
            case "FIREFOX":
                driver.set(new FirefoxDriver());
                break;
            case "CHROME":
                driver.set(new ChromeDriver());
                break;
            case "EDGE":
                driver.set(new EdgeDriver());
                break;
            default:
                throw new RuntimeException("'browser': " + browser + " is not supported.!!");
        }
        getDriver().manage().deleteAllCookies();
        getDriver().manage().window().maximize();
        return getDriver();
    }

    public static synchronized WebDriver getDriver() {
        return driver.get();
    }
}
