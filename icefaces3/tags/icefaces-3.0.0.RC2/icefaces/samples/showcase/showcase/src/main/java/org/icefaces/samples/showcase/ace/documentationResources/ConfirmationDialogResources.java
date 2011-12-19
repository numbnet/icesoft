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
                    title="ace:confirmationDialog",
                    resource = ResourceRootPath.FOR_WIKI +"confirmationDialog"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:confirmationDialog",
                    resource = ResourceRootPath.FOR_ACE_TLD + "confirmationDialog.html")
        }
)
@ManagedBean(name= ConfirmationDialogResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationDialogResources extends ComponentExampleImpl< ConfirmationDialogResources > implements Serializable {
    public static final String BEAN_NAME = "confirmationDialogResources";
    public ConfirmationDialogResources()
    {
        super(ConfirmationDialogResources.class);
    }
}