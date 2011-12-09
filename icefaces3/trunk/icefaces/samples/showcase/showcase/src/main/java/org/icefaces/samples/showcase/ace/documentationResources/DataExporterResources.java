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
                    title="ace:dataExporter",
                    resource = ResourceRootPath.FOR_WIKI + "DataExporter"),
                
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:excludeFromExport",
                    resource = ResourceRootPath.FOR_WIKI + "ExcludeFromExport"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:dataExporter",
                    resource = ResourceRootPath.FOR_ACE_TLD + "dataExporter.html"),
                @ExampleResource(type = ResourceType.tld,
                    title="ace:excludeFromExport",
                    resource = ResourceRootPath.FOR_ACE_TLD + "excludeFromExport.html")
                
        }
)
@ManagedBean(name= DataExporterResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataExporterResources extends ComponentExampleImpl<DataExporterResources> implements Serializable {
    public static final String BEAN_NAME = "dataExporterResources";
    public DataExporterResources()
    {
        super(DataExporterResources.class);
    }
}