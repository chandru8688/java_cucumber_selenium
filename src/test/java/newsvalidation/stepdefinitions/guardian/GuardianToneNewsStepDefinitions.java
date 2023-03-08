package newsvalidation.stepdefinitions.guardian;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import newsvalidation.pages.google.GoogleSearchPage;
import newsvalidation.pages.guardian.GuardianToneNewsPage;
import newsvalidation.utilities.dataproviders.ConfigFileParser;
import newsvalidation.factory.DriverFactory;
import static org.assertj.core.api.Assertions.*;

public class GuardianToneNewsStepDefinitions {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuardianToneNewsStepDefinitions.class);
    private final ConfigFileParser configFileParser = new ConfigFileParser();
    private GuardianToneNewsPage guardianToneNewsPage = new GuardianToneNewsPage(DriverFactory.getDriver());
    private GoogleSearchPage googleSearchPage;
    private String newsArticleToSearchFromGuardian;
    private String formattedNewsArticleToSearch;
    private List<String> formattedNewsArticleToSearchList;
    private int totalWordsInFormattedArticle;
    private int googleMatchingArticleCount = 0;

    @Given("user is in Guardian tone news webpage")
    public void userIsInGuardianToneNewsWebpage() throws InterruptedException {
        assertThat(guardianToneNewsPage.openGuardianToneNewsUrl()).
                isEqualTo(configFileParser.getGuardianToneNewsPageTitleFromConfig());
    }

    @And("the first news article is displayed")
    public void firstNewsArticleIsDisplayed() {
        assertThat(guardianToneNewsPage.isFirstNewsArticleDisplayed()).isTrue();
        newsArticleToSearchFromGuardian = guardianToneNewsPage.getFirstGuardianNewsArticleData();
    }

    @When("user checks Google to validate the news")
    public void userChecksOtherSourcesToValidateTheNews() {
        List<String> googleFirstThreeSearchResults;
        int matchingWordsRequirement = 0;
        googleSearchPage = guardianToneNewsPage.getDriverForGooglePage();
        // Checking for matching articles in Google search.
        assertThat(googleSearchPage.openGoogleSearchUrl()).
                isEqualTo(configFileParser.getGoogleSearchPageTitleFromConfig());
        formattedNewsArticleToSearch = getSearchableArticleData();
        LOGGER.info("Searching Google using formatted input article data -> " + formattedNewsArticleToSearch);
        googleSearchPage.performSearchForNewsArticle(formattedNewsArticleToSearch);
        googleFirstThreeSearchResults = googleSearchPage.getFirstThreeSearchResultsAsList();
        // Setting requirement for matching words in Google search articles as per the size[total words] of formatted article search data
        if(totalWordsInFormattedArticle > 11) {
            matchingWordsRequirement = 4;
        } else if(totalWordsInFormattedArticle > 7) {
            matchingWordsRequirement = 3;
        } else if(totalWordsInFormattedArticle > 4) {
            matchingWordsRequirement = 2;
        } else {
            matchingWordsRequirement = 1;
        }
        LOGGER.info("Expected number of matching words in Google news article as per requirement is: " + matchingWordsRequirement);
        LOGGER.info("Working on with first 3 matching search results from Google: ");
        // Looping through Google search results to find if it is matching Guardian news article
        for(int searchResult = 0; searchResult < googleFirstThreeSearchResults.size(); searchResult++){
            LOGGER.info("Working with search result number: " + (searchResult+1) + ". Below is the data:");
            LOGGER.info(googleFirstThreeSearchResults.get(searchResult).toUpperCase());
            int matchingWordCount = 0;
            // Getting words to search from formattedNewsArticleToSearchList
            for(String wordToSearch: formattedNewsArticleToSearchList){
                if (googleFirstThreeSearchResults.get(searchResult).toUpperCase().contains(wordToSearch)) {
                    LOGGER.info("Found a matching word '"+ wordToSearch +"' in news article");
                    matchingWordCount = matchingWordCount + 1;
                }
            }
            // If condition to decide if Google search article is matching Guardian news article
            if (matchingWordCount >= matchingWordsRequirement){
                LOGGER.info("Matching news article found.!!! Number of matching words is: " + matchingWordCount);
                googleMatchingArticleCount = googleMatchingArticleCount + 1;
                LOGGER.info("Number of Successful matching news articles from Google: " + googleMatchingArticleCount);
            }
        }
    }

    @Then("he confirms that the article is valid")
    public void userConfirmsThatArticleIsValid() {
        boolean guardianArticleIsValid;
        // If condition to check if out of 3 Google results there are 2 matching articles.
        // If there are more 2 or more than 2 matching articles we decide that Guardian news is valid.
        if(googleMatchingArticleCount >= 2){
            LOGGER.info("'" + formattedNewsArticleToSearch + "' - Guardian news article is a Valid news");
            guardianArticleIsValid = true;
        }
        else {
            LOGGER.info("'" + formattedNewsArticleToSearch + "' - Guardian news article is not a Valid news");
            guardianArticleIsValid = false;
        }
        assertThat(guardianArticleIsValid).isTrue();
    }

    private String getSearchableArticleData(){
        String newsArticleToSearchUpdated = newsArticleToSearchFromGuardian.replace("\n", " ").
                replace("\r"," ").replace(",", "");
        // General words to be removed from Guardian article data to enable efficient search
        List<String> wordsToRemove =
                Arrays.asList("TO", "BY", "ON", "WITH", "FOR", "HE", "IS", "IN", "WAS", "HAS", "PM", "AM", "PUBLISHED:", "AND", ",", "-", "_");
        formattedNewsArticleToSearchList =
                Pattern.compile(" ")
                        .splitAsStream(newsArticleToSearchUpdated.toUpperCase().strip())
                        .collect(Collectors.toCollection(ArrayList<String>::new));
        for(String word: wordsToRemove){
            formattedNewsArticleToSearchList.remove(word);
        }
        formattedNewsArticleToSearchList.remove(formattedNewsArticleToSearchList.size()-1);
        totalWordsInFormattedArticle = formattedNewsArticleToSearchList.size();
        LOGGER.info("Total words in formatted article to search: " + totalWordsInFormattedArticle);
        LOGGER.info(String.join(" ", formattedNewsArticleToSearchList).toUpperCase());
        if (totalWordsInFormattedArticle > configFileParser.getGuardianArticleMaximumWordsLimitFromConfig()){
            LOGGER.info("Article to search is too big and will not yield proper search results. So truncating and limiting article search data to 15 words maximum");
            formattedNewsArticleToSearchList =
                    formattedNewsArticleToSearchList.subList(0, configFileParser.getGuardianArticleMaximumWordsLimitFromConfig());
        }
        return String.join(" ", formattedNewsArticleToSearchList).toUpperCase();
    }
}
