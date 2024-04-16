package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

public class CreateFolder2Test extends BaseTest {

    private void createNewHolder(String folderName) {
        getDriver().findElement(By.linkText("New Item")).click();
        getDriver().findElement(By.name("name")).sendKeys(folderName);
        getDriver().findElement(By.xpath("//label/span[text()='Folder']")).click();
        getDriver().findElement(By.id("ok-button")).click();
    }

    private void openDashboard() {
        getDriver().findElement(By.id("jenkins-head-icon")).click();
    }

    @Test
    public void testNewlyCreateFolderIsEmptyNN() {
        final String folderName = "NewProjectFolder";
        final String thisFolderIsEmptyMessage = "This folder is empty";
        final  String createAJodLinkText = "Create a job";

        createNewHolder(folderName);
        openDashboard();

        getDriver().findElement(By.linkText(folderName)).click();
        final String actualFolderName = getDriver().findElement(By.xpath("//h1")).getText();
        final String actualEmptyStateMessage = getDriver().findElement(By.xpath("//section[@class='empty-state-section']/h2")).getText();
        final WebElement newJobLinc = getDriver().findElement(By.xpath("//a[@href='newJob']"));
        final String actualNewJobLincText = newJobLinc.getText();

        Assert.assertEquals(actualFolderName, folderName);
        Assert.assertEquals(actualEmptyStateMessage, thisFolderIsEmptyMessage);
        Assert.assertTrue(newJobLinc.isDisplayed(), "newJobLink is NOT displayed");
        Assert.assertEquals(actualNewJobLincText, createAJodLinkText);
    }

}
