package com.icesoft.icefaces.samples.showcase.components.table;

import com.icesoft.icefaces.samples.showcase.common.Employee;

public class GroupBean {

    private Employee[] employees;
    public GroupBean() {
        employees = new Employee[] {
                new Employee("IT", "Application", "Martin"),
                new Employee("IT", "Application", "Robert"),
                new Employee("IT", "Application", "Xinsho"),
                new Employee("IT", "Application", "Gary"),
                new Employee("IT", "Testing", "Ryan"),
                new Employee("IT", "Testing", "Faisal"),
                new Employee("IT", "Testing", "Dan"),
                new Employee("IT", "Development", "Susan"),
                new Employee("IT", "Development", "Allison"),
                new Employee("IT", "Development", "Bob"),                
                new Employee("Marketing", "Paper Media", "Ali"),
                new Employee("Marketing", "Paper Media", "Damin"),
                new Employee("Marketing", "Paper Media", "John"),
                new Employee("Marketing", "Electronic Media", "Tim"),
                new Employee("Sales", "Internet", "Ryan"),
                new Employee("Sales", "Internet", "Faisal"),
                new Employee("Sales", "Telephonic", "Dan")               
        };
    }
    public Employee[] getEmployees() {
        return employees;
    }
    public void setEmployees(Employee[] employees) {
        this.employees = employees;
    }
}
