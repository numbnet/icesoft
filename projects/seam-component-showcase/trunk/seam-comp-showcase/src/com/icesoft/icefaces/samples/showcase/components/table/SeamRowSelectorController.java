package com.icesoft.icefaces.samples.showcase.components.table;

import static org.jboss.seam.ScopeType.PAGE;

import java.util.ArrayList;

import javax.faces.event.ValueChangeEvent;

import com.icesoft.icefaces.samples.showcase.common.Employee;


import org.icefaces.application.showcase.view.bean.examples.component.rowSelector.RowSelectController;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import org.jboss.seam.log.Log;

import com.icesoft.faces.component.ext.RowSelectorEvent;


//not using this bean at all...just use the list that is generated
//in components.xml 
@Scope(PAGE)
@Name("rowSelectController")
public class SeamRowSelectorController extends RowSelectController{
	
	@Logger Log log;
	@In("#{employeesListRowSelector.resultList}")
	private ArrayList seamEmployees;
	   // list of selected employees


 
    @Create
    public void init(){
      super.employees = this.seamEmployees;
    }
 
    @Override
    public ArrayList getEmployees(){return this.employees;}
    /**
     * SelectionListener bound to the ice:rowSelector component.  Called
     * when a row is selected in the UI.
     * Had to override this one too as Seam uses annotated Entity class
     * with data in the DB
     *
     * @param event from the ice:rowSelector component
     */
    @Override
    public void rowSelectionListener(RowSelectorEvent event) {
        // clear our list, so that we can build a new one
        selectedEmployees.clear();
        
        /* If application developers rely on validation to control submission of the form or use the result of
           the selection in cascading control set up the may want to defer procession of the event to
           INVOKE_APPLICATION stage by using this code fragment
		    if (event.getPhaseId() != PhaseId.INVOKE_APPLICATION) {
		       event.setPhaseId( PhaseId.INVOKE_APPLICATION );
		       event.queue();
		       return;
		    }

         */

        // build the new selected list
        Employee employee;
        for(int i = 0, max = employees.size(); i < max; i++){
        employee = (Employee)employees.get(i);
            if (employee.isSelected()) {
                selectedEmployees.add(employee);
            }
        }
        /* If application developers do not rely on validation and want to bypass UPDATE_MODEL and
           INVOKE_APPLICATION stages, they may be able to use the following statement:
           FacesContext.getCurrentInstance().renderResponse();
           to send application to RENDER_RESPONSE phase shortening the app. cycle
         */
    }
    /**
     * Clear the selection list if going from multi select to single select.
     *  Again...have to override due to different Employee Object used
     * @param event jsf action event.
     */
    @Override
    public void changeSelectionMode(ValueChangeEvent event) {
        String newValue = event.getNewValue().toString(); 
        multiple = false;
        enhancedMultiple = false;
        if ("Single".equals(newValue)){
            selectedEmployees.clear();

            // build the new selected list
            Employee employee;
            for(int i = 0, max = employees.size(); i < max; i++){
                employee = (Employee)employees.get(i);
                employee.setSelected(false);
            }
        } else if ("Multiple".equals(newValue)){
            multiple = true;
        } else if ("Enhanced Multiple".equals(newValue)){
            enhancedMultiple = true;
        }
    }
	
}
