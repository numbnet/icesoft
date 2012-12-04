package org.icemobile.component;

public interface IPagePanel extends IMobiComponent{
    
    public static final String HEADER_FACET = "header";
    public static final String BODY_FACET = "body";
    public static final String FOOTER_FACET = "footer";

    
    public Object getHeader();
    public Object getBody();
    public Object getFooter();

}
