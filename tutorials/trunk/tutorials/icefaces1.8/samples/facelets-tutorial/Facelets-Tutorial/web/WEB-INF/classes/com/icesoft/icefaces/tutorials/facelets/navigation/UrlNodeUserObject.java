package com.icesoft.icefaces.tutorials.facelets.navigation;

import com.icesoft.faces.component.tree.IceUserObject;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The UrlNodeUserObject object is responsible for storing extra data
 * for a url.  The url along with text is bound to a ice:commanLink object which
 * will launch a new browser window pointed to the url.
 */
public class UrlNodeUserObject extends IceUserObject {

    // url to show when a node is clicked
    private String url;

    private boolean selected;

    public UrlNodeUserObject(DefaultMutableTreeNode wrapper) {
        super(wrapper);
    }

    /**
     * Gets the url value of this IceUserObject.
     *
     * @return string representing a URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL.
     *
     * @param url a valid URL with protocol information such as
     *            http://icesoft.com
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        System.out.println("Setting value " + selected);
    }

}
