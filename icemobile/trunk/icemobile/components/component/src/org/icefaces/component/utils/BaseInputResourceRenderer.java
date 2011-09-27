/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
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
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */
package org.icefaces.component.utils;

import javax.el.ValueExpression;
import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;


/**
 * tested validation support for resource container components for beta
 * still must test converter support
 */
public class BaseInputResourceRenderer extends Renderer {
    private static Logger logger = Logger.getLogger(BaseInputResourceRenderer.class.getName());


    protected String getOptionAsString(FacesContext facesContext, UIInput uiComponent, Converter converter, Object value) {
        if (converter != null)
            return converter.getAsString(facesContext, uiComponent, value);
        else if (value == null)
            return "";
        else  {
            Map map = (HashMap)value;
            if (map.containsKey("absolutePath")) {
               return (String)this.getPropertyFromMap(map,"absolutePath");
            }
            else return "";
        }
    }

    protected Converter getConverter(FacesContext facesContext, UIInput uiComponent) {
        Converter converter = uiComponent.getConverter();

        if (converter != null) {
            return converter;
        } else {
            ValueExpression ve = uiComponent.getValueExpression("value");

            if (ve != null) {
                Class<?> valueType = ve.getType(facesContext.getELContext());

                return facesContext.getApplication().createConverter(valueType);
            }
        }

        return null;
    }

     @Override
     public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException {
        UIInput uiInput = (UIInput) uiComponent;
        Map valueMap = (HashMap) submittedValue;
        Converter converter = uiInput.getConverter();
        String value = (String)this.getPropertyFromMap(valueMap,"absolutePath");
        //first ask the converter
        if (converter != null) {
            return converter.getAsObject(facesContext, uiInput, value);
        }
        //Try to guess
        else {  //can't really guess about this as it needs a custom validator
          /*  ValueExpression ve = uiInput.getValueExpression("value");

            if (ve != null) {
                Class<?> valueType = ve.getType(facesContext.getELContext());
                Converter converterForType = facesContext.getApplication().createConverter(valueType);

                if (converterForType != null) {
                    return converterForType.getAsObject(facesContext, uiInput, value);
                }
            }*/
        }

       return submittedValue;
    }

    /**
     *
     * Algorithm works as follows;
     * - If it's an input component, submitted value is checked first since it'd be the value to be used in case validation errors
     * terminates jsf lifecycle
     * - Finally the value of the component is retrieved from backing bean and if there's a converter, converted value is returned
     * <p/>
     * - If the component is not a value holder, toString of component is used to support Facelets UIInstructions.
     *
     * @param facesContext   FacesContext instance
     * @param component UIComponent instance whose value will be returned
     * @return End text
     */
    public static String getStringValueToRender(FacesContext facesContext, UIComponent component) {
        String value = null;
        if (component instanceof ValueHolder) {
            if (component instanceof EditableValueHolder) {
                Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
                if (submittedValue != null) {
                    Map map = (HashMap)submittedValue;
                    if (map.containsKey("absolutePath")) {
                        value = (String)map.get("absolutePath");
                    }
                }
                return value;
            }

            ValueHolder valueHolder = (ValueHolder) component;
            Object valueObj = valueHolder.getValue();
            if (valueObj == null)
                return "";

            //first ask the converter
            if (valueHolder.getConverter() != null) {
                return valueHolder.getConverter().getAsString(facesContext, component, valueObj);
            }
            //Try to guess
            else {
                return ""; //no guessing
            }

            //No converter found just return the value as string
        } else {
            //This would get the plain texts on UIInstructions when using Facelets
            if (value != null)
                return value.trim();
            else
                return "";
        }
    }

    //not sure if need this yet as none of these uiComponents render their own children yet.
    protected void renderChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        for (UIComponent child : uiComponent.getChildren()) {
            renderChild(facesContext, child);
        }
    }

    protected void renderChild(FacesContext facesContext, UIComponent child) throws IOException {
        if (!child.isRendered()) {
            return;
        }

        child.encodeBegin(facesContext);

        if (child.getRendersChildren()) {
            child.encodeChildren(facesContext);
        } else {
            renderChildren(facesContext, child);
        }
        child.encodeEnd(facesContext);
    }


    public void setSubmittedValue(UIComponent uiComponent, Object value) {
        if (uiComponent instanceof UIInput) {
            ((UIInput) uiComponent).setSubmittedValue(value);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Sets submitted value to" + value + " on component  " + uiComponent.getClientId());
            }
        }
    }


  /*  protected Object getValue(UIComponent uiComponent) {
        //need this to support conversion.  use UIInput or ValueHolder??
        if (uiComponent instanceof UIInput) {
            Object value = ((UIInput) uiComponent).getValue();

            if (logger.isLoggable(Level.FINE)) {
                logger.fine("component.getValue() = " + value + " for component=" + uiComponent.getClientId());
            }
            return value;
        }

        return null;
    }  */

    protected List<SelectItem> getSelectItems(FacesContext facesContext, UIInput uiComponent) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();

        for (UIComponent child : uiComponent.getChildren()) {
            if (child instanceof UISelectItem) {
                UISelectItem uiSelectItem = (UISelectItem) child;

                selectItems.add(new SelectItem(uiSelectItem.getItemValue(), uiSelectItem.getItemLabel()));
            } else if (child instanceof UISelectItems) {
                UISelectItems uiSelectItems = ((UISelectItems) child);
                Object value = uiSelectItems.getValue();

                if (value instanceof SelectItem[]) {
                    selectItems.addAll(Arrays.asList((SelectItem[]) value));
                } else if (value instanceof Map) {
                    Map map = (Map) value;

                    for (Object key : map.keySet()) {
                        selectItems.add(new SelectItem(map.get(key), String.valueOf(key)));
                    }
                } else if (value instanceof Collection) {
                    Collection collection = (Collection) value;
                    String var = (String) uiSelectItems.getAttributes().get("var");

                    if (var != null) {
                        for (Object object : collection) {
                            facesContext.getExternalContext().getRequestMap().put(var, object);
                            String itemLabel = (String) uiSelectItems.getAttributes().get("itemLabel");
                            Object itemValue = uiSelectItems.getAttributes().get("itemValue");

                            selectItems.add(new SelectItem(itemValue, itemLabel));
                        }
                    } else {
                        for (Object aCollection : collection) {
                            selectItems.add((SelectItem) aCollection);
                        }
                    }
                }
            }
        }

        return selectItems;
    }

        /*if the image properties have to be gotten from the image map to encode*/
    public Object getPropertyFromMap(Map<String, Object> componentMap, String key) {
        if (componentMap.containsKey(key)) {
            return componentMap.get(key);
        } else return null;
    }

    private boolean containsKey(Map<String, Object> componentMap, String key) {
        if (componentMap.containsKey(key)) return true;
        else return false;
    }
}
