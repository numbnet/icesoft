package com.icesoft.icefaces.tutorial.component.inputfile.basic;

import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;

import javax.faces.event.ActionEvent;


/**
 * <p>
 * A basic backing bean for a ice:inputFile component.  This bean contains a
 * reference to FileInfo.
 * </p>
 */
public class User {

	private FileInfo currentFile;

	public FileInfo getCurrentFile() {
		return currentFile;
	}

	public void uploadActionListener(ActionEvent actionEvent) {
        InputFile inputFile = (InputFile) actionEvent.getSource();
        currentFile = inputFile.getFileInfo();
	}
    

}
