package com.icesoft.faces.mock.test;

import com.icesoft.faces.metadata.*;
import java.io.IOException;
import java.net.URL;

import javax.faces.component.UIComponentBase;
import javax.faces.render.RenderKitFactory;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.icesoft.jsfmeta.MetadataXmlParser;
import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.FacesConfigBean;
import com.sun.rave.jsfmeta.beans.RendererBean;
import javax.faces.component.UIComponent;

public class ICECompsTestCase extends TestCase {

    private ComponentBean[] componentBeans;
    private UIComponentBase[] uiComponentBases;
    private RendererBean[] rendererBeans;

    @Override
    protected void tearDown() throws Exception {
        componentBeans = null;
        uiComponentBases = null;
        rendererBeans = null;
    }

    public static void main() {
        junit.textui.TestRunner.run(RendererTypeTest.suite());
    }

    protected void setUp() {

        if (componentBeans == null && uiComponentBases == null) {
            componentBeans = getComponentBeanInfo();
            rendererBeans = getRendererBean();
            uiComponentBases = new UIComponentBase[componentBeans.length];

            for (int j = 0; j < componentBeans.length; j++) {
                Object newObject = null;

                try {
                    Class namedClass = Class.forName(componentBeans[j].getComponentClass());
                    newObject = namedClass.newInstance();

                    if (newObject instanceof UIComponentBase) {
                        uiComponentBases[j] = (UIComponentBase) newObject;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                }
            }
        }
    }

    protected UIComponent[] getComponents(){
        return uiComponentBases;
    }
    
    //TODO: baseline Component
    private FacesConfigBean getFacesConfigBean() {

        FacesConfigBean facesConfigBean = null;
        MetadataXmlParser jsfMetaParser = new MetadataXmlParser();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL localUrl = classLoader.getResource(".");
            String newPath = "file:" + localUrl.getPath() + "../../../component/conf/META-INF/faces-config.xml";
            URL url = new URL(newPath);

            facesConfigBean = jsfMetaParser.parse(url);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return facesConfigBean;
    }

    private ComponentBean[] getComponentBeanInfo() {

        ComponentBean[] cb = getFacesConfigBean().getComponents();
        return cb;
    }

    private RendererBean[] getRendererBean() {

        RendererBean[] rb = getFacesConfigBean().getRenderKit(
                RenderKitFactory.HTML_BASIC_RENDER_KIT).getRenderers();
        return rb;
    }
}
