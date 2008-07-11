package com.icesoft.icefaces.samples.showcase.common;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.jboss.seam.annotations.Name;

@Entity
@Name("employee")
public class Employee implements Serializable {
	private Long id;
    private String deptName;
    private String subDeptName;
    private String name;
    
    //need empty constructor for Entity
    public Employee(){
    	
    }
    
    @Id @GeneratedValue
    public Long getId()
    {
       return id;
    }
    public void setId(Long id)
    {
       this.id = id;
    }
    
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getSubDeptName() {
        return subDeptName;
    }
    public void setSubDeptName(String subDeptName) {
        this.subDeptName = subDeptName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Employee(String deptName, String subDeptName, String name) {
        this.deptName = deptName;
        this.subDeptName = subDeptName;
        this.name = name;
    }
    
    
}
