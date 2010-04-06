package com.icesoft.icefaces.samples.showcase.common;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.jboss.seam.annotations.Name;

@Entity
@Name("employee")
@Table(name = "Employee")
public class Employee implements Serializable {
    private Long id;
	private String firstName;
	private String lastName;
	private String phone;	
    private String departmentName;
    private String subDepartmentName;
    private transient boolean selected = false;
    protected transient boolean edit=false;

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
  
    public String getFirstName(){
    	return this.firstName;
    }
    public String getLastName(){
    	return this.lastName;
    }
    public String getPhone(){
    	return this.phone;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public void setFirstName(String firstName){
    	this.firstName = firstName;
    }
    public void setLastName(String lastName){
    	this.lastName = lastName;
    }
    public void setPhone(String phone){
    	this.phone = phone;
    }
    public void setDepartmentName(String deptName) {
        this.departmentName = deptName;
    }
    public String getSubDepartmentName() {
        return subDepartmentName;
    }
    public void setSubDepartmentName(String subDeptName) {
        this.subDepartmentName = subDeptName;
    }

    public Employee(String departmentName, String subDepartmentName,
            String firstName, String lastName, String phone ) {
		this.departmentName = departmentName;
		this.subDepartmentName = subDepartmentName;
		this.lastName = lastName;
		this.firstName = firstName;
		this.phone = phone;
	}    
    
    public void setSelected(boolean selected){
    	this.selected = selected;
    }
    @Transient
    public boolean isSelected(){
    	return this.selected;
    }
    @Transient
    public boolean isEdit() {
		return edit;
	}
	public void setEdit(boolean edit) {
		this.edit = edit;
	}
}
