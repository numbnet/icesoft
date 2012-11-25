package org.icemobile.samples.mobileshowcase.view.examples.device.contacts;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.icemobile.samples.mobileshowcase.view.metadata.annotation.Destination;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.Example;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.ExampleResource;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.ExampleResources;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.ResourceType;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

@Destination(
        title = "example.device.contacts.destination.title.short",
        titleExt = "example.device.contacts.destination.title.long",
        titleBack = "example.device.contacts.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/device/contactlist-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/device/contactlist-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "contactlist-example.xhtml",
                        resource = "/WEB-INF/includes/examples/device/contactlist-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "CameraBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/device/contacts/ContactsBean.java")
        }
)
@ManagedBean(name = ContactsBean.BEAN_NAME)
@SessionScoped
public class ContactsBean extends ExampleImpl<ContactsBean> implements Serializable{
    
    private String rawContact;
    private String name;
    private String phone;
    private String email;
    private String pattern;
    
    public static final String BEAN_NAME = "contactsBean";
    
    public ContactsBean() {
        super(ContactsBean.class);
    }

    public void setRawContact(String rawContact) {
        this.rawContact = rawContact;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getRawContact() {
        return rawContact;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
