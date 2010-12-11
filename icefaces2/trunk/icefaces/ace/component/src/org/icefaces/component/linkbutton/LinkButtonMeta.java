package org.icefaces.component.linkbutton;

import org.icefaces.component.annotation.*;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UICommandMeta;

@Component(
        tagName ="linkButton",
        componentClass ="org.icefaces.component.linkbutton.LinkButton",
        rendererClass ="org.icefaces.component.linkbutton.LinkButtonRenderer",
        componentType = "org.icefaces.LinkButton",
        rendererType = "org.icefaces.LinkButtonRenderer",
        extendsClass = "javax.faces.component.UICommand",
        generatedClass = "org.icefaces.component.linkbutton.LinkButtonBase",
        componentFamily="com.icesoft.faces.LinkButton"
)

@ResourceDependencies({
        @ResourceDependency(name="yui/yui-min.js",library="yui/3_1_1"),
        @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
        @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
        @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
        @ResourceDependency(name="util.js",library="org.icefaces.component.util"),
        @ResourceDependency(name="component.js",library="org.icefaces.component.util"),
        @ResourceDependency(name="linkbutton.js",library="org.icefaces.component.linkbutton"),
		@ResourceDependency(name="linkbutton.css",library="org.icefaces.component.linkbutton")
})

public class LinkButtonMeta extends UICommandMeta {

    @Property(tlddoc = "href of link. If specified and actionListener is absent, linkButton works " +
                       "as normal anchor. If specified and actionListener is present, linkButton works " +
                       "as AJAX event source, but href may be opened in a new tab or window")
    private String href;

    @Property(tlddoc ="standard HTML href language attribute")
    private String hrefLang;

    @Property(defaultValue="false",
              tlddoc="When singleSubmit is true, triggering an action on this component " +
                     "will submit and execute this component only (equivalent to " +
                     "<f:ajax execute=\"@this\" render=\"@all\">). When singleSubmit is false, " +
                     "triggering an action on this component will submit and execute the full " +
                     "form that this component is contained within. The default value is false.")
    private boolean singleSubmit;

    @Property (defaultValue="false", tlddoc="If true, disables the YUI component on the page. Currently not supported.")
    private boolean disabled;

    @Property (tlddoc="This property defines the link text visible in the component", implementation=Implementation.GENERATE,
    defaultValue="Default Anchor Label")
    private Object value; 

    @Property (defaultValue="0", tlddoc="tabindex of the component")
    private int tabindex;

    @Property(tlddoc="The CSS style class of the component")
    private String styleClass;

    @Property(tlddoc="the inline style of the component")
    private String style;

    @Property(tlddoc="If the link is a traditional anchor, this is the traditional target attribute")
    private String target; 
}
