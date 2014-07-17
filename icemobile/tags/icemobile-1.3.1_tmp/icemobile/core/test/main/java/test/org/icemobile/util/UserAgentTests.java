/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package test.org.icemobile.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.icemobile.util.UserAgentInfo;
import org.junit.Before;
import org.junit.Test;

public class UserAgentTests  {
    
    List<String> androidPhones = new ArrayList<String>();
    List<String> androidTablets = new ArrayList<String>();
    List<String> blackberries = new ArrayList<String>();
    List<String> ies = new ArrayList<String>();
    
    
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
        processUserAgentFile("blackberries.txt",blackberries);
        processUserAgentFile("ie.txt", ies);
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

    @Test
    public void testBlackberries() throws Exception{
        
        for( String userAgent : blackberries ){
            UserAgentInfo userAgentInfo = new UserAgentInfo(userAgent);
            System.out.println("Testing Blackberries: "+userAgent);
            assertFalse("android: "+userAgent, userAgentInfo.isAndroidOS());
            assertFalse("tablet: "+userAgent, userAgentInfo.isTabletBrowser());
            assertTrue("not handheld: "+userAgent, userAgentInfo.isMobileBrowser());
            assertFalse("desktop: "+userAgent, userAgentInfo.isDesktopBrowser());
            assertTrue("blackberry: "+userAgent, userAgentInfo.isBlackberryOS());
            assertFalse("ios: "+userAgent, userAgentInfo.isIOS());
            assertFalse("windows: "+userAgent, userAgentInfo.isWindowsOS());
            assertFalse("mac: "+userAgent, userAgentInfo.isMacOS());
        }
        
    }
    
    @Test
    public void testIEs() throws Exception{
        
        for( String userAgent : ies ){
            UserAgentInfo userAgentInfo = new UserAgentInfo(userAgent);
            System.out.println("Testing IEs: "+userAgent);
            assertFalse("android: "+userAgent, userAgentInfo.isAndroidOS());
            assertFalse("tablet: "+userAgent, userAgentInfo.isTabletBrowser());
            assertFalse("blackberry: "+userAgent, userAgentInfo.isBlackberryOS());
            assertFalse("ios: "+userAgent, userAgentInfo.isIOS());
            assertTrue("windows: "+userAgent, userAgentInfo.isWindowsOS());
            assertFalse("mac: "+userAgent, userAgentInfo.isMacOS());
            assertTrue("ie: " + userAgent, userAgentInfo.isIE());
        }
        
    }

}
