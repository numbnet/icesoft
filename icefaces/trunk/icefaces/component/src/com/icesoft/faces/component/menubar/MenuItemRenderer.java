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

package com.icesoft.faces.component.menubar;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.InvalidComponentTypeException;
import com.icesoft.faces.component.PORTLET_CSS_DEFAULT;
import com.icesoft.faces.component.ext.HtmlCommandLink;
import com.icesoft.faces.component.ext.HtmlGraphicImage;
import com.icesoft.faces.component.ext.HtmlOutputText;
import com.icesoft.faces.component.ext.HtmlPanelGroup;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.util.CoreUtils;
import com.icesoft.faces.util.DOMUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import java.beans.Beans;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.List;


public class MenuItemRenderer extends MenuItemRendererBase {

    private static final String HIDDEN_FIELD_NAME = "cl";


    private static String SUB = "_sub";
    private static String KEYWORD_NULL = "null";
    private static String KEYWORD_THIS = "this";
    private final String DEFAULT_IMAGEDIR = "/xmlhttp/css/xp/css-images/";
    private static final String SUBMENU_IMAGE = "submenu.gif";
    private static String LINK_SUFFIX = "link";

    public void decode(FacesContext facesContext, UIComponent uiComponent) {

        validateParameters(facesContext, uiComponent, null);
        if (isStatic(uiComponent)) {
            return;
        }
        String componentId = uiComponent.getClientId(facesContext);
        Map requestParameterMap =
                facesContext.getExternalContext().getRequestParameterMap();
        String hiddenFieldName = deriveCommonHiddenFieldName(facesContext,
                                                             (MenuItem) uiComponent);
        String hiddenFieldNameInRequestMap =
                (String) requestParameterMap.get(hiddenFieldName);

        if (hiddenFieldNameInRequestMap == null
            || hiddenFieldNameInRequestMap.equals("")) {
            // this command link did not invoke the submit
            return;
        }

        // debugging
        //examineRequest(facesContext, uiComponent, requestParameterMap, hiddenFieldName, hiddenFieldNameInRequestMap);
        String commandLinkClientId = componentId + ":" + LINK_SUFFIX;
        if (hiddenFieldNameInRequestMap.equals(commandLinkClientId)) {
            ActionEvent actionEvent = new ActionEvent(uiComponent);
            uiComponent.queueEvent(actionEvent);
        }
    }


    // this component renders its children, so, this method will be called once
    // for each top-level menu node. From there, this component will manage
    // the rendering of all children components. The idea is to end up with
    // a fairly flat structure. There will exist a master div that contains the
    // entire menu. Inside that div there will exist a first-order child div for
    // each top level menu item and there will exist a first-order child div for
    // each submenu. Inside the submenu div there will exist a div to hold each
    // menu item in the submenu. Note that there is no nesting of submenu items
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        if (!(uiComponent.getParent() instanceof MenuBar) &&
            !(uiComponent.getParent() instanceof MenuItems)) {
            throw new InvalidComponentTypeException(
                    "MenuBar expected as parent of top-level MenuItem");
        }
        // If static model declaration (in the jsp) is employed then the
        // immediate parent will be the Menu component
        // Else if the model declaration is in the bean class then there
        // is a MenuItems between the MenuItem and the Menu component
        MenuBar menuComponent = null;
        if (uiComponent.getParent() instanceof MenuBar) {
            menuComponent = (MenuBar) uiComponent.getParent();
        } else if (uiComponent.getParent().getParent() instanceof MenuBar) {
            menuComponent = (MenuBar) uiComponent.getParent().getParent();
        } else {
            throw new InvalidComponentTypeException("Expecting MenuBar");
        }

        // is vertical ?
        boolean vertical = menuComponent.getOrientation().equalsIgnoreCase(
                MenuBar.ORIENTATION_VERTICAL);

        validateParameters(facesContext, uiComponent, MenuItemBase.class);

        // first render
        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        String clientId = uiComponent.getClientId(facesContext);
        if (!domContext.isInitialized()) {
            Element topLevelDiv = domContext.createRootElement(HTML.DIV_ELEM);
            topLevelDiv.setAttribute(HTML.ID_ATTR, clientId);
        }
        Element topLevelDiv = (Element) domContext.getRootNode();
        topLevelDiv.setAttribute(HTML.NAME_ATTR, "TOP_LEVEL");

