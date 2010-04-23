package org.icefaces.htmlunit;

import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebWindowAdapter;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import junit.framework.TestCase;
import junit.framework.Assert;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * This Test class is checking escaped CData in rendered output and
 * inputText components. It also checks that the update is correctly
 * applied when the DOM is mangled enough to send a whole form down. 
 */
public class ICE_3182Test extends TestCase {

    static final Pattern end_cdata = Pattern.compile("Output: (.+)");


    /**
     *  Check that the input text fields with the canned cdata termination
     * characters render properly during the first render. 
     */
    @Test
    public void testCdataPageLoad() throws Exception {

        final WebClient webClient = new WebClient( BrowserVersion.FIREFOX_3 );
        HtmlPage page = webClient.getPage("http://localhost:9090/ICE-3182/");
        webClient.setAjaxController(new MyAjaxController());

        // check to see if the pre-rendered input fields have the correct values


        String val = getHtmlInputValue(page, "form1:inOne");
        Assert.assertEquals( "]]>" , val);

        val = getHtmlInputValue(page, "form1:inTwo");
        Assert.assertEquals( "]]>",  val);

    }

    /**
     * Post some cdata terminating characters into the input text field and
     * verify that they show up in the echoing text field, and i suppose in the
     * input text field itself. 
     */
    @Test
    public void testPostbackResponse() throws Exception {

        final WebClient webClient = new WebClient( BrowserVersion.FIREFOX_3 );
        HtmlPage page = webClient.getPage("http://localhost:9090/ICE-3182/");
        webClient.setAjaxController(new MyAjaxController());

        // check to see if the pre-rendered input fields have the correct values

        String val = getHtmlInputValue(page, "form1:inOne");
        Assert.assertEquals( "]]>" , val);

        val = getHtmlInputValue(page, "form1:inTwo");
        Assert.assertEquals( "]]>", val);

        setInputTextValue(page, "form1:in1", "aaa]]>bbb");

        // This is a simple postback. The other button will include a
        // ui:include section which will cause the entire form to be updated. 
        HtmlPage page2 = clickElement(page, "form1:sendButton", webClient);

        Matcher m = end_cdata.matcher( page2.asText() );

        if (m.find()) {
            String value = m.group();
            if (value.indexOf("aaa]]>bbb") == -1 ) {
                Assert.fail("Failed to find expected output");
            }
        } else {
            Assert.fail("Failed to find expected output");
        }

    }


    /**
     * Regression test for bug ICE-3192
     */
    @Test
    public void testFormRenderResponse() throws Exception {

        final WebClient webClient = new WebClient( BrowserVersion.FIREFOX_3 );
        HtmlPage page = webClient.getPage("http://localhost:9090/ICE-3182/");
        webClient.setAjaxController(new MyAjaxController());

        // check to see if the pre-rendered input fields have the correct values

        String val = getHtmlInputValue(page, "form1:inOne");
        Assert.assertEquals( "]]>" , val);

        val = getHtmlInputValue(page, "form1:inTwo");
        Assert.assertEquals( "]]>", val);

        setInputTextValue(page, "form1:in1", "aaa]]>bbb");

        // This is a simple postback. The other button will include a
        // ui:include section which will cause the entire form to be updated.
        HtmlPage page2 = clickElement(page, "form1:childAdder", webClient);


        Matcher m = end_cdata.matcher( page2.asText() );

        if (m.find()) {
            String value = m.group();
            if (value.indexOf("aaa]]>bbb") == -1 ) {
                Assert.fail("Failed to find expected output");
            }
        } else {
            Assert.fail("Failed to find expected output");
        }

        val = getHtmlInputValue(page, "form1:inOne");
        Assert.assertEquals( "]]>" , val);

        val = getHtmlInputValue(page, "form1:inTwo");
        Assert.assertEquals( "]]>", val);
    }




    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(ICE_3182Test.class);
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("org.icefaces.htmlunit.ICE_3182Test");
    }

    /**
     * Utility method for finding and clicking an element on a page.
     *
     * @param page Existing HtmlPage
     * @param elementId Id of clickable element, fully qualified containing form id's
     * @param client WebClient instance
     * @return new copy of HtmlPage
     * @throws java.io.IOException from underlying test framework
     */
    protected HtmlPage clickElement(HtmlPage page, String elementId, WebClient client) throws IOException {

        HtmlElement element = page.getElementById(elementId);
        Assert.assertNotNull  ("Clickable element: " + elementId + " is not found", element);

        page = (HtmlPage) element.click();
        client.waitForBackgroundJavaScript(2000);
        return page;
    }

    /**
     * Utility method for finding and setting the value of an input text field
     * @param page
     * @param id
     * @param value
     */
    protected void setInputTextValue(HtmlPage page, String id, String value ) {

        HtmlTextInput inputField = (HtmlTextInput) page.getElementById(id);
        Assert.assertNotNull(inputField);
        inputField.setValueAttribute( value);
    }

    /**
     * Fetch the value from an input text field
     * @param page HtmlPage
     * @param id id of input text field
     * @return Value attribute of field
     */
    protected String getHtmlInputValue(HtmlPage page, String id) {
        HtmlInput element = (HtmlInput) page.getElementById( id );
        Assert.assertNotNull( "Input element: " + id + " not found",element );
        return element.getValueAttribute();
    }


    /**
     * We need to convert some requests to synchronous in order not to miss
     * some updates.
     */
    public class MyAjaxController extends AjaxController {

        public boolean processSynchron(HtmlPage page,
                                       WebRequestSettings settings,
                                       boolean async) {


            // It seems that if the send-updated-views Ajax requests can be handled
            // asynchronously, but that the User interface interaction works best
            // if run synchronously. Running both synchronously causes a deadlock.
            // Running both asynchronously lets some updates get skipped.

            if (settings.getUrl().toString().indexOf("icefaces.xhtml") > -1) {
//                System.out.println("Ajax request to: " + settings.getUrl() + " type: " +  settings.getHttpMethod() );
//                System.out.println(page.getPage().asText());
                return super.processSynchron( page, settings, false  );
            } else {
                return   super.processSynchron( page, settings, async  );
            }
        }

    }

    // Not used.
    public class MyWindowListener extends WebWindowAdapter {

        public void webWindowContentChanged(WebWindowEvent event) {
            System.out.println("WebWindowContent changed! " + event);
        }

    }

    public class MyDomChangeListener implements DomChangeListener {

        public void nodeAdded(DomChangeEvent event) {
//            System.out.println("Node added: " + event);
        }
        public void nodeDeleted(DomChangeEvent event) {
//            System.out.println("Node deleted: " + event.getChangedNode().getNodeName());
        }
    }
}