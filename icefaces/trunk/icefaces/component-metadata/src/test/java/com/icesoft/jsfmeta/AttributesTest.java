package com.icesoft.jsfmeta;

import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.FacesConfigBean;
import com.sun.rave.jsfmeta.beans.PropertyBean;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.xml.sax.SAXException;


public class AttributesTest extends TestCase{
    
    private String METADATA_XML = "extended-faces-config.xml";
    private FacesConfigBean facesConfigBean;
    private static List extendedComponents = new ArrayList();
    private static Map customComponents = new HashMap();    
    static {
        extendedComponents.add("HtmlCommandButton");
        extendedComponents.add("HtmlCommandLink");
        extendedComponents.add("HtmlDataTable");
        extendedComponents.add("HtmlForm");
        extendedComponents.add("HtmlGraphicImage");
        extendedComponents.add("HtmlInputHidden");
        extendedComponents.add("HtmlInputSecret");
        extendedComponents.add("HtmlInputText");
        extendedComponents.add("HtmlInputTextarea");
        extendedComponents.add("HtmlMessage");
        extendedComponents.add("HtmlMessages");
        extendedComponents.add("HtmlOutputFormat");
        extendedComponents.add("HtmlOutputLabel");
        extendedComponents.add("HtmlOutputLink");
        extendedComponents.add("HtmlOutputText");
        extendedComponents.add("HtmlPanelGrid");
        extendedComponents.add("HtmlPanelGroup");
        extendedComponents.add("HtmlSelectBooleanCheckbox");
        extendedComponents.add("HtmlSelectManyCheckbox");
        extendedComponents.add("HtmlSelectManyListbox");
        extendedComponents.add("HtmlSelectManyMenu");
        extendedComponents.add("HtmlSelectOneListbox");
        extendedComponents.add("HtmlSelectOneMenu");
        extendedComponents.add("HtmlSelectOneRadio");
        customComponents.put("com.icesoft.faces.Column", "com.icesoft.faces.component.ext.UIColumn");        
        customComponents.put("com.icesoft.faces.BorderLayout", "com.icesoft.faces.component.panelborder.PanelBorder");
        customComponents.put("com.icesoft.faces.ColumnGroup", "com.icesoft.faces.component.ext.ColumnGroup");
        customComponents.put("com.icesoft.faces.Columns", "com.icesoft.faces.component.ext.UIColumns");
        customComponents.put("com.icesoft.faces.DataScroller", "com.icesoft.faces.component.datapaginator.DataPaginator");
        customComponents.put("com.icesoft.faces.File", "com.icesoft.faces.component.inputfile.InputFile");
        customComponents.put("com.icesoft.faces.GMap", "com.icesoft.faces.component.gmap.GMap");
        customComponents.put("com.icesoft.faces.GMapControl", "com.icesoft.faces.component.gmap.GMapControl");
        customComponents.put("com.icesoft.faces.GMapDirection", "com.icesoft.faces.component.gmap.GMapDirection");
        customComponents.put("com.icesoft.faces.GMapGeoXml", "com.icesoft.faces.component.gmap.GMapGeoXml");
        customComponents.put("com.icesoft.faces.GMapLatLng", "com.icesoft.faces.component.gmap.GMapLatLng");
        customComponents.put("com.icesoft.faces.GMapLatLngs", "com.icesoft.faces.component.gmap.GMapLatLngs");
        customComponents.put("com.icesoft.faces.GMapMarker", "com.icesoft.faces.component.gmap.GMapMarker");
        customComponents.put("com.icesoft.faces.HeaderRow", "com.icesoft.faces.component.ext.HeaderRow");
        customComponents.put("com.icesoft.faces.HtmlCheckbox", "com.icesoft.faces.component.ext.HtmlCheckbox");
        customComponents.put("com.icesoft.faces.HtmlRadio", "com.icesoft.faces.component.ext.HtmlRadio");
        customComponents.put("com.icesoft.faces.InputRichText", "com.icesoft.faces.component.inputrichtext.InputRichText");
        customComponents.put("com.icesoft.faces.Menu", "com.icesoft.faces.component.menubar.MenuBar");
        customComponents.put("com.icesoft.faces.MenuNode", "com.icesoft.faces.component.menubar.MenuItem");
        customComponents.put("com.icesoft.faces.MenuNodeSeparator", "com.icesoft.faces.component.menubar.MenuItemSeparator");
        customComponents.put("com.icesoft.faces.MenuNodes", "com.icesoft.faces.component.menubar.MenuItems");
        customComponents.put("com.icesoft.faces.MenuPopup", "com.icesoft.faces.component.menupopup.MenuPopup");
        customComponents.put("com.icesoft.faces.OutputBody", "com.icesoft.faces.component.ext.OutputBody");
        customComponents.put("com.icesoft.faces.OutputChart", "com.icesoft.faces.component.outputchart.OutputChart");
        customComponents.put("com.icesoft.faces.OutputConnectionStatus", "com.icesoft.faces.component.outputconnectionstatus.OutputConnectionStatus");
        customComponents.put("com.icesoft.faces.OutputDeclaration", "com.icesoft.faces.component.outputdeclaration.OutputDeclaration");
        customComponents.put("com.icesoft.faces.OutputHead", "com.icesoft.faces.component.ext.OutputHead");
        customComponents.put("com.icesoft.faces.OutputHtml", "com.icesoft.faces.component.ext.OutputHtml");        
        customComponents.put("com.icesoft.faces.OutputMedia", "com.icesoft.faces.component.outputmedia.OutputMedia");   
        customComponents.put("com.icesoft.faces.OutputResource", "com.icesoft.faces.component.outputresource.OutputResource");   
        customComponents.put("com.icesoft.faces.OutputStyleComp", "com.icesoft.faces.component.style.OutputStyle");   
        customComponents.put("com.icesoft.faces.PanelCollapsible", "com.icesoft.faces.component.panelcollapsible.PanelCollapsible");        
        customComponents.put("com.icesoft.faces.PanelDivider", "com.icesoft.faces.component.paneldivider.PanelDivider");   
        customComponents.put("com.icesoft.faces.PanelLayout", "com.icesoft.faces.component.panellayout.PanelLayout");   
        customComponents.put("com.icesoft.faces.PanelPopup", "com.icesoft.faces.component.panelpopup.PanelPopup");   
        customComponents.put("com.icesoft.faces.PanelSeries", "com.icesoft.faces.component.panelseries.PanelSeries");        
        customComponents.put("com.icesoft.faces.PanelStack", "com.icesoft.faces.component.panelstack.PanelStack");   
        customComponents.put("com.icesoft.faces.PanelTab", "com.icesoft.faces.component.paneltabset.PanelTab");   
        customComponents.put("com.icesoft.faces.PanelTabSet", "com.icesoft.faces.component.paneltabset.PanelTabSet");   
        customComponents.put("com.icesoft.faces.PanelTooltip", "com.icesoft.faces.component.paneltooltip.PanelTooltip");        
        customComponents.put("com.icesoft.faces.Portlet", "com.icesoft.faces.component.portlet.Portlet");   
        customComponents.put("com.icesoft.faces.Progress", "com.icesoft.faces.component.outputprogress.OutputProgress");   
        customComponents.put("com.icesoft.faces.RowSelector", "com.icesoft.faces.component.ext.RowSelector");   
        customComponents.put("com.icesoft.faces.SelectInputDate", "com.icesoft.faces.component.selectinputdate.SelectInputDate");        
        customComponents.put("com.icesoft.faces.SelectInputText", "com.icesoft.faces.component.selectinputtext.SelectInputText");   
        customComponents.put("com.icesoft.faces.SortHeader", "com.icesoft.faces.component.commandsortheader.CommandSortHeader");   
        customComponents.put("com.icesoft.faces.TreeNode", "com.icesoft.faces.component.tree.TreeNode");   
        customComponents.put("com.icesoft.faces.TreeView", "com.icesoft.faces.component.tree.Tree");        
        customComponents.put("com.icesoft.faces.dragdrop.PanelPositioned", "com.icesoft.faces.component.panelpositioned.PanelPositioned");   
        customComponents.put("com.icesoft.faces.series", "com.icesoft.faces.component.panelseries.UISeries");   
    }
    
