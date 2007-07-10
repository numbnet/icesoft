package com.icesoft.icefaces.tutorial.component.inputfile.advanced;

import java.io.File;

/**
 * <p>
 * A basic backing bean for a ice:inputFile component.  This bean contains a
 * reference to the file that is uploaded by the inputFile component.
 * </p>
 */

import javax.faces.event.*;

import com.icesoft.faces.component.inputfile.InputFile;

public class User {

    private String componentStatus;
    private String fileLocation2;
    private String fileLocation3;
    
    public String getComponentStatus() {
        return componentStatus;
    }

    public void setComponentStatus(String componentStatus) {
        this.componentStatus = componentStatus;
    }

    public String getFileLocation2() {
        return fileLocation2;
    }
    
    public String getFileLocation3() {
        return fileLocation3;
    }

    public void setFileLocation3(String fileLocation3) {
        this.fileLocation3 = fileLocation3;
    }

    public void setFileLocation2(String fileLocation2) {
        this.fileLocation2 = fileLocation2;
    }

    public void action(ActionEvent event){
        InputFile inputFile =(InputFile) event.getSource();
        //file has been saved
         if (inputFile.getStatus() == InputFile.SAVED) {
             componentStatus = "SAVED";
        }
         //invalid file, happens when clicking on upload without selecting a file, or a file with no contents.
         if (inputFile.getStatus() == InputFile.INVALID) {
             componentStatus = "INVALID";
         }
       //file size exceeded the limit
        if (inputFile.getStatus() == InputFile.SIZE_LIMIT_EXCEEDED) {
            componentStatus = "SIZE_LIMIT_EXCEEDED";
        }
       //indicate that the request size is not specified.
        if (inputFile.getStatus() == InputFile.UNKNOWN_SIZE) {
            componentStatus = "UNKNOWN_SIZE";
       }
    }
    
    public void checkFileLocation(ActionEvent event){
        InputFile inputFile =(InputFile) event.getSource();
        //file has been saved
         if (inputFile.getStatus() == InputFile.SAVED) {
             if(inputFile.getId().endsWith("2")){
                 fileLocation2 = inputFile.getFile().getPath();
             }
             if(inputFile.getId().endsWith("3")){
                 fileLocation3 = inputFile.getFile().getPath();
             }
        }
    }










    

}
