/*
 * Original Code developed and contributed by Prime Technology.
 * Subsequent Code Modifications Copyright 2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.util;

import org.icefaces.ace.renderkit.CoreRenderer;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ProjectStage;
import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.render.Renderer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComponentUtils {

    private final static Logger logger = Logger.getLogger(ComponentUtils.class.getName());

    /**
     * Algorithm works as follows;
     * - If it's an input component, submitted value is checked first since it'd be the value to be used in case validation errors
     * terminates jsf lifecycle
     * - Finally the value of the component is retrieved from backing bean and if there's a converter, converted value is returned
     * <p/>
     * - If the component is not a value holder, toString of component is used to support Facelets UIInstructions.
     *
     * @param context   FacesContext instance
     * @param component UIComponent instance whose value will be returned
     * @return End text
     */
    public static String getStringValueToRender(FacesContext facesContext, UIComponent component) {
        if (component instanceof ValueHolder) {

            if (component instanceof EditableValueHolder) {
                Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
                if (submittedValue != null) {
                    return submittedValue.toString();
                }
            }

            ValueHolder valueHolder = (ValueHolder) component;
            Object value = valueHolder.getValue();
            if (value == null)
                return "";

            //first ask the converter
            if (valueHolder.getConverter() != null) {
                return valueHolder.getConverter().getAsString(facesContext, component, value);
            }
            //Try to guess
            else {
                ValueExpression expr = component.getValueExpression("value");
                if (expr != null) {
                    Class<?> valueType = expr.getType(facesContext.getELContext());
                    if (valueType != null) {
                        Converter converterForType = facesContext.getApplication().createConverter(valueType);

                        if (converterForType != null)
                            return converterForType.getAsString(facesContext, component, value);
                    }
                }
            }

            //No converter found just return the value as string
            return value.toString();
        } else {
            //This would get the plain texts on UIInstructions when using Facelets
            String value = component.toString();

            if (value != null)
                return value.trim();
            else
                return "";
        }
    }


    /**
     * Attempts to return the property accessed in a simple EL expression of the form
     * ${someObject.property}.
     *
     * @param An EL expression to parse the property from.
     * @return The property name of the EL expression.
     */
    public static String resolveField(ValueExpression expression) {
        String expressionString = expression.getExpressionString();
        return expressionString.substring(expressionString.indexOf(".") + 1, expressionString.length() - 1);
    }

    /**
     * Resolves the end text to render by using a specified value
     *
     * @param context   FacesContext instance
     * @param component UIComponent instance whose value will be returned
     * @return End text
     */
    public static String getStringValueToRender(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) return null;

        ValueHolder valueHolder = (ValueHolder) component;

        Converter converter = valueHolder.getConverter();
        if (converter != null) {
            return converter.getAsString(facesContext, component, value);
        } else {
            ValueExpression expr = component.getValueExpression("value");
            if (expr != null) {
                Class<?> valueType = expr.getType(facesContext.getELContext());
                Converter converterForType = facesContext.getApplication().createConverter(valueType);

                if (converterForType != null)
                    return converterForType.getAsString(facesContext, component, value);
            }
        }

        return value.toString();
    }

    public static UIComponent findParentForm(FacesContext context, UIComponent component) {
        UIComponent parent = component;
        while (parent != null)
            if (parent instanceof UIForm) break;
            else parent = parent.getParent();

        return parent;
    }

    public static void decorateAttribute(UIComponent component, String attribute, String value) {
        String attributeValue = (String) component.getAttributes().get(attribute);

        if (attributeValue != null) {
            if (attributeValue.indexOf(value) == -1) {
                String decoratedValue = attributeValue + ";" + value;

                component.getAttributes().put(attribute, decoratedValue);
            } else {
                component.getAttributes().put(attribute, attributeValue);
            }
        } else {
            component.getAttributes().put(attribute, value);
        }
    }

    public static List<SelectItem> createSelectItems(UIComponent component) {
        List<SelectItem> items = new ArrayList<SelectItem>();
        Iterator<UIComponent> children = component.getChildren().iterator();

        while (children.hasNext()) {
            UIComponent child = children.next();

            if (child instanceof UISelectItem) {
                UISelectItem selectItem = (UISelectItem) child;

                items.add(new SelectItem(selectItem.getItemValue(), selectItem.getItemLabel()));
            } else if (child instanceof UISelectItems) {
                Object selectItems = ((UISelectItems) child).getValue();

                if (selectItems instanceof SelectItem[]) {
                    SelectItem[] itemsArray = (SelectItem[]) selectItems;

                    for (SelectItem item : itemsArray)
                        items.add(new SelectItem(item.getValue(), item.getLabel()));

                } else if (selectItems instanceof Collection) {
                    Collection<SelectItem> collection = (Collection<SelectItem>) selectItems;

                    for (SelectItem item : collection)
                        items.add(new SelectItem(item.getValue(), item.getLabel()));
                }
            }
        }

        return items;
    }

    public static String escapeJQueryId(String id) {
        return "#" + id.replaceAll(":", "\\\\\\\\:");
    }

    public static String formatKeywords(FacesContext facesContext, UIComponent component, String processRequest) {
        String process = processRequest;

        if (process.indexOf("@this") != -1)
            process = process.replaceFirst("@this", component.getClientId(facesContext));
/*
        if (process.indexOf("@form") != -1) {
            UIComponent form = ComponentUtils.findParentForm(facesContext, component);
            if (form == null)
                throw new FacesException("Component " + component.getClientId(facesContext) + " needs to be enclosed in a form");

            process = process.replaceFirst("@form", form.getClientId(facesContext));
        }
*/
        if (process.indexOf("@parent") != -1)
            process = process.replaceFirst("@parent", component.getParent().getClientId(facesContext));

        return process;
    }

    public static String findClientIds(FacesContext context, UIComponent component, String list) {
        if (list == null) return "@none";

        String formattedList = formatKeywords(context, component, list);
        String[] ids = formattedList.split("[,\\s]+");
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < ids.length; i++) {
            if (i != 0) buffer.append(" ");
            String id = ids[i].trim();

            if (id.equals("@all") || id.equals("@none")) buffer.append(id);
            else if (id.equals("@form")) {
                UIComponent form = ComponentUtils.findParentForm(context, component);
                if (form == null)
                    throw new FacesException("Component " + component.getClientId(context) + " needs to be enclosed in a form");

                buffer.append(form.getClientId(context));
            }
            else {

                UIComponent comp = component.findComponent(id);
                if (comp != null) buffer.append(comp.getClientId(context));
                else {
                    if (context.getApplication().getProjectStage().equals(ProjectStage.Development)) {
                        logger.log(Level.INFO, "Cannot find component with identifier \"{0}\" in view.", id);
                    }
                    buffer.append(id);
                }
            }
        }

        return buffer.toString();
    }

    public static String findComponentClientId(String id) {
        UIComponent component = null;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        component = findComponent(facesContext.getViewRoot(), id);

        return component.getClientId(facesContext);
    }

    public static UIComponent findComponent(UIComponent base, String id) {
        if (id.equals(base.getId()))
            return base;

        UIComponent kid = null;
        UIComponent result = null;
        Iterator<UIComponent> kids = base.getFacetsAndChildren();
        while (kids.hasNext() && (result == null)) {
            kid = (UIComponent) kids.next();
            if (id.equals(kid.getId())) {
                result = kid;
                break;
            }
            result = findComponent(kid, id);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    public static String getWidgetVar(String id) {
        UIComponent component = findComponent(FacesContext.getCurrentInstance().getViewRoot(), id);
        Renderer renderer = FacesContext.getCurrentInstance().getRenderKit()
                .getRenderer(component.getFamily(), component.getRendererType());

        if (component == null) throw new FacesException("Cannot find component " + id + " in view.");
        else if (!(renderer instanceof CoreRenderer))
            throw new FacesException("Component with id " + id + " is not derived from CoreRenderer.");

        return ((CoreRenderer) renderer).resolveWidgetVar(component);
    }

    public static boolean isLiteralText(UIComponent component) {
        return component.getFamily().equalsIgnoreCase("facelets.LiteralText");
    }
}