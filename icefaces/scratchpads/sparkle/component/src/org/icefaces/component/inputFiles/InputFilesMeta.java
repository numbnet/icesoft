package org.icefaces.component.inputFiles;

import org.icefaces.component.annotation.Property;
import org.icefaces.component.annotation.Component;
import javax.el.MethodExpression;

@Component(
    tagName         = "inputFiles",
    componentClass  = "org.icefaces.component.inputFiles.InputFiles",
    rendererClass   = "org.icefaces.component.inputFiles.InputFilesRenderer",
    generatedClass  = "org.icefaces.component.inputFiles.InputFilesBase",
    extendsClass    = "javax.faces.component.UIComponentBase",
    componentType   = "org.icefaces.faces.InputFiles",
    rendererType    = "org.icefaces.faces.InputFilesRenderer",
    componentFamily = "org.icefaces.faces.InputFiles"
)
public class InputFilesMeta {
    @Property(defaultValue="false",
        tlddoc="Default is false, means uses full submit")
    private Boolean singleSubmit;

    @Property(isMethodExpression=true,
        methodExpressionArgument="org.icefaces.component.inputFiles.InputFilesEvent")
    private MethodExpression inputFilesListener;
}
