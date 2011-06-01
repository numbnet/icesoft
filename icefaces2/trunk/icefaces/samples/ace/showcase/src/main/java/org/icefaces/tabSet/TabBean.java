/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.tabSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.Application;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.ace.component.tab.TabPane;
import org.icefaces.ace.component.tab.TabSet;

@ManagedBean (name="tabBean")
@SessionScoped
public class TabBean implements Serializable{
    private String inpTxt;
    private String richText;
    private TabSet tabSet;
    private int tabIndex = 0;
    private int userDefineIndex = 0;
    private boolean renderTab1 = true;
    private boolean renderTab2 = true;
    private boolean renderTab3 = true;
    private SelectItem[] orientations = {new SelectItem("top", "top"), new SelectItem("bottom", "bottom"), new SelectItem("left", "left"), new SelectItem("right", "right")};
    private SelectItem[] tabIndexes = {new SelectItem(new Integer(0), "1"), new SelectItem(new Integer(1), "2")};
    private String orientation="top";  //originally set to top

    private String tabContents = "This tabPane represents a simple pane with text";
    private boolean cancelOnInvalid = false;
    private boolean showPopup;
    private int selectedTabIndex = 0;
    private String delayedContents = "TabPane contents";
    private boolean closeButton1, closeButton2, closeButton3;
    private int labelFacetIndex=0;
    private boolean closeTabValue;
    private boolean renderPanel = true; 
    private List<Employee> employees = new ArrayList<Employee>();
    
 
	private boolean formRendered = true;
    public TabBean() {
    	employees.add(new Employee("John", "Smith", "123 Oak Dr, Calgary, AB", "jsmith@icesoft.com"));
    	employees.add(new Employee("Nancy", "Brown", "456 Elm Lane, Calgary, AB", "nbrown@icesoft.com"));
    	employees.add(new Employee("James", "Gagnon", "789 Birch Grove, Calgary, AB", "jgagnon@icesoft.com"));
    	employees.add(new Employee("Sara", "Messier", "120 Maple Circle, Calgary, AB", "smessier@icesoft.com"));
    }
    
   public void tabsetChangeListener(ValueChangeEvent event) {
        System.out.println("tabsetChangeListener "+ event.getComponent());
   }
   
    public String getInpTxt() {
        return inpTxt;
    }
    public void setInpTxt(String inpTxt) {
        this.inpTxt = inpTxt;
    }
    public String getRichText() {
        return richText;
    }
    public void setRichText(String richText) {
        this.richText = richText;
    }

    public TabSet getTabSet() {
        return tabSet;
    }
    public void setTabSet(TabSet tabSet) {
        this.tabSet = tabSet;
    } 
    int i=2;
 
    public int getTabIndex() {
        return tabIndex;
    }
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
   }
    public SelectItem[] getTabIndexes() {
        return tabIndexes;
    }
    public void setTabIndexes(SelectItem[] tabIndexes) {
        this.tabIndexes = tabIndexes;
    }


    public void tabIndexChanged(ValueChangeEvent event) {
        System.out.println("------>> TabIndexChanged");
        
        try {
            tabIndex = ((Integer)event.getNewValue()).intValue();
        } catch (Exception e){}
    }
    
    public void orientationChanged(ValueChangeEvent event) {
        System.out.println("------>> orientationChanged");
        
        try {
            orientation = String.valueOf(event.getNewValue());
        } catch (Exception e){}
    }
    
    public boolean isRenderTab1() {
        return renderTab1;
    }
    public void setRenderTab1(boolean renderTab) {
        this.renderTab1 = renderTab;
    }
    public boolean isRenderTab2() {
        return renderTab2;
    }
    public void setRenderTab2(boolean renderTab) {
        this.renderTab2 = renderTab;
    }
    public boolean isRenderTab3() {
        return renderTab3;
    }
    public void setRenderTab3(boolean renderTab) {
        this.renderTab3 = renderTab;
    }
    
    
    private void resetIndexes() {
        int childCount = tabSet.getChildCount();
        int diff = 1;
        if (!renderTab1) {
            childCount = childCount -1;
            diff = diff + 1;
        }
     
        
        tabIndexes = new SelectItem[childCount];
        for (int i=0; i < childCount; i++) {
            System.out.println("i ="+ i + " : diff="+ diff);
            tabIndexes[i] = new SelectItem(new Integer(i), new String( ""+(i + diff)));
        }
    }
    public SelectItem[] getOrientations() {
        return orientations;
    }
    public void setOrientations(SelectItem[] orientations) {
        this.orientations = orientations;
    }

    public String getOrientation() {
        return orientation;
    }
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
 
    public String getTabContents() {
        return tabContents;
    }
    public void setTabContents(String tabContents) {
        this.tabContents = tabContents;
    }    
  
 
    public void tabChangeListener(ValueChangeEvent event) {
        this.selectedTabIndex = Integer.parseInt(event.getNewValue().toString());
        System.out.println("TabBean:tabChangeListener: selectedTabIndex="+this.selectedTabIndex);
    }
    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }
    public void setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }
    
    private boolean faceTab1Rendered = true;

    public boolean isFaceTab1Rendered() {
        return faceTab1Rendered;
    }
 
    private boolean faceTab2Rendered = true;

    public boolean isFaceTab2Rendered() {
        return faceTab2Rendered;
    }
     private boolean faceTab3Rendered = true;

    public boolean isFaceTab3Rendered() {
        return faceTab3Rendered;
    }
     
    public void closeTab1(ActionEvent event) {
        faceTab1Rendered = false;
    }
    public void closeTab2(ActionEvent event) {
        faceTab2Rendered = false;
    }
    public void closeTab3(ActionEvent event) {
        faceTab3Rendered = false;
    }

    public void openClosedTabs(ActionEvent event){
        faceTab1Rendered = faceTab2Rendered = faceTab3Rendered = true;
    }
    
    
    public void formRenderedChange(ActionEvent event) {
        formRendered = !formRendered;
        System.out.println("Form rendered value change to "+ formRendered);
    }

    public boolean isFormRendered() {
        return formRendered;
    }

    public void setFormRendered(boolean formRendered) {
        this.formRendered = formRendered;
    }

    public boolean isCloseTabValue() {
        return closeTabValue;
    }

    public void setCloseTabValue(boolean closeTabValue) {
        this.closeTabValue = closeTabValue;
    }
    
    public void toggleRenderPanel(ActionEvent event) {
    	renderPanel = !renderPanel;    	
    }

	public boolean isCancelOnInvalid() {
		return cancelOnInvalid;
	}

	public void setCancelOnInvalid(boolean cancelOnInvalid) {
		this.cancelOnInvalid = cancelOnInvalid;
	}

	public boolean isRenderPanel() {
			return renderPanel;
	}

	public void setRenderPanel(boolean renderPanel) {
			this.renderPanel = renderPanel;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
}
