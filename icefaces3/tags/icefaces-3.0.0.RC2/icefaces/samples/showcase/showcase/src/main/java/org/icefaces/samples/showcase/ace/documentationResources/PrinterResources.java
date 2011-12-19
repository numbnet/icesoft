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
                    title="ace:printer",
                    resource = ResourceRootPath.FOR_WIKI + "Printer"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:printer",
                    resource = ResourceRootPath.FOR_ACE_TLD + "printer.html")
                
        }
)
@ManagedBean(name= PrinterResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PrinterResources extends ComponentExampleImpl<PrinterResources> implements Serializable {
    public static final String BEAN_NAME = "printerResources";
    public PrinterResources()
    {
        super(PrinterResources.class);
    }
}
