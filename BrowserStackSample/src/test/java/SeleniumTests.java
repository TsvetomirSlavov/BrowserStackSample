
import org.openqa.selenium.*;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.net.URL;

/**
 * Created by merty on 13.03.2016.
 */
public class SeleniumTests {


    public class BrowserStackTestCase {

        private WebDriver driver;
        public static final String USERNAME = "Your Browser Stack username here";
        public static final String AUTOMATE_KEY = "Your unique user key here";
        public static final String URL = "http://" + USERNAME + ":" + AUTOMATE_KEY + "@hub.browserstack.com/wd/hub";

        @BeforeTest
        @Parameters(value = {"browser", "browser_version", "os", "os_version"})
        public void setUp(String browser, String browser_version, String os, String os_version) throws Exception {
            DesiredCapabilities capability = new DesiredCapabilities();


            capability.setCapability("resolution", "1920x1080");
            capability.setCapability("os", os);
            capability.setCapability("os_version", os_version);
            capability.setCapability("browser", browser);
            capability.setCapability("browser_version", browser_version);
            capability.setCapability("project", "BrowserStackSampleProject");
            driver = new RemoteWebDriver(new URL(URL), capability);
            driver.manage().window().maximize();
        }

        @Test(priority = 1)
        public void bilyonerLogin() throws Exception {
            driver.get("http://www.bilyoner.com");
            Thread.sleep(3000);
            System.out.println("Page title : " + driver.getTitle());
            Assert.assertTrue(driver.getTitle().contains("Bilyoner.com"));
            WebElement username = driver.findElement(By.cssSelector("#j_username"));
            username.sendKeys("your bilyoner.com username here");
            username.sendKeys(Keys.TAB);
            WebElement password = driver.findElement(By.cssSelector("#j_password"));
            password.sendKeys("your password here");
            WebElement loginButton = driver.findElement(By.cssSelector(".loginButton"));
            loginButton.click();
            Thread.sleep(3000);
        }

        @Test(priority = 2)
        public void openSeleniumPage() {
            driver.get("http://seleniumhq.org");
            System.out.println("Page title : " + driver.getTitle());
            Assert.assertEquals("Selenium - Web Browser Automation", driver.getTitle());
        }

        @AfterMethod
        public void takeScreenShot(ITestResult result) {
            if (result.getStatus() == ITestResult.FAILURE) {
                driver = new Augmenter().augment(driver);

                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                try {
                    FileUtils.copyFile(srcFile, new File("C:\\screenshots\\Screenshot" + result.getParameters().toString() + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @AfterTest
        public void tearDown() throws Exception {
            driver.quit();
        }
    }
}
