package com.icesoft.icefaces.samples.showcase.components.table;

import static org.jboss.seam.ScopeType.PAGE;

import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import com.icesoft.icefaces.samples.showcase.common.Employee;

//not using this bean at all...just use the list that is generated
//in components.xml 
@Scope(PAGE)
@Name("groupBean")
public class GroupBean extends EntityQuery{
	private static final String EJBQL = "select e from Employee e";


    public GroupBean() {
        setEjbql(EJBQL); 
    }

}
