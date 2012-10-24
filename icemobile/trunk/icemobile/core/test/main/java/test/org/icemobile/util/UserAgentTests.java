package test.org.icemobile.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.icemobile.util.UserAgentInfo;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class UserAgentTests  {
    
    List<String> androidPhones = new ArrayList<String>();
    List<String> androidTablets = new ArrayList<String>();
    
    private void processUserAgentFile(String file, List<String> list) throws IOException{
        InputStream in = this.getClass().getResourceAsStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while( (line = reader.readLine()) != null){
            list.add(line);
        }
        in.close();
        reader.close();
    }
    
    @Before
    public void setUp() throws Exception {
        processUserAgentFile("android-tablets.txt",androidTablets);
        processUserAgentFile("android-handhelds.txt",androidPhones);
    }
           
    @Test
    public void testAndroidTablets() throws Exception{
        
        for( String userAgent : androidTablets ){
            UserAgentInfo userAgentInfo = new UserAgentInfo(userAgent);
            System.out.println("Testing Android Tablet: "+userAgent);
            assertTrue("not android: "+userAgent, userAgentInfo.isAndroidOS());
            assertTrue("not tablet: "+userAgent, userAgentInfo.isTabletBrowser());
            assertFalse("handheld: "+userAgent, userAgentInfo.isMobileBrowser());
            assertFalse("desktop: "+userAgent, userAgentInfo.isDesktopBrowser());
            assertFalse("blackberry: "+userAgent, userAgentInfo.isBlackberryOS());
            assertFalse("ios: "+userAgent, userAgentInfo.isIOS());
            assertFalse("windows: "+userAgent, userAgentInfo.isWindowsOS());
            assertFalse("mac: "+userAgent, userAgentInfo.isMacOS());
        }
        
    }
    
    @Test
    public void testAndroidHandhelds() throws Exception{
        
        for( String userAgent : androidPhones ){
            UserAgentInfo userAgentInfo = new UserAgentInfo(userAgent);
            System.out.println("Testing Android Handheld: "+userAgent);
            assertTrue("not android: "+userAgent, userAgentInfo.isAndroidOS());
            assertFalse("tablet: "+userAgent, userAgentInfo.isTabletBrowser());
            assertTrue("not handheld: "+userAgent, userAgentInfo.isMobileBrowser());
            assertFalse("desktop: "+userAgent, userAgentInfo.isDesktopBrowser());
            assertFalse("blackberry: "+userAgent, userAgentInfo.isBlackberryOS());
            assertFalse("ios: "+userAgent, userAgentInfo.isIOS());
            assertFalse("windows: "+userAgent, userAgentInfo.isWindowsOS());
            assertFalse("mac: "+userAgent, userAgentInfo.isMacOS());
        }
        
    }


}
