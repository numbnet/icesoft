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

package com.icesoft.icefaces.samples.showcase.navigation;

import com.icesoft.faces.component.tree.IceUserObject;
import com.icesoft.icefaces.samples.showcase.util.StyleBean;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.core.Manager;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p>The PageContentBean class is responsible for holding state information
 * which will allow a TreeNavigation and NavigationBean display dynamic content.
 * </p>
 *
 * @since 0.3.0
 */

public class PageContentBean extends IceUserObject implements Serializable{
	   private static Log log =
           LogFactory.getLog(PageContentBean.class);   
    // template, default panel to make visible in a panel stack
    private String templateName = "";

    // text to be displayed in navigation link
    private String menuDisplayText;
    // title information to be displayed
    private String menuContentTitle;
    private String menuContentInclusionFile;
    
    // all but one component doesn't require LR Conversation
    private boolean startLongRunningConversation=false;

    // True indicates that there is content associated with link and as a
    // result templateName and contentPanelName can be used. Otherwise we
    // just toggle the branch visibility.
    private boolean pageContent = true;

    // Each component example needs extra information to be displayed.  These
    // variables store that information

    // message bundle for component.
    private static ResourceBundle messages = null;

    // view reference to control the visible content
 //   @In @Out
 //   private NavigationBean navigationBean;

    private TreeNavigation treeNav;
    /**
     * Build a default node for the tree.  We also change the default icon and
     * always expand branches.
     */
    public PageContentBean(TreeNavigation treeNav) {
        super(null);
        init();
        this.treeNav = treeNav;
    }

    /**
     * Initialize internationalization.
     */
    private void init() {
    	//why set the images statically?  They should be set properly from 
    	//the StyleBean's current set of images???
 //       log.info("in init and setting icons");
        setBranchContractedIcon(StyleBean.XP_BRANCH_CONTRACTED_ICON);
        setBranchExpandedIcon(StyleBean.XP_BRANCH_EXPANDED_ICON);
        setLeafIcon("./images/gear.gif");
        setExpanded(true);

        //will set the locale from the LanguageBean--soon to be LocaleBean??
 //        log.info("getting locale");
        Locale locale=null;
        try{
             locale=FacesContext.getCurrentInstance().getViewRoot().getLocale();
        }catch (Exception e){
  //          log.info("exception getting locale from FacesContext");
        }
        // assign a default locale if the faces context has none, shouldn't happen
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
   //     log.info("loading messages");
        messages = ResourceBundle.getBundle(
                "com.icesoft.icefaces.samples.showcase.resources.messages",
                locale);
   //     log.info("end of init");  
    }



    /**
     * Gets the template name to display in the showcase.jspx.  The template is
     * a panel in a panel stack which will be made visible.
     *
     * @return panel stack template name.
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * Sets the template name to be displayed when selected in tree. Selection
     * will only occur if pageContent is true.
     *
     * @param templateName valid panel name in showcase.jspx
     */
    public void setTemplateName(String templateName) {
 //   	log.info("setting template name to "+templateName);
        this.templateName = templateName;
    }

    /**
     * Gets the menu display text.  This text will be shown in the navigation
     * tree.
     *
     * @return menu display text.
     */
    public String getMenuDisplayText() {
        // get localized string value
        return messages.getString(menuDisplayText);
    }

    /**
     * Sets the text to be displayed in the menu.  This text string must match a
     * resource property in com.icesoft.icefaces.samples.showcase.resources.messages
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
        if (menuContentTitle != null && !menuContentTitle.equals("")) {
            return messages.getString(menuContentTitle);
        } else {
            return "";
        }
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
    
    public String getMenuContentInclusionFile() {
        return menuContentInclusionFile;
    }

    /**
     * This is necessary for the Facelets version of component-showcase.
     * Unlike the JSP version of component-showcase, which uses static includes,
     *   the Facelets version uses dynamic inclusion tied to an EL expression,
     *   which will call getMenuContentInclusionFile().
     * 
     * @param menuContentInclusionFile The server-side path to the file to be included
     */
    public void setMenuContentInclusionFile(String menuContentInclusionFile) {
        this.menuContentInclusionFile = menuContentInclusionFile;
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
     * Sets the navigationSelectionBeans selected state
     */
    public void contentVisibleAction(ActionEvent event) {
        if (isPageContent()) {
            // only toggle the branch expansion if we have already selected the node
                // toggle the branch node expansion
                setExpanded(!isExpanded());
 //               if (this.templateName.equals("tabbedPaneContentPanel")){
 //               	log.info("this is a panelTabSet so start LR Conversation");
  //              	setStartLongRunningConversation(true);
//                }else{
//                	log.info("not panelTabSet & LR Conversation="+Manager.instance().isLongRunningConversation());
//                	setStartLongRunningConversation(false);
//                }
                	
                treeNav.setCurrentPageContent(this);
    //        }
    //        navigationBean.setSelectedPanel(this);
         }
        // Otherwise toggle the node visibility, only changes the state
        // of the nodes with children.
        else {
            setExpanded(!isExpanded());
        }
    }
/*DON'T NEED THIS ANYMORE!!! made panelTabSet page scoped !!
 * all components but panelTabSet currently don't require a LR conversation
 * Thus check to see if a LR conversation exists and end it.  Seam will 
 * automatically demote to temporary conversation
 */
	public boolean isStartLongRunningConversation() {
		return startLongRunningConversation;
	}

	/*DON'T NEED THIS ANYMORE!!! made panelTabSet page scoped !!
	 * so far the only PageContentBean requiring a LR conversation is panelTabSet
	 * If startLongRunningConversation == true, then make sure there isn't one already 
	 * present to join.  If not, then start one, otherwise join ?? or maybe end the
	 * old one and start a new one???
	 */
	public void setStartLongRunningConversation(boolean startLongRunningConversation) {
		log.info("setStartLongRunningConversation = "+startLongRunningConversation);
		this.startLongRunningConversation = startLongRunningConversation;
		if (startLongRunningConversation){
			if (Manager.instance().isLongRunningConversation() ){
				log.info("already have a long running Conversation!!");
				//join it!
				joinConversation();
			}
			else {
				log.info("starting LR Conversation");
				startConversation();
			}
		}else if (Manager.instance().isLongRunningConversation()){
			log.info("ending LR Conversation");
			endConversation();
		}

	}
	@End
	public void endConversation(){
		log.info("LR Conversation ended");
	}
	@Begin
	public void startConversation(){
		log.info("LR Conversation started");
	}
	@Begin(join=true)
	public void joinConversation(){
		log.info("Joined Conversation");
	}
}