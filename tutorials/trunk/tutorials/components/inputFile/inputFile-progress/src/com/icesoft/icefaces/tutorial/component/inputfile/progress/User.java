package com.icesoft.icefaces.tutorial.component.inputfile.progress;

import java.util.EventObject;

import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.component.inputfile.FileInfo;

import javax.faces.event.ActionEvent;

/**
 * <p>
 * A basic backing bean for a ice:inputFile component.  This bean contains a
 * reference to the file that is uploaded by the inputFile component.
 * </p>
 */
public class User {

	private FileInfo currentFile;
	private int fileProgress;

	public FileInfo getCurrentFile() {
		return currentFile;
	}

    public int getFileProgress() {
        return fileProgress;
    }

	public void uploadActionListener(ActionEvent actionEvent) {
        InputFile inputFile = (InputFile) actionEvent.getSource();
        currentFile = inputFile.getFileInfo();
	}

    /**
     * <p>This method is bound to the inputFile component and is executed
     * multiple times during the file upload process.  Every call allows
     * the user to finds out what percentage of the file has been uploaded.
     * This progress information can then be used with a progressBar component
     * for user feedback on the file upload progress. </p>
     *
     * @param event holds a InputFile object in its source which can be probed
     *              for the file upload percentage complete.
     */

	public void progressListener(EventObject event) {
        InputFile ifile = (InputFile) event.getSource();
        fileProgress = ifile.getFileInfo().getPercent();
	}

}
