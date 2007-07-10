package com.icesoft.icefaces.tutorial.component.inputfile.progress;

import java.io.File;
import java.util.EventObject;

import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;

/**
 * <p>
 * A basic backing bean for a ice:inputFile component.  This bean contains a
 * reference to the file that is uploaded by the inputFile component.
 * </p>
 */
public class User {

    private File file;
    private String fileLocation;
    private int percent;
    private PersistentFacesState state;

    public User(){
        state = PersistentFacesState.getInstance();
    }

    public File getFile() {
        return file;
    }

    public String getFileLocation(){
        return fileLocation;
    }

    public int getPercent() {
        return percent;
    }

    public void setFile(File file) {
        this.file = file;
        fileLocation = file.getPath();
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    // Required to bind the InputFile component to its OutputProgress component.
    public void progress (EventObject event) {
        InputFile inputFileComponent = (InputFile)event.getSource();
        percent = inputFileComponent.getFileInfo().getPercent();
        try {
            if (state != null) {
                state.render();
            }
        } catch (RenderingException ee) {
            System.out.println(ee.getMessage());
        }
    }

}
