/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.ace.generator.xmlbuilder;

import org.icefaces.ace.generator.utils.Utility;
import org.icefaces.ace.meta.annotation.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


public class FacesConfigBuilder extends XMLBuilder{
    public FacesConfigBuilder() {
        super("faces-config.xml");
        Element root = getDocument().createElement("faces-config");
        root.setAttribute("xmlns",              "http://java.sun.com/xml/ns/javaee");
        root.setAttribute("xmlns:xsi",          "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xsi:schemaLocation", "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_2_0.xsd");
        root.setAttribute("version",            "2.0");
        root.setAttribute("metadata-complete",  "false");
        getDocument().appendChild(root);
        addEffectBehavior("Animation");
        Element render_kit = getDocument().createElement("render-kit");
        root.appendChild(render_kit);        
    }
    
    public void addComponentInfo(Class clazz, Component component) {
        Element compElement = getDocument().createElement("component");
        Element compTypeElement = getDocument().createElement("component-type"); 
        Element compClassElement = getDocument().createElement("component-class"); 
        Text comp_class =getDocument().createTextNode(component.componentClass());

        getDocument().getDocumentElement().appendChild(compElement);
        compElement.appendChild(compTypeElement);
        compElement.appendChild(compClassElement);
        try {
            Text comp_type_text = getDocument().createTextNode(component.componentType());
            System.out.println("TYPE in CONFIG "+ comp_type_text);
            compTypeElement.appendChild(comp_type_text);
            compClassElement.appendChild(comp_class);
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        
    }



    public void addRendererInfo(Class clazz, Component component) {
        try {
            //renderkit
            String rendererType = component.rendererType();
            if ("".equals(rendererType)) return;
            Text comp_renderer_type_text = getDocument().createTextNode(rendererType);
            
            String componentFamily = Utility.getFamily(component);

            Text comp_family_text = getDocument().createTextNode(componentFamily);
            
            String rendererClass = component.rendererClass();
            if (rendererClass.equals(component.EMPTY)) {
                rendererClass = component.componentClass()+ "Renderer";
            }
            Text rendererClassText = getDocument().createTextNode(rendererClass);
            
            Element render_kit = (Element) getDocument().getElementsByTagName("render-kit").item(0);
            
            Element rendererElement = getDocument().createElement("renderer");            
            Element compFamilyElement = getDocument().createElement("component-family");
            Element rendererTypeElement = getDocument().createElement("renderer-type"); 
            Element rendererClassElement = getDocument().createElement("renderer-class"); 
            render_kit.appendChild(rendererElement);
            rendererElement.appendChild(compFamilyElement);
            rendererElement.appendChild(rendererTypeElement);
            rendererElement.appendChild(rendererClassElement);
            rendererTypeElement.appendChild(comp_renderer_type_text);
            compFamilyElement.appendChild(comp_family_text);
            rendererClassElement.appendChild(rendererClassText);
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    public void addEntry(Class clazz, Component component) {
        this.addComponentInfo(clazz, component);
        this.addRendererInfo(clazz, component);
    }
    
    private void addEffectBehavior(String name) {
        Element behavior = getDocument().createElement("behavior");
        Element behaviorId = getDocument().createElement("behavior-id"); 
        behaviorId.appendChild(getDocument().createTextNode("org.icefaces.ace.animation."+ name));
        
        Element behaviorClass = getDocument().createElement("behavior-class"); 
        behaviorClass.appendChild(getDocument().createTextNode("org.icefaces.ace.component.animation."+ name + "Behavior"));
        behavior.appendChild(behaviorId);
        behavior.appendChild(behaviorClass);
        getDocument().getDocumentElement().appendChild(behavior);
    }
 
    
    /*
    public void write() {
        //<application>
        //  <system-event-listener>
        //    <system-event-listener-class>org.icefaces.event.FormSubmit</system-event-listener-class>
        //    <system-event-class>javax.faces.event.PostAddToViewEvent</system-event-class>
        //  </system-event-listener>
        //</application>
        Element faces_config = (Element) getDocument().getElementsByTagName("faces-config").item(0);

        super.write();
    }
    */
}
