package org.icemobile.component;

public interface IContactList extends IMobiComponent{
    
    public String getLabel();
    public void setLabel(String label);
    public String getPattern();
    public void setPattern(String pattern);
    public boolean isMultipleSelect();
    public void setMultipleSelect(boolean multipleSelect);
    public String getFields();
    public void setFields(String fields);
    
}
