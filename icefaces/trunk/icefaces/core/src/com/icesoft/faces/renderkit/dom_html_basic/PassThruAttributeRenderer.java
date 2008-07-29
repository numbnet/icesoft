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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 *
 */

package com.icesoft.faces.renderkit.dom_html_basic;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.CurrentStyle;
import com.icesoft.faces.context.effects.LocalEffectEncoder;
import com.icesoft.faces.renderkit.RendererUtil;

import org.w3c.dom.Element;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for the rendering of html pass thru attributes.
 */
public class PassThruAttributeRenderer {

    public static final String[] passThruAttributeNames =  {
        "accept",
        "accesskey",
        "alt",
        "bgcolor",
        "border",
        "cellpadding",
        "cellspacing",
        "charset",
        "cols",
        "coords",
        "dir",
        "enctype",
        "frame",
        "height",
        "hreflang",
        "lang",
        "longdesc",
        "maxlength",
        "onblur",
        "onchange",
        "onclick",
        "ondblclick",
        "onfocus",
        "onkeydown",
        "onkeypress",
        "onkeyup",
        "onload",
        "onmousedown",
        "onmousemove",
        "onmouseout",
        "onmouseover",
        "onmouseup",
        "onreset",
        "onselect",
        "onsubmit",
        "onunload",
        "rel",
        "rev",
        "rows",
        "rules",
        "shape",
        "size",
        "style",
        "summary",
        "tabindex",
        "target",
        "title",
        "usemap",
        "width",
        "width",
        "onclickeffect",
        "ondblclickeffect",
        "onmousedowneffect",
        "onmouseupeffect",
        "onmousemoveeffect",
        "onmouseovereffect",
        "onmouseouteffect",
        "onchangeeffect",
        "onreseteffect",
        "onsubmiteffect",
        "onkeypresseffect",
        "onkeydowneffect",
        "onkeyupeffect",
        "autocomplete"
    };

    public static final String[] booleanPassThruAttributeNames = {
        "disabled",
        "ismap",     
        "readonly"
    };
    
    static {
        Arrays.sort(passThruAttributeNames);
        Arrays.sort(booleanPassThruAttributeNames);
    }


    /**
     * Render pass thru attributes to the root element of the DOMContext
     * associated with the UIComponent parameter. The excludedAttributes
     * argument is a String array of the names of attributes to omit. Do not
     * render attributes contained in the excludedAttributes argument.
     *
     * @param facesContext
     * @param uiComponent
     * @param excludedAttributes attributes to exclude
     */
    public static void renderAttributes(FacesContext facesContext,
                                        UIComponent uiComponent,
                                        String[] excludedAttributes) {
        renderAttributes(
                facesContext, uiComponent, null, null, excludedAttributes);
    }

    /**
     * Render pass thru attributes to the attributeElement (instead of root) 
     * associated with the UIComponent parameter. The excludedAttributes
     * argument is a String array of the names of attributes to omit. Do not
     * render attributes contained in the excludedAttributes argument.
     *
     * @param facesContext
     * @param uiComponent
     * @param attributeElement
     * @param styleElement The Element to apply styling on
     * @param excludedAttributes attributes to exclude
     */
    public static void renderAttributes(FacesContext facesContext,
                                        UIComponent uiComponent,
                                        Element attributeElement,
                                        Element styleElement,
                                        String[] excludedAttributes) {
        if (excludedAttributes == null) excludedAttributes = new String[0]; 
        if (excludedAttributes.length > 0) {
            Arrays.sort(excludedAttributes);
        }        
        String[] supportedAttributes = (String[]) uiComponent.getAttributes()
                           .get(RendererUtil.SUPPORTED_PASSTHRU_ATT);
        attributeElement = getTargetElement (facesContext, uiComponent, attributeElement);
        if (supportedAttributes == null) {
            renderNonBooleanAttributes(
                facesContext, uiComponent, attributeElement, excludedAttributes);
        } else {
            for (int i=0; i < supportedAttributes.length; i++) {
                if (excludedAttributes.length > 0 &&
                        Arrays.binarySearch(excludedAttributes,  supportedAttributes[i]) > -1){
                           continue;
                }                
                Object value = null;
                if ((value = uiComponent.getAttributes().get(supportedAttributes[i])) != null &&
                        !PassThruAttributeRenderer.attributeValueIsSentinel(value)) {
                    attributeElement.setAttribute(supportedAttributes[i], value.toString());
                }
            } 
        }
        renderBooleanAttributes(
                facesContext, uiComponent, attributeElement, excludedAttributes);
        CurrentStyle.apply(facesContext, uiComponent, styleElement, null);

        if(attributeElement == null) {
            DOMContext domContext =
                    DOMContext.getDOMContext(facesContext, uiComponent);
            Element rootElement = (Element) domContext.getRootNode();
            attributeElement = rootElement;
        }
        LocalEffectEncoder
                .encodeLocalEffects(uiComponent, attributeElement, facesContext);
        renderOnFocus(uiComponent, attributeElement);
        renderOnBlur(attributeElement);
    }

    /**
     * Render the icefaces onfocus handler to the root element. This should be
     * restricted to input type elements and commandlinks.
     *
     * @param uiComponent
     * @param root
     */
    public static void renderOnFocus(UIComponent uiComponent, Element root) {
        // check the type of the root node
        String nodeName = root.getNodeName();

        if (nodeName.equalsIgnoreCase(HTML.ANCHOR_ELEM) ||
            nodeName.equalsIgnoreCase(HTML.INPUT_ELEM) ||
            nodeName.equalsIgnoreCase(HTML.SELECT_ELEM)) {
            String original =
                    (String) uiComponent.getAttributes().get("onfocus");
            String onfocus = "setFocus(this.id);";

            if (original == null) {
                original = "";
            }
            root.setAttribute(HTML.ONFOCUS_ATTR, onfocus + original);
        }
    }

