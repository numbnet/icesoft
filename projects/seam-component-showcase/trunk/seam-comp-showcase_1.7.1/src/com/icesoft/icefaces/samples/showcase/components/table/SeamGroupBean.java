package com.icesoft.icefaces.samples.showcase.components.table;

import static org.jboss.seam.ScopeType.PAGE;

import java.util.List;
import java.io.Serializable;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;


//not using this bean at all...just use the list that is generated
//in components.xml 
@Scope(PAGE)
@Name("groupBean")
public class SeamGroupBean implements Serializable{
	
	@In("#{employeesList.resultList}")
	private List employees;

	public List getEmployees(){
		return this.employees;
	}
	
	
}
