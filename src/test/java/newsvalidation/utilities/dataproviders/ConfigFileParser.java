package newsvalidation.utilities.dataproviders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigFileParser {
    private final Properties project_applications_url_property_file = new Properties();
    private final Properties guardian_application_details_file = new Properties();
    private final Properties google_application_details_file = new Properties();

    private void readProjectApplicationsUrlConfig() {
        try (InputStream inputFile = Files.newInputStream
                (Paths.get("src/test/resources/configFiles/project_applications_url.properties"))) {
            project_applications_url_property_file.load(inputFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void readGuardianApplicationDetailsConfig() {
        try (InputStream inputFile = Files.newInputStream
                (Paths.get("src/test/resources/configFiles/Guardian/guardian_application_details.properties"))) {
            guardian_application_details_file.load(inputFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void readGoogleApplicationDetailsConfig() {
        try (InputStream inputFile = Files.newInputStream
                (Paths.get("src/test/resources/configFiles/Google/google_application_details.properties"))) {
            google_application_details_file.load(inputFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Project Application URL's:
    public String getGuardianToneNewsPageUrlFromConfig() {
        readProjectApplicationsUrlConfig();
        return (project_applications_url_property_file.getProperty("guardian.tone_news.page.url"));
    }

    public String getGoogleSearchPageUrlFromConfig() {
        readProjectApplicationsUrlConfig();
        return (project_applications_url_property_file.getProperty("google.search.page.url"));
    }

    // Guardian Application Details:
    public String getGuardianToneNewsPageTitleFromConfig() {
        readGuardianApplicationDetailsConfig();
        return (guardian_application_details_file.getProperty("guardian.tone_news.page.title"));
    }

    public int getGuardianArticleMaximumWordsLimitFromConfig() {
        readGuardianApplicationDetailsConfig();
        return Integer.parseInt((guardian_application_details_file.getProperty("guardian.article.maximum_words_limit")));
    }

    public int getGuardianArticleNumberOfMatchingResultsFromConfig() {
        readGuardianApplicationDetailsConfig();
        return Integer.parseInt((guardian_application_details_file.getProperty("guardian.article.number_of_matching_results")));
    }

    // Google Application Details:
    public String getGoogleSearchPageTitleFromConfig() {
        readGoogleApplicationDetailsConfig();
        return (google_application_details_file.getProperty("google.search.page.title"));
    }
}
