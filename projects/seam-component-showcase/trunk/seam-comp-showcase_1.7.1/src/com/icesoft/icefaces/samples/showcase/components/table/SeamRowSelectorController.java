package com.icesoft.icefaces.samples.showcase.components.table;

import static org.jboss.seam.ScopeType.PAGE;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import com.icesoft.icefaces.samples.showcase.common.Employee;
import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import com.icesoft.faces.component.ext.RowSelectorEvent;


//not using this bean at all...just use the list that is generated
//in components.xml 
@Scope(PAGE)
@Name("rowSelectController")
public class SeamRowSelectorController implements Serializable{
	
	@In("#{employeesListRowSelector.resultList}")
	private List employees;
	   // list of selected employees
    private ArrayList selectedEmployees = new ArrayList();

    // flat to indicate multiselect row enabled.
    private boolean multiRowSelect;

 
    /**
     * SelectionListener bound to the ice:rowSelector component.  Called
     * when a row is selected in the UI.
     *
     * @param event from the ice:rowSelector component
     */
    public void rowSelectionListener(RowSelectorEvent event) {
        // clear our list, so that we can build a new one
        selectedEmployees.clear();

        // build the new selected list
        Employee employee;
        for(int i = 0, max = employees.size(); i < max; i++){
        employee = (Employee)employees.get(i);
            if (employee.isSelected()) {
                selectedEmployees.add(employee);
            }
        }
    }

    /**
     * Clear the selection list if going from multi select to single select.
     *
     * @param event jsf action event.
     */
    public void rowSelectionListener(ValueChangeEvent event) {
        // if multi select then want to make sure we clear the selected states
        if (!((Boolean) event.getNewValue()).booleanValue()) {
            selectedEmployees.clear();

            // build the new selected list
            Employee employee;
            for(int i = 0, max = employees.size(); i < max; i++){
                employee = (Employee)employees.get(i);
                employee.setSelected(false);
            }
        }
    }

    public ArrayList getSelectedEmployees() {
        return selectedEmployees;
    }

    public void setSelectedEmployees(ArrayList selectedEmployees) {
        this.selectedEmployees = selectedEmployees;
    }

    public boolean isMultiRowSelect() {
        return multiRowSelect;
    }

    /**
     * Sets the selection more of the rowSelector.
     *
     * @param multiRowSelect true indicates multi-row select and false indicates
     *                       single row selection mode.
     */
    public void setMultiRowSelect(boolean multiRowSelect) {
        this.multiRowSelect = multiRowSelect;
    }
	public List getEmployees(){
		return this.employees;
	}
	
	
}
