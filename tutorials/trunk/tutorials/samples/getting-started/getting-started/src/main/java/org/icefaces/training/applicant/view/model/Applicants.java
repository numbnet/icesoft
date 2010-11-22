package org.icefaces.training.applicant.view.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

@ManagedBean
@ApplicationScoped
public class Applicants implements Serializable{
	
	private List<JobApplicant> applicantsList = new ArrayList<JobApplicant>();

	public void setApplicantsList(List<JobApplicant> applicantsList) {
		this.applicantsList = applicantsList;
	}

	public List<JobApplicant> getApplicantsList() {
		return applicantsList;
	}
	
	public void refresh(List<JobApplicant> refreshApplicants) {
		setApplicantsList(refreshApplicants);
	}

}
