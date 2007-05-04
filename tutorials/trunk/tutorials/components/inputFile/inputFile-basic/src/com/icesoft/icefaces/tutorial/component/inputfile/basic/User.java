package com.icesoft.icefaces.tutorial.component.inputfile.basic;

import java.io.File;

/**
 * <p>
 * A basic backing bean for a ice:inputFile component.  This bean contains a
 * reference to the file that is uploaded by the inputFile component.
 * </p>
 */
public class User {

    File file;
    String fileLocation;

    public File getFile() {
        return file;
    }
    
    public String getFileLocation(){
        return fileLocation;
    }

    public void setFile(File file) {
        this.file = file;
        fileLocation = file.getPath();
    }
    

}
