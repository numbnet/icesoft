package org.icefaces.tutorial.singlesubmit.beans;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name="personBean")
@ViewScoped
public class PersonBean implements Serializable {
	private String name;
	private int age;
	private String gender;
	private String favorite;
	
	public PersonBean() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getFavorite() {
		return favorite;
	}
	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}
	
	public void submitButton(ActionEvent event) {
		System.out.println("Submit Clicked: " + name + ", " + age + ", " + gender + ", " + favorite);
	}
}
