/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package com.icesoft.applications.faces.webmail.navigation;

import com.icesoft.applications.faces.webmail.WebmailMediator;
import com.icesoft.applications.faces.webmail.WebmailBase;
import com.icesoft.faces.component.dragdrop.DragEvent;
import com.icesoft.faces.component.dragdrop.DropEvent;
import com.icesoft.faces.component.tree.IceUserObject;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <p>The PageContentBean class is responsible for holding state information
 * which will allow a NavigationTree and NavigationSelectionBean display
 * dynamic content. </p>
 *
 * @since 1.0
 */
public class NavigationContent extends IceUserObject implements WebmailBase {

    // logger
    protected static Log log = LogFactory.getLog(NavigationContent.class);

    // template, default panel to make visible in a panel stack
    private String templateName = "";

    // text to be displayed in navigation link
    private String menuDisplayText;
    // title information to be displayed
    private String menuContentTitle;

    // True indicates that there is content associated with link and as a
    // resulst templateName and contentPanelName can be used. Otherwise we
    // just toggle the branch visibility.
    private boolean pageContent = true;

    // flag to indicate drag and drop when used in the tree node
    private String enableDnd = "false";

    // Each component example needs extra information tobe displayed.  this
    // variables store that information

    // message bundle for component.
    private static ResourceBundle messages = null;

    // view reference to control the visible content
    protected NavigationSelectionBean navigationSelection;

    // effects related to dnd, success and failure effects
    protected final Effect dropSuccessEffect = new Highlight("#FFFF66");
    protected final Effect dropFailureEffect = new Highlight("#FF3333");
    // current effect to show, based on drop event turnout.
    protected Effect currentEffect;

    public NavigationContent() {
        super(null);
        init();
    }

    /**
     * Build a default node for the tree.  We also change the default icon
     * and always expand branches.
     */
    public NavigationContent(DefaultMutableTreeNode treeNode,
                             NavigationSelectionBean navigationSelection) {
        super(treeNode);
        this.navigationSelection = navigationSelection;
        init();
    }

    public NavigationSelectionBean getNavigationSelection() {
        return navigationSelection;
    }

    public void setNavigationSelection(NavigationSelectionBean navigationSelection) {
        this.navigationSelection = navigationSelection;
    }

    /**
     * Initilize internationalization.
     */
    public void init() {
        restoreDefaultIcons();
        setExpanded(true);
        // set folder icons, if needed.
        setBranchContractedIcon("images/folderclosed.gif");
        setBranchExpandedIcon("images/folderopen.gif");
        setLeafIcon("images/folderclosed.gif");
        messages = WebmailMediator.getMessageBundle();
    }

    /**
     * Dispose this object on webmail shutdown.  Currently does nothing but can
     * be overridden.
     */
    public void dispose() {

    }

    /**
     * Gets the template name to display in the showCase.jspx.  The template
     * a panel in a panel stack which will be made visible.
     *
     * @return panel stack template name.
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * Sets the template name to be displayed when selected in tree. Selection will
     * only occur if pageContent is true.
     *
     * @param templateName valid panel name in showcase.jspx
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * Gets the menu display text.  This text will be shown in the navigation tree.
     *
     * @return menu display text.
     */
    public String getMenuDisplayText() {
        // get localized string value
        String value;
        try {
            value = messages.getString(menuDisplayText);
        }
        catch (MissingResourceException e) {
            return menuDisplayText;
        }
        return value;
    }

    /**
     * Sets the text to be displayed in the menu.  This text string must match
     * a resource property in com.icesoft.icefaces.samples.showcase.resources.messages
     *
     * @param menuDisplayText menu text to display
     */
    public void setMenuDisplayText(String menuDisplayText) {
        if (menuDisplayText != null) {
            this.menuDisplayText = menuDisplayText;
            // set tree node text value
            setText(getMenuDisplayText());
        } else {
            this.menuDisplayText = "";
        }
    }

    /**
     * Get the text to be displayed as the content title.  This text string must
     * match resource property in com.icesoft.icefaces.samples.showcase.resources.messages
     *
     * @return menu content title
     */
    public String getMenuContentTitle() {
        String value;
        try {
            if (menuContentTitle != null) {
                value = messages.getString(menuContentTitle);
            } else {
                return "";
            }
        }
        catch (MissingResourceException e) {
            return menuContentTitle;
        }
        return value;
    }

    /**
     * Sets the menu content title.
     *
     * @param menuContentTitle menu content title name.
     */
    public void setMenuContentTitle(String menuContentTitle) {
        if (menuContentTitle != null) {
            this.menuContentTitle = menuContentTitle;
        } else {
            this.menuContentTitle = "";
        }
    }

    /**
     * Does the node contain content.
     *
     * @return true if the page contains content; otherwise, false.
     */
    public boolean isPageContent() {
        return pageContent;
    }

    /**
     * Sets the page content.
     *
     * @param pageContent True if the page contains content; otherwise, false.
     */
    public void setPageContent(boolean pageContent) {
        this.pageContent = pageContent;
    }

    /**
     * Sets the navigationSelctionBeans selected state
     */
    public void contentVisibleAction(ActionEvent event) {
        if (isPageContent()) {

            // only toggle the branch expansion if we have already selected the node
            if (navigationSelection.getSelectedPanel().equals(this)) {
                // toggle the branch node expansion
                setExpanded(!isExpanded());
            }
            navigationSelection.setSelectedPanel(this);
        }
        // Otherwise toggle the node visibility, only changes the state
        // of the notes with children.
        else {
            setExpanded(!isExpanded());
        }
    }

    /**
     * Called to restore the default icons for the navigation objects view. 
     */
    public void restoreDefaultIcons(){

    }

    /**
     * Called when an drop event occures on this navigation content.
     *
     * @param event drop event.
     */
    public void navigationDropAction(DropEvent event) {
        currentEffect = dropFailureEffect;
        dropFailureEffect.setFired(false);
    }

    /**
     * Called when a drap event occures on the navigation content.  Not implemented
     * @param event
     */
    public void navigationDragAction(DragEvent event) {

    }

    /**
     * Gets the drop effect associated with this navigation node.
     * @return effect if any, null otherwise. 
     */
    public Effect getDropEffect(){
        return currentEffect;
    }


    public String getEnableDnd() {
        return enableDnd;
    }

    public void setEnableDnd(String enableDnd) {
        this.enableDnd = enableDnd;
    }
}