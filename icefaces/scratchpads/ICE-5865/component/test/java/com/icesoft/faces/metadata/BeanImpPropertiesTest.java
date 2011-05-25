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

package com.icesoft.faces.metadata;

import com.sun.faces.util.ReflectionUtils;
import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.PropertyBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.faces.component.UIComponent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.beanutils.BeanUtils;

import sun.reflect.misc.MethodUtil;

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
                boolean newLine = false;
                String propertyName = null; 
                String methodName = null;               
                try {
                    propertyName = pds[i].getPropertyName();                    

                    if (pds[i].getPropertyClass() == null || pds[i].getPropertyClass().equalsIgnoreCase("String")) {
                        pds[i].setPropertyClass("java.lang.String");
                    }

                    if(propertyName.equalsIgnoreCase("expanded")){
                        continue;
                    }
                    methodName = propertyName.substring(0, 1).toUpperCase()+propertyName.substring(1);
                    message = "Failed under test Component= " + components[j].getClass().getName() + " property name= " + propertyName +", type boolean" + "\n" + "\tset"+methodName + "(..) doesn't take boolean";
                    Object propValue = testDataProvider.getSimpleTestObject(pds[i].getPropertyClass());
                    if(pds[i].getPropertyClass().equals("boolean")){
                        //check if setMethod found
                        Method setMethod = ReflectionUtils.lookupMethod(components[j].getClass(), "set"+methodName, boolean.class);
                        if(setMethod == null){
                            System.out.println(" "+message);
                        }
                        
                    }
                  //set the value
                    BeanUtils.setProperty(components[j], propertyName, propValue);
                } catch (java.lang.IllegalArgumentException ex) {
                    ex.printStackTrace();
                    fail(message);
                    newLine = true;
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                    fail(message);
                    newLine = true;                    
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                    fail(message);
                    newLine = true;                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    fail(message);
                    newLine = true;                    
                }
                
                
                if(pds[i].getPropertyClass().equals("boolean")){
                    try {
                        //check if isMethod exist
                        Method m = MethodUtil.getMethod(components[j].getClass(), "is"+methodName, new Class[0]);
                        //when found, check the return type shouldn't be Boolean instead boolean. Log the message
                        if (m.getReturnType()== Boolean.class) {
                            message = " Failed under test Component= " + components[j].getClass().getName() + " property name= " + propertyName +  ", type boolean \tis"+methodName + "() found return type "+ m.getReturnType();
                            System.out.println(message);
                            newLine = true;
                        }
                    } catch (NoSuchMethodException nsme) {
                        //log if isMethod was not exist
                        message = " Failed under test Component= " + components[j].getClass().getName() + " property name= " + propertyName +  ", type boolean \tis"+methodName + "() not found";
                        System.out.println(message);
                        newLine = true;
                    }
                }
                
                //for output format and readability
                if (newLine) {
                    System.out.println("\n");
                }
            }
        }
    }
}
