package school.redrover;

import org.openqa.selenium.By;

import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

public class MultiConfigurationProjectExecutionTest extends BaseTest {
    final String projectName = "New Multi-configuration project";

    private void createEnableMultiConfigurationProject(String projectName) {
        getDriver().findElement(By.linkText("New Item")).click();
        getDriver().findElement(By.name("name")).sendKeys(projectName);
        getDriver().findElement(By.xpath("//label/span[text()='Multi-configuration project']")).click();
        getDriver().findElement(By.id("ok-button")).click();
    }

    private void openDashboard() {
        getDriver().findElement(By.id("jenkins-head-icon")).click();
    }

    @Test
    public void testDisableMultiConfigurationProjectExecution() {
        createEnableMultiConfigurationProject(projectName);
        openDashboard();
        getDriver().findElement(By.linkText(projectName)).click();

        getDriver().findElement(By.name("Submit")).click();

        final String actualResult = getDriver().findElement(By.xpath("//form[text()]")).getText();

        Assert.assertTrue(actualResult.contains("This project is currently disabled"), "Substring not found");
    }
}
