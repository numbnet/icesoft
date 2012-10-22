package org.icefaces.mobi.component.contactlist;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;

@Component(
        tagName = "fetchContact",
        componentClass = "org.icefaces.mobi.component.contactlist.ContactList",
        rendererClass = "org.icefaces.mobi.component.contactlist.ContactListRenderer",
        generatedClass = "org.icefaces.mobi.component.contactlist.ContactListBase",
        componentType = "org.icefaces.ContactList",
        rendererType = "org.icefaces.ContactListRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.ContactList",
        tlddoc = "The Contact List Tag will invoke the containers native " +
        		"interface to fetch various fields from the contacts database "
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class ContactListMeta {
    
    @Property(tlddoc = "The text appearing in the button")
    private String label;

    @Property(tlddoc = "A complete pattern for matching users. Unfortunately, no wildcards")
    private String pattern;
    
    @Property(tlddoc = "If true, list allows for multiple user selections")
    private boolean multipleSelect;
    
    @Property(tlddoc = "Can contain one or more of contact, email, and phone fields for " +
    		"retrieval in a csv list. Default is contact information")
    private String fields;
    
    @Property(defaultValue = "false", 
            tlddoc = "When disabled, geolocation is not activated")
    private boolean disabled;

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;



}
