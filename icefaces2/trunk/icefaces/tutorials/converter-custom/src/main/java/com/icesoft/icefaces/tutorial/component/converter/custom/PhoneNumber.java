package com.icesoft.icefaces.tutorial.component.converter.custom;

/**
 * Backing bean for the converter example PhoneNumber object.
 */
public class PhoneNumber {
    
    private String countryCode;
    private String areaCode;
    private String prefix;
    private String number;
    
    public String getCountryCode(){
        return countryCode;
    }
    
    public void setCountryCode(String countryCode){
        this.countryCode = countryCode;
    }
    
    public String getAreaCode(){
        return areaCode;
    }
    
    public void setAreaCode(String areaCode){
        this.areaCode = areaCode;
    }
    
    public String getPrefix(){
        return prefix;
    }
    
    public void setPrefix(String prefix){
        this.prefix = prefix;
    }
    
    public String getNumber(){
        return number;
    }
    
    public void setNumber(String number){
        this.number = number;
    }
    
    public String toString(){
        if(countryCode.equals("1")){
            return countryCode + "-" + areaCode + "-" + prefix + "-" + number;
        }
        else{
            return number;
        }
    }
    
    
}
