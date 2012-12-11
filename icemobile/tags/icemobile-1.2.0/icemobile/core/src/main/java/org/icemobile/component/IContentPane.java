package org.icemobile.component;

public interface IContentPane extends IMobiComponent{
    
    public static final String CONTENT_BASE_CLASS = "mobi-contentpane ";
    public static final String CONTENT_HIDDEN_CLASS = "mobi-contentpane-hidden ";
    public static final String CONTENT_SINGLE_BASE_CLASS = "mobi-contentpane-single";
    public static final String CONTENT_SINGLE_HIDDEN_CLASS = "mobi-contentpane-single-hidden";
    public static final String CONTENT_SINGLE_MENUPANE_CLASS = "mobi-contentpane-single-menu-hidden";
        
    public boolean isAccordionPane();
    public IAccordion getAccordionParent();
    public boolean isStackPane();
    public boolean isTabPane();
    public boolean isClient();
    public String getId();
    public String getTitle();
    public void setTitle(String title);
  //  public String getJSSrc();
}
