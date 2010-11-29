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

package org.icefaces.facelets.tag.icefaces.core;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.el.MethodBinding;
import javax.faces.context.FacesContext;
import javax.el.MethodExpression;

/**
 * !! NOTE!!!
 * This copy is a duplicate of the same class in Compat. It's present here to allow
 * compat namespace to exist in sparkle.jar. Any changes to this file need to be duplicated
 * in the original!
 * 
 *
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
                try {
                m.addRule( new MethodRule("progressListener", null, new Class[] {
                        Class.forName("java.util.EventObject" ) }) );
                } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("outputChart") ) {
                try {
                    m.addRule( new MethodRule("renderOnSubmit", Boolean.TYPE,
                                             new Class[] {Class.forName( "com.icesoft.faces.component.outputchart.OutputChart")
                                              }) );
                 } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("panelGroup") ) {
                try { 
                m.addRule( new MethodRule("dragListener", null, new Class[] {
                        Class.forName("com.icesoft.faces.component.dragdrop.DragEvent") }) );
                m.addRule( new MethodRule("dropListener", null, new Class[] {
                        Class.forName("com.icesoft.faces.component.dragdrop.DropEvent") }) );
             } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("panelPositioned") ) {
                try {
                m.addRule( new MethodRule("listener", null, new Class[] {
                        Class.forName("com.icesoft.faces.component.panelpositioned.PanelPositionedEvent")}) );
         } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("panelTabSet") ) {
                try {
                    m.addRule( new MethodRule("tabChangeListener", null, new Class[] {
                            Class.forName("com.icesoft.faces.component.paneltabset.TabChangeEvent") }) );
                } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("rowSelector") ) {
                try {
                    m.addRule( new MethodRule("selectionListener", null, new Class[] {
                            Class.forName("com.icesoft.faces.component.ext.RowSelectorEvent") }) );
                    m.addRule( new MethodRule("selectionAction", null, new Class[0]) );
                    m.addRule( new MethodRule("clickListener", null, new Class[] {
                            Class.forName("com.icesoft.faces.component.ext.ClickActionEvent") }) );
                    m.addRule( new MethodRule("clickAction", null, new Class[0]) );
                } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("panelTooltip") ) {
                try {
                    m.addRule( new MethodRule("displayListener", null, new Class[] {
                            Class.forName( "com.icesoft.faces.component.DisplayEvent" )}) );
                } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("menuPopup") ) {
                try {
                    m.addRule( new MethodRule("displayListener", null, new Class[] {
                            Class.forName("com.icesoft.faces.component.DisplayEvent") }) );
                } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("selectInputText") ) {
                try {
                    m.addRule( new MethodRule("textChangeListener", null, new Class[] {
                            Class.forName("com.icesoft.faces.component.selectinputtext.TextChangeEvent") }) );
                } catch (ClassNotFoundException nfe) {}
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

class MethodExpressionMethodBinding extends MethodBinding implements
                                                          Serializable {
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