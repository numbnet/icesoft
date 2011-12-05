package org.icefaces.ace.util.collections;

import org.icefaces.ace.model.filter.FilterConstraint;

import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

public class PropertyConstraintPredicate implements Predicate {
    ValueExpression filterBy;
    String filterValue;
    FilterConstraint filterConstraint;
    FacesContext facesContext;

    public PropertyConstraintPredicate(FacesContext context, ValueExpression filterBy, String filterValue, FilterConstraint constraint) {
        this.filterValue = filterValue;
        this.filterConstraint = constraint;
        this.facesContext = context;
        this.filterBy = filterBy;
    }

    public boolean evaluate(Object object) {
        Object value = filterBy.getValue(facesContext.getELContext());

        if (value != null)
            return filterConstraint.applies(value.toString(), filterValue);
        else if (filterValue != null && filterValue.length() > 0)
            return false;
        else
            return true;
    }
}