        String rootItemSubClass = CSS_DEFAULT.MENU_BAR_ITEM_STYLE;
        if (vertical) {
            rootItemSubClass = CSS_DEFAULT.MENU_BAR_VERTICAL_SUFFIX_STYLE +
                                rootItemSubClass;
        }
        String qualifiedName = ((MenuItem) uiComponent).
        getUserDefinedStyleClass(menuComponent.getItemStyleClass(), 
                rootItemSubClass);
        
        if (uiComponent.getChildCount() > 0) {
            topLevelDiv.setAttribute(HTML.CLASS_ATTR, CoreUtils.addPortletStyleClassToQualifiedClass(
                    qualifiedName, rootItemSubClass, PORTLET_CSS_DEFAULT.PORTLET_MENU_CASCADE_ITEM));
            String displayEvent = HTML.ONMOUSEOVER_ATTR;
            if (vertical) {
                String supermenu = menuComponent.getClientId(facesContext);
                Element parentNode = (Element) topLevelDiv.getParentNode();
                if (parentNode.getAttribute(HTML.NAME_ATTR).equals("TOP_LEVEL_SUBMENU")) {
                    supermenu += "_sub";
                }
                topLevelDiv.setAttribute(displayEvent,
                                         "Ice.Menu.hideOrphanedMenusNotRelatedTo(this);" +
                                         expand(supermenu, clientId + "_sub",
                                                KEYWORD_THIS) + "Ice.Menu.appendHoverClasses(this);");
            } else {
                topLevelDiv.setAttribute(displayEvent,
                                         "Ice.Menu.hideOrphanedMenusNotRelatedTo(this);" +
                                         expand("this", clientId + "_sub",
                                                KEYWORD_NULL) + "Ice.Menu.appendHoverClasses(this);");
            }
        } else {
            topLevelDiv.setAttribute(HTML.CLASS_ATTR, CoreUtils.addPortletStyleClassToQualifiedClass(
                    qualifiedName, rootItemSubClass, PORTLET_CSS_DEFAULT.PORTLET_MENU_ITEM));
            topLevelDiv.setAttribute(HTML.ONMOUSEOVER_ATTR,
                                     "Ice.Menu.hideOrphanedMenusNotRelatedTo(this);Ice.Menu.appendHoverClasses(this);");
        }
        topLevelDiv.setAttribute(HTML.ONMOUSEOUT_ATTR, "Ice.Menu.removeHoverClasses(this);");

        DOMContext.removeChildren(topLevelDiv);
        Element masterDiv = topLevelDiv;
        while(masterDiv != null &&
              !masterDiv.getAttribute(HTML.NAME_ATTR).equals("MENU") )
        {
            masterDiv = (Element) masterDiv.getParentNode();
        }

        renderAnchor(facesContext, domContext, (MenuItem) uiComponent,
                     topLevelDiv, menuComponent, vertical);

