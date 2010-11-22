package org.icefaces.training.applicant.view.controller;


import java.util.Iterator;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.event.AjaxBehaviorEvent;

import org.icefaces.training.applicant.services.JobApplicantServiceImpl;
import org.icefaces.training.applicant.view.model.Applicants;
import org.icefaces.training.applicant.view.model.JobApplicant;
import org.icefaces.training.applicant.view.util.FacesUtils;
/**
 * Applicant Controller. 
 */
@ManagedBean(name="applicantController")
@ApplicationScoped
public class ApplicantController {
	
	@ManagedProperty(value="#{jobApplicantServiceImpl}")
	private JobApplicantServiceImpl jobApplicantService;


    @Override
    public String toString(){
        return "jobApplication " + super.toString();
    }
    
	public String addApplicant() {
		JobApplicant jobApplicant = (JobApplicant) FacesUtils.getManagedBean("jobApplicant");
        jobApplicantService.addApplicant(jobApplicant);
        Applicants applicants = (Applicants) FacesUtils.getManagedBean("applicants");
        applicants.refresh(jobApplicantService.getApplicants());
		return "applicants?faces-redirect=true";
	}
	
	public void clearForm(AjaxBehaviorEvent ae) {		
		// Clear bean values
		JobApplicant jobApplicant = (JobApplicant) FacesUtils.getManagedBean("jobApplicant");
        jobApplicant.clear();		
		
        // Clear component values
        // Retrieve a reference to the containing form
		UIComponent form = getContainingForm(ae.getComponent());
		// Clear all input components in the form
        clearEditableValueHolders(form);
	}
	
	public UIComponent getContainingForm(UIComponent component){		
		if(!(component.getParent() instanceof UIForm)){
			return getContainingForm(component.getParent());
		}else{
			return component.getParent();
		}		
	}
	
	public void clearEditableValueHolders(UIComponent form){		
		Iterator<UIComponent> iterator = form.getFacetsAndChildren();
		while(iterator.hasNext()){
			UIComponent component = iterator.next();
			if(component instanceof EditableValueHolder){
				((EditableValueHolder) component).resetValue();
			}
			clearEditableValueHolders(component);
		}
	}

	public JobApplicantServiceImpl getJobApplicantService() {
		return jobApplicantService;
	}

	public void setJobApplicantService(JobApplicantServiceImpl jobApplicantService) {
		this.jobApplicantService = jobApplicantService;
	}

}
