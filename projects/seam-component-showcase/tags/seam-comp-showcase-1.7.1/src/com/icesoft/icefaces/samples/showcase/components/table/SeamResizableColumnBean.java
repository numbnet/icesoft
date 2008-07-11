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
@Name("resizeableColumnBean")
public class SeamResizableColumnBean extends EntityQuery{
	private static final String EJBQL = "select e from Employee e";


	@Override
	public Integer getMaxResults() {
		return 25;
	}
	
	@Override
	public String getEjbql(){
		return EJBQL;
	}

	public List getEmployees(){
		return this.getResultList();
	}
	
	
}
