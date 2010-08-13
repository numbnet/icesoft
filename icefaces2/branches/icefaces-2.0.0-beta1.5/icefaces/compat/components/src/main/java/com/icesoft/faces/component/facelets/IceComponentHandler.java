/*
 * Version: MPL 1.1
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
 */

package com.icesoft.faces.component.facelets;

import java.lang.reflect.Method;
import java.io.Serializable;

import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttributeException;
import javax.el.MethodExpression;
import javax.faces.el.MethodBinding;
import javax.faces.context.FacesContext;
import java.lang.reflect.InvocationTargetException;

import com.icesoft.faces.component.dragdrop.DragEvent;
import com.icesoft.faces.component.dragdrop.DropEvent;
import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.icesoft.faces.component.ext.ClickActionEvent;
import com.icesoft.faces.component.panelpositioned.PanelPositionedEvent;
import com.icesoft.faces.component.paneltabset.TabChangeEvent;
import com.icesoft.faces.component.outputchart.OutputChart;
import com.icesoft.faces.component.DisplayEvent;
import com.icesoft.faces.component.selectinputtext.TextChangeEvent;

import java.util.EventObject;

/**
 * @author Mark Collette
 * @since 1.6
 */
public class IceComponentHandler extends ComponentHandler {
    public IceComponentHandler(ComponentConfig componentConfig) {
        super(componentConfig);
    }

    
    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset m = super.createMetaRuleset(type);
        if( tag.getNamespace() != null &&
            tag.getNamespace().equals("http://www.icesoft.com/icefaces/component") )
        {
            if( tag.getLocalName().equals("inputFile") ) {
                m.addRule( new MethodRule("progressListener", null, new Class[] {EventObject.class}) );
            }
            else if( tag.getLocalName().equals("outputChart") ) {
                m.addRule( new MethodRule("renderOnSubmit", Boolean.TYPE, new Class[] {OutputChart.class}) );
            }
            else if( tag.getLocalName().equals("panelGroup") ) {
                m.addRule( new MethodRule("dragListener", null, new Class[] {DragEvent.class}) );
                m.addRule( new MethodRule("dropListener", null, new Class[] {DropEvent.class}) );
            }
            else if( tag.getLocalName().equals("panelPositioned") ) {
                m.addRule( new MethodRule("listener", null, new Class[] {PanelPositionedEvent.class}) );
            }
            else if( tag.getLocalName().equals("panelTabSet") ) {
                m.addRule( new MethodRule("tabChangeListener", null, new Class[] {TabChangeEvent.class}) );
            }
            else if( tag.getLocalName().equals("rowSelector") ) {
                m.addRule( new MethodRule("selectionListener", null, new Class[] {RowSelectorEvent.class}) );
                m.addRule( new MethodRule("selectionAction", null, new Class[0]) );
                m.addRule( new MethodRule("clickListener", null, new Class[] {ClickActionEvent.class}) );
                m.addRule( new MethodRule("clickAction", null, new Class[0]) );
            }
            else if( tag.getLocalName().equals("panelTooltip") ) {
                m.addRule( new MethodRule("displayListener", null, new Class[] {DisplayEvent.class}) );
            }
            else if( tag.getLocalName().equals("menuPopup") ) {
                m.addRule( new MethodRule("displayListener", null, new Class[] {DisplayEvent.class}) );
            }
            else if( tag.getLocalName().equals("selectInputText") ) {
                m.addRule( new MethodRule("textChangeListener", null, new Class[] {TextChangeEvent.class}) );
            }
        }
        return m;
    }
}

class MethodRule extends MetaRule  {
    private final String methodName;
    private final Class returnTypeClass;
    private final Class[] params;
    
    public MethodRule(String methodName, Class returnTypeClass, Class[] params) {
        this.methodName = methodName;
        this.returnTypeClass = returnTypeClass;
        this.params = params;
    }

    public Metadata applyRule(String name, TagAttribute attribute,
            MetadataTarget meta) {
        if (!name.equals(this.methodName))  {
            return null;
        }

        if (MethodBinding.class.equals(meta.getPropertyType(name))) {
            Method method = meta.getWriteMethod(name);
            if (null != method) {
                return new MethodBindingMetadata(method, attribute,
                        this.returnTypeClass, this.params);
            }
        } else if (MethodExpression.class.equals(meta.getPropertyType(name))) {
            Method method = meta.getWriteMethod(name);
            if (null != method) {
                return new MethodExpressionMetadata(method, attribute,
                        this.returnTypeClass, this.params);
            }
        }

        return null;
    }

}

class MethodBindingMetadata extends Metadata  {
    private final Method method;
    private final TagAttribute attribute;
    private Class[] params;
    private Class returnTypeClass;
    
    public MethodBindingMetadata(Method method, TagAttribute attribute, 
            Class returnTypeClass, Class[] params)  {
        this.method = method;
        this.attribute = attribute;
        this.returnTypeClass = returnTypeClass;
        this.params = params;
    }

    public void applyMetadata(FaceletContext faceletContext, Object instance) {
        MethodExpression expr = attribute.getMethodExpression(faceletContext,
                returnTypeClass, params);
        try {
            method.invoke(instance, new MethodExpressionMethodBinding(expr) );
        } catch (InvocationTargetException e)  {
            throw new TagAttributeException(attribute, e.getCause());
        } catch (Exception e)  {
            throw new TagAttributeException(attribute, e);
        }
    }

}

class MethodExpressionMetadata extends Metadata  {
    private final Method method;
    private final TagAttribute attribute;
    private Class[] params;
    private Class returnTypeClass;
    
    public MethodExpressionMetadata(Method method, TagAttribute attribute, 
            Class returnTypeClass, Class[] params)  {
        this.method = method;
        this.attribute = attribute;
        this.returnTypeClass = returnTypeClass;
        this.params = params;
    }

    public void applyMetadata(FaceletContext faceletContext, Object instance) {
        MethodExpression expr = attribute.getMethodExpression(faceletContext,
                returnTypeClass, params);
        try {
            method.invoke(instance, expr );
        } catch (InvocationTargetException e)  {
            throw new TagAttributeException(attribute, e.getCause());
        } catch (Exception e)  {
            throw new TagAttributeException(attribute, e);
        }
    }

}

class MethodExpressionMethodBinding extends MethodBinding implements Serializable {
    private final MethodExpression expression;

    public MethodExpressionMethodBinding(MethodExpression expression) {
        this.expression = expression;
    }
    
    public Class getType(FacesContext facesContext)  {
        return expression.getMethodInfo(facesContext.getELContext()).getReturnType();
    }
    
    public Object invoke(FacesContext facesContext, Object[] params)  {
        return expression.invoke(facesContext.getELContext(), params);
    }
    
    public String getExpressionString() {
        return expression.getExpressionString();
    }

}