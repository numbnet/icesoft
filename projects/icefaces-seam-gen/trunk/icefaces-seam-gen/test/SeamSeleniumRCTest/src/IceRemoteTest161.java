/*
 * IceRemoteTest161.java
 *
 * Created on September 21, 2007, 2:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

//package com.icesoft.test.client.seam;

import com.thoughtworks.selenium.*;
import org.openqa.selenium.server.*;
import junit.framework.*;
import junit.extensions.*;
import java.util.regex.Pattern;


 public class IceRemoteTest161 extends TestCase{
    
    /** Creates a new instance of IceRemoteTest161 */
    public IceRemoteTest161(String name) {
        super(name);
    }
    
     private Selenium selenium;
     // name of the application to run test on
     private String project ="/iceEar/";
     
    
     public static void main(String[] args){
         junit.textui.TestRunner.run(suite());
     }
     
     public static Test suite(){
         TestSuite tests = new TestSuite();
		tests.addTest(new IceRemoteTest161("testLogin"));
               
		tests.addTest(new IceRemoteTest161("testOfficeSelect"));
		tests.addTest(new IceRemoteTest161("testOfficeSearch")); 
		tests.addTest(new IceRemoteTest161("testOfficeCED"));
               
		tests.addTest(new IceRemoteTest161("testOrderSelect"));
		tests.addTest(new IceRemoteTest161("testOrderSearch"));
		tests.addTest(new IceRemoteTest161("testOrderCED"));
                
                tests.addTest(new IceRemoteTest161("testCustomerCED"));
		tests.addTest(new IceRemoteTest161("testCustomerSelect"));
		tests.addTest(new IceRemoteTest161("testCustomerSearch"));
		
                tests.addTest(new IceRemoteTest161("testPaymentsListSearch"));
		tests.addTest(new IceRemoteTest161("testPaymentsListSelect"));              
                tests.addTest(new IceRemoteTest161("testPaymentsListCED"));
                
             	tests.addTest(new IceRemoteTest161("testEmployeeListSearch"));
                tests.addTest(new IceRemoteTest161("testEmployeeListSelect"));
                tests.addTest(new IceRemoteTest161("testEmployeeListCED"));
                
		tests.addTest(new IceRemoteTest161("testOrderDetailsListSelect"));
		tests.addTest(new IceRemoteTest161("testOrderdetailsListCED"));
                
                tests.addTest(new IceRemoteTest161("testProductListSelect"));
                tests.addTest(new IceRemoteTest161("testProductListSearch"));
                
                tests.addTest(new IceRemoteTest161("testProductLinesListSearch"));
		tests.addTest(new IceRemoteTest161("testProductLinesListSelect"));
                tests.addTest(new IceRemoteTest161("testProdLineAndListCED"));
                 
                
		
                TestSetup wrapper = new TestSetup(tests) {
			public void setUp() throws Exception {
                          //   SeleniumRC.getCurrentInstance().getSelenium().open("http://localhost:8080/iceEar/");
			}

			public void tearDown() throws Exception {
				SeleniumRC.destoryCurrentInstance();
               			}
		};
		return wrapper;
		
     }
     
     public void testOfficeSelect() throws Exception {
       selenium=SeleniumRC.getCurrentInstance().getSelenium();
         selenium.open(project+"home.seam");
		
                selenium.click("OfficesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Offices search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listofficesFormId:officecodedecId:listofficecodeTextId"));
		assertTrue(selenium.isElementPresent("listofficesFormId:statedecId:liststateTextId"));
		assertTrue(selenium.isElementPresent("listofficesFormId:postalcodedecId:listpostalcodeTextId"));
		assertTrue(selenium.isElementPresent("listofficecodeLinkId"));
		assertTrue(selenium.isElementPresent("listaddressline2LinkId"));
		assertTrue(selenium.isElementPresent("listcountryLinkId"));
		selenium.click("listofficecodeLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listofficesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("officesHome".equals(selenium.getText("viewTextofficesHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("1"));
		assertTrue(selenium.isTextPresent("San Francisco"));
		assertTrue(selenium.isTextPresent("+1 650 219 4782"));
		assertTrue(selenium.isTextPresent("100 Market Street"));
		assertTrue(selenium.isTextPresent("Suite 300"));
		assertTrue(selenium.isTextPresent("CA"));
		assertTrue(selenium.isTextPresent("USA"));
		assertTrue(selenium.isTextPresent("94080"));
		assertTrue(selenium.isTextPresent("NA"));
		selenium.click("viewDoneOfficesEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Offices search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listterritoryLinkId");
		selenium.waitForPageToLoad("30000");
      
	}
     