    /**
     * Render the icefaces onblur handler to the root element. This should be
     * restricted to input type elements and commandlinks.
     *
     * @param root
     */
    public static void renderOnBlur(Element root) {
        // check the type of the root node
        // onblur will clear focus id
        String nodeName = root.getNodeName();

        if (nodeName.equalsIgnoreCase(HTML.ANCHOR_ELEM) ||
            nodeName.equalsIgnoreCase(HTML.INPUT_ELEM) ||
            nodeName.equalsIgnoreCase(HTML.SELECT_ELEM)) {
            String original = root.getAttribute("onblur");
            String onblur = "setFocus('');";

            if (original == null) {
                original = "";
            }
            root.setAttribute(HTML.ONBLUR_ATTR, onblur + original);
        }
    }

    private static void renderBooleanAttributes(
            FacesContext facesContext, UIComponent uiComponent,
            Element targetElement,
            String[] excludedAttributes) {

        Object nextPassThruAttributeName;
        Object nextPassThruAttributeValue = null;
        boolean primitiveAttributeValue;
        
        for (int i =0; i < booleanPassThruAttributeNames.length; i++) {
            nextPassThruAttributeName = booleanPassThruAttributeNames[i];
            if (excludedAttributes.length > 0 &&
                    Arrays.binarySearch(excludedAttributes,  nextPassThruAttributeName) > -1){
                       continue;
            }
            nextPassThruAttributeValue = uiComponent.getAttributes().get(
                    nextPassThruAttributeName);
            if (nextPassThruAttributeValue != null) {
                if (nextPassThruAttributeValue instanceof Boolean) {
                    primitiveAttributeValue = ((Boolean)
                            nextPassThruAttributeValue).booleanValue();
                } else {
                    if (!(nextPassThruAttributeValue instanceof String)) {
                        nextPassThruAttributeValue =
                                nextPassThruAttributeValue.toString();
                    }
                    primitiveAttributeValue = (new Boolean((String)
                            nextPassThruAttributeValue)).booleanValue();
                }
                if (primitiveAttributeValue) {
                    targetElement.setAttribute(
                            nextPassThruAttributeName.toString(),
                            nextPassThruAttributeName.toString());
                }
            }
        }
    }

    private static void renderNonBooleanAttributes(
            FacesContext facesContext, UIComponent uiComponent,
            Element targetElement,
            String[] excludedAttributes) {

        Object nextPassThruAttributeName = null;
        Object nextPassThruAttributeValue = null;

        for  (int i = 0; i < passThruAttributeNames.length; i++) {
            nextPassThruAttributeName = passThruAttributeNames[i];
            if (excludedAttributes.length > 0 &&
                    Arrays.binarySearch(excludedAttributes,  nextPassThruAttributeName) > -1){
                       continue;
            }
            nextPassThruAttributeValue =
                    uiComponent.getAttributes().get(nextPassThruAttributeName);
            // Only render non-null attributes.
            // Some components have attribute values
            // set to the Wrapper classes' minimum value - don't render
            // an attribute with this sentinel value.
            if (nextPassThruAttributeValue != null &&
                !attributeValueIsSentinel(nextPassThruAttributeValue)) {
                targetElement.setAttribute(
                        nextPassThruAttributeName.toString(),
                        nextPassThruAttributeValue.toString());
//remove the else clause that was here; it's trying to remove a node
//that doesn't exist
            }
        }

    }


    public static boolean attributeValueIsSentinel(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            if (((Boolean) value).booleanValue() == false) {
                return true;
            }
            return false;
        }
        if (value instanceof Number) {
            if (value instanceof Integer) {
                if (((Integer) value).intValue() == Integer.MIN_VALUE) {
                    return true;
                }
                return false;
            }
            if (value instanceof Long) {
                if (((Long) value).longValue() == Long.MIN_VALUE) {
                    return true;
                }
                return false;
            }
            if (value instanceof Short) {
                if (((Short) value).shortValue() == Short.MIN_VALUE) {
                    return true;
                }
                return false;
            }
            if (value instanceof Float) {
                if (((Float) value).floatValue() == Float.MIN_VALUE) {
                    return true;
                }
                return false;
            }
            if (value instanceof Double) {
                if (((Double) value).doubleValue() == Double.MIN_VALUE) {
                    return true;
                }
                return false;
            }
            if (value instanceof Byte) {
                if (((Byte) value).byteValue() == Byte.MIN_VALUE) {
                    return true;
                }
                return false;
            }
        }
        if (value instanceof Character) {
            if (((Character) value).charValue() == Character.MIN_VALUE) {
                return true;
            }
            return false;
        }
        return false;
    }

    static final String[] getpassThruAttributeNames() {
        return passThruAttributeNames;
    }
    
    static Element getTargetElement(FacesContext facesContext, 
            UIComponent uiComponent, Element targetElement) {
        if(targetElement == null) {
            DOMContext domContext =
                    DOMContext.getDOMContext(facesContext, uiComponent);
            Element rootElement = (Element) domContext.getRootNode();
            if (rootElement == null) {
                throw new FacesException("DOMContext is null");
            }
            targetElement = rootElement;
        }
        return targetElement;
    }
}
