package org.icefaces.util.cssurlmapper.ant;

import java.io.*;
import java.util.*;
import org.icefaces.util.cssurlmapper.*;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class CssUrlMapperTask {

    private String libraryname = "";
	private String inputdir = "";
	private String outputdir = "";
	private String referencedir = "";

    public void setLibraryname(String libraryname) {
        this.libraryname = libraryname;
    }
	
    public void setInputdir(String inputdir) {
        this.inputdir = inputdir;
    }
	
    public void setOutputdir(String outputdir) {
        this.outputdir = outputdir;
    }
	
    public void setReferencedir(String referencedir) {
        this.referencedir = referencedir;
    }
	
    public void execute() throws BuildException {
		
		try {
			if (libraryname.equals("") || inputdir.equals("") || outputdir.equals("")) {
				throw new BuildException("cssurlmapper: libraryname, inputdir and outputdir are required.");
			}
			
			File inputdirFile = new File(inputdir);
			if (!inputdirFile.exists()) {
				System.out.println("cssurlmapper: WARNING: inputdir wasn't found when trying to process: [libraryname: " + libraryname + ", inputdir: " + inputdir + ", outputdir:" + outputdir + "]. Skipping.");
				return;
				//throw new BuildException("cssurlmapper: inputdir does not exist.");
			} else if (!inputdirFile.isDirectory()) {
				throw new BuildException("cssurlmapper: inputdir is not a real directory.");
			}
			
			File outputdirFile = new File(outputdir);
			outputdirFile.mkdirs();
			if (!outputdirFile.exists()) {
				throw new BuildException("cssurlmapper: could not create output directory.");
			} else if (!outputdirFile.isDirectory()) {
				throw new BuildException("cssurlmapper: outputdir is not a real directory.");
			}
			
			File referencedirFile = null;
			if (!referencedir.equals("")) {
				referencedirFile = new File(referencedir);
				if (!referencedirFile.exists()) {
					throw new BuildException("cssurlmapper: referencedir does not exist.");
				} else if (!inputdirFile.isDirectory()) {
					throw new BuildException("cssurlmapper: referencedir is not a real directory.");
				}
				String referencedirPath = referencedirFile.getCanonicalPath();
				String inputdirPath = inputdirFile.getCanonicalPath();
				if (!inputdirPath.startsWith(referencedirPath)) {
					throw new BuildException("cssurlmapper: referencedir must be an ancestor of inputdir.");
				}
			}
			
			if (referencedirFile != null) {
				Main.processDirectory(inputdirFile, libraryname, referencedirFile, outputdirFile);
			} else {
				Main.processDirectory(inputdirFile, libraryname, inputdirFile, outputdirFile);
			}
		} catch (IOException e) {
			throw new BuildException(e);
		}
    }
}