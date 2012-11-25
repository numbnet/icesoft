package org.icemobile.samples.springbasic;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class PanelPopupBean {

    private boolean visible;
    private int width;
    private boolean autoCenter;
    private int height;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isAutoCenter() {
        return autoCenter;
    }

    public void setAutoCenter(boolean autoCenter) {
        this.autoCenter = autoCenter;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
