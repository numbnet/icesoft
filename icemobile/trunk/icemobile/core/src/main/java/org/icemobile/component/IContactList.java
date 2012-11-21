package org.icemobile.component;

public interface IContactList extends IMobiComponent{
    
    public String getButtonLabel();
    public void setButtonLabel(String label);
    public String getPattern();
    public void setPattern(String pattern);
    public String getFields();
    public void setFields(String fields);
    public String getScript(String id, boolean isSX);
    //move to base class once vetted
    public String getPostURL();
    public String getSessionId();

}
