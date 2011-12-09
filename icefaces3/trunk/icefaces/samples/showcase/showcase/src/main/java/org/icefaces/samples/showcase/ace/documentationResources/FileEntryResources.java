package org.icefaces.samples.showcase.ace.documentationResources;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import org.icefaces.samples.showcase.metadata.context.ResourceRootPath;

@ExampleResources(
        resources ={
                
                // WIKI Resources
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:fileEntry",
                    resource = ResourceRootPath.FOR_WIKI + "FileEntry"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:fileEntry",
                    resource = ResourceRootPath.FOR_ACE_TLD + "fileEntry.html")
                
        }
)
@ManagedBean(name= FileEntryResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class FileEntryResources extends ComponentExampleImpl<FileEntryResources> implements Serializable {
    public static final String BEAN_NAME = "fileEntryResources";
    public FileEntryResources()
    {
        super(FileEntryResources.class);
    }
}
