package icemobile.junit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.lang.Object;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(BlockJUnit4ClassRunner.class)
//@RunWith(RepeatTest.class)
//@Repeat(2)
public class TestChrome2Test extends TestCase {

    private static ChromeDriverService service;
    private WebDriver driver;
    private static String testElementId = "buttonOne";
    private static String testCarouselId = "cartwo" ;
 //   private static String pathToApp = "http://10.0.1.21:8080/jsptagtest";
    private static int swipeDistance = -175;
    private static int swipeTolerance = 2;
    private static String pathToApp = "http://10.18.39.103:8080/jsptagtest";
    @BeforeClass
    public static void createAndStartService() {
        Object o = new ChromeDriverService.Builder();

        service = new ChromeDriverService.Builder()
          .usingDriverExecutable(new File("/Users/jguglielmin/icefaces/testDrivers/chromedriver"))
          .usingAnyFreePort()
          .build();
        try {
            service.start();
        }catch(Exception e){
            System.out.println("could not start service");
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void createAndStopService() {
      service.stop();
    }

    @Before
    public void createDriver() {
      driver = new RemoteWebDriver(service.getUrl(),
          DesiredCapabilities.chrome());
    }

    @After
    public void quitDriver() {
        driver.quit();
    }

    @Test
    public void testButtonPage(){
        //navigate to app
        String firstNav = pathToApp+"/button/button.jsp";
       //final String testElementId = "buttonOne";
        System.out.println("firstNav="+firstNav);
        driver.get(firstNav);
        //wait until ready
        try {
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement element = driver.findElement(By.id(testElementId));
                return element.isEnabled();
            }
        });
        }catch (Exception e){
            System.out.println("trouble with first nav");
        }
        WebElement select = driver.findElement(By.id("selButType"));
        WebElement testElement = driver.findElement(By.id(testElementId));
        List<WebElement> options = select.findElements(By.tagName("option"));
        WebElement submitAttribute = driver.findElement(By.id("updateBtn"));
        String defaultClass = "mobi-button ui-btn-up-c ";
        String importantClass = defaultClass+"mobi-button-important ";
        String backClass = defaultClass + "mobi-button-back ";
        String attentionClass = defaultClass+"mobi-button-attention ";
        String unimportantClass = defaultClass + "mobi-button-unimportant ";
        List<String> messages = new ArrayList<String>(Arrays.asList("default",
                "important", "back", "attention", "unimportant"));
        List<String> cssClasses = new ArrayList<String>();
        cssClasses.add(0, defaultClass);
        cssClasses.add(1, importantClass);
        cssClasses.add(2, backClass);
        cssClasses.add(3, attentionClass);
        cssClasses.add(4, unimportantClass);
        int length = options.size();
        System.out.println("first css for button="+testElement.getAttribute("class"));
        for (int i=0; i<length; i++ ){
//            System.out.println(" cssClasses i="+i+" is ="+cssClasses.get(i));
//            System.out.println(String.format("Option is: %s", option.getAttribute("value")));
            select = driver.findElement(By.id("selButType"));
            options = select.findElements(By.tagName("option"));
            WebElement option = options.get(i);
            option.click();
            submitAttribute = driver.findElement(By.id("updateBtn"));
            submitAttribute.click();
            try {
               (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
                   public Boolean apply(WebDriver d) {
                       WebElement element = driver.findElement(By.id(testElementId));
                       return element.isEnabled();
                   }
               });
            }catch (Exception e){
                System.out.println(" trouble in loop for button type="+messages.get(i));
            }
            testElement = driver.findElement(By.id(testElementId));
         //   System.out.println(" for i="+i+" should have ="+cssClasses.get(i)+" have ="+testElement.getAttribute("class"));
            assertEquals("test style ="+messages.get(i), testElement.getAttribute("class"),cssClasses.get(i));
        }
        System.out.println("finished testbuttonPage with no errors");
    }

    @Test
    public void testCarousel(){
        //navigate to app
        String firstNav = pathToApp+"/carousel/carousel.jsp";
       //final String testElementId = "buttonOne";
        System.out.println("firstNav="+firstNav);
        driver.get(firstNav);
        //wait until ready
        try {
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement element = driver.findElement(By.id(testCarouselId)) ;
                return element.isEnabled();
            }
        });
        }catch (Exception e){
            System.out.println("trouble with navigation to Carousel test page");
        }

        WebElement unorderedList  = driver.findElement(By.id("cartwo_carousel"));
        List<WebElement> links = unorderedList.findElements(By.tagName("li"));
        if (links.isEmpty()){
            System.out.println(" LInks list is empty");
        }
        else if (links.size() < 5){
            System.out.println("Links list not correct size has size="+links.size());
        }else {
        //    System.out.println(" size of links list ="+links.size());
            WebElement firstElementToGrab = links.get(3);
            Point point = firstElementToGrab.getLocation();
            int x = point.getX();
            System.out.println("location of first element="+firstElementToGrab.getLocation());
            System.out.println(" X locn before = "+x);
            Actions builder = new Actions(driver);
            Action swipe = builder.clickAndHold(firstElementToGrab).moveByOffset(swipeDistance, 0).release().build();
            swipe.perform();
            Point point2 = firstElementToGrab.getLocation();
            int x2 = point2.getX();
            System.out.println("   AFTER action location="+firstElementToGrab.getLocation());
          //  System.out.println(" \t\t x2 ="+x2);
           // System.out.println(" x2 less x1="+(x2-x));
            int calcSwipeDistance = x2-x;
            int upperRange = calcSwipeDistance + swipeTolerance;
            int lowerRange = calcSwipeDistance - swipeTolerance;
            System.out.println(" calc = "+calcSwipeDistance+" low="+lowerRange+" high="+upperRange);
            assertTrue("Error swipe greater than tolerance.", upperRange >= swipeDistance);
            assertTrue("Error swipe greater than tolerance.", lowerRange <= swipeDistance);
         //   assertEquals(calcSwipeDistance, swipeDistance );
        }
    }

    private void waitForElement(final String id) throws Exception {
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement element = driver.findElement(By.id(id)) ;
                return element.isEnabled();
            }
        });
        System.out.println(" after waitForElement");
    }

    private boolean hasClass(WebElement element, String className){
        return element.getAttribute("class").equals(className);
    }
}
