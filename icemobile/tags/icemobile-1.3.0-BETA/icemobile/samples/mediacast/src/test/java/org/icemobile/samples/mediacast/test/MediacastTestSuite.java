/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icemobile.samples.mediacast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.iphone.IPhoneDriver;
import org.openqa.selenium.safari.SafariDriver;

/*
 * Tests for the Mediacast demo
 */

public class MediacastTestSuite {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	
	private static final String WIN_SAMPLE_IMG = "C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg";
	private static final String MAC_SAMPLE_IMG = "";
	private static final int IMPLICIT_WAIT = 30;
	
	private static final String HOME_PAGE = "/mediacast/mediacast.jsf";
	private static final String TITLE_ID = "title";
	private static final String[] GALLERY_LINK_ID = {
		"small:headerFrm:galleryLink",
		"large:headerFrm:galleryLink"};
	private static final String[] BACK_BTN_ID = {
		"small:headerFrm:backBtn",
		"large:headerFrm:backBtn"
	};
	private static final String[] ADD_PHOTO_BTN_ID = {
		"small:cform:addPhotoBtn",
		"large:body-wrapper:cform:addPhotoBtn"
	};
	private static final String[] CAMERA_ID = {
		"small:cform:camera",
		"large:body-wrapper:cform:camera"
	};
	private static final String UPLOAD_PHOTO_ID[] = {
		"small:cform:uploadPhoto",
		"large:body-wrapper:cform:uploadPhoto"
	};
	private static final String TITLE_INPUT_ID[] = {
		"small:cform:title",
		"large:body-wrapper:cform:title"
	};
	private static final String[] DESC_INPUT_ID = {
		"small:cform:description",
		"large:body-wrapper:cform:description"
	};
	private static final String SAMPLE_TITLE = "Flower";
	private static final String SAMPLE_DESC = "Some nice flowers";
	private static final String[] DONE_BTN_ID = {
		"small:cform:doneBtn",
		"large:body-wrapper:cform:doneBtn"
	};
	private static final String SUCCESS_MSG = "The Media Message was sent successfully.";
	private static final String[] SUCCESS_MSG_XPATH = {
		"//div[@id='small:cform:messagePopup_popup']/h4",
		"//div[@id='large:body-wrapper:cform:messagePopup_popup']/h4"
	};
	private static final String[] CLOSE_POPUP_BTN_ID = {
		"small:cform:closePopup",
		"large:body-wrapper:cform:closePopup"
	};
	private static final String[] FIRST_CAROUSEL_LINK_ID = {
		"small:recentMediaFrm:recentMediaCarousel:0:mediaLink",
		"large:menu-wrapper:recentMediaFrm:recentMedia:0:mediaLink"
	};
	private static final String[] VIEWER_TITLE_XPATH = {
		"//div[@id='small:titleRow']/h3",
		"//div[@id='large:body-wrapper:titleRow']/h3"
	};
	private static final String[] VIEWER_DESC_ID = {
		"small:description",
		"large:body-wrapper:description"
	};
	private static final String SMALL = "small";
	private static final String LARGE = "large";
	private static List<String> views;
	
	static{
		views = new ArrayList<String>();
		views.add(SMALL);
		views.add(LARGE);
	}
	
	@Before
	public void setUp() throws Exception {
		baseUrl = "http://localhost:8080/";
	}
		
	@Test
	public void testFirefoxDesktop() throws Exception{
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		testLargeViewBasicImageFileUpload();
		driver.quit();
	}
	
	@Test //download separate chrome driver http://code.google.com/p/chromedriver/downloads/list
	public void testChromeDesktop() throws Exception{
		System.setProperty("webdriver.chrome.driver", "C:\\work\\lib\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		testLargeViewBasicImageFileUpload();
		driver.quit();
	}
	
	@Test
	public void testIEDesktop() throws Exception{
		driver = new InternetExplorerDriver();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		testLargeViewBasicImageFileUpload();
		driver.quit();
	}
	
	@Test
	public void testSafariDesktop() throws Exception{
		driver = new SafariDriver();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		testLargeViewBasicImageFileUpload();
		driver.quit();
	}
	
	//@Test
	//http://code.google.com/p/selenium/wiki/AndroidDriver
	public void testAndroidPhone() throws Exception{
		driver = new AndroidDriver();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		testSmallViewBasicImageFileUpload();
		driver.quit();
	}
	
	//@Test
	//http://code.google.com/p/selenium/wiki/IPhoneDriver
	//works through the use of an iphone application running on your iphone, ipod touch or iphone simulator.
	public void testIPhone() throws Exception{
		driver = new IPhoneDriver();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		testSmallViewBasicImageFileUpload();
		driver.quit();
	}
	
	private void testBasicImageFileUpload(String view) throws Exception {
		int idx = views.indexOf(view);
		driver.get(baseUrl + HOME_PAGE);
		assertEquals("Mediacast", driver.findElement(By.id(TITLE_ID)).getText());
		driver.findElement(By.id(GALLERY_LINK_ID[idx])).click();
		Thread.sleep(1000);
		assertEquals("Media Gallery", driver.findElement(By.id(TITLE_ID)).getText());
		driver.findElement(By.id(BACK_BTN_ID[idx])).click();
		Thread.sleep(1000);
		driver.findElement(By.id(ADD_PHOTO_BTN_ID[idx])).click();
		String img = isWindows() ? WIN_SAMPLE_IMG : MAC_SAMPLE_IMG;
		driver.findElement(By.id(CAMERA_ID[idx])).sendKeys(img);
		driver.findElement(By.id(UPLOAD_PHOTO_ID[idx])).click();
		Thread.sleep(1000);
		driver.findElement(By.name(TITLE_INPUT_ID[idx])).clear();
		driver.findElement(By.name(TITLE_INPUT_ID[idx])).sendKeys(SAMPLE_TITLE);
		driver.findElement(By.id(DESC_INPUT_ID[idx])).clear();
		driver.findElement(By.id(DESC_INPUT_ID[idx])).sendKeys(SAMPLE_DESC);
		driver.findElement(By.id(DONE_BTN_ID[idx])).click();
		Thread.sleep(1000);
		assertEquals(SUCCESS_MSG, driver.findElement(By.xpath(
				SUCCESS_MSG_XPATH[idx])).getText());
		driver.findElement(By.id(CLOSE_POPUP_BTN_ID[idx])).click();
		Thread.sleep(1000);
		driver.findElement(By.id(FIRST_CAROUSEL_LINK_ID[idx])).click();
		assertEquals("Flower", driver.findElement(By.xpath(
				VIEWER_TITLE_XPATH[idx])).getText());
		assertEquals("Some nice flowers", driver.findElement(
				By.id(VIEWER_DESC_ID[idx])).getText());

	}
	
	private void testLargeViewBasicImageFileUpload() throws Exception {
		testBasicImageFileUpload(LARGE);
	}
	
	private void testSmallViewBasicImageFileUpload() throws Exception {
		testBasicImageFileUpload(SMALL);	
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
