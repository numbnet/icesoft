package org.icefaces.fileentry;

import org.icefaces.component.fileentry.FileEntryEvent;
import org.icefaces.component.fileentry.FileEntryInfo;
import org.icefaces.component.fileentry.FileEntry;

import javax.faces.event.ActionEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Iterator;

@ManagedBean(name="fileBean")
@SessionScoped
public class FileBean {
    
    private String locale;
    public String getLocale() {
        if (locale == null) {
            Locale lo = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
            if (lo == null) {
                lo = Locale.getDefault();
            }
            locale = lo.getLanguage(); 
        }
        return locale;
    }
    public void setLocale(String locale) { this.locale = locale; }
    
    private SelectItem[] locales;
    public SelectItem[] getLocales() {
        if (locales == null) {
            ArrayList<Locale> list = new ArrayList<Locale>();
            Iterator<Locale> it = FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
            while (it.hasNext()) {
                Locale locale = it.next();
                list.add(locale);
            }
            int sz = list.size();
            locales = new SelectItem[sz];
            for (int i = 0; i < sz; i++) {
                locales[i] = new SelectItem(list.get(i).getLanguage(), list.get(i).getLanguage());
            }
        }
        return locales;
    }
    
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
    
    public void fileEntryListener(FileEntryEvent e) {
        System.out.println("fileEntryListener()  e: " + e);
        System.out.println("fileEntryListener()  phase: " + e.getPhaseId());
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
        private FileEntryInfo info;
        
        private boolean infosEqual;
        private boolean saved;
        private String fileName;
        private String clientId;
        private String phase;
        
        Item(boolean immediate) {
            this.immediate = immediate;
        }
        
        public void listener(FileEntryEvent e) {
            System.out.println("Item.listener()  e: " + e);
            FileEntry fileEntry = (FileEntry) e.getComponent();
            FileEntryInfo info = fileEntry.getInfo();
            infosEqual = info.equals(this.info); // this.info can be null
            if (info.getFiles().size() > 0) {
                saved = info.getFiles().get(0).isSaved();
                fileName = info.getFiles().get(0).getFileName();
            }
            else {
                saved = false;
                fileName = null;
            }
            clientId = fileEntry.getClientId();
            phase = e.getPhaseId().toString();
        }
        
        public FileEntryInfo getInfo() { return info; } 
        public void setInfo(FileEntryInfo info) {
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
