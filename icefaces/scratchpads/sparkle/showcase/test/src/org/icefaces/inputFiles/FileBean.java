package org.icefaces.inputFiles;

import org.icefaces.component.inputFiles.InputFilesEvent;
import org.icefaces.component.inputFiles.InputFilesInfo;
import org.icefaces.component.inputFiles.InputFiles;

import javax.faces.event.ActionEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

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
    
    private Boolean checkValue;
    public Boolean getCheckValue() { return checkValue; }
    public void setCheckValue(Boolean cv) { checkValue = cv; }
    
    private Date dateValue;
    public Date getDateValue() { return dateValue; }
    public void setDateValue(Date dv) { dateValue = dv; }
    
    public void mixed(ActionEvent e) {
        System.out.println("mixed()  e: " + e);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("actionListener"));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("actionListener  clientId: " + e.getComponent().getClientId()));
    }
    
    public void inputFilesListener(InputFilesEvent e) {
        System.out.println("inputFilesListener()  e: " + e);
        System.out.println("inputFilesListener()  phase: " + e.getPhaseId());
    }
    
    public List getTableList() {
        if (tableList == null) {
            int sz = 4;
            tableList = new ArrayList(sz);
            for (int i = 0; i < sz; i++) {
                tableList.add( new Item((i % 2) == 0) );
            }
        }
        return tableList;
    }
    private List tableList;

    public List getTableList2() {
        if (tableList2 == null) {
            int sz = 4;
            tableList2 = new ArrayList(sz);
            for (int i = 0; i < sz; i++) {
                tableList2.add( new Item((i % 2) == 0) );
            }
        }
        return tableList2;
    }
    private List tableList2;

    
    public static class Item {
        private boolean immediate;
        private InputFilesInfo info;
        
        private boolean infosEqual;
        private boolean saved;
        private String fileName;
        private String clientId;
        private String phase;
        
        Item(boolean immediate) {
            this.immediate = immediate;
        }
        
        public void listener(InputFilesEvent e) {
            System.out.println("Item.inputFilesListener()  e: " + e);
            InputFiles inputFiles = (InputFiles) e.getComponent();
            InputFilesInfo info = inputFiles.getInfo();
            infosEqual = info.equals(this.info); // this.info can be null
            if (info.getFiles().size() > 0) {
                saved = info.getFiles().get(0).isSaved();
                fileName = info.getFiles().get(0).getFileName();
            }
            else {
                saved = false;
                fileName = null;
            }
            clientId = inputFiles.getClientId();
            phase = e.getPhaseId().toString();
        }
        
        public InputFilesInfo getInfo() { return info; } 
        public void setInfo(InputFilesInfo info) {
            System.out.println("Item.setInfo()  info: " + info);
            this.info = info;
        }
        
        public boolean isImmediate() { return immediate; }
        public boolean isInfosEqual() { return infosEqual; }
        public boolean isSaved() { return saved; }
        public String getFileName() { return fileName; }
        public String getClientId() { return clientId; }
        public String getPhase() { return phase; }
    }
}
