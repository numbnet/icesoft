package com.icesoft.faces.metadata;

import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.PropertyBean;

import java.lang.reflect.InvocationTargetException;
import javax.faces.component.UIComponent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.beanutils.BeanUtils;

public class BeanImpPropertiesTest extends ICECompsTestCase {

    public static Test suite() {
        return new TestSuite(BeanImpPropertiesTest.class);
    }

    public static void main() {
        junit.textui.TestRunner.run(BeanImpPropertiesTest.suite());
    }

    public void testComponentProperties() {

        UIComponent[] oldAllComps = getComponents();
        UIComponent[] components = new UIComponent[oldAllComps.length];
        System.arraycopy(oldAllComps, 0, components, 0, oldAllComps.length);
        TestPropDataProvider testDataProvider = new TestPropDataProvider();
        for (int j = 0; j < components.length; j++) {
            if (!components[j].getClass().getName().startsWith("com.icesoft")) {
                continue;
            }
            ComponentBean componentBean = getComponentBean(components[j]);
            PropertyBean[] pds = componentBean.getProperties();
            String message = null;

            for (int i = 0; i < pds.length; i++) {
                try {
                    String propertyName = pds[i].getPropertyName();
                    if (pds[i].getPropertyClass() == null || pds[i].getPropertyClass().equalsIgnoreCase("String")) {
                        pds[i].setPropertyClass("java.lang.String");
                    }

                    if(propertyName.equalsIgnoreCase("expanded")){
                        continue;
                    }

                    message = "Failed under test Component= " + components[j].getClass().getName() + " property name= " + propertyName + "\n" + "\tproperty class=" + pds[i].getPropertyClass();
                    Object propValue = testDataProvider.getSimpleTestObject(pds[i].getPropertyClass());
                    BeanUtils.setProperty(components[j], propertyName, propValue);
                } catch (java.lang.IllegalArgumentException ex) {
                    ex.printStackTrace();
                    fail(message);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                    fail(message);
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                    fail(message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    fail(message);
                }

            }
        }
    }
}
