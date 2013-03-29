package org.icemobile.component;

public interface IPanelPopup extends IMobiComponent{

    public static final String BLACKOUT_PNL_HIDDEN_CLASS = "mobi-panelpopup-bg-hide ";
    public static final String BLACKOUT_PNL_CLASS = "mobi-panelpopup-bg ";
    public static final String CONTAINER_CLASS = "mobi-panelpopup-container ";
    public static final String HIDDEN_CONTAINER_CLASS = "mobi-panelpopup-container-hide ";
    public static final String TITLE_CLASS = "mobi-panelpopup-title-container ";


    public String getHeaderText();
    public void setHeaderText(String text);
    public boolean isClientSide();
    public void setClientSide(boolean clientSide);

    public String getId();
    public boolean isAutoCenter();
    public void setAutoCenter(boolean autoCenter);

    public void setWidth(int width);
    public int getWidth();

    public void setHeight(int height);
    public int getHeight();

    public boolean isVisible();
    public void setVisible(boolean visible);

}
