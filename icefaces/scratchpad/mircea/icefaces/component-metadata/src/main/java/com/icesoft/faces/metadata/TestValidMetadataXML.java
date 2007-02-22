package com.icesoft.faces.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.faces.render.RenderKitFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.icesoft.jsfmeta.MetadataXmlParser;
import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.FacesConfigBean;
import com.sun.rave.jsfmeta.beans.PropertyBean;
import com.sun.rave.jsfmeta.beans.RenderKitBean;
import com.sun.rave.jsfmeta.beans.RendererBean;

/*
 * TestValidMetadataXml validates metadata.
 *
 */

public class TestValidMetadataXML{
    
    private String INPUT_METADATA_XML = "extended-faces-config.xml";
    private String OUTPUT_METADATA_XML = "extended-faces-config-stream.xml";
    
    
    public static void main(String[] args) {
        
        TestValidMetadataXML test = new TestValidMetadataXML();
        test.setUp();
        test.testMetadata();        
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
    
    protected void setUp(){
        
        File confFile = getConfDir();        
        boolean isConfFile = confFile.isDirectory();
        if(!isConfFile){
            System.out.println("no conf directory in the build directory: "+ confFile);
            if( !confFile.mkdirs() )
                System.out.println("conf directory could not be created");
        }
        
        String xsltStreamSourceString = confFile.getPath() + File.separatorChar
                + "xslt_conf" + File.separatorChar + "translate-conf.xsl";
        String outputStreamString = confFile.getPath() + File.separatorChar
                + OUTPUT_METADATA_XML;
        String streamSourceString = confFile.getPath() + File.separatorChar
                + INPUT_METADATA_XML;
        
        try {
            
            TransformerFactory tFactory = TransformerFactory.newInstance();
            StreamSource xsltStreamSource = new StreamSource(
                    xsltStreamSourceString);
            Transformer transformer = tFactory.newTransformer(xsltStreamSource);
            OutputStream outputStream = new FileOutputStream(outputStreamString);
            StreamResult streamResult = new StreamResult(outputStream);
            StreamSource streamSource = new StreamSource(streamSourceString);
            transformer.transform(streamSource, streamResult);
            
        } catch (TransformerException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public void testMetadata(){
        
        File confDir = getConfDir();        
        String outputStreamString = confDir.getPath() + File.separatorChar
                + INPUT_METADATA_XML;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        documentBuilderFactory.setValidating(true);
        documentBuilderFactory.setNamespaceAware(true);
        
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        documentBuilder.setErrorHandler(new ErrorHandler() {
            public void error(SAXParseException ex) {
                System.err.println("Please check the following \n"+"line number="+ex.getLineNumber()+
                        " column number= "+ ex.getColumnNumber()+
                        "\n URL="+ex.getSystemId());
                ex.printStackTrace();
                System.exit(1);
            }
            
            public void fatalError(SAXParseException ex) throws SAXException {
                System.err.println("Please check the following \n"+"line number="+ex.getLineNumber()+
                        " column number="+ ex.getColumnNumber()+
                        "\n URL="+ex.getSystemId());
                ex.printStackTrace();
                System.exit(1);
            }
            
            public void warning(SAXParseException ex) {
                System.err.println("Please check the following \n"+"line number="+ex.getLineNumber()+
                        " column number="+ ex.getColumnNumber()+
                        "\n URL="+ex.getSystemId());
                ex.printStackTrace();
                System.exit(1);
            }
        });
        
        try {
            Document document = documentBuilder.parse(new File(outputStreamString));
        }  catch (SAXException se) {
            se.printStackTrace();
            System.exit(1);
        }  catch (IOException se) {
            se.printStackTrace();
            System.exit(1);
        }
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
