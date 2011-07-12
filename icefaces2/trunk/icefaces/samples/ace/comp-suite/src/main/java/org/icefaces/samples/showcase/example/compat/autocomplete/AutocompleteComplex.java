package org.icefaces.samples.showcase.example.compat.autocomplete;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.selectinputtext.TextChangeEvent;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = AutocompleteBean.BEAN_NAME,
        title = "example.compat.autocomplete.complex.title",
        description = "example.compat.autocomplete.complex.description",
        example = "/resources/examples/compat/autocomplete/autocompleteComplex.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="autocompleteComplex.xhtml",
                    resource = "/resources/examples/compat/"+
                               "autocomplete/autocompleteComplex.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AutocompleteComplex.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/autocomplete/AutocompleteComplex.java")
        }
)
@ManagedBean(name= AutocompleteComplex.BEAN_NAME)
@ViewScoped
public class AutocompleteComplex extends ComponentExampleImpl<AutocompleteComplex> implements Serializable {
	
	public static final String BEAN_NAME = "autocompleteComplex";
	
	private List<SelectItem> availableCities = AutocompleteData.wrapList();
	private String selectedText = null; // Text the user is typing in
	private String selectedCity;        // Entry the user selected
	
	public AutocompleteComplex() {
		super(AutocompleteComplex.class);
	}
	
	public List<SelectItem> getAvailableCities() { return availableCities; }
	public String getSelectedText() { return selectedText; }
	public String getSelectedCity() { return selectedCity; }
	
	public void setAvailableCities(List<SelectItem> availalbeCities) { this.availableCities = availableCities; }
	public void setSelectedText(String selectedText) { this.selectedText = selectedText; }
	public void setSelectedCity(String selectedCity) { this.selectedCity = selectedCity; }
	
	public void textChanged(TextChangeEvent event) {
	    // Filter the list of cities based on what the user has typed so far
	    availableCities = AutocompleteData.wrapList(event.getNewValue().toString());
	}
	
	public void submitText(ActionEvent event) {
	    setSelectedCity(getSelectedText());
	    
	    // Clear out filtered list now that we have made a choice
	    availableCities = null;
	}
}
