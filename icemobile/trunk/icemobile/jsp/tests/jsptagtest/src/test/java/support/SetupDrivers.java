package support;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriverService;
import java.io.File;

public class SetupDrivers {
    /* need to have chromedriver downloaded and placed in resources folder :
        <project root>/src/main/resources/chromedriver/mac/chromedriver
        <project root>/src/main/resources/chromedriver/windwos/chromedriver.exe
     */
 /*  private static String MAC_DRIVER = "/chromedriver/mac/chromedriver";
    private static String WINDOWS_DRIVER = "/chromedriver/windows/chromedriver.exe";

    public static void setupChromeDriver(){
        if (System.getProperty("os.name").contains("mac")){
            File macDriver = new File(SetupDrivers.class.getResource(MAC_DRIVER).getFile());
            if (!macDriver.canExecute()) {
                macDriver.setExecutable(true);
            }
            System.setProperty("webdriver.chrome.driver", SetupDrivers.class.getResource(MAC_DRIVER).getFile());
            // do we have to check that Chrome is available? assuming it is for now
        } else {//assume windows box
            System.setProperty("webdriver.chrome.driver",SetupDrivers.class.getResource(WINDOWS_DRIVER).getFile());

        }
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        ChromeDriver driver = new ChromeDriver(options);
    }   */

}
