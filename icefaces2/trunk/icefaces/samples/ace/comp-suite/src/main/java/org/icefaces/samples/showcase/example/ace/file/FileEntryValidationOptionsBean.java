/*
 * ICESOFT COMMERCIAL SOURCE CODE LICENSE V 1.1
 *
 * The contents of this file are subject to the ICEsoft Commercial Source
 * Code License Agreement V1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the
 * License at
 * http://www.icesoft.com/license/commercial-source-v1.1.html
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * Copyright 2009-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
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
    private String maxFileSizeMessage = "The uploaded file is too large.";
    private boolean required = false;
    private String requiredMessage = "";
    private boolean disabled = false;
    private boolean useCustomValidator = false;

    // Invalidate and delete any files not named test.txt
    public void exampleCustomValidator(FileEntryEvent entryEvent) {
        if (!useCustomValidator) return;
        FileEntryResults results = ((FileEntry)entryEvent.getComponent()).getResults();
        for (FileEntryResults.FileInfo file : results.getFiles()) {
            if (file.getSize() < 1200) file.updateStatus(file.getStatus(), true, true);
            FacesUtils.addErrorMessage("Example Validator: File deleted. Size less than 1200 hundred bytes.");
        }
    }

    public boolean isUseCustomValidator() {
        return useCustomValidator;
    }

    public void setUseCustomValidator(boolean useCustomValidator) {
        this.useCustomValidator = useCustomValidator;
    }

    public Integer getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getRequiredMessage() {
        return requiredMessage;
    }

    public void setRequiredMessage(String requiredMessage) {
        this.requiredMessage = requiredMessage;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
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

    public void setMaxFileSizeString(String maxFileSize) {
        Scanner s = new Scanner(maxFileSize);
        try {
            this.maxFileSize = s.nextInt();
        } catch (Exception e) {
            this.maxFileSize = 0;
            FacesUtils.addErrorMessage("Max file size could not be set to: " + maxFileSize);
        }
    }

    public String getMaxFileSizeMessage() {
        return maxFileSizeMessage;
    }

    public void setMaxFileSizeMessage(String maxFileSizeMessage) {
        this.maxFileSizeMessage = maxFileSizeMessage;
    }

    public FileEntryValidationOptionsBean() {
        super(FileEntryValidationOptionsBean.class);
    }
}
