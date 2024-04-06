package school.redrover.runner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JenkinsUtils {

    private static final HttpClient client = HttpClient.newBuilder().build();

    private static String sessionId;

    private static String getCrumbFromPage(String page) {
        final String CRUMB_TAG = "data-crumb-value=\"";

        int crumbTagBeginIndex = page.indexOf(CRUMB_TAG) + CRUMB_TAG.length();
        int crumbTagEndIndex = page.indexOf('"', crumbTagBeginIndex);

        return page.substring(crumbTagBeginIndex, crumbTagEndIndex);
    }

    private static Set<String> getSubstringsFromPage(String page, String from, String to) {
        // 255 - максимально возможная длинна имени, но если используется не латиница или специальные символы, страка будет длинней из-за кодирования (пробел - %20)
        return getSubstringsFromPage(page, from, to, 256);
    }

    private static Set<String> getSubstringsFromPage(String page, String from, String to, int maxSubstringLength) {
        Set<String> result = new HashSet<>();

        int index = page.indexOf(from);
        while (index != -1) {
            index += from.length();
            int endIndex = page.indexOf(to, index);

            if (endIndex != -1 && endIndex - index < maxSubstringLength) {
                result.add(page.substring(index, endIndex));
            } else {
                endIndex = index;
            }

            index = page.indexOf(from, endIndex);
        }

        return result;
    }

    private static String[] getHeader() {
        List<String> result = new ArrayList<>(List.of("Content-Type", "application/x-www-form-urlencoded"));
        if (sessionId != null) {
            result.add("Cookie");
            result.add(sessionId);
        }
        return result.toArray(String[]::new);
    }

    private static HttpResponse<String> getHttp(String url) {
        try {
            return client.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .headers(getHeader())
                            .GET()
                            .build(),
                    HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpResponse<String> postHttp(String url, String body) {
        try {
            return client.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .headers(getHeader())
                            .POST(HttpRequest.BodyPublishers.ofString(body))
                            .build(),
                    HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getPage(String uri) {
        HttpResponse<String> page = getHttp(ProjectUtils.getUrl() + uri);
        if (page.statusCode() != 200) {
            final String HEAD_COOKIE = "set-cookie";

            HttpResponse<String> loginPage = getHttp(ProjectUtils.getUrl() + "login?from=%2F");
            sessionId = loginPage.headers().firstValue(HEAD_COOKIE).orElse(null);

            // Поле sessionId используется внутри postHttp
            HttpResponse<String> indexPage = postHttp(ProjectUtils.getUrl() + "j_spring_security_check",
                    String.format("j_username=%s&j_password=%s&from=%%2F&Submit=",
                            URLEncoder.encode(ProjectUtils.getUserName(), StandardCharsets.UTF_8),
                            URLEncoder.encode(ProjectUtils.getPassword(), StandardCharsets.UTF_8)));
            sessionId = indexPage.headers().firstValue(HEAD_COOKIE).orElse("");

            page = getHttp(ProjectUtils.getUrl() + uri);
        }

        if (page.statusCode() == 403) {
            throw new RuntimeException(String.format("Authorization does not work with user: \"%s\" and password: \"%s\"", ProjectUtils.getUserName(), ProjectUtils.getPassword()));
        } else if (page.statusCode() != 200) {
            throw new RuntimeException("Something went wrong while clearing data");
        }

        return page.body();
    }

    private static void deleteByLink(String link, Set<String> names, String crumb) {
        String fullCrumb = String.format("Jenkins-Crumb=%s", crumb);
        for (String name : names) {
            postHttp(String.format(ProjectUtils.getUrl() + link, name), fullCrumb);
        }
    }

    private static void deleteJobs() {
        String mainPage = getPage("");
        deleteByLink("job/%s/doDelete",
                getSubstringsFromPage(mainPage, "href=\"job/", "/\""),
                getCrumbFromPage(mainPage));
    }

    private static void deleteViews() {
        String mainPage = getPage("");
        deleteByLink("view/%s/doDelete",
                getSubstringsFromPage(mainPage, "href=\"/view/", "/\""),
                getCrumbFromPage(mainPage));

        String viewPage = getPage("me/my-views/view/all/");
        deleteByLink("user/admin/my-views/view/%s/doDelete",
                getSubstringsFromPage(viewPage, "href=\"/user/admin/my-views/view/", "/\""),
                getCrumbFromPage(viewPage));
    }

    private static void deleteUsers() {
        String userPage = getPage("manage/securityRealm/");
        deleteByLink("manage/securityRealm/user/%s/doDelete",
                getSubstringsFromPage(userPage, "href=\"user/", "/\"").stream()
                        .filter(user -> !user.equals(ProjectUtils.getUserName())).collect(Collectors.toSet()),
                getCrumbFromPage(userPage));
    }

    private static void deleteNodes() {
        String mainPage = getPage("");
        deleteByLink("manage/computer/%s/doDelete",
                getSubstringsFromPage(mainPage, "href=\"/computer/", "/\""),
                getCrumbFromPage(mainPage));
    }

    private static void deleteDescription() {
        String mainPage = getPage("");
        postHttp(ProjectUtils.getUrl() + "submitDescription",
                String.format(
                        "description=&Submit=&Jenkins-Crumb=%1$s&json=%%7B%%22description%%22%%3A+%%22%%22%%2C+%%22Submit%%22%%3A+%%22%%22%%2C+%%22Jenkins-Crumb%%22%%3A+%%22%1$s%%22%%7D",
                        getCrumbFromPage(mainPage)));
    }

    private static void deleteCredentials() {
        String mainPage = getPage("");
        postHttp(ProjectUtils.getUrl() + "manage/credentials/store/system/domain/_/doDelete",
                String.format("Jenkins-Crumb=%s", getCrumbFromPage(mainPage)));

        postHttp(ProjectUtils.getUrl() + "user/admin/credentials/store/user/domain/_/doDelete",
                String.format("Jenkins-Crumb=%s", getCrumbFromPage(mainPage)));
    }

    static void clearData() {
        JenkinsUtils.deleteViews();
        JenkinsUtils.deleteJobs();
        JenkinsUtils.deleteUsers();
        JenkinsUtils.deleteNodes();
        JenkinsUtils.deleteDescription();
        JenkinsUtils.deleteCredentials();
    }

    static void login(WebDriver driver) {
        driver.findElement(By.name("j_username")).sendKeys(ProjectUtils.getUserName());
        driver.findElement(By.name("j_password")).sendKeys(ProjectUtils.getPassword());
        driver.findElement(By.name("Submit")).click();
    }

    static void logout(WebDriver driver) {
        ProjectUtils.get(driver);

        driver.findElement(By.xpath("//a[@href='/logout']")).click();
    }
}

