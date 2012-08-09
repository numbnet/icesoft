package org.icemobile.samples.mediacast.test;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.iphone.IPhoneDriver;

/*
 * Tests for the Mediacast demo
 */

public class MediacastTestSuiteWD {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	
	private static final String WIN_SAMPLE_IMG = "C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg";
	private static final String MAC_SAMPLE_IMG = "";
	private static final int IMPLICIT_WAIT = 30;
	
	@Before
	public void setUp() throws Exception {
		baseUrl = "http://localhost:8080/";
	}
		
	@Test
	public void testFirefoxDesktop() throws Exception{
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		testBasicImageFileUpload();
		driver.quit();
	}
	
	@Test //download separate chrome driver http://code.google.com/p/chromedriver/downloads/list
	public void testChromeDesktop() throws Exception{
		System.setProperty("webdriver.chrome.driver", "C:\\work\\lib\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		testBasicImageFileUpload();
		driver.quit();
	}
	
	@Test
	public void testIEDesktop() throws Exception{
		driver = new InternetExplorerDriver();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		testBasicImageFileUpload();
		driver.quit();
	}
	
	@Test
	public void testSafariDesktop() throws Exception{
		driver = new SafariDriver();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		testBasicImageFileUpload();
		driver.quit();
	}
	
	//@Test
	//http://code.google.com/p/selenium/wiki/AndroidDriver
	public void testAndroid() throws Exception{
		driver = new AndroidDriver();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		testBasicImageFileUpload();
		driver.quit();
	}
	
	//@Test
	//http://code.google.com/p/selenium/wiki/IPhoneDriver
	//works through the use of an iphone application running on your iphone, ipod touch or iphone simulator.
	public void testIPhone() throws Exception{
		driver = new IPhoneDriver();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		testBasicImageFileUpload();
		driver.quit();
	}
	
	private void testBasicImageFileUpload() throws Exception {
		driver.get(baseUrl + "/mediacast/mediacast.jsf");
		assertEquals("Mediacast", driver.findElement(By.id("title")).getText());
		driver.findElement(By.id("galleryLinkAndARFrm:galleryLink")).click();
		Thread.sleep(1000);
		assertEquals("Gallery", driver.findElement(By.id("title")).getText());
		driver.findElement(By.id("backBtnFrm:backBtn")).click();
		Thread.sleep(1000);
		assertEquals("Augmented", driver.findElement(By.xpath("//li[@id='galleryLinkAndARFrm:arRow']/div")).getText());
		driver.findElement(By.id("photoBtn")).click();
		String img = isWindows() ? WIN_SAMPLE_IMG : MAC_SAMPLE_IMG;
		driver.findElement(By.id("camera")).sendKeys(img);
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("Flower");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Some nice flowers");
		driver.findElement(By.id("uploadBtn")).click();
		Thread.sleep(3000);//sleep for 3 seconds to wait for push
		driver.findElement(By.xpath("//a[@id='recentMediaFrm:recentMediaCarousel:0:mediaLink']")).click();
		assertEquals("Flower", driver.findElement(By.xpath("(//span[@id='title'])[2]")).getText());
		assertEquals("Some nice flowers", driver.findElement(By.id("description")).getText());
	}

	@After
	public void tearDown() throws Exception {
		if( driver != null ){
			driver.quit();
		}
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
 	}
 
	public static boolean isMac() {
 		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("mac") >= 0);
 	}
}
