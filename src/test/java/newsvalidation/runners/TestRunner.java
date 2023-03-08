package newsvalidation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = {"src/test/resources/features"},
        plugin = {"pretty", "html:target/report/html/Destination.html", "json:target/report/cucumber.json",
                "junit:target/report/cucumber-report.xml"},
        glue = {"newsvalidation.stepdefinitions"}
)
public class TestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
