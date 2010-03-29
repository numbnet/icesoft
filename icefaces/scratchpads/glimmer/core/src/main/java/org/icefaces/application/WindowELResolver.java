package org.icefaces.application;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.faces.context.FacesContext;
import java.beans.FeatureDescriptor;
import java.util.Collections;
import java.util.Iterator;

public class WindowELResolver extends ELResolver {

    public Object getValue(ELContext elContext, Object base, Object property) {
        if (property == null) {
            throw new PropertyNotFoundException();
        }
        if (base == null && WindowScopeManager.ScopeName.equals(property.toString())) {
            WindowScopeManager.ScopeMap customScope = getScope(elContext);
            elContext.setPropertyResolved(true);
            return customScope;
        } else if (base != null && base instanceof WindowScopeManager.ScopeMap) {
            return lookup(elContext, (WindowScopeManager.ScopeMap) base, property.toString());
        } else if (base == null) {
            return lookup(elContext, getScope(elContext), property.toString());
        }
        return null;
    }

    public Class getType(ELContext elContext, Object base, Object property) {
        return Object.class;
    }

    public void setValue(ELContext elContext, Object base, Object property, Object value) {
        //do nothing!
    }

    public boolean isReadOnly(ELContext elContext, Object base, Object property) {
        return true;
    }

    public Iterator getFeatureDescriptors(ELContext elContext, Object base) {
        return Collections.<FeatureDescriptor>emptyList().iterator();
    }

    public Class getCommonPropertyType(ELContext elContext, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
    }

    private WindowScopeManager.ScopeMap getScope(ELContext elContext) {
        FacesContext ctx = (FacesContext) elContext.getContext(FacesContext.class);
        return WindowScopeManager.lookup(ctx).lookupWindowScope();
    }


    private Object lookup(ELContext elContext, WindowScopeManager.ScopeMap scope, String key) {
        if (null == scope) {
            return null;
        }
        Object value = scope.get(key);
        elContext.setPropertyResolved(value != null);
        return value;
    }
}
