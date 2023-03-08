Feature: Guardian site news validation
  Verify if news articles in Guardian website is valid

  @author-Chandrasekar
  @app-guardian
  Scenario: Verify that first news article in Guardian website is valid by checking in Google
    Given user is in Guardian tone news webpage
    And the first news article is displayed
    When user checks Google to validate the news
    Then he confirms that the article is valid
