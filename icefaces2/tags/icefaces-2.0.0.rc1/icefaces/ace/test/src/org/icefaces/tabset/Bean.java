package org.icefaces.tabset;

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
public class Bean {
    private String inpTxt;
    private String richText;
    private Date selectedDate;
    private TabSet tabSet;
    private int tabIndex = 0;
    private int userDefineIndex = 0;
    private boolean renderTab0 = true;
    private boolean renderTab1 = true;
    private SelectItem[] orientations = {new SelectItem("top", "top"), new SelectItem("bottom", "bottom"), new SelectItem("left", "left"), new SelectItem("right", "right")};
    private SelectItem[] tabIndexes = {new SelectItem(new Integer(0), "1"), new SelectItem(new Integer(1), "2")};
    private String userDefineOrientation = "top";
    private String orientation;
    private String txt1;
    private String txt2;
    private String txt3;
    private String txt4;    
    private String tabContents = "The TabView control is defined by YAHOO.widget.TabView. To create a TabView from existing markup you can simply pass the id (or object reference) for the HTMLElement that will become the TabView. If you follow the default markup pattern outlined below, the tabs will be constructed automatically.";
    private List movies = new ArrayList();
    private String title;
    private String director;
    private String plot;
    private String genre;
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
    public Bean() {
        Movie m1 = new Movie();
        m1.setTitle("The Shawshank Redemption (1994)");
        m1.setDirector("Frank Darabont");
        m1.setPlot("Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency");
        m1.setGenre("Drama");
        movies.add(m1);
        
        Movie m2 = new Movie();
        m2.setTitle("The Godfather (1972)");
        m2.setDirector("Francis Ford Coppola");
        m2.setPlot("The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.");
        m2.setGenre("Crime | Drama | Thriller");
        movies.add(m2);     
        
        Movie m3 = new Movie();
        m3.setTitle("Pulp Fiction (1994)");
        m3.setDirector("Quentin Tarantino");
        m3.setPlot("The lives of two mob hit men, a boxer, a gangster's wife, and a pair of diner bandits intertwine in four tales of violence and redemption.");
        m3.setGenre("Crime | Thriller");
        movies.add(m3);     
        
        Movie m4 = new Movie();
        m4.setTitle("Casablanca (1942)");
        m4.setDirector("Michael Curtiz");
        m4.setPlot("Set in unoccupied Africa during the early days of World War II: An American expatriate meets a former lover, with unforeseen complications.");
        m4.setGenre("Drama");
        movies.add(m4); 
        
        Movie m5 = new Movie();
        m5.setTitle("Memento (2000)");
        m5.setDirector("Christopher Nolan");
        m5.setPlot("A man, suffering from short-term memory loss, uses notes and tattoos to hunt for the man he thinks killed his wife.");
        m5.setGenre("Mystery | Thriller");
        movies.add(m5); 
        
        Movie m6 = new Movie();
        m6.setTitle("Fight Club (1999)");
        m6.setDirector("David Fincher");
        m6.setPlot("An office employee and a soap salesman build a global organization to help vent male aggression.");
        m6.setGenre("Action | Crime | Drama | Thriller");
        movies.add(m6); 
        
        for (int i= 0; i < 500; i++) {
            m6 = new Movie();
            m6.setTitle("Fight Club (1999)"+i);
            m6.setDirector("David Fincher");
            m6.setPlot("An office employee and a soap salesman build a global organization to help vent male aggression.");
            m6.setGenre("Action | Crime | Drama | Thriller");
            movies.add(m6);             
        }
        
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
    public Date getSelectedDate() {
        return selectedDate;
    }
    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
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
    public String getUserDefineOrientation() {
        return userDefineOrientation;
    }
    public void setUserDefineOrientation(String userDefineOrientation) {
        this.userDefineOrientation = userDefineOrientation;
    }
    public String getOrientation() {
        return orientation;
    }
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    public String getTxt1() {
        System.out.println("getTxt1()" + txt1);
        return txt1;
    }
    public void setTxt1(String txt1) {
        System.out.println("setTxt1()" + txt1);        
        this.txt1 = txt1;
    }
    public String getTxt2() {
        System.out.println("getTxt2()" + txt2);        
        return txt2;
    }
    public void setTxt2(String txt2) {
        System.out.println("setTxt2()" + txt2);        
        this.txt2 = txt2;
    }
    public String getTabContents() {
        return tabContents;
    }
    public void setTabContents(String tabContents) {
        this.tabContents = tabContents;
    }
    public List getMovies() {
        return movies;
    }
    public void setMovies(List movies) {
        this.movies = movies;
    }
   
    public void addMovie(ActionEvent event) {
        Movie m = new Movie();
        m.setDirector(director);
        m.setGenre(genre);
        m.setPlot(plot);
        m.setTitle(title);
        movies.add(m);
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public String getPlot() {
        return plot;
    }
    public void setPlot(String plot) {
        this.plot = plot;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public void setMovieIndexes(List indexes) {
        
    }
    
    public List getMovieIndexes() {
        List indexes = new ArrayList();
        for (int i=0; i <movies.size(); i++) {
            indexes.add(new SelectItem(""+i));
        }
        return indexes;
    }    
    
    Object deleteIndex = "0";
    public void movieIndexChanged(ValueChangeEvent event) {
        deleteIndex = event.getNewValue();
    }

    public void deleteMovie(ActionEvent event) {
        if (deleteIndex != null) {
            movies.remove(Integer.parseInt(deleteIndex.toString()));
        }
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
    public int getLabelFacetIndex() {
        return labelFacetIndex;
    }
    public void setLabelFacetIndex(int labelFacetIndex) {
        this.labelFacetIndex = labelFacetIndex;
    }

    public String getTxt3() {
        System.out.println("getTxt3()" + txt3);         
        return txt3;
    }

    public void setTxt3(String txt3) {
        System.out.println("setTxt3()" + txt3);         
        this.txt3 = txt3;
    }

    public String getTxt4() {
        System.out.println("getTxt4()" + txt4);         
        return txt4;
    }

    public void setTxt4(String txt4) {
        System.out.println("setTxt4()" + txt4);         
        this.txt4 = txt4;
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
   
}
