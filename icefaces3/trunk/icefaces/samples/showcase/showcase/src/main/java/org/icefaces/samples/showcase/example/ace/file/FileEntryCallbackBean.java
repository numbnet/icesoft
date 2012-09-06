/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.samples.showcase.example.ace.file;

import org.icefaces.ace.component.fileentry.FileEntryStatus;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;

@ComponentExample(
        parent = FileEntryBean.BEAN_NAME,
        title = "example.ace.fileentry.callback.title",
        description = "example.ace.fileentry.callback.description",
        example = "/resources/examples/ace/fileentry/fileEntryCallback.xhtml"
)
@ExampleResources(
resources ={
    // xhtml
    @ExampleResource(type = ResourceType.xhtml,
            title="fileEntryCallback.xhtml",
            resource = "/resources/examples/ace/"+
                       "fileentry/fileEntryCallback.xhtml"),
    // Java Source
    @ExampleResource(type = ResourceType.java,
            title="FileEntryCallbackBean.java",
            resource = "/WEB-INF/classes/org/icefaces/samples/"+
                       "showcase/example/ace/file/FileEntryCallbackBean.java")
}
)
@ManagedBean(name= FileEntryCallbackBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class FileEntryCallbackBean extends ComponentExampleImpl<FileEntryCallbackBean> implements Serializable 
{
    public static final String BEAN_NAME = "fileEntryCallback";

    public FileEntryCallbackBean() {
        super(FileEntryCallbackBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    // Invalidate and delete any files not named test.txt
    public void customValidator(FileEntryEvent entryEvent) {
        FileEntryResults results = ((FileEntry)entryEvent.getComponent()).getResults();
        for (FileEntryResults.FileInfo file : results.getFiles()) {
            if (file.isSaved()) {
                if (!file.getContentType().equals("application/pdf")){
                    file.updateStatus(new FileEntryStatus() {
                            public boolean isSuccess() {
                                return false;
                            }
                            public FacesMessage getFacesMessage(
                                    FacesContext facesContext, UIComponent fileEntry, FileEntryResults.FileInfo fi) {
                                return new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Only PDF files can be uploaded. Your upload has been cancelled.",
                                    "Only PDF files can be uploaded. Your upload has been cancelled.");
                            }
                        },
                        true, true);
                }
            }
        }
    }
}