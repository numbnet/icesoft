package org.icemobile.samples.mobileshowcase.view.examples.device.contacts;

import java.io.Serializable;

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
    
    private String contactOne;
    
    public static final String BEAN_NAME = "contactsBean";
    
    public ContactsBean() {
        super(ContactsBean.class);
    }

    public String getContactOne() {
        return contactOne;
    }

    public void setContactOne(String contactOne) {
        this.contactOne = contactOne;
    }

}
