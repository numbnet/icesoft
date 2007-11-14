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
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.io.IOException;
import java.util.*;


public class SelectManyCheckboxListRenderer extends MenuRenderer {

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        validateParameters(facesContext, uiComponent, null);

        String componentName = uiComponent.getClass().getName();
        if (componentName.equals("com.icesoft.faces.component.ext.HtmlRadio") ||
                componentName.equals("com.icesoft.faces.component.ext.HtmlCheckbox")) {
            renderOption(facesContext, uiComponent);
            return;
        }

        int counter = 0;

        boolean renderVertically = false;
        String layout = (String) uiComponent.getAttributes().get("layout");
        if (layout != null && layout.equals("spread")) {
            return;
        }
        if (layout != null) {
            renderVertically =
                    layout.equalsIgnoreCase("pageDirection") ? true : false;
        }

        int border = getBorderSize(uiComponent);

        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);

        Element rootTR = null;

        // remove all existing table rows from the root table
        if (domContext.isInitialized()) {
            DOMContext.removeChildrenByTagName(
                    (Element) domContext.getRootNode(), "tr");
        } else {
            Element root = domContext.createElement("table");
            domContext.setRootNode(root);
            if (idNotNull(uiComponent)) {
                setRootElementId(facesContext, root, uiComponent);
            }
        }

        Element rootTable = (Element) domContext.getRootNode();
        String styleClass =
                (String) uiComponent.getAttributes().get("styleClass");
        if (styleClass != null) {
            rootTable.setAttribute("class", styleClass);
        }
        String style = (String) uiComponent.getAttributes().get("style");
        if (style != null && style.length() > 0) {
            rootTable.setAttribute("style", style);
        }
        else {
            rootTable.removeAttribute("style");
        }
        rootTable.setAttribute("border", new Integer(border).toString());

        if (!renderVertically) {
            rootTR = domContext.createElement("tr");
            rootTable.appendChild(rootTR);
        }

        Iterator options = getSelectItems(uiComponent);

        //We should call uiComponent.getValue() only once, becase if it binded with the bean,
        //The bean method would be called as many time as this method call.
        Object componentValue = ((UIInput) uiComponent).getValue();

        while (options.hasNext()) {
            SelectItem nextSelectItem = (SelectItem) options.next();

            counter++;

            // render a SelectItemGroup in a nested table
            if (nextSelectItem instanceof SelectItemGroup) {

                Element nextTR = null;
                Element nextTD = null;

                if (nextSelectItem.getLabel() != null) {
                    if (renderVertically) {
                        nextTR = domContext.createElement("tr");
                        rootTable.appendChild(nextTR);
                    }
                    nextTD = domContext.createElement("td");
                    nextTR.appendChild(nextTD);
                    Text label = domContext.getDocument()
                            .createTextNode(nextSelectItem.getLabel());
                    nextTD.appendChild(label);
                }
                if (renderVertically) {
                    nextTR = domContext.createElement("tr");
                    rootTable.appendChild(nextTR);
                }
                nextTD = domContext.createElement("td");
                nextTR.appendChild(nextTD);

                SelectItem[] selectItemsArray =
                        ((SelectItemGroup) nextSelectItem).getSelectItems();
                for (int i = 0; i < selectItemsArray.length; ++i) {
                    renderOption(facesContext, uiComponent, selectItemsArray[i],
                                 renderVertically, nextTR, counter,
                                 componentValue);
                }
            } else {
                renderOption(facesContext, uiComponent, nextSelectItem,
                             renderVertically, rootTR, counter,
                             componentValue);
            }
        }

        domContext.stepOver();
        domContext.streamWrite(facesContext, uiComponent);
    }

    private int getBorderSize(UIComponent uiComponent) {
        int border = 0;
        Object borderAttribute = uiComponent.getAttributes().get("border");
        if (borderAttribute instanceof Integer) {
            if (((Integer) borderAttribute).intValue() != Integer.MIN_VALUE) {
                border = ((Integer) borderAttribute).intValue();
            }
        } else {
            try {
                border = Integer.valueOf(borderAttribute.toString()).intValue();
            } catch (NumberFormatException nfe) {
                // couldn't parse it; stick with the default (initial) value of 0
            }
        }
        return border;
    }

    protected void renderOption(FacesContext facesContext,
                                UIComponent uiComponent,
                                SelectItem selectItem, boolean renderVertically,
                                Element rootTR, int counter,
                                Object componentValue)
            throws IOException {

        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        Element rootTable = (Element) domContext.getRootNode();

        boolean disabled = false;
        if (uiComponent.getAttributes().get("disabled") != null) {
            if ((uiComponent.getAttributes().get("disabled"))
                    .equals(Boolean.TRUE)) {
                disabled = true;
            }
        }
        if (selectItem.isDisabled()) {
            disabled = true;
        }

        if (renderVertically) {
            rootTR = domContext.createElement("tr");
            rootTable.appendChild(rootTR);
        }
        Element td = domContext.createElement("td");
        rootTR.appendChild(td);

        Element label = domContext.createElement("label");
        td.appendChild(label);
        
        Element inputElement = domContext.createElement("input");
        inputElement
                .setAttribute("name", uiComponent.getClientId(facesContext));
        inputElement.setAttribute("id",
                                  uiComponent.getClientId(facesContext) + ":_" +
                                  counter);
        label.appendChild(inputElement);
        HashSet excludes = new HashSet();
        String accesskey =
                (String) uiComponent.getAttributes().get("accesskey");
        if (accesskey != null) {
            inputElement.setAttribute("accesskey", accesskey);
            excludes.add("accesskey");
        }

        String formattedOptionValue = formatComponentValue(
                facesContext,
                uiComponent,
                selectItem.getValue());
        inputElement.setAttribute("value", formattedOptionValue);
        inputElement.setAttribute("type", "checkbox");

        boolean isSelected;
        Object submittedValues[] =
                getSubmittedSelectedValues(uiComponent);
        if (submittedValues != null) {
            isSelected = isSelected(formattedOptionValue, submittedValues);
        } else {
            Object selectedValues =
                    getCurrentSelectedValues(uiComponent);
            isSelected = isSelected(selectItem.getValue(), selectedValues);
        }

        if (isSelected) {
            inputElement.setAttribute("checked", Boolean.TRUE.toString());
        }
        if (disabled) {
            inputElement.setAttribute("disabled", "disabled");
        }


        String selectItemLabel = selectItem.getLabel();
        if (selectItemLabel != null) {
            Text textNode =
                    domContext.getDocument().createTextNode(selectItemLabel);
            inputElement.appendChild(textNode);
        }
        addJavaScript(facesContext, uiComponent, inputElement, excludes);
        excludes.add("style");
        excludes.add("border");
        excludes.add("readonly");
        String[] excludesStringArray = new String[excludes.size()];
        excludesStringArray = (String[]) excludes.toArray(excludesStringArray);
        PassThruAttributeRenderer.renderAttributes(
                facesContext, uiComponent,
                inputElement, rootTable,
                excludesStringArray);
    }

    protected void renderOption(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        validateParameters(facesContext, uiComponent, null);

        UIComponent forComponent = findForComponent(facesContext, uiComponent);
        if (!(forComponent instanceof UISelectMany)) {
            throw new IllegalStateException("Could not find UISelectMany component for checkbox.");
        }
        String layout = (String) forComponent.getAttributes().get("layout");
        if (layout == null || !layout.equals("spread")) {
            return;
        }
        List selectItemList = getSelectItemList(forComponent);
        if (selectItemList.isEmpty()) {
            throw new IllegalStateException("Could not find select items for UISelectMany component.");
        }

        UISelectMany selectMany = (UISelectMany) forComponent;
        int checkboxIndex = ((Integer) uiComponent.getAttributes().get("index")).intValue();
        if (checkboxIndex < 0) checkboxIndex = 0;
        if (checkboxIndex >= selectItemList.size()) checkboxIndex = selectItemList.size() - 1;
        SelectItem selectItem = (SelectItem) selectItemList.get(checkboxIndex);

        String selectManyClientId = selectMany.getClientId(facesContext);
        String checkboxClientId = selectManyClientId + ":_" + checkboxIndex;

        String selectItemValue = formatComponentValue(facesContext, selectMany, selectItem.getValue());
        String selectItemLabel = selectItem.getLabel();

        boolean isChecked;
        Object submittedValues[] = getSubmittedSelectedValues(selectMany);
        if (submittedValues != null) {
            isChecked = isSelected(selectItemValue, submittedValues);
        } else {
            Object selectedValues = getCurrentSelectedValues(selectMany);
            isChecked = isSelected(selectItem.getValue(), selectedValues);
        }

        DOMContext domContext = DOMContext.attachDOMContext(facesContext, uiComponent);
        if (domContext.isInitialized()) {
            DOMContext.removeChildren(domContext.getRootNode());
        } else {
            domContext.createRootElement(HTML.SPAN_ELEM);
        }
        Node rootNode = domContext.getRootNode();
        HashSet excludes = new HashSet();

        Element input = domContext.createElement(HTML.INPUT_ELEM);
        input.setAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_CHECKBOX);
        input.setAttribute(HTML.ID_ATTR, checkboxClientId);
        input.setAttribute(HTML.NAME_ATTR, selectManyClientId);
        input.setAttribute(HTML.VALUE_ATTR, selectItemValue);
        if (selectItem.isDisabled()) {
            input.setAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR);
        }
        if (isChecked) {
            input.setAttribute(HTML.CHECKED_ATTR, HTML.CHECKED_ATTR);
        }
        addJavaScript(facesContext, selectMany, input, excludes);

        Element label = domContext.createElement(HTML.LABEL_ATTR);
        label.setAttribute(HTML.FOR_ATTR, checkboxClientId);
        if (selectItemLabel != null) label.appendChild(domContext.createTextNode(selectItemLabel));

        PassThruAttributeRenderer.renderAttributes(facesContext, selectMany, input, label, getExcludesArray(excludes));

        rootNode.appendChild(input);
        rootNode.appendChild(label);

        domContext.stepOver();
        domContext.streamWrite(facesContext, uiComponent);
    }

    protected void addJavaScript(FacesContext facesContext,
                                 UIComponent uiComponent, Element root,
                                 Set excludes) {
    }

    protected List getSelectItemList(UIComponent uiComponent) {
        List list = new ArrayList();
        Iterator iter = getSelectItems(uiComponent);
        while (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof SelectItemGroup) {
                addSelectItemGroupToList((SelectItemGroup) o, list);
                continue;
            }
            list.add(o);
        }
        return list;
    }

    private void addSelectItemGroupToList(SelectItemGroup selectItemGroup, List list) {
        SelectItem[] selectItems = selectItemGroup.getSelectItems();
        if (selectItems == null || selectItems.length == 0) {
            list.add(selectItemGroup);
            return;
        }
        for (int i = 0; i < selectItems.length; i++) {
            SelectItem selectItem = selectItems[i];
            if (selectItem instanceof SelectItemGroup) {
                addSelectItemGroupToList((SelectItemGroup) selectItem, list);
                continue;
            }
            list.add(selectItem);
        }
    }
}