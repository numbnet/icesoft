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
    
    private String contact;
    private String name;
    private String phone;
    private String email;
    private String pattern;
    
    public static final String BEAN_NAME = "contactsBean";
    
    public ContactsBean() {
        super(ContactsBean.class);
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
        if( contact != null && !"".equals(contact)){
            String[] tokens = contact.split("%26");
            for( int i = 0 ; i < tokens.length ; i++ ){
                System.out.println("tokens="+tokens);
                String key = tokens[i].substring(0,tokens[i].indexOf("%3D"));
                String val = tokens[i].substring(tokens[i].indexOf("%3D")+3);
                System.out.println("key="+key+", val="+val);
                if( "contact".equals(key)){
                    name = val;
                }
                else if( "phone".equals(key)){
                    phone = val;
                }
                else if( "email".equals(key)){
                    email = val;
                }
                
            }
        }
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

}
