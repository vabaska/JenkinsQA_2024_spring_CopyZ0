package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

public class FreestyleProjectTest extends BaseTest {

    private void createFreestyleProject() {
        getDriver().findElement(By.linkText("New Item")).click();
        getDriver().findElement(By.name("name")).sendKeys("Test Project");
        getDriver().findElement(By.xpath("//label/span[text()='Freestyle project']")).click();
        getDriver().findElement(By.id("ok-button")).click();
    }

    private void openDashboard() {
        getDriver().findElement(By.id("jenkins-head-icon")).click();
    }

    @Test
    public void testRenameFreestyleProjectViaDropdownMenu() {
        final String renameProject = "Project renamed";
        createFreestyleProject();
        openDashboard();

        WebElement elementToHoverOver = getDriver().findElement(By.linkText("Test Project"));

        Actions actions = new Actions(getDriver());
        actions.moveToElement(elementToHoverOver).perform();

        getDriver().findElement(By.xpath("//tr[@id='job_Test Project']/td[3]/a/button")).click();
        getDriver().findElement(By.xpath("//div[@id='tippy-5']/div/div/div/a[4]")).click();

        final WebElement rename = getDriver().findElement(By.xpath("//input[@value='Test Project']"));
        rename.clear();
        rename.sendKeys(renameProject);
        getDriver().findElement(By.name("Submit")).click();

        final String actualProjectRename = getDriver().findElement(By.xpath("//h1")).getText();

        Assert.assertEquals(actualProjectRename, renameProject);

        openDashboard();

        final WebElement actualProjectRenameLink = getDriver().findElement(By.xpath("//a[@href='job/Project%20renamed/']"));

        Assert.assertTrue(actualProjectRenameLink.isDisplayed(), "actualProjectRenameLink is NOT displayed");
    }

    @Test

    public void deleteFreestyleProjectViaDropdownMenu() {
        createFreestyleProject();
        openDashboard();

        WebElement elementToHoverOver = getDriver().findElement(By.linkText("Test Project"));
        Actions actions = new Actions(getDriver());
        actions.moveToElement(elementToHoverOver).perform();

        getDriver().findElement(By.xpath("//tr[@id='job_Test Project']/td[3]/a/button")).click();
        getDriver().findElement(By.xpath("//div[@id='tippy-5']/div/div/div/button[2]")).click();
        getDriver().findElement(By.xpath("//div/button[@data-id='ok']")).click();

        final String createAJodLinkText = "Create a job";
        final String actualNewJobLincText = getDriver().findElement(By.xpath("//a[@href='newJob']")).getText();

        Assert.assertEquals(actualNewJobLincText, createAJodLinkText);

        final String textWelcome = "Welcome to Jenkins!";
        final String linkCreateAJob = getDriver().findElement(By.xpath("//h1")).getText();
        final WebElement createAJobLink = getDriver().findElement(By.xpath("//h1"));

        Assert.assertEquals(linkCreateAJob, textWelcome);
        Assert.assertTrue(createAJobLink.isDisplayed(), "Create a Job link is NOT displayed");

        final WebElement newJobLinc = getDriver().findElement(By.xpath("//a[@href='newJob']"));

        Assert.assertTrue(newJobLinc.isDisplayed(), "newJobLink is NOT displayed");
    }
}
