/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.fileentry;

import org.icefaces.ace.component.fileentry.*;

import javax.faces.event.ActionEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Iterator;

@ManagedBean(name="fileBean")
@SessionScoped
public class FileBean implements Serializable {
    
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
    
    public void fileEntryListenerInvalidateDelete(FileEntryEvent e) {
        System.out.println("fileEntryListenerInvalidateDelete()  e: " + e);
        System.out.println("fileEntryListenerInvalidateDelete()  phase: " + e.getPhaseId());
        System.out.println("fileEntryListenerInvalidateDelete()  invalidate: " + invalidate);
        if (invalidate) {
            FileEntry fileEntry = (FileEntry) e.getComponent();
            FileEntryResults results = fileEntry.getResults();
            for(FileEntryResults.FileInfo fi : results.getFiles()) {
                fi.updateStatus(FileEntryStatuses.INVALID, true, true);
            }
        }
    }

    public FileEntryCallback getCallback() {
        System.out.println("getCallback()");
        return new ByteArrayCallback();
    }

    public FileEntryCallback getCallbackBeginRE() {
        return new RuntimeExceptionCallback(
                new RuntimeException("RuntimeException for callback"),
                true, false, false);
    }

    public FileEntryCallback getCallbackWriteRE() {
        return new RuntimeExceptionCallback(
                new RuntimeException("RuntimeException for callback"),
                false, true, false);
    }

    public FileEntryCallback getCallbackEndRE() {
        return new RuntimeExceptionCallback(
                new RuntimeException("RuntimeException for callback"),
                false, false, true);
    }

    private boolean immediate;
    public boolean isImmediate() { return immediate; }
    public void setImmediate(boolean imm) { immediate = imm; }

    private boolean invalidate;
    public boolean isInvalidate() { return invalidate; }
    public void setInvalidate(boolean inv) { invalidate = inv; }

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

    
    public static class Item implements Serializable {
        private boolean immediate;
        private FileEntryResults results;
        
        private boolean resultsEqual;
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
            FileEntryResults results = fileEntry.getResults();
            resultsEqual = results.equals(this.results); // this.results can be null
            if (results.getFiles().size() > 0) {
                saved = results.getFiles().get(0).isSaved();
                fileName = results.getFiles().get(0).getFileName();
            }
            else {
                saved = false;
                fileName = null;
            }
            clientId = fileEntry.getClientId();
            phase = e.getPhaseId().toString();
        }
        
        public FileEntryResults getResults() { return results; }
        public void setResults(FileEntryResults results) {
            System.out.println("Item.setResults()  results: " + results);
            this.results = results;
        }
        
        public boolean isImmediate() { return immediate; }
        public boolean isResultsEqual() { return resultsEqual; }
        public boolean isSaved() { return saved; }
        public String getFileName() { return fileName; }
        public String getClientId() { return clientId; }
        public String getPhase() { return phase; }
    }
}
