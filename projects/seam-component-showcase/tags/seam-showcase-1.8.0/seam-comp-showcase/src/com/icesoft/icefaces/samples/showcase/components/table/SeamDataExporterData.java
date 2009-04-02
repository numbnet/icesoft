package com.icesoft.icefaces.samples.showcase.components.table;

import static org.jboss.seam.ScopeType.PAGE;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


@Scope(PAGE)
@Name("dataExportData")
public class SeamDataExporterData {

	@In("#{employeesList.resultList}")
	private ArrayList employees;
	@Logger
	Log log;
	
	public List getEmployees(){
		return this.employees;
	}
	
}
