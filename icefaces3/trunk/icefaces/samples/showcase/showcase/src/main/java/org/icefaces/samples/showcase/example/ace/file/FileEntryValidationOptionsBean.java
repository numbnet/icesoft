/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.samples.showcase.example.ace.file;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.util.FacesUtils;

import org.icefaces.ace.component.fileentry.*;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.Scanner;

@ComponentExample(
        parent = FileEntryBean.BEAN_NAME,
        title = "menu.ace.fileentry.subMenu.validation",
        description = "example.ace.fileentry.valid.description",
        example = "/resources/examples/ace/fileentry/validation.xhtml"
)
@ExampleResources(
resources ={
    // xhtml
    @ExampleResource(type = ResourceType.xhtml,
            title="validation.xhtml",
            resource = "/resources/examples/ace/"+
                       "fileentry/validation.xhtml"),
    // Java Source
    @ExampleResource(type = ResourceType.java,
            title="FileEntryValidationOptionsBean.java",
            resource = "/WEB-INF/classes/org/icefaces/samples/"+
                       "showcase/example/ace/file/FileEntryValidationOptionsBean.java")
}
)
@ManagedBean(name= FileEntryValidationOptionsBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class FileEntryValidationOptionsBean extends ComponentExampleImpl<FileEntryValidationOptionsBean>
        implements Serializable {

    public static final String BEAN_NAME = "fileEntryValidation";

    private boolean maxFileSizeSet = false;
    private Integer maxFileSize = 0;

//    // Invalidate and delete any files not named test.txt
//    public void exampleCustomValidator(FileEntryEvent entryEvent) {
//        if (!useCustomValidator) return;
//        FileEntryResults results = ((FileEntry)entryEvent.getComponent()).getResults();
//        for (FileEntryResults.FileInfo file : results.getFiles()) {
//            if (file.getSize() < 1200) file.updateStatus(file.getStatus(), true, true);
//            FacesUtils.addErrorMessage("Example Validator: File deleted. Size less than 1200 hundred bytes.");
//        }
//    }

    public Integer getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public boolean isMaxFileSizeSet() {
        return maxFileSizeSet;
    }

    public void setMaxFileSizeSet(boolean maxFileSizeSet) {
        this.maxFileSizeSet = maxFileSizeSet;
    }

    public String getMaxFileSizeString() {
        return maxFileSize + " (bytes)";
    }

    public String getMaxFileSizeMessage() {
        return "File size can not exeed "+(maxFileSize/102400)*100+" KB";
    }

    public FileEntryValidationOptionsBean() {
        super(FileEntryValidationOptionsBean.class);
    }
}
