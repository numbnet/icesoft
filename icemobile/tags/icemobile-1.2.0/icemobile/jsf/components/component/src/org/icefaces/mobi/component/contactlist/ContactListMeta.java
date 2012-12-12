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
        tlddoc = "Renders a button that can access the device contact list and allow selection and retrieval " +
        		"of a contact. "
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class ContactListMeta {
    
    @Property(tlddoc = "The button label. ", defaultValue="Fetch Contact")
    private String buttonLabel;
    
    @Property(defaultValue = "name, email, phone", tlddoc = "Determines which contact fields are retrieved.  Can contain one or more of 'name', 'email', and 'phone' fields submitted contained in a comma-separated string. ")
    private String fields;
    
    @Property(defaultValue = "false", 
            tlddoc = org.icefaces.mobi.utils.TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLECLASS)
    private String styleClass;
    
    @Property(tlddoc = "The encoded result containing all fields.")
    private String value;
    
    @Property(tlddoc = "The name of the selected contact.")
    private String name;
    
    @Property(tlddoc = "The phone number of the selected contact.")
    private String phone;
    
    @Property(tlddoc = "The email of the selected contact.")
    private String email;
}
