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
    private String contact;
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

    public void setRawContact(String rawContact) {
        this.rawContact = rawContact;
        //raw contact string will be in encoded format 
        //of [contact=val&][phone=val&][email=val&]
        if( rawContact != null && !"".equals(rawContact)){
            try {
              //contact string has to be decoded
                String decoded = URLDecoder.decode(rawContact,"UTF-8");
                String[] tokens = decoded.split("&");
                for( int i = 0 ; i < tokens.length ; i++ ){
                  //each contact field will have a key and value
                    String key = tokens[i].substring(0,tokens[i].indexOf("="));
                    String val = tokens[i].substring(tokens[i].indexOf("=")+1);
                  //possible keys are 'contact', 'name', 'phone', and 'email'
                    if( "contact".equals(key)){
                        contact = val;
                    }
                    else if( "name".equals(key)){
                        name = val;
                    }
                    else if( "phone".equals(key)){
                        phone = val;
                    }
                    else if( "email".equals(key)){
                        email = val;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
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

    public String getRawContact() {
        return rawContact;
    }

}
