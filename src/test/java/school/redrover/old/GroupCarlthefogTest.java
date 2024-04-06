package school.redrover.old;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Ignore
public class GroupCarlthefogTest extends BaseTest {

    private final static String FULL_NAME = "John Smith";
    private final static String EMAIL = "john.smith@gmail.com";
    private final static String ADDRESS = "5th Ave, New York";
    private final static String PERM_ADDRESS = "5th Ave, New York";

    @Test
    public void testSaucedemo() {
        String expectedPageName = "Swag Labs";

        WebDriver driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        String actualPageName = driver.findElement(By.xpath("//div[@class= 'app_logo']")).getText();

        Assert.assertEquals(expectedPageName, actualPageName);
        driver.quit();
    }

    @Test
    public void testPageHeader() {
        String expectedPageHeader1 = "99 Bottles of Beer";
        String expectedPageHeader2 = "Welcome to 99 Bottles of Beer";

        WebDriver driver = new ChromeDriver();
        driver.get("https://www.99-bottles-of-beer.net/");

        String actualPageHeader1 = driver.findElement(By.xpath("//h1")).getText();
        String actualPageHeader2 = driver.findElement(By.xpath("//div[@id='main']//h2")).getText();

        Assert.assertEquals(expectedPageHeader1, actualPageHeader1);
        Assert.assertEquals(expectedPageHeader2, actualPageHeader2);

        driver.quit();
    }

    @Test
    public void testElementsList() {
        int expectedElementsQuantity = 6;
        List<String> expectedElementsHeader = new ArrayList<>(Arrays.asList(
                "Elements",
                "Forms",
                "Alerts, Frame & Windows",
                "Widgets",
                "Interactions",
                "Book Store Application"));

        WebDriver driver = new ChromeDriver();
        driver.get("https://demoqa.com/");

        int actualElementsQuantity = driver.findElements(By.xpath("//div[@class='card-body']")).size();

        List<WebElement> elementsHeaderList = driver.findElements(By.xpath("//div[@class='card-body']"));
        List<String> actualElementsHeader = WebElementsToString(elementsHeaderList);

        Assert.assertEquals(expectedElementsQuantity, actualElementsQuantity);
        Assert.assertEquals(expectedElementsHeader, actualElementsHeader);

        driver.quit();
    }

    public static List<String> WebElementsToString(List<WebElement> elementsHeaderList) {
        List<String> stringList = new ArrayList<>();
        for (WebElement element : elementsHeaderList) {
            stringList.add(element.getText());
        }

        return stringList;
    }

    @Test
    public void testElementsButtonMenuList() {
        int expectedMenuListQuantity = 9;
        List<String> expectedItemName = new ArrayList<>(Arrays.asList(
                "Text Box",
                "Check Box",
                "Radio Button",
                "Web Tables",
                "Buttons",
                "Links",
                "Broken Links - Images",
                "Upload and Download",
                "Dynamic Properties"));

        WebDriver driver = new ChromeDriver();
        driver.get("https://demoqa.com/");

        driver.findElement(By.xpath("//div[@id='app']/div/div/div[2]/div/div[1]/div/div[1]")).click();
        String currentURL = driver.getCurrentUrl();

        int actualMenuListQuantity = driver.findElements(By.xpath("//div[@class='element-list collapse show']//li"))
                .size();
        List<WebElement> elementsHeaderList = driver.findElements(By.xpath("//div[@class='element-list collapse show']//li"));
        List<String> actualItemName = WebElementsToString(elementsHeaderList);

        Assert.assertTrue(currentURL.contains("elements"));
        Assert.assertEquals(expectedMenuListQuantity, actualMenuListQuantity);
        Assert.assertEquals(expectedItemName, actualItemName);

        driver.quit();
    }

    @Test
    public void testLibrary() {

        String expectedHeader1 = "For Adults";
        String expectedHeader2 = "Kids Recommended";

        getDriver().get("https://www.olathelibrary.org/");

        getDriver().manage().timeouts().implicitlyWait(Duration.ofMillis(5000));

        WebElement servicesTab = getDriver().findElement(By.xpath("//a[text()='Services']"));
        Actions actions = new Actions(getDriver());
        actions.moveToElement(servicesTab).build().perform();

        getDriver().findElement(By.xpath("//a[text()='Adult Book Recommendations']")).click();
        WebElement header1 = getDriver().findElement(By.xpath("//header[@id='widget_6876_11653_2227']"));
        String actualHeader1 = header1.getText();

        Assert.assertEquals(actualHeader1, expectedHeader1);

        WebElement kidsTab = getDriver().findElement(By.xpath("//a[text()='Kids']"));
        actions.moveToElement(kidsTab).build().perform();

        getDriver().findElement(By.xpath("//a[text()='Kids Recommended']")).click();
        WebElement header2 = getDriver().findElement(By.xpath("//header[@id='widget_4280_11723_2315']"));
        String actualHeader2 = header2.getText();

        Assert.assertEquals(actualHeader2, expectedHeader2);
    }

    @Test
    public void testMortgagePage() {

        getDriver().get("https://www.bankofamerica.com/");

        getDriver().findElement(By.xpath("//a[@id='navHomeLoans']")).click();
        getDriver().findElement(By.xpath("//a[@id='mortgage']")).click();

        Assert.assertTrue(getDriver().findElement(By.xpath("//h1[@id='skip-to-h1']")).getText().contains("Mortgage"));
    }

