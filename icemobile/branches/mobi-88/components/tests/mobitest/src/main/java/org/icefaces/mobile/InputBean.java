package org.icefaces.mobile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name="input")
@ViewScoped
public class InputBean implements Serializable {
   private String placeholder="placeholder";
   private String patternTest;
   private String defaultInput;
   private String someText;
   private String someText2;
   private String searchPlaceholder="searchPlaceholder";
   private String telephone;
   private String url;
   private String email;
   private String phone;
   private double converterVal;
   private double doubleVal;
   private String selectedInp;
   private List nameList= new ArrayList();
   private String textField ="some kind of text field";
   private String name;
   private int sliderValue=25;  //initial value
   private int sliderVal2 = 15;
   private int sliderVal3 = 20;
   private int numberInput = 90;
   private int numberInput2 = 450;
   private String eventString="none";
   private String password;
   private String flipswitchVal = "Off";
   
   
   public InputBean(){
	   //System.out.println("INPUTBEAN CONSTRUCTOR version="+this);
   }
public int getNumberInput2() {
	return numberInput2;
}
public void setNumberInput2(int numberInput2) {
	this.numberInput2 = numberInput2;
}
public String getPlaceholder() {
	return placeholder;
}
public void setPlaceholder(String placeholder) {
	this.placeholder = placeholder;
}
public String getPatternTest() {
	return patternTest;
}
public void setPatternTest(String patternTest) {
	this.patternTest = patternTest;
}
public String getDefaultInput() {
	return defaultInput;
}
public void setDefaultInput(String defaultInput) {
	this.defaultInput = defaultInput;
}
public String getSearchPlaceholder() {
	return searchPlaceholder;
}
public void setSearchPlaceholder(String searchPlaceholder) {
	this.searchPlaceholder = searchPlaceholder;
}
public String getTelephone() {
	return telephone;
}
public void setTelephone(String telephone) {
	this.telephone = telephone;
}
public String getSelectedInp() {
	return selectedInp;
}
public void setSelectedInp(String selectedInp) {
	this.selectedInp = selectedInp;
}
public List getNameList() {
	return nameList;
}
public void setNameList(List nameList) {
	this.nameList = nameList;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getSliderValue() {
	return sliderValue;
}
public void setSliderValue(int sliderValue) {
	this.sliderValue = sliderValue;
}
public String getTextField() {
	return textField;
}
public void setTextField(String textField) {
	this.textField = textField;
}
public String getSomeText() {
	return someText;
}
public void setSomeText(String someText) {
	this.someText = someText;
}
public String getSomeText2() {
	return someText2;
}
public void setSomeText2(String someText2) {
	this.someText2 = someText2;
}
public int getSliderVal2() {
	return sliderVal2;
}
public void setSliderVal2(int sliderVal2) {
	this.sliderVal2 = sliderVal2;
}
public int getSliderVal3() {
	return sliderVal3;
}
public void setSliderVal3(int sliderVal3) {
	this.sliderVal3 = sliderVal3;
}

public void defaultEvent(ActionEvent ae){
	this.setEventString("DefaultEVent");
}
public void importantEvent(ActionEvent ae){
	this.setEventString("ImportantEvent");
}
public String getEventString() {
	return eventString;
}
public void setEventString(String eventString) {
	this.eventString = eventString;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getFlipswitchVal() {
	return flipswitchVal;
}
public void setFlipswitchVal(String flipswitchVal) {
	this.flipswitchVal = flipswitchVal;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public int getNumberInput() {
	return numberInput;
}
public void setNumberInput(int numberInput) {
	this.numberInput = numberInput;
}
public double getConverterVal() {
	return converterVal;
}
public void setConverterVal(double converterVal) {
	this.converterVal = converterVal;
}
public double getDoubleVal() {
	return doubleVal;
}
public void setDoubleVal(double doubleVal) {
	this.doubleVal = doubleVal;
}

   
}
