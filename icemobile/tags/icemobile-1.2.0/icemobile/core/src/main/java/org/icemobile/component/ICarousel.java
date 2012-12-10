package org.icemobile.component;

public interface ICarousel extends IMobiComponent{

    public static final String JS_ISCROLL = "iscroll.js";
    public static final String LIB_ISCROLL = "org.icefaces.component.util";
    public static final String LIB_ISCROLL_JSP = "javascript";
    public static final String CAROUSEL_CLASS = "mobi-carousel";
    public static final String SCROLLER_CLASS = "mobi-carousel-scroller";
    public static final String LIST_CLASS = "mobi-carousel-list";
    public static final String CAROUSEL_CURSOR_CLASS = "mobi-carousel-cursor ";
    public static final String CAROUSEL_CURSOR_LISTCLASS = "mobi-carousel-cursor-list ";
    public static final String CAROUSEL_CURSOR_CURSOR_CENTER_CLASS = "mobi-carousel-cursor-center";

    public String getName();//required for jsp submit
    public String getPreviousLabel();
    public void setPreviousLabel(String prevLabel);

    public void setNextLabel(String nextLabel);
    public String getNextLabel();

    public int getSelectedItem();
    public void setSelectedItem(int selected);
    /*
        <p> the src resource is calculated differently to load the iscroll lib in the body</p>
     */
    public String getIScrollSrc();

    /*  need count of items in list for cursor listing */
    public int getRowCount();

    public StringBuilder getJSConfigOptions();
}
