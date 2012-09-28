package org.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.demo.model.Person;

import com.icesoft.faces.component.ext.RowSelectorEvent;

@ManagedBean
@ViewScoped
public class DataTableBean implements Serializable {

	private static final long serialVersionUID = -2904483316550697214L;
	private List<Person> personData;
	private Person personDetails;
	private Integer index = null;
	private Integer newIndex;
	private boolean confirmDialog = false;
	private boolean detailsHidden = true;
	private boolean change = false;

	@PostConstruct
	public void initData() {
		personData = new ArrayList<Person>();
		// Add some people
		personData.add(new Person(1, "Peter", "Parker",
				"150 Main Street, New York, NY, USA"));
		personData.add(new Person(2, "Ryan", "Penner",
				"528 Newmarket Road, Wilston, QLD, Australia"));
		personData.add(new Person(3, "Paul", "Wilson",
				"50 Big Ben Road, London, UK"));
	}

	public List<Person> getPersonData() {
		return personData;
	}

	public void setPersonData(List<Person> personData) {
		this.personData = personData;
	}

	public Person getPersonDetails() {
		return personDetails;
	}

	public void setPersonDetails(Person personDetails) {
		this.personDetails = personDetails;
	}

	public void selectionListener(RowSelectorEvent event) {
		// First selection or no changes to the current record
		if (index == null || !change) {
			index = event.getRow();
			try {
				setPersonDetails((Person) personData.get(index).clone());
			} catch (CloneNotSupportedException e) {
				System.out.println(e);
			}

			detailsHidden = false;
		}
		// Something else is already selected
		else if (index != event.getRow()) {
			// Throw a warning to the user
			newIndex = event.getRow();
			setConfirmDialog(true);
		}
	}

	public void confirmYes() {
		index = newIndex;
		setPersonDetails(personData.get(index));
		confirmDialog = false;
		detailsHidden = false;
		change = false; // We discard the old record
	}

	public void confirmNo() {
		newIndex = null;
		confirmDialog = false;

	}

	public void changeListener(AjaxBehaviorEvent event) {
		change = true;
	}

	public void save() {
		// Reset the change flag
		change = false;
		// Save the person
		personData.set(index, personDetails);
	}

	public boolean isConfirmDialog() {
		return confirmDialog;
	}

	public void setConfirmDialog(boolean confirmDialog) {
		this.confirmDialog = confirmDialog;
	}

	public boolean isDetailsHidden() {
		return detailsHidden;
	}

	public void setDetailsHidden(boolean detailsHidden) {
		this.detailsHidden = detailsHidden;
	}
}
