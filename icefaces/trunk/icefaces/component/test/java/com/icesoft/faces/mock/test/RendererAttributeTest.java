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
 
package com.icesoft.faces.mock.test;

import com.icesoft.faces.mock.test.container.MockTestCase;
import com.sun.rave.jsfmeta.beans.RendererBean;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.render.Renderer;
import org.apache.commons.beanutils.PropertyUtils;

public class RendererAttributeTest extends MockTestCase {

    public void testRenderer() {

//        UIViewRoot uiViewRoot = getViewHandler().createView(getFacesContext(), this.getClass().getName() + "_view_id");
//        getFacesContext().setViewRoot(uiViewRoot);

        //limited scope
       // myRenderer();
    }

    public void myRenderer() {
        try {
            UIComponent[] uiComponent = getUIComponents();
            List<UIComponent> myComps = new ArrayList(uiComponent.length);
            for (int i = 0; i < uiComponent.length; i++) {
                Map propsMap = new HashMap();
                CompPropsUtils.describe_useMetaBeanInfo(uiComponent[i], propsMap);
                if (propsMap.get("readonly") != null) {
                    myComps.add(uiComponent[i]);
                    uiComponent[i].getRendererType();
                }
            }

            String componentFamily = null;
            String rendererType = null;
            String rendererClassName = null;

            UIComponent[] hasDisabledComponent = myComps.toArray(new UIComponent[myComps.size()]);

            for (int i = 0; i < hasDisabledComponent.length; i++) {
                UIComponent tmpComponent = hasDisabledComponent[i];

                try {
                    PropertyUtils.setSimpleProperty(tmpComponent, "disabled", true);
                } catch (IllegalAccessException illegalAccessException) {
                    print(illegalAccessException.getMessage());
                } catch (InvocationTargetException invocationTargetException) {
                    print(invocationTargetException.getMessage());
                } catch (NoSuchMethodException noSuchMethodException) {
                    print(noSuchMethodException.getMessage());
                }

                componentFamily = tmpComponent.getFamily();
                rendererType = tmpComponent.getRendererType();
                RendererBean rendererBean = getRendererBean(componentFamily, rendererType);
                if (rendererBean == null) {
                    continue;
                }
                rendererClassName = rendererBean.getRendererClass();
                Renderer renderer = (Renderer) Class.forName(rendererClassName).newInstance();
                String message = "\n\tRenderer="+rendererClassName+" Component="+ tmpComponent.getClass().getName();
                print(message);

            }



        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RendererAttributeTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(RendererAttributeTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(RendererAttributeTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void print(String message) {
        Logger.getLogger(RendererAttributeTest.class.getName()).log(Level.INFO, message);
    }
}
