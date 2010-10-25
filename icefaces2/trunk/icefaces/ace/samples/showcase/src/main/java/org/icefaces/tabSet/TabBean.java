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

import org.icefaces.component.tab.TabPane;
import org.icefaces.component.tab.TabSet;

@ManagedBean (name="tabBean")
@SessionScoped
public class TabBean implements Serializable{
    private String inpTxt;
    private String richText;
    private TabSet tabSet;
    private int tabIndex = 0;
    private int userDefineIndex = 0;
    private boolean renderTab0 = true;
    private boolean renderTab1 = true;
    private SelectItem[] orientations = {new SelectItem("top", "top"), new SelectItem("bottom", "bottom"), new SelectItem("left", "left"), new SelectItem("right", "right")};
    private SelectItem[] tabIndexes = {new SelectItem(new Integer(0), "1"), new SelectItem(new Integer(1), "2")};
    private String orientation="top";  //originally set to top

    private String tabContents = "This tabPane represents a simple pane with text";
    private boolean cancelOnInvalid = false;
    private boolean showPopup;
    private int selectedTabIndex = 0;
    private String delayedContents = "TabPane contents";
    private boolean closeButton;
    private int labelFacetIndex=0;
    private boolean closeTabValue;
    private boolean renderPanel = true; 
    private int counter = 0;
    
    public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public boolean isRenderPanel() {
		return renderPanel;
	}

	public void setRenderPanel(boolean renderPanel) {
		this.renderPanel = renderPanel;
	}
	private boolean formRendered = true;
    public TabBean() {
   
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
    public void addTab(ActionEvent event) {
        System.out.println("Add tab called................." +
        		"" );
        Application application = FacesContext.getCurrentInstance().getApplication();

        TabPane tabPane = (TabPane) application
                .createComponent(TabPane.COMPONENT_TYPE);
        String tabid = "id"+ i;
        tabPane.setId(tabid);
        
        HtmlOutputText output = (HtmlOutputText) application
        .createComponent(HtmlOutputText.COMPONENT_TYPE); 
        output.setId(FacesContext.getCurrentInstance().getViewRoot().createUniqueId());
        output.setRendererType("javax.faces.Text");
        output.setValue("This is tab "+ ++i);
        tabPane.getChildren().add(output);
        tabPane.setLabel("Tab "+ i);
        tabSet.getChildren().add(tabPane);
        resetIndexes();        
    }
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
    
    public int getUserDefineIndex() {
        return userDefineIndex;
    }
    public void setUserDefineIndex(int userDefineIndex) {
        this.userDefineIndex = userDefineIndex;
    }
    public boolean isRenderTab0() {
        return renderTab0;
    }
    public void setRenderTab0(boolean renderTab0) {
        this.renderTab0 = renderTab0;
    }
    public boolean isRenderTab1() {
        return renderTab1;
    }
    public void setRenderTab1(boolean renderTab1) {
        this.renderTab1 = renderTab1;
    }
    
    public String getRederTab0Txt() {
        return "Tab1 render ="+ renderTab0;
    }
    
    public String getRederTab1Txt() {
        return "Tab2 render ="+ renderTab1;
    }  
    
    public void flipTab0(ActionEvent event) {
        renderTab0 = !renderTab0;
        resetIndexes();        
    }
    
    public void flipTab1(ActionEvent event) {
        renderTab1 = !renderTab1; 
        resetIndexes();
    }
    
    private void resetIndexes() {
        int childCount = tabSet.getChildCount();
        int diff = 1;
        if (!renderTab0) {
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
  
    public boolean isShowPopup() {
        return showPopup;
    }
    public void setShowPopup(boolean showPopup) {
        this.showPopup = showPopup;
    }
    public void flipPopupState(ActionEvent event) {
        showPopup = !showPopup;
    }
    public String getDelayedContents() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return delayedContents;
        
    }
    public void setDelayedContents(String delayedContents) {
        this.delayedContents = delayedContents;
    }
    
    public void tabChangeListener(ValueChangeEvent event) {
        selectedTabIndex = Integer.parseInt(event.getNewValue().toString());
    }
    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }
    public void setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }
    public boolean isCloseButton() {
        return closeButton;
    }
    public void setCloseButton(boolean closeButton) {
        this.closeButton = closeButton;
    }
    
    private boolean faceTabRendered = true;

    public boolean isFaceTabRendered() {
        return faceTabRendered;
    }
    public void setFaceTabRendered(boolean faceTabRendered) {
        this.faceTabRendered = faceTabRendered;
    }
    
    public void closeTab(ActionEvent event) {
        faceTabRendered = !faceTabRendered;
        labelFacetIndex = 0;
    }
    public void openClosedTab(ActionEvent event){
        faceTabRendered = !faceTabRendered;
        labelFacetIndex = 0;	
    }
    
    public int getLabelFacetIndex() {
        return labelFacetIndex;
    }
    public void setLabelFacetIndex(int labelFacetIndex) {
        this.labelFacetIndex = labelFacetIndex;
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
    
    public void incrementCounter(ActionEvent event) {
    	counter++;    	
    }

	public boolean isCancelOnInvalid() {
		return cancelOnInvalid;
	}

	public void setCancelOnInvalid(boolean cancelOnInvalid) {
		this.cancelOnInvalid = cancelOnInvalid;
	}
   
}
