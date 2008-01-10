package com.icesoft.icefaces.samples.showcase.common;

public class Employee {
    private String deptName;
    private String subDeptName;
    private String name;
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
