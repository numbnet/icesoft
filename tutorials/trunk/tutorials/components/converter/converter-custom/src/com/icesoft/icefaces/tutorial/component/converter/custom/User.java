package com.icesoft.icefaces.tutorial.component.converter.custom;

import java.util.Date;

/*
 * Basic Backing bean for the ICEfaces converter example
 */
public class User {
    
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private int age;
    private Date birthDate;
    private float salary;
    private PhoneNumber phone;
    
    /** Creates a new instance of User */
    public User() {
    }
    
    public User(String firstName, String lastName, String address, 
            String city, int age, Date birthDate, float salary, PhoneNumber phone){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.age = age;
        this.birthDate = birthDate;
        this.salary = salary;
        this.phone = phone;
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    public String getAddress(){
        return address;
    }
    
    public void setAddress(String address){
        this.address = address;
    }
    
    public String getCity(){
        return city;
    }
    
    public void setCity(String city){
        this.city = city;
    }
    
    public int getAge(){
        return age;
    }
    
    public void setAge(int age){
        this.age = age;
    }
    
    public Date getBirthDate(){
        return birthDate;
    }
    
    public void setBirthDate(Date birthDate){
        this.birthDate = birthDate;
    }
    
    public float getSalary(){
        return salary;
    }
    
    public void setSalary(float salary){
        this.salary = salary;
    }
    
    public PhoneNumber getPhone(){
        return phone;
    }
    
    public void setPhone(PhoneNumber phone){
        this.phone = phone;
    }
    
    public String toString(){
        return "First Name: " + firstName + "/n" + "Last Name: " + lastName +
                "/n" + "Address: " + address + "/n" + "City: " + city + "/n"
                + "Age: " + age + "/n" + "Birth Date: " + birthDate.toString();
                
    }
    
}
