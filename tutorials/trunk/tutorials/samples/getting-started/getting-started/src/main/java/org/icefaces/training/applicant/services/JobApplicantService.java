package org.icefaces.training.applicant.services;

import java.util.List;

import org.icefaces.training.applicant.view.model.JobApplicant;

public interface JobApplicantService {

	public void addApplicant(JobApplicant applicant);
	
	public List<JobApplicant> getApplicants();
}

