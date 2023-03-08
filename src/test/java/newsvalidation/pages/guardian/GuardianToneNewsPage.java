package newsvalidation.pages.guardian;

import newsvalidation.pages.google.GoogleSearchPage;
import newsvalidation.utilities.dataproviders.ConfigFileParser;
import newsvalidation.utilities.support.CommonFunctions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuardianToneNewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuardianToneNewsPage.class);
    private final ConfigFileParser configFileParser = new ConfigFileParser();
    private CommonFunctions commonFunctions = new CommonFunctions();

    @FindBy(xpath = "//iframe[@title='The Guardian consent message']")
    private WebElement consentMessageIframe;

    @FindBy(xpath = "//button[@title='Yes, I’m happy']")
    private WebElement cookieBannerAcceptButton;

    @FindBy(xpath = "(//li/div)[1]")
    private WebElement firstNewsArticle;

    private WebDriver pageDriver;

    public GuardianToneNewsPage(WebDriver driver){
        pageDriver = driver;
        PageFactory.initElements(driver, this);
    }

    public String openGuardianToneNewsUrl(){
        pageDriver.get(configFileParser.getGuardianToneNewsPageUrlFromConfig());
        commonFunctions.threadWaitForMediumTime();
        pageDriver.switchTo().defaultContent();
        pageDriver.switchTo().frame(consentMessageIframe);
        cookieBannerAcceptButton.click();
        pageDriver.switchTo().defaultContent();
        return(pageDriver.getTitle());
    }

    public boolean isFirstNewsArticleDisplayed(){
        int firstNewsArticleIndex = 1;
        boolean firstNewsArticleFound = false;
        do{
            // Ignoring any ongoing Live news or sports and considering only already published news article
            if(!pageDriver.findElements(By.xpath("(//li/div)["+firstNewsArticleIndex+"]//h3/a/div")).isEmpty()){
                // Ignoring Live articles and Live sports/videos which has the xpath mentioned in If condition
                LOGGER.info(firstNewsArticle.getText() + " ---> a LIVE event, Video or discussion found and not a News article. Trying next one.!!");
            } else if(!pageDriver.findElements(By.xpath("(//li/div)["+firstNewsArticleIndex+"]//span[contains(@class, 'inline-video-icon')]")).isEmpty()) {
                // Ignoring video articles as it is not a news article
                LOGGER.info(firstNewsArticle.getText() + " ---> a video article found and not a News article. Trying next one.!!");
            }
             else {
                    firstNewsArticleFound = true;
                    firstNewsArticle = pageDriver.findElement(By.xpath("(//li/div)[" + firstNewsArticleIndex + "]"));
                    LOGGER.info("First News Article xpath found and it is --> (//li/div)[" + firstNewsArticleIndex + "]");
                    LOGGER.info("First News Article link URL --> " + pageDriver.findElement(By.xpath("((//li/div)[" + firstNewsArticleIndex + "]//a)[1]")).getAttribute("href"));
                    LOGGER.info("First News Article text from Guardian --> " + firstNewsArticle.getText());
                }
            firstNewsArticleIndex = firstNewsArticleIndex + 1;
        } while(!firstNewsArticleFound);
        return (firstNewsArticle.isDisplayed());
    }

    public String getFirstGuardianNewsArticleData(){
        return (firstNewsArticle.getText());
    }

    public GoogleSearchPage getDriverForGooglePage(){
        return new GoogleSearchPage(pageDriver);
    }
}
