package org.icefaces.inputFiles;

import org.icefaces.component.inputFiles.InputFilesEvent;

import javax.faces.event.ActionEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="fileBean")
@SessionScoped
public class FileBean {
    private String regularText = "";
    public String getRegularText() { return regularText; }
    public void setRegularText(String t) { regularText = t; }
    
    public String getRegularTextUppercase() { return regularText.toUpperCase(); }
    
    public void regular(ActionEvent e) {
        System.out.println("regular()  e: " + e);
    }
    
    private String mixedText = "";
    public String getMixedText() { return mixedText; }
    public void setMixedText(String t) { mixedText = t; }
    
    public String getMixedTextUppercase() { return mixedText.toUpperCase(); }
    
    public void mixed(ActionEvent e) {
        System.out.println("mixed()  e: " + e);
    }
    
    public void inputFilesListener(InputFilesEvent e) {
        System.out.println("inputFilesListener()  e: " + e + "  phase: " + e.getPhaseId());
    }
}
