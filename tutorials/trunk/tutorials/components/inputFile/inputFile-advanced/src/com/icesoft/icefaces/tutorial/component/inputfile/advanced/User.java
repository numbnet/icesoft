package com.icesoft.icefaces.tutorial.component.inputfile.advanced;

import javax.faces.event.*;

import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.component.inputfile.FileInfo;

public class User {

    private String componentStatus;
    private String fileLocation2;
    private String fileLocation3;

    public void uploadActionListener(ActionEvent actionEvent) {
        InputFile inputFile =(InputFile) actionEvent.getSource();
        FileInfo fileInfo = inputFile.getFileInfo();
        //file has been saved
        if (fileInfo.isSaved()) {
            componentStatus = "Custom saved message.";
        }
        //upload failed, generate custom messages
        if (fileInfo.isFailed()) {
            if(fileInfo.getStatus() == FileInfo.INVALID){
                componentStatus = "Custom invalid message.";
            }
            if(fileInfo.getStatus() == FileInfo.SIZE_LIMIT_EXCEEDED){
                componentStatus = "Custom size limit exceeded message.";
            }
            if(fileInfo.getStatus() == FileInfo.INVALID_CONTENT_TYPE){
                componentStatus = "Custom invalid content type message.";
            }
            if(fileInfo.getStatus() == FileInfo.INVALID_NAME_PATTERN){
                componentStatus = "Custom invalid name pattern message - we have set the fileNamePattern attribute to only accept .pdf files.";
            }
        }
    }

    public void checkFileLocation(ActionEvent event){
        InputFile inputFile =(InputFile) event.getSource();
        FileInfo fileInfo = inputFile.getFileInfo();
        //file has been saved
         if (fileInfo.isSaved()) {
             // Path with uniqueFolder attribute default
             if(inputFile.getId().endsWith("2")){
                 fileLocation2 = fileInfo.getPhysicalPath();
             }
             // Path with uniqueFolder attribute set to 'false'
             if(inputFile.getId().endsWith("3")){
                 fileLocation3 = fileInfo.getPhysicalPath();
             }
        }
    }

    public String getComponentStatus() {
        return componentStatus;
    }

    public String getFileLocation2() {
        return fileLocation2;
    }
    
    public String getFileLocation3() {
        return fileLocation3;
    }

}
