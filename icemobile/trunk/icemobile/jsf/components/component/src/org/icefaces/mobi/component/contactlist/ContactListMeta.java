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
        tlddoc = "The fetchContact Tag invokes the containers native " +
        		"interface to fetch various fields from the contacts database "
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class ContactListMeta {
    
    @Property(tlddoc = "The text appearing in the button.", defaultValue="Fetch Contact")
    private String buttonLabel;
    
    @Property(defaultValue = "name", tlddoc = "Deterimes which contact fields are fetched.  Can contain one or more of 'name', 'email', and 'phone' fields submitted contained in a comma-separated string.")
    private String fields;
    
    @Property(defaultValue = "false", 
            tlddoc = org.icefaces.mobi.utils.TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLECLASS)
    private String styleClass;
    
    @Property(tlddoc = "The result.")
    private String value;
    
    @Property(tlddoc = "The name of the contact.")
    private String name;
    
    @Property(tlddoc = "The phone number of the contact.")
    private String phone;
    
    @Property(tlddoc = "The email of the contact.")
    private String email;
}
