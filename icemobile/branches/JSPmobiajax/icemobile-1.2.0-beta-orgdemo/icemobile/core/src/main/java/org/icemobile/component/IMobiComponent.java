package org.icemobile.component;

import org.icemobile.util.ClientDescriptor;

public interface IMobiComponent {

    public void setStyle(String style) ;
    public String getStyle() ;
    public void setStyleClass(String styleClass);
    public String getStyleClass() ;
    public void setDisabled(boolean disabled);
    public boolean isDisabled();
    public String getClientId();
    public ClientDescriptor getClient();



}
