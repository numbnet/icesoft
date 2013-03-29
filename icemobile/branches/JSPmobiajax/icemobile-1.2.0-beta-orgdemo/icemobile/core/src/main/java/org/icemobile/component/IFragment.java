package org.icemobile.component;

import org.icemobile.util.ClientDescriptor;
/*
  a fragment gets all it's information from a parent and is intended to
  make similar the facet of the jsf child component and the child tag
  of a component in jsp
 */
public interface IFragment {

    public void setStyle(String style) ;
    public String getStyle() ;
    public void setStyleClass(String styleClass);
    public String getStyleClass() ;
    public String getClientId();

}
