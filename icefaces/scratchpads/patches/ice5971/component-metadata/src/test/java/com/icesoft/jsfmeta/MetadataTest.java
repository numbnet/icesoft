/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
 
package com.icesoft.jsfmeta;

import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.FacesConfigBean;
import com.sun.rave.jsfmeta.beans.PropertyBean;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.xml.sax.SAXException;

public class MetadataTest extends TestCase{
    
    private String METADATA_XML = "extended-faces-config.xml";
    private FacesConfigBean facesConfigBean;
    
    public static Test suite() {
        return new TestSuite(MetadataTest.class);
    }
    
    public static void main() {
        junit.textui.TestRunner.run(MetadataTest.suite());
    }
    
    protected void setUp(){
        
        File confFile = getConfDir();
        boolean isConfFile = confFile.isDirectory();
        if(!isConfFile){
            System.out.println("no conf directory in the build directory: "+ confFile);
            if( !confFile.mkdirs() )
                System.out.println("conf directory could not be created");
        }
        MetadataXmlParser jsfMetaParser = new MetadataXmlParser();
        String filePath = confFile.getPath() + File.separatorChar
                + METADATA_XML;
        try {
            facesConfigBean = jsfMetaParser.parse(new File(filePath));
            
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void testMetadata(){
        
        ComponentBean[] componentBeans = facesConfigBean.getComponents();
        for(int i=0; i<componentBeans.length; i++){
            PropertyBean[] propertyBeans = componentBeans[i].getProperties();
            for(int j=0; j<propertyBeans.length; j++){
                if(propertyBeans[j].getPropertyClass() != null && propertyBeans[j].getPropertyClass().trim().equalsIgnoreCase("boolean")){                    
                    if(propertyBeans[j].getEditorClass() != null){
                        boolean flag = propertyBeans[j].getEditorClass().trim().length() > 0;
                        assertFalse("\ncomponent class="+componentBeans[i].getComponentClass()+
                                "\ncomponent type="+componentBeans[i].getComponentType()+" has boolean property named="+propertyBeans[j].getPropertyName()
                        +" \nusing wrong editor="+ propertyBeans[j].getEditorClass(), flag);
                    }
                }
            };
        }
    }
    
    private File getConfDir() {
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(".");
        
        File buildDir = new File( convertFileUrlToPath(url) );
        
        if(!buildDir.isDirectory()){
            System.out.println("test build directory does not exist: "+buildDir);
            System.exit(1);
        }
        
        File confFile = new File(buildDir.getParent() + File.separatorChar
                + "classes" + File.separator + "conf");
        
        return confFile;
    }
    /**
     * Kind of hack-ish attempt at solving problem that if the directory,
     *  where we're building the component-metadata in,  has special
     *  characters in its path, like spaces, then the URL to it will be
     *  escaped, which will be interpretted as a different directory,
     *  unless we unescape it.
     */
    private static String convertFileUrlToPath(URL url) {
        
        String path = url.getPath();
        if( url.toExternalForm().startsWith("file:") ) {
            StringBuffer sb = new StringBuffer( path.length() );
            int pathLength = path.length();
            for(int i = 0; i < pathLength;) {
                char c = path.charAt(i);
                if( c == '%' ) {
                    if( (i+1) < pathLength && isHexDigit(path.charAt(i+1)) ) {
                        int increment = 2;
                        if( (i+2) < pathLength && isHexDigit(path.charAt(i+2)) )
                            increment++;
                        try {
                            char unescaped = (char) Integer.parseInt(
                                    path.substring(i+1, i+increment), 16);
                            
                            sb.append( unescaped );
                            i += increment;
                            continue;
                        } catch(NumberFormatException nfe) {
                            // Not a valid hex escape, so just fall through,
                            //  and append it to the path
                        }
                    }
                }
                sb.append( c );
                i++;
            }
            path = sb.toString();
        }
        return path;
    }
    
    private static boolean isHexDigit(char c) {
        return ( (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') );
    }
}