    public static Test suite() {
        return new TestSuite(AttributesTest.class);
    }
    
    public static void main() {
        junit.textui.TestRunner.run(AttributesTest.suite());
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
        try {
        ComponentBean[] componentBeans = facesConfigBean.getComponents();
        for(int i=0; i<componentBeans.length; i++){
            String componentType = componentBeans[i].getComponentType();  
            String component = componentType.substring(componentType.lastIndexOf(".")+1);
            if (componentType.equals("com.icesoft.faces.ApplyEffect") ) continue;
            System.out.println(); 
            System.out.println("\t\t\t\t"+ componentType); 
            System.out.println();             
            boolean isExtended = false;
            Object sunComp = null;
            Object iceExtendedComp = null;
            Object customComp = null;
            
            Map sunPropMap = null;
            Map iceExtendedCompPropMap = null;
            Map customCompPropMap = null;            
            // 1. check if its an extended component
            if (extendedComponents.contains(component)){
                isExtended = true;
                sunComp = getClassObject("javax.faces.component.html."+ component);
                iceExtendedComp = getClassObject("com.icesoft.faces.component.ext."+ component);
                sunPropMap = getProperties(sunComp);
                iceExtendedCompPropMap = getProperties(iceExtendedComp);
            } else if (customComponents.containsKey(componentType)) {
                customComp = getClassObject(customComponents.get(componentType).toString());      
                customCompPropMap = getProperties(customComp);
            }
            
            PropertyBean[] propertyBeans = componentBeans[i].getProperties();
            for(int j=0; j<propertyBeans.length; j++){
                PropertyBean property = propertyBeans[j];
                String propertyName = property.getPropertyName();
                if (isExtended) {
                    String baseProp = null;
                    String extendedProp = null;
                    if (sunPropMap.containsKey(property.getPropertyName())) {
                        baseProp = sunPropMap.get(property.getPropertyName()).toString();
                    } else {
                        baseProp = "Property not found";
                    }
                    
                    if (iceExtendedCompPropMap.containsKey(property.getPropertyName())) {
                        extendedProp = iceExtendedCompPropMap.get(property.getPropertyName()).toString();
                    } else {
                        extendedProp = "Property not found";                             
                    }
                    //assert
                    System.out.println(propertyName + ":\tbase component value ["+ baseProp + "]\t extended component value ["+ extendedProp + "]\tmetadata value [" + property.getDefaultValue() + "]");
                } else {
                    if (customCompPropMap.containsKey(property.getPropertyName())) {
                        //assert
                        System.out.println(propertyName + ":\tcomponent value ["+ customCompPropMap.get(propertyName) + "]\t metadata value [" + property.getDefaultValue() + "]");
                    }
                }
            }//for
        }
        }catch (Exception e){}
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
    
    private Object getClassObject(String className) {
        Class c = null;
        Object obj = null;
        try {
            c = Class.forName(className);
            obj = c.newInstance() ;
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private Map getProperties(Object instance) {
        try {
            PropertyDescriptor[] propertyDescriptor = Introspector.getBeanInfo(instance.getClass()).
            getPropertyDescriptors();      
            Map PropMap = new HashMap();
            for (int j = 0; j < propertyDescriptor.length; j++) {
                Method readMethod = propertyDescriptor[j].getReadMethod();
                if (readMethod != null) {
                    String attName =  propertyDescriptor[j].getName();
                    try {
                        String value = readMethod.invoke(instance, new Object[0])+"";
                        PropMap.put(attName, value);
                    } catch (Exception e) {
                        PropMap.put(attName, "Method can not be invoked out of JSF lifecycle");
                    }
                }
            }    
            return PropMap;
        } catch (Exception e ){ return Collections.EMPTY_MAP;}
    }
}
