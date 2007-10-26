package com.icesoft.icefaces.samples.showcase.components.fileUpload;

import java.io.Serializable;
import java.io.File;
import java.util.Date;

/**
 * <p>The FileRegistry class is the utility bean for the inputfile showcase
 * demonstration. It is used to store file information in the files uploaded list.</p>
 *
 * @since 
 */
public class FileEntry implements Serializable {

	private static final long serialVersionUID = 7625899643164777545L;
	private String absolutePath;
	private String folder;
    private Boolean selected = Boolean.FALSE;
	private String fileName = "none";
	private String fileType = "none";

	private long dateModified;
	private long size;


    public FileEntry(File file, String fileType) {
    	fileName=file.getName();
    	absolutePath=file.getAbsolutePath();
    	folder = file.getParent();
    	dateModified = file.lastModified();
    	size=file.length();
    	this.fileType = fileType;
    }
    public FileEntry(File file){
       	fileName=file.getName();
    	absolutePath=file.getAbsolutePath();
    	folder = file.getParent();
    	dateModified = file.lastModified();
    	size=file.length();
    }
    public String getFileName() {
    	 if (!fileName.equals("none"))return fileName;
    	 else return "";
    }
    public String getFileType() {
        if (!fileType.equals("none"))return fileType;
        else return "";
    }
    public String getDateModified() {
    	Date date = new Date(dateModified);  	
        return date.toString();
    }
    public String getFolder(){ return this.folder;}
    public long getSize(){return size;}
    public String getAbsolutePath(){return absolutePath;}

    public Boolean getSelected() {
        return selected;
    }
 
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    
    public String toString(){
    	return "fNm="+fileName+" selected="+selected;
    }
}

