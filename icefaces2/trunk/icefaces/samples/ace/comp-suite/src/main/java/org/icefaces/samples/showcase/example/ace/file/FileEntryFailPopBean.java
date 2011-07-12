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

import org.icefaces.ace.component.fileentry.*;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = FileEntryBean.BEAN_NAME,
        title = "example.ace.fileentry.failpop.title",
        description = "example.ace.fileentry.failpop.description",
        example = "/resources/examples/ace/fileentry/failpop.xhtml"
)
@ExampleResources(
resources ={
    // xhtml
    @ExampleResource(type = ResourceType.xhtml,
            title="failpop.xhtml",
            resource = "/resources/examples/ace/"+
                       "fileentry/failpop.xhtml"),
    // Java Source
    @ExampleResource(type = ResourceType.java,
            title="FileEntryFailPopBean.java",
            resource = "/WEB-INF/classes/org/icefaces/samples/"+
                       "showcase/example/ace/file/FileEntryFailPopBean.java")
}
)
@ManagedBean(name= FileEntryFailPopBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class FileEntryFailPopBean extends ComponentExampleImpl<FileEntryFailPopBean>
        implements Serializable {

    public static final String BEAN_NAME = "fileEntryFailPop";

    private boolean useListener;
    private String popupMessage;
    private String fileExtension;

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public boolean isUseListener() {
        return useListener;
    }

    public void setUseListener(boolean useListener) {
        this.useListener = useListener;
    }

    public String getPopupMessage() {
        return popupMessage;
    }

    public void setPopupMessage(String popupMessage) {
        this.popupMessage = popupMessage;
    }

    public void popupValidationListener(FileEntryEvent event) {
        if (!useListener) return;
            for (FileEntryResults.FileInfo file : ((FileEntry)event.getComponent()).getResults().getFiles()) {
                if (file.getFileName().endsWith(fileExtension)) {
                    file.updateStatus(file.getStatus(), true, true);
                    // throw a popup
                }
            }
        }


    public FileEntryFailPopBean() {
        super(FileEntryFailPopBean.class);
    }
}