        if ((uiComponent.getChildCount() > 0) &&
            (((MenuItem) uiComponent).isChildrenMenuItem())) {
            renderChildrenRecursive(facesContext, menuComponent, uiComponent,
                                    vertical, masterDiv);

        }
    }

    private String expand(String supermenu, String submenu, String submenuDiv) {
        // delimit ids to force resolution from ids to elements
        if (!(supermenu.equalsIgnoreCase(KEYWORD_NULL)) &&
            !(supermenu.equalsIgnoreCase(KEYWORD_THIS))) {
            supermenu = "$('" + supermenu + "')";
        }
        if (!(submenu.equalsIgnoreCase(KEYWORD_NULL)) &&
            !(submenu.equalsIgnoreCase(KEYWORD_THIS))) {
            submenu = "$('" + submenu + "')";
        }
        if (!(submenuDiv.equalsIgnoreCase(KEYWORD_NULL)) &&
            !(submenuDiv.equalsIgnoreCase(KEYWORD_THIS))) {
            submenuDiv = "$('" + submenuDiv + "')";
        }
        return "Ice.Menu.show(" + supermenu + "," + submenu + "," + submenuDiv +
               ");";
    }

    protected static String deriveCommonHiddenFieldName(
            FacesContext facesContext,
            UIComponent uiComponent) {

        if (Beans.isDesignTime()){
            return "";
        }
        UIComponent parentNamingContainer = findForm(uiComponent);
        String parentClientId = parentNamingContainer.getClientId(facesContext);
        String hiddenFieldName = parentClientId
                                 + NamingContainer.SEPARATOR_CHAR
                                 + UIViewRoot.UNIQUE_ID_PREFIX
                                 + HIDDEN_FIELD_NAME;
        return hiddenFieldName;
    }

    // this method is used to render topLevel horizontal menu items
    // horizontal menu items do not have icons or subMenu indicators
    private Element makeTopLevelAnchor(FacesContext facesContext,
                                       MenuItem menuItem,
                                       MenuBar menuBar) {

        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, menuItem);
        Element anchor = domContext.createElement(HTML.ANCHOR_ELEM);
        if (!menuItem.isDisabled()) {
            anchor.setAttribute(HTML.HREF_ATTR, menuItem.getLink());
            if (menuItem.getTarget() != null) {
                anchor.setAttribute(HTML.TARGET_ATTR, menuItem.getTarget());
            }
        }
        // create div
        Element div = domContext.createElement(HTML.DIV_ELEM);

        anchor.appendChild(div);

        // only render icons if noIcons is false
        if ((!menuBar.getNoIcons().equalsIgnoreCase("true")) &&
            (getIcon(menuItem) != null)) {
            // do not render icon if it is the default blank image
            // this only applies to horizontal top level menu items
            if (!getIcon(menuItem).endsWith(MenuItem.DEFAULT_ICON)) {
                Element iconImg = domContext.createElement(HTML.IMG_ELEM);
                iconImg.setAttribute(HTML.SRC_ATTR, CoreUtils.resolveResourceURL(facesContext, getIcon(menuItem)));
                iconImg.setAttribute(HTML.STYLE_ATTR, "border:none;");
                iconImg.setAttribute(HTML.CLASS_ATTR, menuItem.
                        getUserDefinedStyleClass(menuBar.getItemImageStyleClass(), 
                                CSS_DEFAULT.MENU_BAR_ITEM_STYLE+
                                CSS_DEFAULT.MENU_ITEM_IMAGE_STYLE));
                div.appendChild(iconImg);
            }
        }

        // create a span for text
        Element span = domContext.createElement(HTML.SPAN_ELEM);
        if (!menuItem.isDisabled()) {
            anchor.setAttribute(HTML.STYLE_CLASS_ATTR, "iceLink");
        } else {
            anchor.setAttribute(HTML.STYLE_CLASS_ATTR, "iceLink-dis");
        }
        span.setAttribute(HTML.CLASS_ATTR, menuItem.
                getUserDefinedStyleClass(menuBar.getItemLabelStyleClass(), 
                        CSS_DEFAULT.MENU_BAR_ITEM_LABEL_STYLE));
        div.appendChild(span);
        // create text
        Node text = domContext.createTextNode(DOMUtils.escapeAnsi(menuItem.getValue().toString()));
        span.appendChild(text);

        return anchor;
    }

    // this method is used to render top level vertical menu items
    private Element makeTopLevelVerticalAnchor(FacesContext facesContext,
                                               MenuItem menuItem,
                                               MenuBar menuBar) {

        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, menuItem);
        Element anchor = domContext.createElement(HTML.ANCHOR_ELEM);
        if (!menuItem.isDisabled()){
            anchor.setAttribute(HTML.HREF_ATTR, menuItem.getLink());
            if (menuItem.getTarget() != null) {
                anchor.setAttribute(HTML.TARGET_ATTR, menuItem.getTarget());
            }
        }
        // create div
        Element div = domContext.createElement(HTML.DIV_ELEM);

        anchor.appendChild(div);

        if (menuItem.getChildCount() > 0) {
            Element subImg = domContext.createElement(HTML.IMG_ELEM);
            subImg.setAttribute(HTML.SRC_ATTR, CoreUtils.resolveResourceURL(facesContext, getSubMenuImage(menuBar)));
            subImg.setAttribute(HTML.STYLE_ATTR, "border:none;");
            subImg.setAttribute(HTML.CLASS_ATTR,
                                menuBar.getSubMenuIndicatorStyleClass());
            div.appendChild(subImg);
        }

        // only render icons if noIcons is false
        if ((!menuBar.getNoIcons().equalsIgnoreCase("true")) &&
            (getIcon(menuItem) != null)) {
            Element iconImg = domContext.createElement(HTML.IMG_ELEM);
            iconImg.setAttribute(HTML.SRC_ATTR, CoreUtils.resolveResourceURL(facesContext, getIcon(menuItem)));
            iconImg.setAttribute(HTML.STYLE_ATTR, "border:none;");
            iconImg.setAttribute(HTML.CLASS_ATTR, menuItem.
                    getUserDefinedStyleClass(menuBar.getItemImageStyleClass(), 
                            CSS_DEFAULT.MENU_BAR_VERTICAL_SUFFIX_STYLE+
                            CSS_DEFAULT.MENU_BAR_ITEM_STYLE+
                            CSS_DEFAULT.MENU_ITEM_IMAGE_STYLE));
            div.appendChild(iconImg);
        }

        // create a span for text
        Element span = domContext.createElement(HTML.SPAN_ELEM);
        if (!menuItem.isDisabled()) {
            anchor.setAttribute(HTML.STYLE_CLASS_ATTR,"iceLink");
        } else {
            anchor.setAttribute(HTML.STYLE_CLASS_ATTR,"iceLink-dis");
        }
        span.setAttribute(HTML.CLASS_ATTR,  menuItem.
                getUserDefinedStyleClass(menuBar.getItemLabelStyleClass(), 
                        CSS_DEFAULT.MENU_BAR_VERTICAL_SUFFIX_STYLE+
                        CSS_DEFAULT.MENU_BAR_ITEM_LABEL_STYLE));

        div.appendChild(span);
        // create text
        Node text = domContext.createTextNode(DOMUtils.escapeAnsi(menuItem.getValue().toString()));
        span.appendChild(text);

        return anchor;
    }

    private Element makeAnchor(FacesContext facesContext, DOMContext domContext,
                               MenuItem menuItem, MenuBar menuBar) {

        Element anchor = domContext.createElement(HTML.ANCHOR_ELEM);
        if (!menuItem.isDisabled()) {
            anchor.setAttribute(HTML.HREF_ATTR, menuItem.getLink());
            if (menuItem.getTarget() != null) {
                anchor.setAttribute(HTML.TARGET_ATTR, menuItem.getTarget());
            }
        }
        // create div
        Element div = domContext.createElement(HTML.DIV_ELEM);

        anchor.appendChild(div);

        if (menuItem.getChildCount() > 0) {
            Element subImg = domContext.createElement(HTML.IMG_ELEM);
            subImg.setAttribute(HTML.SRC_ATTR, CoreUtils.resolveResourceURL(facesContext, getSubMenuImage(menuBar)));
            subImg.setAttribute(HTML.STYLE_ATTR, "border:none;");
            subImg.setAttribute(HTML.CLASS_ATTR,
                                menuBar.getSubMenuIndicatorStyleClass());
            div.appendChild(subImg);
        }

        // only render icons if noIcons is false
        if ((!menuBar.getNoIcons().equalsIgnoreCase("true")) &&
            (getIcon(menuItem) != null)) {
            Element iconImg = domContext.createElement(HTML.IMG_ELEM);
            iconImg.setAttribute(HTML.SRC_ATTR, CoreUtils.resolveResourceURL(facesContext, getIcon(menuItem)));
            iconImg.setAttribute(HTML.STYLE_ATTR, "border:none;");
            iconImg.setAttribute(HTML.CLASS_ATTR, menuItem.getImageStyleClass());
            div.appendChild(iconImg);
        }

        // create a span for text
        Element span = domContext.createElement(HTML.SPAN_ELEM);
        if (!menuItem.isDisabled()) {
            anchor.setAttribute(HTML.STYLE_CLASS_ATTR,"iceLink");
        } else {
            anchor.setAttribute(HTML.STYLE_CLASS_ATTR,"iceLink-dis");
        }
        span.setAttribute(HTML.CLASS_ATTR, menuItem.getLabelStyleClass());

        div.appendChild(span);
        // create text
        Node text = domContext.createTextNode(DOMUtils.escapeAnsi(menuItem.getValue().toString()));
        span.appendChild(text);

        return anchor;
    }

    private void renderChildrenRecursive(FacesContext facesContext,
                                         MenuBar menuComponent,
                                         UIComponent uiComponent,
                                         boolean vertical, Element masterDiv) {
//StackTraceElement[] ste = Thread.currentThread().getStackTrace();
//System.out.println("renderChildrenRecursive()  "+ste.length+"  called in: " + this);
//System.out.println("renderChildrenRecursive()  "+ste.length+"    uiComponent: " + uiComponent);
//if(uiComponent instanceof MenuItem)
//  System.out.println("renderChildrenRecursive()  "+ste.length+"    Name: " + ((MenuItem)uiComponent).getValue());
        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, uiComponent);
        // create the div that will hold all the sub menu items
        Element submenuDiv = domContext.createElement(HTML.DIV_ELEM);
        submenuDiv.setAttribute(HTML.NAME_ATTR, "SUBMENU");
        String subMenuDivId = uiComponent.getClientId(facesContext) + SUB;
        submenuDiv.setAttribute(HTML.ID_ATTR, subMenuDivId);

        
        submenuDiv.setAttribute(HTML.CLASS_ATTR, menuComponent.getSubMenuStyleClass());
        submenuDiv.setAttribute(HTML.STYLE_ATTR, "display:none");
        masterDiv.appendChild(submenuDiv);
        // check if this menuItem is disabled, if it is lets disable the  children
        // render each menuItem in this submenu
        boolean disabled = false;
        Boolean disObj = (Boolean) uiComponent.getAttributes().get("disabled");
        if(disObj != null && disObj.booleanValue())
            disabled = true;
        for (int childIndex = 0; childIndex < uiComponent.getChildCount(); childIndex++) {
            UIComponent nextSubMenuItem =
                (UIComponent) uiComponent.getChildren().get(childIndex);
//System.out.println("renderChildrenRecursive()  "+ste.length+"      Render  childIndex: " + childIndex + "  child: " + nextSubMenuItem);
            if(nextSubMenuItem instanceof MenuItem) {
//System.out.println("renderChildrenRecursive()  "+ste.length+"              MenuItem  : " + ((MenuItem)nextSubMenuItem).getValue());
                renderSubMenuItem(
                    facesContext, domContext,
                    (MenuItem) nextSubMenuItem, menuComponent,
                    disabled, vertical,
                    submenuDiv, subMenuDivId);
            }
            else if(nextSubMenuItem instanceof MenuItems) {
//System.out.println("renderChildrenRecursive()  "+ste.length+"              MenuItems");
                renderSubMenuItems(
                    facesContext, domContext,
                    (MenuItems) nextSubMenuItem, menuComponent,
                    disabled, vertical,
                    submenuDiv, subMenuDivId);
            }
            else if(nextSubMenuItem instanceof MenuItemSeparator) {
//System.out.println("renderChildrenRecursive()  "+ste.length+"              MenuItemSeparator");
                renderSubMenuItemSeparator(
                    domContext, (MenuItemSeparator) nextSubMenuItem, submenuDiv);
            }
        }

        // recurse
        // check if parent is disabled , if it is the child items should also be disabled.
        // we should not render child MenuItems of a disabled menuItem

        for (int childIndex = 0; childIndex < uiComponent.getChildCount(); childIndex++) {
            UIComponent nextSubMenuItem =
                (UIComponent) uiComponent.getChildren().get(childIndex);
//System.out.println("renderChildrenRecursive()  "+ste.length+"      Recurse  childIndex: " + childIndex + "  child: " + nextSubMenuItem);
            if(nextSubMenuItem instanceof MenuItem) {
                MenuItem mi = (MenuItem) nextSubMenuItem;
//System.out.println("renderChildrenRecursive()  "+ste.length+"               MenuItem  : " + mi.getValue());
                if(mi.isChildrenMenuItem()) {
                    renderChildrenRecursive(
                        facesContext, menuComponent, mi,
                        vertical, masterDiv);
                }
            }
            else if(nextSubMenuItem instanceof MenuItems) {
//System.out.println("renderChildrenRecursive()  "+ste.length+"               MenuItems");
                MenuItems mis = (MenuItems) nextSubMenuItem;
                List kids = mis.prepareChildren();
                if(kids != null) {
                    for(int kidIndex = 0; kidIndex < kids.size(); kidIndex++) {
                        UIComponent nextKid = (UIComponent) kids.get(kidIndex);
//System.out.println("renderChildrenRecursive()  "+ste.length+"      Recurse  kidIndex: " + kidIndex + "  kid: " + nextKid);
                        if(nextKid instanceof MenuItem) {
                            MenuItem mi = (MenuItem) nextKid;
//System.out.println("renderChildrenRecursive()  "+ste.length+"               MenuItem  : " + mi.getValue());
                            if(mi.isChildrenMenuItem()) {
                                renderChildrenRecursive(
                                    facesContext, menuComponent, mi,
                                    vertical, masterDiv);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void renderSubMenuItemSeparator(DOMContext domContext, MenuItemSeparator nextSubMenuItem, Element submenuDiv) {
        Element subMenuItemDiv = domContext.createElement(HTML.DIV_ELEM);
        submenuDiv.appendChild(subMenuItemDiv);
        renderSeparatorDiv(domContext, subMenuItemDiv, nextSubMenuItem);
    }
    
    private void renderSubMenuItems(
        FacesContext facesContext, DOMContext domContext,
        MenuItems nextSubMenuItems, MenuBar menuComponent,
        boolean disabled, boolean vertical,
        Element submenuDiv, String subMenuDivId)
    {
        List children = nextSubMenuItems.prepareChildren();
        if(children != null) {
            for(int i = 0; i < children.size(); i++) {
                MenuItemBase mib = (MenuItemBase) children.get(i);
                if(mib instanceof MenuItem) {
                    renderSubMenuItem(
                        facesContext, domContext,
                        (MenuItem) mib, menuComponent,
                        disabled, vertical,
                        submenuDiv, subMenuDivId);
                }
                else if(mib instanceof MenuItemSeparator) {
                    renderSubMenuItemSeparator(
                        domContext, (MenuItemSeparator) mib, submenuDiv);
                }
            }
        }
    }
    
    private void renderSubMenuItem(
        FacesContext facesContext, DOMContext domContext,
        MenuItem nextSubMenuItem, MenuBar menuComponent,
        boolean disabled, boolean vertical,
        Element submenuDiv, String subMenuDivId)
    {
        Element subMenuItemDiv = domContext.createElement(HTML.DIV_ELEM);
        submenuDiv.appendChild(subMenuItemDiv);
        String qualifiedName = nextSubMenuItem.getStyleClass();
        subMenuItemDiv.setAttribute(HTML.NAME_ATTR, "ITEM");
        String subMenuItemClientId = nextSubMenuItem.getClientId(facesContext);
        subMenuItemDiv.setAttribute(HTML.ID_ATTR, subMenuItemClientId);
        if (nextSubMenuItem.isChildrenMenuItem()) {
            subMenuItemDiv.setAttribute(HTML.CLASS_ATTR,
                CoreUtils.addPortletStyleClassToQualifiedClass(
                    qualifiedName, qualifiedName,
                    PORTLET_CSS_DEFAULT.PORTLET_MENU_CASCADE_ITEM));
            subMenuItemDiv.setAttribute(HTML.ONMOUSEOVER_ATTR,
                "Ice.Menu.hideOrphanedMenusNotRelatedTo(this);" +
                expand(subMenuDivId, subMenuItemClientId + SUB, KEYWORD_THIS) +
                "Ice.Menu.appendHoverClasses(this);");
        } else {
            subMenuItemDiv.setAttribute(HTML.CLASS_ATTR,
                CoreUtils.addPortletStyleClassToQualifiedClass(
                    qualifiedName, qualifiedName,
                    PORTLET_CSS_DEFAULT.PORTLET_MENU_ITEM));
            subMenuItemDiv.setAttribute(HTML.ONMOUSEOVER_ATTR,
                "Ice.Menu.hideOrphanedMenusNotRelatedTo(this);Ice.Menu.appendHoverClasses(this);");
        }
        subMenuItemDiv.setAttribute(HTML.ONMOUSEOUT_ATTR,
            "Ice.Menu.removeHoverClasses(this);");
        // if parent is disabled apply the disabled attribute value of the parent menuItem to this submenuItem
        if (disabled) {
            nextSubMenuItem.setDisabled(disabled);
        }
        // add a command link if we need one
        renderAnchor(facesContext, domContext,
            nextSubMenuItem, subMenuItemDiv,
            menuComponent, vertical);
    }
    
    /**
     * @param facesContext
     * @param domContext
     * @param nextSubMenuItem
     * @param subMenuItemDiv
     */
    private void renderAnchor(FacesContext facesContext, DOMContext domContext,
                              MenuItem nextSubMenuItem,
                              Element subMenuItemDiv,
                              MenuBar menuComponent, boolean vertical) {

        // check if the nextSubMenuItem isRendered
        if (!nextSubMenuItem.isRendered()) {
            return;
        }

        // check if this is a Top Level Menu or MenuItems
        if ((nextSubMenuItem.getParent() instanceof MenuBar) ||
            ((nextSubMenuItem.getParent() instanceof MenuItems)
             && (nextSubMenuItem.getParent().getParent() instanceof MenuBar))) {
            // handle action/actionListeners if attached to top level menuItems
            if (nextSubMenuItem.hasActionOrActionListener()) {
                HtmlCommandLink link = new HtmlCommandLink();
                if (nextSubMenuItem.isDisabled()) {
                    link.setDisabled(true);
                } else { // only add action and actionlisteners on enabled menuItems
                    MethodBinding action = nextSubMenuItem.getAction();
                    if (action != null) {
                        link.setAction(action);
                    }
                    MethodBinding actionListener = nextSubMenuItem.getActionListener();
                    if (actionListener != null) {
                        link.setActionListener(actionListener);
                    }
                    ActionListener[] actionListeners = nextSubMenuItem.getActionListeners();
                    if (actionListeners != null) {
                        for(int i = 0; i < actionListeners.length; i++) {
                            link.removeActionListener(actionListeners[i]);
                            link.addActionListener(actionListeners[i]);
                        }
                    }
                }
                link.setValue(nextSubMenuItem.getValue());
                link.setParent(nextSubMenuItem);
                link.setId(LINK_SUFFIX);
                //link.setStyleClass("");
                Node lastCursorParent = domContext.getCursorParent();
                domContext.setCursorParent(subMenuItemDiv);
                if (vertical) {
                    addChildren(link, nextSubMenuItem, menuComponent);
                } else {
                    addTopLevelChildren(link, nextSubMenuItem, menuComponent);
                }

                ((MenuItem) nextSubMenuItem).addParameter(link);
                try {
                    link.encodeBegin(facesContext);
                    link.encodeChildren(facesContext);
                    link.encodeEnd(facesContext);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                domContext.setCursorParent(lastCursorParent);
            } else {
                // anchor
                Element anchor = null;
                if (vertical) {
                    anchor = makeTopLevelVerticalAnchor(facesContext,
                                                        nextSubMenuItem,
                                                        menuComponent);
                } else {
                    anchor = makeTopLevelAnchor(facesContext,
                                                nextSubMenuItem,
                                                menuComponent);
                }
                subMenuItemDiv.appendChild(anchor);
            }
        } else if (nextSubMenuItem.hasActionOrActionListener()) {
            HtmlCommandLink link = new HtmlCommandLink();
            if (nextSubMenuItem.isDisabled()){
                link.setDisabled(true);
            } else { // only set action and actionListeners on enabled menuItems
                MethodBinding action = nextSubMenuItem.getAction();
                if (action != null) {
                    link.setAction(action);
                }
                MethodBinding actionListener = nextSubMenuItem.getActionListener();
                if (actionListener != null) {
                    link.setActionListener(actionListener);
                }
                ActionListener[] actionListeners = nextSubMenuItem.getActionListeners();
                if (actionListeners != null) {
                    for(int i = 0; i < actionListeners.length; i++) {
                        link.removeActionListener(actionListeners[i]);
                        link.addActionListener(actionListeners[i]);
                    }
                }
            }
            link.setValue(nextSubMenuItem.getValue());
            link.setParent(nextSubMenuItem);
            link.setId(LINK_SUFFIX);

            Node lastCursorParent = domContext.getCursorParent();
            domContext.setCursorParent(subMenuItemDiv);
            addChildren(link, nextSubMenuItem, menuComponent);
            ((MenuItem) nextSubMenuItem).addParameter(link);
            try {
                link.encodeBegin(facesContext);
                link.encodeChildren(facesContext);
                link.encodeEnd(facesContext);

            } catch (IOException e) {
                e.printStackTrace();
            }
            domContext.setCursorParent(lastCursorParent);

        } else {
            // anchor
            Element anchor = makeAnchor(facesContext, domContext,
                                        nextSubMenuItem, menuComponent);
            subMenuItemDiv.appendChild(anchor);
        }
    }

    // this method is used to add icon and label
    // to top level horizontal menu items
    private void addTopLevelChildren(HtmlCommandLink link,
                                     MenuItem nextSubMenuItem,
                                     MenuBar menuComponent) {
        HtmlPanelGroup div = new HtmlPanelGroup();

        if ((!menuComponent.getNoIcons().equalsIgnoreCase("true"))
            && (getIcon(nextSubMenuItem) != null)) {
            HtmlGraphicImage image = new HtmlGraphicImage();
            image.setUrl(getIcon(nextSubMenuItem));
            image.setStyle("border:none;");
            image.setStyleClass(nextSubMenuItem.getImageStyleClass());
            div.getChildren().add(image);
        }

        HtmlOutputText outputText = new HtmlOutputText();
        outputText.setValue(link.getValue());
//        if (!nextSubMenuItem.isDisabled()) {
//            outputText.setStyleClass("iceSubMenuRowLabel");
//        } else {
//            outputText.setStyleClass("iceSubMenuRowLabel-dis");
//        }
        outputText.setStyleClass(nextSubMenuItem.getLabelStyleClass());
        link.setValue("");
        div.getChildren().add(outputText);

        link.getChildren().add(div);
    }

    // this method is used to add icon, label and indicator
    // to top level vertical menu items
    void addChildren(HtmlCommandLink link, MenuItem nextSubMenuItem,
                     MenuBar menuComponent) {
        HtmlPanelGroup div = new HtmlPanelGroup();
        if (nextSubMenuItem.getChildCount() > 0 &&
            nextSubMenuItem.isChildrenMenuItem()) {
            HtmlGraphicImage image = new HtmlGraphicImage();
            image.setUrl(getSubMenuImage(menuComponent));
            image.setStyle("border:none;");
            image.setStyleClass(menuComponent.getSubMenuIndicatorStyleClass());
            div.getChildren().add(image);
        }

        if ((!menuComponent.getNoIcons().equalsIgnoreCase("true"))
            && (getIcon(nextSubMenuItem) != null)) {
            HtmlGraphicImage image = new HtmlGraphicImage();
            image.setUrl(getIcon(nextSubMenuItem));
            image.setStyle("border:none;");
            image.setStyleClass(nextSubMenuItem.getImageStyleClass());
            div.getChildren().add(image);
        }

        HtmlOutputText outputText = new HtmlOutputText();
        outputText.setValue(link.getValue());
//        if (!nextSubMenuItem.isDisabled()) {
//            outputText.setStyleClass("iceSubMenuRowLabel");
//        } else {
//            outputText.setStyleClass("iceSubMenuRowLabel-dis");
//        }
        outputText.setStyleClass(nextSubMenuItem.getLabelStyleClass());
        link.setValue("");
        div.getChildren().add(outputText);

        link.getChildren().add(div);
    }

    private void renderSeparatorDiv(DOMContext domContext, Element parent, 
            MenuItemSeparator menuItemSeparator) {
        Element hr = domContext.createElement("hr");
        parent.setAttribute(HTML.CLASS_ATTR, menuItemSeparator.getStyleClass());
        parent.appendChild(hr);
    }

    /**
     * @return SubMenuImage url
     */
    private String getSubMenuImage(MenuBar menuComponent) {
        String customPath = null;
        if ((customPath = menuComponent.getImageDir()) != null) {
            return customPath + SUBMENU_IMAGE;
        }
        return DEFAULT_IMAGEDIR + SUBMENU_IMAGE;
    }

    protected String getTextValue(UIComponent component) {
        if (component instanceof MenuItem) {
            return ((MenuItem) component).getValue().toString();
        }
        return null;
    }

    protected String getIcon(UIComponent component) {
        if (component instanceof MenuItem) {
            return ((MenuItem) component).getIcon();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.icesoft.faces.component.menubar.MenuItemRendererBase
     * #encodeChildren(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
    }

    /**
     * This method is used for debugging.
     *
     * @param facesContext
     * @param uiComponent
     * @param requestParameterMap
     * @param hiddenFieldName
     */
    private void examineRequest(FacesContext facesContext,
                                UIComponent uiComponent,
                                Map requestParameterMap, String hiddenFieldName,
                                String hiddenValue) {
        Iterator entries = requestParameterMap.entrySet().iterator();
        System.out.println("decoding " + ((MenuItem) uiComponent).getValue());
        System.out.println("request map");
        while (entries.hasNext()) {
            Map.Entry next = (Map.Entry) entries.next();
            if (!next.getKey().toString().equals("rand")) {
                System.out.println("[" + next.getKey().toString() + "=" +
                                   next.getValue() + "]");
            }
        }
        System.out
                .println("looking for hidden field [" + hiddenFieldName + "]");
        System.out.println(
                "client id = [" + uiComponent.getClientId(facesContext));
        System.out.println(
                "################################################ QUEUEING for hidden field [" +
                hiddenValue + "]");
    }

    /* (non-Javadoc)
     * @see com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer
     * #encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, uiComponent);
        super.encodeEnd(facesContext, uiComponent);
        domContext.streamWrite(facesContext, uiComponent);
    }


}
