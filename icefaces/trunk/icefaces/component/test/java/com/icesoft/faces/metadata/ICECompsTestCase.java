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



import junit.framework.TestCase;


import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.RendererBean;
import javax.faces.component.UIComponent;

public class ICECompsTestCase extends TestCase {

    private static ComponentBean[] componentBeans;
    private static UIComponent[] uiComponent;
    private static RendererBean[] rendererBeans;

    protected void setUp() {

        if (componentBeans == null || uiComponent == null) {
         
            componentBeans = ICECompsListHelper.getComponentBeans();
            rendererBeans = ICECompsListHelper.getRendererBean();
            uiComponent = ICECompsListHelper.getComponents();
        }
    }

    public UIComponent[] getComponents() {
        return uiComponent;
    }

    public ComponentBean[] getComponentBean() {
        return componentBeans;
    }

    public RendererBean[] getRendererBean() {
        return rendererBeans;
    }

    public ComponentBean getComponentBean(UIComponent uiComponent){        
        for (int i = 0; i < componentBeans.length; i++) {
            ComponentBean componentBean = componentBeans[i];
            if(uiComponent.getClass().getName().equals(componentBean.getComponentClass())){
                return componentBean;
            }
        }
        return null;
    }
}
