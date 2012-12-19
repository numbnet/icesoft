package org.icemobile.component;

public interface ISplitPane extends IMobiComponent{
    
    public static final String SPLITPANE_BASE_CSS = "mobi-splitpane" ;
    public static final String SPLITPANE_NONSCROLL_CSS = "mobi-splitpane-nonScrollable";
    public static final String SPLITPANE_SCROLLABLE_CSS = "mobi-splitpane-scrollable";
    public static final String SPLITPANE_DIVIDER_CSS = "mobi-splitpane-divider";
    /**
    * <p>the component id that this thumbnail represents, either camera or camcorder.</p>
    */
    public int getColumnDivider();

    /**
     * <p> the thumb base class depends on if an upload has been detected (jsf only)</p>
     */
    public boolean isScrollable();


}
