package newsvalidation.pages.google;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import newsvalidation.pages.guardian.GuardianToneNewsPage;
import newsvalidation.utilities.dataproviders.ConfigFileParser;
import newsvalidation.utilities.support.CommonFunctions;
import newsvalidation.factory.DriverFactory;


public class GoogleSearchPage extends DriverFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuardianToneNewsPage.class);
    ConfigFileParser configFileParser = new ConfigFileParser();
    CommonFunctions commonFunctions = new CommonFunctions();
    List<String> firstThreeSearchResultsData = new ArrayList<>();
    WebDriverWait wait;

    @FindBy(xpath = "(//div[contains(text(),'Accept all')])[2]")
    private WebElement cookieBannerAcceptButton;

    @FindBy(xpath = "//input[@name='q']")
    private WebElement searchBox;

    private WebDriver pageDriver;

    public GoogleSearchPage(WebDriver driver){
        pageDriver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(pageDriver, Duration.ofSeconds(30));
    }

    public String openGoogleSearchUrl() {
        pageDriver.get(configFileParser.getGoogleSearchPageUrlFromConfig());
        commonFunctions.threadWaitForShortTime();
        cookieBannerAcceptButton.click();
        return pageDriver.getTitle();
    }

    public void performSearchForNewsArticle(String textToSearch) {
        wait.until(ExpectedConditions.elementToBeClickable(searchBox)).click();
        searchBox.sendKeys(textToSearch);
        searchBox.sendKeys(Keys.ENTER);
        commonFunctions.threadWaitForShortTime();
    }

    public List<String> getFirstThreeSearchResultsAsList(){
        // If Else block to handle different types of Google search results page and different xpaths
        if(!pageDriver.findElements(By.xpath("//span[contains(text(),'More news')]")).isEmpty()) {
            ((JavascriptExecutor)pageDriver).executeScript("window.scrollBy(0,400)");
            LOGGER.info("Google view for current search is --> More News View");
            extractFirstThreeSearchResultsToList(true);
        } else {
            LOGGER.info("Google view for current search is --> Normal View");
            extractFirstThreeSearchResultsToList(false);
        }
        return firstThreeSearchResultsData;
    }

    /**
     * Method to handle 2 different google search views based on boolean parameter 'googleMoreNewsView'
     * Updates 'firstThreeSearchResultsText' String List with first 3 search results from Google
     */
    private void extractFirstThreeSearchResultsToList(boolean googleMoreNewsView){
        int numOfResultsFound = 0;
        int searchIndex = 1;
        String sourceNewsPartialXpathFirstPart = "";
        String sourceNewsPartialXpathSecondPart = "";
        if (googleMoreNewsView){
            sourceNewsPartialXpathFirstPart = " (//span[contains(text(),'More news')]/following::div/div/div/div/div/a)[";
            sourceNewsPartialXpathSecondPart = "]/parent::div/parent::div/parent::div/parent::div";
        } else {
            sourceNewsPartialXpathFirstPart = "(//h1[text()='Search Results']/following::div[1]/div/div)[";
            sourceNewsPartialXpathSecondPart = "]";
        }
        do {
            // Ignoring Guardian website from Google search results
            if(pageDriver.findElement(By.xpath(sourceNewsPartialXpathFirstPart+searchIndex+sourceNewsPartialXpathSecondPart)).getText().toUpperCase().contains("THEGUARDIAN")) {
                LOGGER.info("Guardian results found in first 3 Google search results. Skipping it and this will not be considered as source for analysis..!!");
            }
            else {
                String searchResultsData = "";
                if(pageDriver.findElement(By.xpath(sourceNewsPartialXpathFirstPart+searchIndex+sourceNewsPartialXpathSecondPart)).getText().contains("Missing")){
                    // Working on Google search article which contains some missing words suggestion as search words are duplicated in this case
                    LOGGER.info("Google search result contains 'Missing words' section. Working on alternate xpath's to ignore our search data");
                    searchResultsData = pageDriver.findElement(By.xpath(sourceNewsPartialXpathFirstPart + searchIndex + sourceNewsPartialXpathSecondPart+"/div/div[1]")).getText();
                    searchResultsData = searchResultsData + " " + pageDriver.findElement(By.xpath(sourceNewsPartialXpathFirstPart + searchIndex + sourceNewsPartialXpathSecondPart+"/div/div[2]")).getText();
                } else {
                    searchResultsData = pageDriver.findElement(By.xpath(sourceNewsPartialXpathFirstPart + searchIndex + sourceNewsPartialXpathSecondPart)).getText();
                }
                firstThreeSearchResultsData.add(searchResultsData);
                numOfResultsFound = numOfResultsFound + 1;
            }
            searchIndex = searchIndex + 1;
        } while(numOfResultsFound < configFileParser.getGuardianArticleNumberOfMatchingResultsFromConfig());
    }
}
