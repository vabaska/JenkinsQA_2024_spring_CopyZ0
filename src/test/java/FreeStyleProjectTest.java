import com.beust.ah.A;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;
import school.redrover.runner.ProjectUtils;

import java.util.List;

public class FreeStyleProjectTest extends BaseTest {

    //Navigation Test
    @Test
    public void testCreateNewJobArrowIconNavigatesToNewJob() {
        final String expectedURL = ProjectUtils.getBaseURL() + "/newJob"; //"http://localhost:8080/newJob"
        final String expectedTitle = "New Item [Jenkins]";

        String oldURL = getDriver().getCurrentUrl();
        String oldTitle = getDriver().getTitle();

        getDriver().findElement(By.xpath("//a[@href='newJob']//span[@class='trailing-icon']")).click();

        final String newURL = getDriver().getCurrentUrl();
        final String newTitle = getDriver().getTitle();

        Assert.assertNotEquals(newURL, oldURL);
        Assert.assertNotEquals(newTitle, oldTitle);

        Assert.assertEquals(newURL, expectedURL);
        Assert.assertEquals(newTitle, expectedTitle);
    }
    //Functional CREATE NEW FREESTYLE PROJECT JOB test
    @Test(dependsOnMethods = "testCreateNewJobArrowIconNavigatesToNewJob")
    public void testCreateFreestyleProject() {
        final int expectedAmountOfJobsCreated = 1;
        final String expectedJobNameCreate = "Test";

        getDriver().findElement(By.xpath("//a[@href='newJob']//span[@class='trailing-icon']")).click();
        getDriver().findElement(By.id("name")).sendKeys(expectedJobNameCreate);
        getDriver().findElement(By.xpath("//li[@class='hudson_model_FreeStyleProject']")).click(); ////li[@class='hudson_model_FreeStyleProject']
        getDriver().findElement(By.id("ok-button")).click();

        getDriver().findElement(By.id("jenkins-name-icon")).click();

        List<WebElement> job = getDriver().findElements(By.xpath("//table[@id='projectstatus']/tbody/tr"));

        Assert.assertEquals(job.size(), expectedAmountOfJobsCreated);

        WebElement jobNameElement = job.get(0).findElement(By.xpath("//td/a/span"));
        final String actualJobName = jobNameElement.getText();

        Assert.assertTrue(jobNameElement.isDisplayed());
        Assert.assertEquals(actualJobName, expectedJobNameCreate);
    }
}