public void testLogin() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
		selenium.click("menuLoginId");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("loginFormId:username"));
		assertTrue(selenium.isElementPresent("loginFormId:password"));
		selenium.type("loginFormId:username", "mandeep");
		selenium.fireEvent("loginFormId:username", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("mandeep".equals(selenium.getValue("loginFormId:username"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("loginFormId:password", "hello");
		selenium.fireEvent("loginFormId:password", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("hello".equals(selenium.getValue("loginFormId:password"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("loginFormId:loginButtonId");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Welcome, mandeep")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

    
}

public void testOfficeSearch() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
		selenium.click("OfficesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Offices search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listofficesFormId:officecodedecId:listofficecodeTextId"));
		assertTrue(selenium.isElementPresent("listofficesFormId:citydecId:listcityTextId"));
		assertTrue(selenium.isElementPresent("listofficesFormId:phonedecId:listphoneTextId"));
		assertTrue(selenium.isElementPresent("listofficesFormId:addressline1decId:listaddressline1TextId"));
		assertTrue(selenium.isElementPresent("listofficesFormId:addressline2decId:listaddressline2TextId"));
		assertTrue(selenium.isElementPresent("listofficesFormId:statedecId:liststateTextId"));
		assertTrue(selenium.isElementPresent("listofficesFormId:countrydecId:listcountryTextId"));
		assertTrue(selenium.isElementPresent("listofficesFormId:postalcodedecId:listpostalcodeTextId"));
		assertTrue(selenium.isElementPresent("listofficesFormId:territorydecId:listterritoryTextId"));
		assertTrue(selenium.isElementPresent("listofficesFormId:listSearchButtonId"));
		assertTrue(selenium.isElementPresent("listofficecodeLinkId"));
		assertTrue(selenium.isElementPresent("listcityLinkId"));
		assertTrue(selenium.isElementPresent("listphoneLinkId"));
		assertTrue(selenium.isElementPresent("listaddressline1LinkId"));
		assertTrue(selenium.isElementPresent("listaddressline2LinkId"));
		assertTrue(selenium.isElementPresent("liststateLinkId"));
		assertTrue(selenium.isElementPresent("listcountryLinkId"));
		assertTrue(selenium.isElementPresent("listpostalcodeLinkId"));
		assertTrue(selenium.isElementPresent("listterritoryLinkId"));
		assertTrue(selenium.isElementPresent("listofficesLinkId"));
		selenium.click("listofficecodeLinkId");
                
		selenium.waitForPageToLoad("30000");
		

		assertTrue(selenium.isTextPresent("Sydney"));
		assertTrue(selenium.isTextPresent("Chiyoda-Ku"));
		assertTrue(selenium.isTextPresent("NSW 2010"));
		selenium.type("listofficesFormId:addressline1decId:listaddressline1TextId", "---------------");
		selenium.fireEvent("listofficesFormId:addressline1decId:listaddressline1TextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("No offices exists")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("listofficesFormId:addressline1decId:listaddressline1TextId", "4-1 Kioicho");
		selenium.fireEvent("listofficesFormId:addressline1decId:listaddressline1TextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("4-1 Kioicho")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("Chiyoda-Ku"));
		assertTrue(selenium.isTextPresent("Tokyo"));
		assertTrue(selenium.isTextPresent("102-8578"));
		selenium.type("listofficesFormId:addressline1decId:listaddressline1TextId", "---------");
		selenium.fireEvent("listofficesFormId:addressline1decId:listaddressline1TextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("No offices exists")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.fireEvent("listofficesFormId:addressline1decId:listaddressline1TextId", "blur");
		selenium.type("listofficesFormId:addressline1decId:listaddressline1TextId", "");
		selenium.fireEvent("listofficesFormId:addressline1decId:listaddressline1TextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("100 Market Street")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("liststateLinkId");
		selenium.waitForPageToLoad("30000");
		

}


public void testOfficeCED() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
		selenium.click("OfficesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Offices search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listCreateofficesId");
		selenium.waitForPageToLoad("30000");
		selenium.type("officeseditForm:officecodeDecoration:officecodeId", "Testing");
		selenium.fireEvent("officeseditForm:officecodeDecoration:officecodeId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("officeseditForm:officecodeDecoration:officecodeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("officeseditForm:cityDecoration:cityId", "Testing");
		selenium.fireEvent("officeseditForm:cityDecoration:cityId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("officeseditForm:cityDecoration:cityId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("officeseditForm:phoneDecoration:phoneId", "Testing");
		selenium.fireEvent("officeseditForm:phoneDecoration:phoneId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("officeseditForm:phoneDecoration:phoneId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("officeseditForm:addressline1Decoration:addressline1Id", "Testing");
		selenium.fireEvent("officeseditForm:addressline1Decoration:addressline1Id", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("officeseditForm:addressline1Decoration:addressline1Id"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("officeseditForm:addressline2Decoration:addressline2Id", "Testing");
		selenium.fireEvent("officeseditForm:addressline2Decoration:addressline2Id", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("officeseditForm:addressline2Decoration:addressline2Id"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("officeseditForm:stateDecoration:stateId", "Testing");
		selenium.fireEvent("officeseditForm:stateDecoration:stateId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("officeseditForm:stateDecoration:stateId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("officeseditForm:countryDecoration:countryId", "Testing");
		selenium.fireEvent("officeseditForm:countryDecoration:countryId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("officeseditForm:countryDecoration:countryId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("officeseditForm:postalcodeDecoration:postalcodeId", "Testing");
		selenium.fireEvent("officeseditForm:postalcodeDecoration:postalcodeId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("officeseditForm:postalcodeDecoration:postalcodeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("officeseditForm:territoryDecoration:territoryId", "Testing");
		selenium.fireEvent("officeseditForm:territoryDecoration:territoryId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("officeseditForm:territoryDecoration:territoryId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("officeseditForm:saveofficesHome");
		Thread.sleep(800);
		Thread.sleep(800);
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully created")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("Testing"));
		selenium.click("viewEditOfficesEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Edit offices")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("officeseditForm:cityDecoration:cityId", "Testing again");
		selenium.fireEvent("officeseditForm:cityDecoration:cityId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing again".equals(selenium.getValue("officeseditForm:cityDecoration:cityId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("doneofficesHome");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("officesHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("Testing again"));
		selenium.click("viewEditOfficesEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit offices".equals(selenium.getText("officeseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("officeseditForm:deleteofficesHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully deleted")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertEquals("Offices search", selenium.getText("listofficesFormId:listOfficesId"));
		selenium.type("listofficesFormId:officecodedecId:listofficecodeTextId", "Testing");
		selenium.fireEvent("listofficesFormId:officecodedecId:listofficecodeTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("No offices exists")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("listofficesFormId:officecodedecId:listofficecodeTextId", "");
		selenium.fireEvent("listofficesFormId:officecodedecId:listofficecodeTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Sydney")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}


}


public void testOrderSelect() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
      selenium.open(project+"home.seam");
		selenium.click("OrdersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Orders search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listordernumberLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listordersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("ordersHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("10100"));
		assertTrue(selenium.isTextPresent("1/6/03"));
		assertTrue(selenium.isTextPresent("1/13/03"));
		assertTrue(selenium.isTextPresent("1/10/03"));
		assertTrue(selenium.isTextPresent("Shipped"));
		assertEquals("Edit", selenium.getValue("viewEditOrdersEdit"));
		assertEquals("Done", selenium.getValue("viewDoneOrdersEdit"));
		assertTrue(selenium.isTextPresent("363"));
		assertTrue(selenium.isTextPresent("Young"));
		selenium.click("viewcustomersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("customersHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("363"));
		assertTrue(selenium.isTextPresent("Online Diecast Creations Co."));
		assertTrue(selenium.isTextPresent("Young"));
		assertTrue(selenium.isTextPresent("Dorothy"));
		assertTrue(selenium.isTextPresent("6035558647"));
		assertTrue(selenium.isTextPresent("2304 Long Airport Avenue"));
		assertTrue(selenium.isTextPresent("Nashua"));
		assertTrue(selenium.isTextPresent("NH"));
		assertTrue(selenium.isTextPresent("62005"));
		assertTrue(selenium.isTextPresent("USA"));
		assertTrue(selenium.isTextPresent("114200.0"));
		assertTrue(selenium.isTextPresent("1216"));
		assertTrue(selenium.isTextPresent("spatterson@classicmodelcars.com"));
		selenium.click("viewemployeesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("employeesHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("employeesHome"));
		assertTrue(selenium.isTextPresent("spatterson@classicmodelcars.com"));
		assertTrue(selenium.isTextPresent("Gifts4AllAges.com"));
		assertTrue(selenium.isTextPresent("Taylor"));
		assertTrue(selenium.isTextPresent("Marta"));
		selenium.click("OrdersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Orders search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listcommentsLinkId");
		selenium.waitForPageToLoad("30000");
}

public void testOrderSearch() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
	selenium.click("OrdersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Orders search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listordersFormId:statusdecId:liststatusTextId"));
		assertTrue(selenium.isElementPresent("listordersFormId:commentsdecId:listcommentsTextId"));
		assertTrue(selenium.isElementPresent("listordersFormId:listSearchButtonId"));
		assertTrue(selenium.isElementPresent("listordernumberLinkId"));
		assertTrue(selenium.isElementPresent("listcolumnHeadercustomersId"));
		assertTrue(selenium.isElementPresent("listorderdateLinkId"));
		assertTrue(selenium.isElementPresent("listrequireddateLinkId"));
		assertTrue(selenium.isElementPresent("listshippeddateLinkId"));
		assertTrue(selenium.isElementPresent("liststatusLinkId"));
		assertTrue(selenium.isElementPresent("listcommentsLinkId"));
		assertTrue(selenium.isElementPresent("listordersLinkId"));
		selenium.click("listordernumberLinkId");
		selenium.waitForPageToLoad("30000");
		

		selenium.type("listordersFormId:statusdecId:liststatusTextId", "in process");
		selenium.fireEvent("listordersFormId:statusdecId:liststatusTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("10420")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("10421"));
		assertTrue(selenium.isTextPresent("10422"));
		assertTrue(selenium.isTextPresent("10423"));
		assertTrue(selenium.isTextPresent("10424"));
		assertTrue(selenium.isTextPresent("10425"));
		assertTrue(selenium.isTextPresent("In Process"));
		selenium.type("listordersFormId:statusdecId:liststatusTextId", "");
		selenium.fireEvent("listordersFormId:statusdecId:liststatusTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Shipped")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listcommentsLinkId");
		selenium.waitForPageToLoad("30000");
		
}

public void testOrderCED() throws Exception
{ 
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
		selenium.click("OrdersId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listCreateordersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Edit orders")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("orderseditForm:ordernumberDecoration:ordernumberId"));
		assertTrue(selenium.isElementPresent("orderseditForm:orderdateDecoration:orderdateId_calendarInputtext"));
		assertTrue(selenium.isElementPresent("orderseditForm:orderdateDecoration:orderdateId_calendarButton"));
		assertTrue(selenium.isElementPresent("orderseditForm:requireddateDecoration:requireddateId_calendarInputtext"));
		assertTrue(selenium.isElementPresent("orderseditForm:requireddateDecoration:requireddateId_calendarButton"));
		assertTrue(selenium.isElementPresent("orderseditForm:shippeddateDecoration:shippeddateId_calendarInputtext"));
		assertTrue(selenium.isElementPresent("orderseditForm:shippeddateDecoration:shippeddateId_calendarButton"));
		assertTrue(selenium.isElementPresent("orderseditForm:statusDecoration:statusId"));
		assertTrue(selenium.isElementPresent("orderseditForm:commentsDecoration:comments"));
		selenium.click("editSelectButtoncustomersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Customers search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("listcustomersFormId:contactlastnamedecId:listcontactlastnameTextId", "0000000");
		selenium.fireEvent("listcustomersFormId:contactlastnamedecId:listcontactlastnameTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("No customers exists")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("listcustomersFormId:contactlastnamedecId:listcontactlastnameTextId", "king");
		selenium.fireEvent("listcustomersFormId:contactlastnamedecId:listcontactlastnameTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Signal Gift Stores")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listcustomernumberLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listcustomersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit orders".equals(selenium.getText("orderseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("orderseditForm:ordernumberDecoration:ordernumberId", "9999");
		selenium.fireEvent("orderseditForm:ordernumberDecoration:ordernumberId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("9999".equals(selenium.getValue("orderseditForm:ordernumberDecoration:ordernumberId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("orderseditForm:orderdateDecoration:orderdateId_calendarInputtext", "08/02/2007");
		selenium.fireEvent("orderseditForm:orderdateDecoration:orderdateId_calendarInputtext", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("08/02/2007".equals(selenium.getValue("orderseditForm:orderdateDecoration:orderdateId_calendarInputtext"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("orderseditForm:requireddateDecoration:requireddateId_calendarInputtext", "08/02/2007");
		selenium.fireEvent("orderseditForm:requireddateDecoration:requireddateId_calendarInputtext", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("08/02/2007".equals(selenium.getValue("orderseditForm:requireddateDecoration:requireddateId_calendarInputtext"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("orderseditForm:shippeddateDecoration:shippeddateId_calendarInputtext", "08/02/2007");
		selenium.fireEvent("orderseditForm:shippeddateDecoration:shippeddateId_calendarInputtext", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("08/02/2007".equals(selenium.getValue("orderseditForm:shippeddateDecoration:shippeddateId_calendarInputtext"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("orderseditForm:statusDecoration:statusId", "shipped");
		selenium.fireEvent("orderseditForm:statusDecoration:statusId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("shipped".equals(selenium.getValue("orderseditForm:statusDecoration:statusId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("orderseditForm:commentsDecoration:comments", "Testing");
		selenium.fireEvent("orderseditForm:commentsDecoration:comments", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("orderseditForm:commentsDecoration:comments"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("orderseditForm:saveordersHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully created")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewEditOrdersEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Edit orders")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("orderseditForm:commentsDecoration:comments", "Testing again");
		selenium.fireEvent("orderseditForm:commentsDecoration:comments", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing again".equals(selenium.getValue("orderseditForm:commentsDecoration:comments"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("orderseditForm:updateordersHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully updated")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("OrdersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Orders search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("listordersFormId:commentsdecId:listcommentsTextId", "testing again");
		selenium.fireEvent("listordersFormId:commentsdecId:listcommentsTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("9999")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listordersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("ordersHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("Testing again"));
		selenium.click("viewEditOrdersEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Edit orders")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertEquals("Testing again", selenium.getText("orderseditForm:commentsDecoration:comments"));
		selenium.click("orderseditForm:deleteordersHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully deleted")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("OrdersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Orders search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("listordersFormId:commentsdecId:listcommentsTextId", "testing again");
		selenium.fireEvent("listordersFormId:commentsdecId:listcommentsTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("No orders exists")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("No orders exists"));
		selenium.type("listordersFormId:commentsdecId:listcommentsTextId", "");
		selenium.fireEvent("listordersFormId:commentsdecId:listcommentsTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("".equals(selenium.getValue("listordersFormId:commentsdecId:listcommentsTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

}

public void testCustomerSelect() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
   
		selenium.click("CustomersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Customers search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listcustomersFormId:customernamedecId:listcustomernameTextId"));
		assertTrue(selenium.isElementPresent("listcustomersFormId:countrydecId:listcountryTextId"));
		assertTrue(selenium.isElementPresent("listcustomersFormId:statedecId:liststateTextId"));
		assertTrue(selenium.isElementPresent("listcustomersFormId:phonedecId:listphoneTextId"));
		assertTrue(selenium.isElementPresent("listcustomernumberLinkId"));
		assertTrue(selenium.isElementPresent("listcolumnHeaderemployeesId"));
		assertTrue(selenium.isElementPresent("listcustomernameLinkId"));
		assertTrue(selenium.isElementPresent("listcontactlastnameLinkId"));
		assertTrue(selenium.isElementPresent("listcontactfirstnameLinkId"));
		assertTrue(selenium.isElementPresent("listphoneLinkId"));
		assertTrue(selenium.isElementPresent("listaddressline1LinkId"));
		assertTrue(selenium.isElementPresent("listaddressline2LinkId"));
		assertTrue(selenium.isElementPresent("listcityLinkId"));
		assertTrue(selenium.isElementPresent("liststateLinkId"));
		assertTrue(selenium.isElementPresent("listpostalcodeLinkId"));
		assertTrue(selenium.isElementPresent("listcountryLinkId"));
		assertTrue(selenium.isElementPresent("listcreditlimitLinkId"));
		assertTrue(selenium.isElementPresent("listcustomersLinkId"));
		selenium.click("listcustomernumberLinkId");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("103"));
		assertTrue(selenium.isTextPresent("54, rue Royale"));
		selenium.click("listcustomersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("customersHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("103"));
		assertTrue(selenium.isTextPresent("Atelier graphique"));
		assertTrue(selenium.isTextPresent("Schmitt"));
		assertTrue(selenium.isTextPresent("Carine"));
		assertTrue(selenium.isTextPresent("40.32.2555"));
		assertTrue(selenium.isTextPresent("54, rue Royale"));
		assertTrue(selenium.isTextPresent("Nantes"));
		assertTrue(selenium.isTextPresent("44000"));
		assertTrue(selenium.isTextPresent("France"));
		assertTrue(selenium.isTextPresent("21000.0"));
		assertTrue(selenium.isElementPresent("viewDoneCustomersEdit"));
		assertTrue(selenium.isTextPresent("1370"));
		assertTrue(selenium.isTextPresent("employeenumber"));
		assertTrue(selenium.isTextPresent("ghernande@classicmodelcars.com"));
		selenium.click("viewemployeesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("employeesHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("ghernande@classicmodelcars.com"));
		selenium.click("CustomersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Customers search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listaddressline2LinkId");
		selenium.waitForPageToLoad("30000"); 
}

public void testCustomerSearch() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
  
		selenium.click("CustomersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Customers search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listcustomersFormId:contactlastnamedecId:listcontactlastnameTextId"));
		selenium.type("listcustomersFormId:contactlastnamedecId:listcontactlastnameTextId", "Tseng");
		selenium.fireEvent("listcustomersFormId:contactlastnamedecId:listcontactlastnameTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Tseng".equals(selenium.getValue("listcustomersFormId:contactlastnamedecId:listcontactlastnameTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Cambridge Collectables Co.")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("6175555555"));
		assertTrue(selenium.isTextPresent("4658 Baden Av."));
		assertTrue(selenium.isTextPresent("Cambridge"));
		selenium.type("listcustomersFormId:contactlastnamedecId:listcontactlastnameTextId", "");
		selenium.fireEvent("listcustomersFormId:contactlastnamedecId:listcontactlastnameTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("".equals(selenium.getValue("listcustomersFormId:contactlastnamedecId:listcontactlastnameTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Schmitt")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

}

public void testCustomerCED() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    selenium.click("CustomersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Customers search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listCreatecustomersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Edit customers")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("customerseditForm:customernumberDecoration:customernumberId"));
		selenium.type("customerseditForm:customernumberDecoration:customernumberId", "91919191");
		selenium.fireEvent("customerseditForm:customernumberDecoration:customernumberId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("91919191".equals(selenium.getValue("customerseditForm:customernumberDecoration:customernumberId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("customerseditForm:customernameDecoration:customernameId", "Testing");
		selenium.fireEvent("customerseditForm:customernameDecoration:customernameId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("customerseditForm:customernameDecoration:customernameId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("customerseditForm:contactlastnameDecoration:contactlastnameId"));
		selenium.type("customerseditForm:contactlastnameDecoration:contactlastnameId", "Testing");
		selenium.fireEvent("customerseditForm:contactlastnameDecoration:contactlastnameId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("customerseditForm:contactlastnameDecoration:contactlastnameId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("customerseditForm:contactfirstnameDecoration:contactfirstnameId"));
		selenium.type("customerseditForm:contactfirstnameDecoration:contactfirstnameId", "Testing");
		selenium.fireEvent("customerseditForm:contactfirstnameDecoration:contactfirstnameId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("customerseditForm:contactfirstnameDecoration:contactfirstnameId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("customerseditForm:phoneDecoration:phoneId", "000-0000");
		selenium.fireEvent("customerseditForm:phoneDecoration:phoneId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("000-0000".equals(selenium.getValue("customerseditForm:phoneDecoration:phoneId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("customerseditForm:addressline1Decoration:addressline1Id", "Testing");
		selenium.fireEvent("customerseditForm:addressline1Decoration:addressline1Id", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("customerseditForm:addressline1Decoration:addressline1Id"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("customerseditForm:addressline2Decoration:addressline2Id", "Testing");
		selenium.fireEvent("customerseditForm:addressline2Decoration:addressline2Id", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("customerseditForm:addressline2Decoration:addressline2Id"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("customerseditForm:cityDecoration:cityId", "Testing");
		selenium.fireEvent("customerseditForm:cityDecoration:cityId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("customerseditForm:cityDecoration:cityId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("customerseditForm:stateDecoration:stateId", "Testing");
		selenium.fireEvent("customerseditForm:stateDecoration:stateId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("customerseditForm:stateDecoration:stateId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("customerseditForm:postalcodeDecoration:postalcodeId", "Testing");
		selenium.fireEvent("customerseditForm:postalcodeDecoration:postalcodeId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("customerseditForm:postalcodeDecoration:postalcodeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("customerseditForm:countryDecoration:countryId", "Testing");
		selenium.fireEvent("customerseditForm:countryDecoration:countryId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("customerseditForm:countryDecoration:countryId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("customerseditForm:savecustomersHome");
		selenium.waitForPageToLoad("30000");
		Thread.sleep(700);
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully created")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("91919191"));
		assertTrue(selenium.isTextPresent("Testing"));
		assertTrue(selenium.isTextPresent("000-0000"));
		selenium.click("viewEditCustomersEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit customers".equals(selenium.getText("customerseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertEquals("000-0000", selenium.getValue("customerseditForm:phoneDecoration:phoneId"));
		selenium.type("customerseditForm:phoneDecoration:phoneId", "999-9999");
		selenium.fireEvent("customerseditForm:phoneDecoration:phoneId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("999-9999".equals(selenium.getValue("customerseditForm:phoneDecoration:phoneId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("customerseditForm:updatecustomersHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully updated")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("91919191"));
		assertTrue(selenium.isTextPresent("999-9999"));
		assertTrue(selenium.isTextPresent("Testing"));
		selenium.click("viewEditCustomersEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit customers".equals(selenium.getText("customerseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertEquals("999-9999", selenium.getValue("customerseditForm:phoneDecoration:phoneId"));
		selenium.click("customerseditForm:deletecustomersHome");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully deleted")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listcustomersFormId:customernamedecId:listcustomernameTextId"));
		selenium.type("listcustomersFormId:customernamedecId:listcustomernameTextId", "Testing");
		selenium.fireEvent("listcustomersFormId:customernamedecId:listcustomernameTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Testing".equals(selenium.getValue("listcustomersFormId:customernamedecId:listcustomernameTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("No customers exists")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("listcustomersFormId:customernamedecId:listcustomernameTextId", "");
		selenium.fireEvent("listcustomersFormId:customernamedecId:listcustomernameTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("".equals(selenium.getValue("listcustomersFormId:customernamedecId:listcustomernameTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
}


public void testPaymentsListSelect() throws Exception
{
        selenium=SeleniumRC.getCurrentInstance().getSelenium();
        selenium.open(project+"home.seam");
		selenium.click("PaymentsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Payments search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listpaymentsFormId:checknumberdecId:listchecknumberTextId"));
		assertTrue(selenium.isElementPresent("listpaymentsFormId:listSearchButtonId"));
		assertTrue(selenium.isElementPresent("listpaymentnumberLinkId"));
		assertTrue(selenium.isElementPresent("listcolumnHeadercustomersId"));
		assertTrue(selenium.isElementPresent("listchecknumberLinkId"));
		assertTrue(selenium.isElementPresent("listpaymentdateLinkId"));
		assertTrue(selenium.isElementPresent("listamountLinkId"));
		assertTrue(selenium.isElementPresent("listpaymentsLinkId"));
		selenium.click("listpaymentnumberLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listpaymentsLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("paymentsHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("0"));
		assertTrue(selenium.isTextPresent("HQ336336"));
		assertTrue(selenium.isTextPresent("10/19/04"));
		assertTrue(selenium.isTextPresent("6066.78"));
		assertTrue(selenium.isElementPresent("viewDonePaymentsEdit"));
		assertTrue(selenium.isElementPresent("viewEditPaymentsEdit"));
		assertTrue(selenium.isTextPresent("Schmitt"));
		selenium.click("viewcustomersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("customersHome".equals(selenium.getText("viewTextcustomersHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("Atelier graphique"));
		assertTrue(selenium.isTextPresent("Schmitt"));
		assertTrue(selenium.isTextPresent("Carine"));
		assertTrue(selenium.isTextPresent("54, rue Royale"));
		assertTrue(selenium.isTextPresent("Nantes"));
		selenium.click("viewemployeesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("employeesHome".equals(selenium.getText("viewTextemployeesHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("ghernande@classicmodelcars.com"));
		assertTrue(selenium.isTextPresent("Hernandez"));
		selenium.click("PaymentsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Payments search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listamountLinkId");
		selenium.waitForPageToLoad("30000");
}        

public void testPaymentsListSearch() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
		selenium.click("PaymentsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Payments search".equals(selenium.getText("listpaymentsFormId:listPaymentsId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listpaymentsFormId:checknumberdecId:listchecknumberTextId"));
		selenium.type("listpaymentsFormId:checknumberdecId:listchecknumberTextId", "HQ336336");
		selenium.fireEvent("listpaymentsFormId:checknumberdecId:listchecknumberTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("HQ336336".equals(selenium.getValue("listpaymentsFormId:checknumberdecId:listchecknumberTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
                Thread.sleep(1000);
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (!selenium.isElementPresent("nextPagepaymentsListId")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("6066.78"));
		assertTrue(selenium.isTextPresent("103"));
		selenium.type("listpaymentsFormId:checknumberdecId:listchecknumberTextId", "");
		selenium.fireEvent("listpaymentsFormId:checknumberdecId:listchecknumberTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("".equals(selenium.getValue("listpaymentsFormId:checknumberdecId:listchecknumberTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("nextPagepaymentsListId")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}


}

public void testPaymentsListCED() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
		selenium.click("PaymentsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Payments search".equals(selenium.getText("listpaymentsFormId:listPaymentsId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listCreatepaymentsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit payments".equals(selenium.getText("paymentseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("editSelectButtoncustomersId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Customers search".equals(selenium.getText("listcustomersFormId:listCustomersId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("nextPagecustomersListId"));
		selenium.type("listcustomersFormId:customernamedecId:listcustomernameTextId", "Atelier graphique");
		selenium.fireEvent("listcustomersFormId:customernamedecId:listcustomernameTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (!selenium.isElementPresent("nextPagecustomersListId")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("Schmitt"));
		assertTrue(selenium.isTextPresent("Nantes"));
		selenium.click("listcustomersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit payments".equals(selenium.getText("paymentseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("paymentseditForm:paymentnumberDecoration:paymentnumberId", "999999");
		selenium.fireEvent("paymentseditForm:paymentnumberDecoration:paymentnumberId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("999999".equals(selenium.getValue("paymentseditForm:paymentnumberDecoration:paymentnumberId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("paymentseditForm:checknumberDecoration:checknumberId", "999999");
		selenium.fireEvent("paymentseditForm:checknumberDecoration:checknumberId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("999999".equals(selenium.getValue("paymentseditForm:checknumberDecoration:checknumberId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("paymentseditForm:paymentdateDecoration:paymentdateId_calendarInputtext", "08/08/2007");
		selenium.fireEvent("paymentseditForm:paymentdateDecoration:paymentdateId_calendarInputtext", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("08/08/2007".equals(selenium.getValue("paymentseditForm:paymentdateDecoration:paymentdateId_calendarInputtext"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("paymentseditForm:amountDecoration:amountId", "99.99");
		selenium.fireEvent("paymentseditForm:amountDecoration:amountId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("99.99".equals(selenium.getValue("paymentseditForm:amountDecoration:amountId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("paymentseditForm:savepaymentsHome"));
		selenium.click("paymentseditForm:savepaymentsHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully created")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewEditPaymentsEdit");
		selenium.waitForPageToLoad("30000");
		selenium.type("paymentseditForm:checknumberDecoration:checknumberId", "000000");
		selenium.fireEvent("paymentseditForm:checknumberDecoration:checknumberId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("000000".equals(selenium.getValue("paymentseditForm:checknumberDecoration:checknumberId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("paymentseditForm:updatepaymentsHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully updated")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewEditPaymentsEdit");
		selenium.waitForPageToLoad("30000");
		selenium.click("paymentseditForm:deletepaymentsHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully deleted")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("listpaymentsFormId:checknumberdecId:listchecknumberTextId", "000000");
		selenium.fireEvent("listpaymentsFormId:checknumberdecId:listchecknumberTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("No payments exists")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("listpaymentsFormId:checknumberdecId:listchecknumberTextId", "");
		selenium.fireEvent("listpaymentsFormId:checknumberdecId:listchecknumberTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("nextPagepaymentsListId")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
}


public void testEmployeeListSelect() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
		selenium.click("EmployeesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Employees search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listemployeesFormId:lastnamedecId:listlastnameTextId"));
		assertTrue(selenium.isElementPresent("listemployeesFormId:firstnamedecId:listfirstnameTextId"));
		assertTrue(selenium.isElementPresent("listemployeesFormId:extensiondecId:listextensionTextId"));
		assertTrue(selenium.isElementPresent("listemployeesFormId:emaildecId:listemailTextId"));
		assertTrue(selenium.isElementPresent("listemployeesFormId:officecodedecId:listofficecodeTextId"));
		assertTrue(selenium.isElementPresent("listemployeesFormId:jobtitledecId:listjobtitleTextId"));
		assertTrue(selenium.isElementPresent("listemployeesFormId:listSearchButtonId"));
		assertTrue(selenium.isElementPresent("listemployeenumberLinkId"));
		assertTrue(selenium.isElementPresent("listlastnameLinkId"));
		assertTrue(selenium.isElementPresent("listfirstnameLinkId"));
		assertTrue(selenium.isElementPresent("listextensionLinkId"));
		assertTrue(selenium.isElementPresent("listemailLinkId"));
		assertTrue(selenium.isElementPresent("listofficecodeLinkId"));
		assertTrue(selenium.isElementPresent("listreportstoLinkId"));
		assertTrue(selenium.isElementPresent("listjobtitleLinkId"));
		assertTrue(selenium.isElementPresent("listemployeesLinkId"));
		selenium.click("listemployeenumberLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listemployeesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("employeesHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("1002"));
		assertTrue(selenium.isTextPresent("Murphy"));
		assertTrue(selenium.isTextPresent("Diane"));
		assertTrue(selenium.isTextPresent("x5800"));
		assertTrue(selenium.isTextPresent("dmurphy@classicmodelcars.com"));
		assertTrue(selenium.isTextPresent("1"));
		assertTrue(selenium.isTextPresent("President"));
		assertTrue(selenium.isElementPresent("viewDoneEmployeesEdit"));
		assertTrue(selenium.isElementPresent("viewEditEmployeesEdit"));
		selenium.click("viewDoneEmployeesEdit");
		selenium.waitForPageToLoad("30000");
		selenium.click("listemployeenumberLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listemployeenumberLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listemployeesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("employeesHome".equals(selenium.getText("viewTextemployeesHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("mgerard@classicmodelcars.com"));
		assertTrue(selenium.isTextPresent("Sales Rep"));
		assertTrue(selenium.isTextPresent("Gerard"));
		selenium.click("selectcustomersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("customersHome".equals(selenium.getText("viewTextcustomersHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("paymentses"));
		assertTrue(selenium.isTextPresent("orderses"));
		selenium.click("viewemployeesLinkId");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("employeesHome"));
		selenium.click("EmployeesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Employees search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listextensionLinkId");
		selenium.waitForPageToLoad("30000");
}


public void testEmployeeListSearch() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    selenium.click("EmployeesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Employees search".equals(selenium.getText("listemployeesFormId:listEmployeesId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listemployeesFormId:firstnamedecId:listfirstnameTextId"));
		selenium.type("listemployeesFormId:firstnamedecId:listfirstnameTextId", "Diane");
		selenium.fireEvent("listemployeesFormId:firstnamedecId:listfirstnameTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Diane".equals(selenium.getValue("listemployeesFormId:firstnamedecId:listfirstnameTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("dmurphy@classicmodelcars.com"));
		assertTrue(selenium.isTextPresent("President"));
		selenium.click("listemployeesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("employeesHome".equals(selenium.getText("viewTextemployeesHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("dmurphy@classicmodelcars.com"));
		assertTrue(selenium.isTextPresent("x5800"));
		assertTrue(selenium.isTextPresent("President"));
		selenium.click("viewDoneEmployeesEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Employees search".equals(selenium.getText("listemployeesFormId:listEmployeesId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("listemployeesFormId:firstnamedecId:listfirstnameTextId", "");
		selenium.fireEvent("listemployeesFormId:firstnamedecId:listfirstnameTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("".equals(selenium.getValue("listemployeesFormId:firstnamedecId:listfirstnameTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("ftseng@classicmodelcars.com")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("gvanauf@classicmodelcars.com"));
                
}

public void testEmployeeListCED() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
		selenium.click("EmployeesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Employees search".equals(selenium.getText("listemployeesFormId:listEmployeesId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listCreateemployeesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit employees".equals(selenium.getText("employeeseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("employeeseditForm:employeenumberDecoration:employeenumberId"));
		selenium.type("employeeseditForm:employeenumberDecoration:employeenumberId", "999999");
		selenium.fireEvent("employeeseditForm:employeenumberDecoration:employeenumberId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("999999".equals(selenium.getValue("employeeseditForm:employeenumberDecoration:employeenumberId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("employeeseditForm:lastnameDecoration:lastnameId"));
		selenium.type("employeeseditForm:lastnameDecoration:lastnameId", "test");
		selenium.fireEvent("employeeseditForm:lastnameDecoration:lastnameId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test".equals(selenium.getValue("employeeseditForm:lastnameDecoration:lastnameId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("employeeseditForm:firstnameDecoration:firstnameId"));
		selenium.type("employeeseditForm:firstnameDecoration:firstnameId", "test");
		selenium.fireEvent("employeeseditForm:firstnameDecoration:firstnameId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test".equals(selenium.getValue("employeeseditForm:firstnameDecoration:firstnameId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("employeeseditForm:extensionDecoration:extensionId"));
		selenium.type("employeeseditForm:extensionDecoration:extensionId", "999");
		selenium.fireEvent("employeeseditForm:extensionDecoration:extensionId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("999".equals(selenium.getValue("employeeseditForm:extensionDecoration:extensionId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("employeeseditForm:emailDecoration:emailId"));
		selenium.type("employeeseditForm:emailDecoration:emailId", "test@test.com");
		selenium.fireEvent("employeeseditForm:emailDecoration:emailId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test@test.com".equals(selenium.getValue("employeeseditForm:emailDecoration:emailId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("employeeseditForm:officecodeDecoration:officecodeId"));
		selenium.type("employeeseditForm:officecodeDecoration:officecodeId", "9999");
		selenium.fireEvent("employeeseditForm:officecodeDecoration:officecodeId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("9999".equals(selenium.getValue("employeeseditForm:officecodeDecoration:officecodeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("employeeseditForm:reportstoDecoration:reportstoId"));
		selenium.type("employeeseditForm:reportstoDecoration:reportstoId", "00000");
		selenium.fireEvent("employeeseditForm:reportstoDecoration:reportstoId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("0".equals(selenium.getValue("employeeseditForm:reportstoDecoration:reportstoId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("employeeseditForm:jobtitleDecoration:jobtitleId"));
		selenium.type("employeeseditForm:jobtitleDecoration:jobtitleId", "test");
		selenium.fireEvent("employeeseditForm:jobtitleDecoration:jobtitleId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test".equals(selenium.getValue("employeeseditForm:jobtitleDecoration:jobtitleId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("employeeseditForm:saveemployeesHome"));
		selenium.click("employeeseditForm:saveemployeesHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully created")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("999999"));
		assertTrue(selenium.isTextPresent("test@test.com"));
		assertTrue(selenium.isElementPresent("viewEditEmployeesEdit"));
		selenium.click("viewEditEmployeesEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit employees".equals(selenium.getText("employeeseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("employeeseditForm:reportstoDecoration:reportstoId"));
		selenium.type("employeeseditForm:reportstoDecoration:reportstoId", "888888");
		selenium.fireEvent("employeeseditForm:reportstoDecoration:reportstoId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("888888".equals(selenium.getValue("employeeseditForm:reportstoDecoration:reportstoId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("employeeseditForm:updateemployeesHome"));
		selenium.click("employeeseditForm:updateemployeesHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully updated")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("viewEditEmployeesEdit"));
		selenium.click("viewEditEmployeesEdit");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("employeeseditForm:deleteemployeesHome"));
		selenium.click("employeeseditForm:deleteemployeesHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully deleted")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

}


public void testOrderDetailsListSelect() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
		selenium.click("OrderdetailsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Orderdetails search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listorderdetailsFormId:listSearchButtonId"));
		assertTrue(selenium.isElementPresent("listorderdetailnumberLinkId"));
		assertTrue(selenium.isElementPresent("listcolumnHeaderordersId"));
		assertTrue(selenium.isElementPresent("listcolumnHeaderproductsId"));
		assertTrue(selenium.isElementPresent("listquantityorderedLinkId"));
		assertTrue(selenium.isElementPresent("listpriceeachLinkId"));
		assertTrue(selenium.isElementPresent("listorderlinenumberLinkId"));
		assertTrue(selenium.isElementPresent("listorderdetailsLinkId"));
		assertTrue(selenium.isElementPresent("nextPageorderdetailsListId"));
		assertTrue(selenium.isElementPresent("lastPageorderdetailsListId"));
		selenium.click("listorderdetailnumberLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listorderdetailsLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("orderdetailsHome".equals(selenium.getText("viewTextorderdetailsHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("0"));
		assertTrue(selenium.isTextPresent("30"));
		assertTrue(selenium.isTextPresent("136.0"));
		assertTrue(selenium.isTextPresent("3"));
		assertTrue(selenium.isTextPresent("10100"));
		assertTrue(selenium.isElementPresent("viewEditOrderdetailsEdit"));
		assertTrue(selenium.isElementPresent("viewDoneOrderdetailsEdit"));
		assertTrue(selenium.isTextPresent("2003-01-06"));
		assertTrue(selenium.isTextPresent("Shipped"));
		assertTrue(selenium.isElementPresent("viewordersLinkId"));
		assertEquals("View", selenium.getText("viewordersLinkId"));
		selenium.click("viewordersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("ordersHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("10100"));
		assertTrue(selenium.isTextPresent("1/6/03"));
		assertTrue(selenium.isTextPresent("1/13/03"));
		assertTrue(selenium.isTextPresent("1/10/03"));
		assertTrue(selenium.isTextPresent("Shipped"));
		assertTrue(selenium.isTextPresent("Online Diecast Creations Co."));
		assertTrue(selenium.isTextPresent("2304 Long Airport Avenue"));
		assertTrue(selenium.isTextPresent("orderdetailses"));
		selenium.click("viewcustomersLinkId");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("viewTextcustomersHomeId"));
		assertTrue(selenium.isTextPresent("363"));
		assertTrue(selenium.isTextPresent("Online Diecast Creations Co."));
		assertTrue(selenium.isTextPresent("2304 Long Airport Avenue"));
		assertTrue(selenium.isTextPresent("Nashua"));
		assertTrue(selenium.isTextPresent("114200.0"));
		assertTrue(selenium.isTextPresent("Young"));
		assertTrue(selenium.isTextPresent("Dorothy"));
		assertTrue(selenium.isTextPresent("6035558647"));
		assertTrue(selenium.isTextPresent("NH"));
		assertTrue(selenium.isTextPresent("USA"));
		assertEquals("paymentses", selenium.getText("//td[2]/table/tbody/tr[2]/td[2]/a/table/tbody/tr/td"));
		assertEquals("orderses", selenium.getText("//td[3]/table/tbody/tr[2]/td[2]/a/table/tbody/tr/td"));
		selenium.click("//td[2]/table/tbody/tr[2]/td[2]/a/table/tbody/tr/td");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("paymentdate")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("selectpaymentsLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("viewTextpaymentsHomeId")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewcustomersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("viewTextcustomersHomeId")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("//td[3]/table/tbody/tr[2]/td[2]/a/table/tbody/tr/td");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("ordernumber")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("Customer has worked with some of our vendors in the past and is aware of their MSRP"));
		selenium.click("selectordersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("viewTextordersHomeId")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewcustomersLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a/table/tbody/tr/td");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("employeenumber")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewemployeesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("employeesHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("spatterson@classicmodelcars.com"));
		assertTrue(selenium.isTextPresent("Sales Rep"));
		selenium.click("selectcustomersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("viewTextcustomersHomeId")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("employeenumber"));
		selenium.click("OrderdetailsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Orderdetails search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listcolumnHeaderproductsId");
		selenium.waitForPageToLoad("30000");
}

public void testOrderdetailsListCED() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
		selenium.click("OrderdetailsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Orderdetails search".equals(selenium.getText("listorderdetailsFormId:listOrderdetailsId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listCreateorderdetailsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit orderdetails".equals(selenium.getText("orderdetailseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("editSelectButtonordersId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listordersLinkId");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("orderdetailseditForm:orderdetailnumberDecoration:orderdetailnumberId"));
		selenium.type("orderdetailseditForm:orderdetailnumberDecoration:orderdetailnumberId", "99999");
		selenium.fireEvent("orderdetailseditForm:orderdetailnumberDecoration:orderdetailnumberId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("99999".equals(selenium.getValue("orderdetailseditForm:orderdetailnumberDecoration:orderdetailnumberId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("orderdetailseditForm:quantityorderedDecoration:quantityorderedId"));
		selenium.type("orderdetailseditForm:quantityorderedDecoration:quantityorderedId", "99999");
		selenium.fireEvent("orderdetailseditForm:quantityorderedDecoration:quantityorderedId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("99999".equals(selenium.getValue("orderdetailseditForm:quantityorderedDecoration:quantityorderedId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("orderdetailseditForm:priceeachDecoration:priceeachId"));
		selenium.type("orderdetailseditForm:priceeachDecoration:priceeachId", "99.99");
		selenium.fireEvent("orderdetailseditForm:priceeachDecoration:priceeachId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("99.99".equals(selenium.getValue("orderdetailseditForm:priceeachDecoration:priceeachId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("orderdetailseditForm:orderlinenumberDecoration:orderlinenumberId"));
		selenium.type("orderdetailseditForm:orderlinenumberDecoration:orderlinenumberId", "9999");
		selenium.fireEvent("orderdetailseditForm:orderlinenumberDecoration:orderlinenumberId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("9999".equals(selenium.getValue("orderdetailseditForm:orderlinenumberDecoration:orderlinenumberId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("//td[2]/table/tbody/tr[2]/td[2]/a/table/tbody/tr/td"));
		selenium.click("//td[2]/table/tbody/tr[2]/td[2]/a/table/tbody/tr/td");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("No products")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("editSelectButtonproductsId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listproductsLinkId");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("//a/table/tbody/tr/td"));
		selenium.click("//a/table/tbody/tr/td");
		selenium.click("orderdetailseditForm:saveorderdetailsHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully created")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewEditOrderdetailsEdit");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("orderdetailseditForm:orderlinenumberDecoration:orderlinenumberId"));
		selenium.type("orderdetailseditForm:orderlinenumberDecoration:orderlinenumberId", "0000");
		selenium.fireEvent("orderdetailseditForm:orderlinenumberDecoration:orderlinenumberId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("0".equals(selenium.getValue("orderdetailseditForm:orderlinenumberDecoration:orderlinenumberId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("orderdetailseditForm:updateorderdetailsHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully updated")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewEditOrderdetailsEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("orderdetailseditForm:deleteorderdetailsHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("orderdetailseditForm:deleteorderdetailsHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully deleted")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
}

public void testProductListSelect() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
		selenium.click("ProductsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Products search".equals(selenium.getText("listproductsFormId:listProductsId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listproductsFormId:productcodedecId:listproductcodeTextId"));
		assertTrue(selenium.isElementPresent("listproductsFormId:productnamedecId:listproductnameTextId"));
		assertTrue(selenium.isElementPresent("listproductsFormId:productscaledecId:listproductscaleTextId"));
		assertTrue(selenium.isElementPresent("listproductsFormId:productvendordecId:listproductvendorTextId"));
		assertTrue(selenium.isElementPresent("listproductsFormId:productdescriptiondecId:listproductdescriptionTextId"));
		assertTrue(selenium.isElementPresent("listproductsFormId:listSearchButtonId"));
		assertTrue(selenium.isElementPresent("listproductcodeLinkId"));
		assertTrue(selenium.isElementPresent("listcolumnHeaderproductlinesId"));
		assertTrue(selenium.isElementPresent("listproductnameLinkId"));
		assertTrue(selenium.isElementPresent("listproductscaleLinkId"));
		assertTrue(selenium.isElementPresent("listproductvendorLinkId"));
		assertTrue(selenium.isElementPresent("listproductdescriptionLinkId"));
		assertTrue(selenium.isElementPresent("listquantityinstockLinkId"));
		assertTrue(selenium.isElementPresent("listbuypriceLinkId"));
		assertTrue(selenium.isElementPresent("listmsrpLinkId"));
		assertTrue(selenium.isElementPresent("listproductsLinkId"));
		selenium.click("listproductcodeLinkId");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("listproductsLinkId"));
		selenium.click("listproductsLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("productsHome".equals(selenium.getText("viewTextproductsHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("This replica features working kickstand, front suspension, gear-shift lever, footbrake lever, drive chain, wheels and steering. All parts are particularly delicate due to their precise scale and require special care and attention."));
		assertTrue(selenium.isTextPresent("1969 Harley Davidson Ultimate Chopper"));
		assertTrue(selenium.isElementPresent("viewproductlinesLinkId"));
		selenium.click("viewproductlinesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("productlinesHome".equals(selenium.getText("viewTextproductlinesHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("Our motorcycles are state of the art replicas of classic as well as contemporary motorcycle legends such as Harley Davidson, Ducati and Vespa. Models contain stunning details such as official logos, rotating wheels, working kickstand, front suspension, gear-shift lever, footbrake lever, and drive chain. Materials used include diecast and plastic. The models range in size from 1:10 to 1:50 scale and include numerous limited edition and several out-of-production vehicles. All models come fully assembled and ready for display in the home or office. Most include a certificate of authenticity."));
		assertTrue(selenium.isElementPresent("selectproductsLinkId"));
		selenium.click("selectproductsLinkId");
		selenium.waitForPageToLoad("30000");
		assertEquals("productsHome", selenium.getText("viewTextproductsHomeId"));
		selenium.click("viewproductlinesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("productlinesHome".equals(selenium.getText("viewTextproductlinesHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("ProductsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Products search".equals(selenium.getText("listproductsFormId:listProductsId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listmsrpLinkId");
		selenium.waitForPageToLoad("30000");
}

public void testProductListSearch() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
		selenium.click("ProductsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Products search".equals(selenium.getText("listproductsFormId:listProductsId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("listproductsFormId:productcodedecId:listproductcodeTextId", "S10_1678");
		selenium.fireEvent("listproductsFormId:productcodedecId:listproductcodeTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("S10_1678".equals(selenium.getValue("listproductsFormId:productcodedecId:listproductcodeTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (!selenium.isElementPresent("nextPageproductsListId")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("This replica features working kickstand, front suspension, gear-shift lever, footbrake lever, drive chain, wheels and steering. All parts are particularly delicate due to their precise scale and require special care and attention."));
		selenium.type("listproductsFormId:productcodedecId:listproductcodeTextId", "");
		selenium.fireEvent("listproductsFormId:productcodedecId:listproductcodeTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("".equals(selenium.getValue("listproductsFormId:productcodedecId:listproductcodeTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("nextPageproductsListId")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
                
                
}


public void testProductLinesListSelect() throws Exception
{
    selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.open(project+"home.seam");
    
		selenium.click("ProductlinesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Productlines search".equals(selenium.getText("listproductlinesFormId:listProductlinesId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("listproductlinesFormId:productlinedecId:listproductlineTextId"));
		assertTrue(selenium.isElementPresent("listproductlinesFormId:textdescriptiondecId:listtextdescriptionTextId"));
		assertTrue(selenium.isElementPresent("listproductlinesFormId:htmldescriptiondecId:listhtmldescriptionTextId"));
		assertTrue(selenium.isElementPresent("listproductlinesFormId:listSearchButtonId"));
		assertTrue(selenium.isElementPresent("listproductlineLinkId"));
		assertTrue(selenium.isElementPresent("listtextdescriptionLinkId"));
		assertTrue(selenium.isElementPresent("listhtmldescriptionLinkId"));
		assertTrue(selenium.isElementPresent("listproductlinesLinkId"));
		selenium.click("listproductlineLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listproductlinesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("productlinesHome".equals(selenium.getText("viewTextproductlinesHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("Attention car enthusiasts: Make your wildest car ownership dreams come true. Whether you are looking for classic muscle cars, dream sports cars or movie-inspired miniatures, you will find great choices in this category. These replicas feature superb attention to detail and craftsmanship and offer features such as working steering system, opening forward compartment, opening rear trunk with removable spare wheel, 4-wheel independent spring suspension, and so on. The models range in size from 1:10 to 1:24 scale and include numerous limited edition and several out-of-production vehicles. All models include a certificate of authenticity from their manufacturers and come fully assembled and ready for display in the home or office."));
		assertTrue(selenium.isElementPresent("selectproductsLinkId"));
		selenium.click("selectproductsLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("productsHome".equals(selenium.getText("viewTextproductsHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewproductlinesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("productlinesHome".equals(selenium.getText("viewTextproductlinesHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("selectproductsLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("productsHome".equals(selenium.getText("viewTextproductsHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertEquals("orderdetailses", selenium.getText("//td[2]/table/tbody/tr[2]/td[2]/a/table/tbody/tr/td"));
		selenium.click("//td[2]/table/tbody/tr[2]/td[2]/a/table/tbody/tr/td");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("orderdetailnumber")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("quantityordered")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("selectorderdetailsLinkId"));
		selenium.click("selectorderdetailsLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("orderdetailsHome".equals(selenium.getText("viewTextorderdetailsHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("viewordersLinkId"));
		selenium.click("viewordersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("viewTextordersHomeId")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertEquals("ordersHome", selenium.getText("viewTextordersHomeId"));
		assertTrue(selenium.isElementPresent("viewcustomersLinkId"));
		selenium.click("viewcustomersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("customersHome".equals(selenium.getText("viewTextcustomersHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertEquals("paymentses", selenium.getText("//td[2]/table/tbody/tr[2]/td[2]/a/table/tbody/tr/td"));
		selenium.click("//td[2]/table/tbody/tr[2]/td[2]/a/table/tbody/tr/td");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("paymentnumber")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("paymentdate")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertEquals("orderses", selenium.getText("//td[3]/table/tbody/tr[2]/td[2]/a/table/tbody/tr/td"));
		selenium.click("//td[3]/table/tbody/tr[2]/td[2]/a/table/tbody/tr/td");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("ordernumber")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("orderdate")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isElementPresent("selectordersLinkId"));
		assertEquals("employees", selenium.getText("//a/table/tbody/tr/td"));
		selenium.click("//a/table/tbody/tr/td");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("employeenumber")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewemployeesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("employeesHome".equals(selenium.getText("viewTextemployeesHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("selectcustomersLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("customersHome")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewemployeesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("employeesHome".equals(selenium.getText("viewTextemployeesHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("ProductlinesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Productlines search".equals(selenium.getText("listproductlinesFormId:listProductlinesId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listhtmldescriptionLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listproductlinesLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("productlinesHome".equals(selenium.getText("viewTextproductlinesHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("selectproductsLinkId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("productsHome".equals(selenium.getText("viewTextproductsHomeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("//a/table/tbody/tr/td");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("productline")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("ProductlinesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Productlines search".equals(selenium.getText("listproductlinesFormId:listProductlinesId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
}       
 
public void testProductLinesListSearch()throws Exception
{
                selenium=SeleniumRC.getCurrentInstance().getSelenium();
                selenium.open(project+"home.seam");
       		selenium.click("ProductlinesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Productlines search".equals(selenium.getText("listproductlinesFormId:listProductlinesId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("Classic Cars"));
		assertTrue(selenium.isTextPresent("Vintage Cars"));
		selenium.type("listproductlinesFormId:productlinedecId:listproductlineTextId", "---");
		selenium.fireEvent("listproductlinesFormId:productlinedecId:listproductlineTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("---".equals(selenium.getValue("listproductlinesFormId:productlinedecId:listproductlineTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
                Thread.sleep(300);
		//assertTrue(selenium.isTextPresent("No productlines exists"));
		selenium.type("listproductlinesFormId:productlinedecId:listproductlineTextId", "ship");
		selenium.fireEvent("listproductlinesFormId:productlinedecId:listproductlineTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("ship".equals(selenium.getValue("listproductlinesFormId:productlinedecId:listproductlineTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
                Thread.sleep(2000);
		assertTrue(selenium.isTextPresent("The perfect holiday or anniversary gift for executives, clients, friends, and family"));
		assertTrue(selenium.isTextPresent("Ships"));
		selenium.type("listproductlinesFormId:productlinedecId:listproductlineTextId", "");
		selenium.fireEvent("listproductlinesFormId:productlinedecId:listproductlineTextId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("".equals(selenium.getValue("listproductlinesFormId:productlinedecId:listproductlineTextId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
                
                for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Classic Cars")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
		assertTrue(selenium.isTextPresent("Classic Cars"));
}

 public void testProdLineAndListCED()throws Exception
 {
     selenium=SeleniumRC.getCurrentInstance().getSelenium();
    selenium.click("ProductlinesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Productlines search".equals(selenium.getText("listproductlinesFormId:listProductlinesId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listCreateproductlinesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit productlines".equals(selenium.getText("productlineseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("productlineseditForm:productlineDecoration:productlineId", "test");
		selenium.fireEvent("productlineseditForm:productlineDecoration:productlineId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test".equals(selenium.getValue("productlineseditForm:productlineDecoration:productlineId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("productlineseditForm:textdescriptionDecoration:textdescription", "test");
		selenium.fireEvent("productlineseditForm:textdescriptionDecoration:textdescription", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test".equals(selenium.getValue("productlineseditForm:textdescriptionDecoration:textdescription"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("productlineseditForm:htmldescriptionDecoration:htmldescription", "test");
		selenium.fireEvent("productlineseditForm:htmldescriptionDecoration:htmldescription", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test".equals(selenium.getValue("productlineseditForm:htmldescriptionDecoration:htmldescription"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("productlineseditForm:saveproductlinesHome");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully created")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewEditProductlinesEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit productlines".equals(selenium.getText("productlineseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("productlineseditForm:textdescriptionDecoration:textdescription", "test again");
		selenium.fireEvent("productlineseditForm:textdescriptionDecoration:textdescription", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test again".equals(selenium.getValue("productlineseditForm:textdescriptionDecoration:textdescription"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("productlineseditForm:updateproductlinesHome");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully updated")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("ProductsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Products search".equals(selenium.getText("listproductsFormId:listProductsId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listCreateproductsId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit products".equals(selenium.getText("productseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("editSelectButtonproductlinesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Productlines search".equals(selenium.getText("listproductlinesFormId:listProductlinesId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("listproductlineLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listproductlineLinkId");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("productline ?"));
		selenium.click("listproductlinesLinkId");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("test again"));
		selenium.type("productseditForm:productcodeDecoration:productcodeId", "test");
		selenium.fireEvent("productseditForm:productcodeDecoration:productcodeId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test".equals(selenium.getValue("productseditForm:productcodeDecoration:productcodeId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("productseditForm:productnameDecoration:productnameId", "test");
		selenium.fireEvent("productseditForm:productnameDecoration:productnameId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test".equals(selenium.getValue("productseditForm:productnameDecoration:productnameId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("productseditForm:productscaleDecoration:productscaleId", "test");
		selenium.fireEvent("productseditForm:productscaleDecoration:productscaleId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test".equals(selenium.getValue("productseditForm:productscaleDecoration:productscaleId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("productseditForm:productvendorDecoration:productvendorId", "test");
		selenium.fireEvent("productseditForm:productvendorDecoration:productvendorId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test".equals(selenium.getValue("productseditForm:productvendorDecoration:productvendorId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("productseditForm:productdescriptionDecoration:productdescription", "test");
		selenium.fireEvent("productseditForm:productdescriptionDecoration:productdescription", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("test".equals(selenium.getValue("productseditForm:productdescriptionDecoration:productdescription"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("productseditForm:quantityinstockDecoration:quantityinstockId", "999");
		selenium.fireEvent("productseditForm:quantityinstockDecoration:quantityinstockId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("999".equals(selenium.getValue("productseditForm:quantityinstockDecoration:quantityinstockId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("productseditForm:buypriceDecoration:buypriceId", "99");
		selenium.fireEvent("productseditForm:buypriceDecoration:buypriceId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("99.0".equals(selenium.getValue("productseditForm:buypriceDecoration:buypriceId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("productseditForm:msrpDecoration:msrpId", "999");
		selenium.fireEvent("productseditForm:msrpDecoration:msrpId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("999.0".equals(selenium.getValue("productseditForm:msrpDecoration:msrpId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("productseditForm:saveproductsHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully created")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewEditProductsEdit");
		selenium.waitForPageToLoad("30000");
		selenium.type("productseditForm:quantityinstockDecoration:quantityinstockId", "888");
		selenium.fireEvent("productseditForm:quantityinstockDecoration:quantityinstockId", "blur");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("888".equals(selenium.getValue("productseditForm:quantityinstockDecoration:quantityinstockId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("productseditForm:updateproductsHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully updated")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("viewEditProductsEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit products".equals(selenium.getText("productseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("productseditForm:deleteproductsHome");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully deleted")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("ProductlinesId");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Productlines search")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("test again"));
		selenium.selectWindow("null");
		selenium.click("listproductlineLinkId");
		selenium.waitForPageToLoad("30000");
		selenium.click("listproductlineLinkId");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("productline ?"));
		selenium.click("listproductlinesLinkId");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("test again"));
		selenium.click("viewEditProductlinesEdit");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Edit productlines".equals(selenium.getText("productlineseditForm:editTextBoxId"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("productlineseditForm:deleteproductlinesHome");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("Successfully deleted")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		assertTrue(selenium.isTextPresent("Successfully deleted"));


 }
 
 }

