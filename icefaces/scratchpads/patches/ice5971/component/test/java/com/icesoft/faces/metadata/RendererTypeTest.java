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

import junit.framework.Test;
import junit.framework.TestSuite;


import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.RendererBean;
import java.util.HashMap;
import javax.faces.component.UIComponent;

public class RendererTypeTest extends ICECompsTestCase {

    public static Test suite() {
        return new TestSuite(RendererTypeTest.class);
    }

    public static void main() {
        junit.textui.TestRunner.run(RendererTypeTest.suite());
    }

    public void testNamingType() {

        ComponentBean[] oldComponentBeans = getComponentBean();
        ComponentBean[] componentBeans = new ComponentBean[oldComponentBeans.length];
        System.arraycopy(oldComponentBeans, 0, componentBeans, 0, oldComponentBeans.length);

        UIComponent[] oldUIComponentBases = getComponents();
        UIComponent[] uiComponentBases = new UIComponent[oldUIComponentBases.length];
        System.arraycopy(oldUIComponentBases, 0, uiComponentBases, 0, oldUIComponentBases.length);

        HashMap tagNameMap = new HashMap();
        for (int i = 0; i < componentBeans.length; i++) {
            String renderTypeUIComponent = uiComponentBases[i].getRendererType();
            String renderTypeComponentBean = componentBeans[i].getRendererType();

            boolean notSameRenderType = renderTypeUIComponent != null && renderTypeComponentBean != null && !renderTypeUIComponent.trim().equalsIgnoreCase(
                    renderTypeComponentBean.trim());

            String message = "RenderType not the same for Component Class=" + componentBeans[i].getComponentClass() + "\n component renderType=" + renderTypeUIComponent + "\n faces-config declared renderType=" + renderTypeComponentBean + "\n\n";
            assertFalse(message, notSameRenderType);

            RendererBean myRendererBean = ICECompsListHelper.getRenderer(uiComponentBases[i].getFamily(), uiComponentBases[i].getRendererType());

            if (myRendererBean == null || myRendererBean.getRendererType() == null) {
                message = "No Renderer defined for " + uiComponentBases[i].getClass().getName();
                fail(message);
            } 
            if (myRendererBean != null && myRendererBean.getRendererType() != null) {
                boolean notSameType = renderTypeUIComponent != null && !(renderTypeUIComponent.trim().equalsIgnoreCase(myRendererBean.getRendererType()));
                message = "RenderType not the same for Component Class=" + componentBeans[i].getComponentClass() + "\n component renderType=" + renderTypeUIComponent + "\n faces-config declared renderType=" + renderTypeComponentBean +
                        "\n Renderer Bean type=" + myRendererBean.getRendererType() + "\n\n";
                assertFalse(message, notSameType);

                if (!tagNameMap.containsKey(myRendererBean.getTagName())) {
                    tagNameMap.put(myRendererBean.getTagName(), myRendererBean.getTagName());
                } else {
                    message = "Tag name=" + myRendererBean.getTagName() + " already defined, please check component =" + uiComponentBases[i].getClass().getName();
                    fail(message);
                }
            }

            String familyTypeUIComponent = uiComponentBases[i].getFamily();
            String familyTypeComponentBean = componentBeans[i].getComponentFamily();

            boolean notSameRenderTypeTwo = familyTypeUIComponent != null && familyTypeComponentBean != null && !familyTypeUIComponent.trim().equalsIgnoreCase(
                    familyTypeComponentBean.trim());

            String messageTwo = "FamilyType not the same for Component Class=" + componentBeans[i].getComponentClass() + "\n component familyType=" + familyTypeUIComponent + "\n faces-config declared familyType=" + familyTypeComponentBean + "\n\n";
            assertFalse(messageTwo, notSameRenderTypeTwo);
        }

    }
}