    @Test
    public void purchaseItemTest() {

        getDriver().get("https://qa-practice.netlify.app");

        getDriver().findElement(By.id("products-list")).click();
        getDriver().findElement(By.xpath("//div[@class='shop-item-details']/button")).click();
        getDriver().findElement(By.xpath("//button[text()='PURCHASE']")).click();
        String value = getDriver().findElement(By.id("message")).getText();

        Assert.assertEquals(value, "Congrats! Your order of $905.99 has been registered!");
    }

    @Test
    public void testExpandtesting() {

        getDriver().get("https://practice.expandtesting.com/login");

        getDriver().findElement(By.id("username")).sendKeys("practice");
        getDriver().findElement(By.name("password")).sendKeys("SuperSecretPassword!");
        getDriver().findElement(By.xpath("//button[@type='submit' and contains(text(),'Login')]")).click();

        String actual = getDriver().findElement(By.id("flash")).getText();

        Assert.assertEquals(actual, "You logged into a secure area!");
    }

    @Test

    public void testLoginConduit() {
        String userName = "Grechka" + Math.random() * 3;
        String email = "Suyn@mail" + userName + ".ru";
        String password = "W1234567" + Math.random() * 3;

        WebDriver driver = getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get("https://demo.realworld.io/#/register");

        WebElement textBoxUserName = driver.findElement(By.xpath("//input[ @ng-model='$ctrl.formData.username']"));
        textBoxUserName.sendKeys(userName);

        WebElement textBoxEmail = driver.findElement(By.xpath("//input[@ng-model=\"$ctrl.formData.email\"]"));
        textBoxEmail.sendKeys(email);

        WebElement textBoxPassword = driver.findElement(By.xpath("//input[@ng-model=\"$ctrl.formData.password\"]"));
        textBoxPassword.sendKeys(password);

        WebElement buttons = driver.findElement(By.xpath("//button[@ng-bind='$ctrl.title']"));
        buttons.click();

        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "https://demo.realworld.io/#/register";

        Assert.assertEquals(actualUrl, expectedUrl, "URL не совпадает");
    }

    @Test
    public void testTexBoxUseForm() {

        String expectedHeader = "Text Box";

        WebDriver driver = getDriver();
        driver.manage().window().maximize();
        driver.get("https://demoqa.com/text-box");

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        String actualHeader = driver.findElement(By.xpath("//div[@id='app']//h1")).getText();

        driver.findElement(By.cssSelector("input#userName")).sendKeys(FULL_NAME);
        driver.findElement(By.cssSelector("input#userEmail")).sendKeys(EMAIL);
        driver.findElement(By.cssSelector("textarea#currentAddress")).sendKeys(ADDRESS);
        driver.findElement(By.cssSelector("textarea#permanentAddress")).sendKeys(PERM_ADDRESS);

        Actions act = new Actions(driver);
        act.sendKeys(Keys.PAGE_DOWN).build().perform();
        WebElement submitButton = driver.findElement(By.cssSelector("button#submit"));
        submitButton.click();

        WebElement expectedName = driver.findElement(By.cssSelector("p#name"));
        WebElement expectedEmail = driver.findElement(By.cssSelector("p#email"));
        WebElement expectedCurrentAddress = driver.findElement(By.cssSelector("p#currentAddress"));
        WebElement expectedPermanentAddress = driver.findElement(By.cssSelector("p#permanentAddress"));

        Assert.assertEquals(actualHeader, expectedHeader);
        Assert.assertEquals(expectedName.getText(), "Name:" + FULL_NAME);
        Assert.assertEquals(expectedEmail.getText(), "Email:" + EMAIL);
        Assert.assertEquals(expectedCurrentAddress.getText(), "Current Address :" + ADDRESS);
        Assert.assertEquals(expectedPermanentAddress.getText(), "Permananet Address :" + PERM_ADDRESS);

        driver.quit();
    }

    @Test
    public void testWikipediaSearch() {

        getDriver().get("https://en.wikipedia.org");
        getDriver().findElement(By.id("searchInput")).sendKeys("Selenium (software)");
        getDriver().findElement(By.xpath("//button[contains(@Class, 'search-input')]")).click();

        getDriver().manage().timeouts().implicitlyWait(Duration.ofMillis(3000));

        String expectedTitle = "Selenium (software) - Wikipedia";

        String actualTitle = getDriver().getTitle();

        Assert.assertEquals(actualTitle, expectedTitle, "Page title doesn't match expected title");
    }

    @Test
    public void GoogleTranslateTest() {
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

        getDriver().get("https://translate.google.com/");

        Assert.assertEquals(getDriver().getTitle().toLowerCase(), "google переводчик");

        WebElement inputField = getDriver().findElement(By.xpath("//textarea"));

        inputField.sendKeys("Привет");

        WebElement translateToEnglish = getDriver().findElement(By.xpath("(//span[contains(text(), 'английский')])[2]/ancestor::button"));

        translateToEnglish.click();

        WebElement translationResult = getDriver().findElement(By.xpath("//span[@lang='en']"));

        Assert.assertEquals(translationResult.getText(), "Hello");
    }
}