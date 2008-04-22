package com.icesoft.faces.component.ext;


import javax.faces.context.FacesContext;

public class HtmlHead extends javax.faces.component.UIComponentBase{


    public HtmlHead() {
        super();
        setRendererType("com.icesoft.faces.Head");
    }
    /**
     * <p>Return the family for this component.</p>
     */
    public String getFamily() {
        return "com.icesoft.faces.Head";
    }

    private Object[] _values;
    public Object saveState(FacesContext _context) {
        if (_values == null) {
            _values = new Object[1];
        }
            _values[0] = super.saveState(_context);
        return _values;
    }    

    public void restoreState(FacesContext _context, Object _state) {
        _values = (Object[]) _state;
        super.restoreState(_context, _values[0]);        
    }    

}