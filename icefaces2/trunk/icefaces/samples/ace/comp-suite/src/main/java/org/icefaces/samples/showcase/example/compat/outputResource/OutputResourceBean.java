package org.icefaces.samples.showcase.example.compat.outputResource;

import java.io.Serializable;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.net.URL;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.FileResource;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.outputResource.title",
        description = "example.compat.outputResource.description",
        example = "/resources/examples/compat/outputResource/outputResource.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="outputResource.xhtml",
                    resource = "/resources/examples/compat/"+
                               "outputResource/outputResource.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="OutputResourceBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/outputResource/OutputResourceBean.java")
        }
)
@Menu(
	title = "menu.compat.outputResource.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.outputResource.subMenu.main",
                    isDefault = true,
                    exampleBeanName = OutputResourceBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.outputResource.subMenu.type",
                    exampleBeanName = OutputResourceType.BEAN_NAME),
            @MenuLink(title = "menu.compat.outputResource.subMenu.attachment",
                    exampleBeanName = OutputResourceAttachment.BEAN_NAME),
            @MenuLink(title = "menu.compat.outputResource.subMenu.filename",
                    exampleBeanName = OutputResourceFilename.BEAN_NAME),
            @MenuLink(title = "menu.compat.outputResource.subMenu.label",
                    exampleBeanName = OutputResourceLabel.BEAN_NAME)
})
@ManagedBean(name= OutputResourceBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class OutputResourceBean extends ComponentExampleImpl<OutputResourceBean> implements Serializable {
	
	public static final String BEAN_NAME = "outputResource";
	
	public static final String CUSTOM_NAME = "Custom-Name.pdf";
	public static final String PDF_NAME = "Training.pdf";
	public static final String IMAGE_NAME = "ICEfaces-2.gif";
	public static final Resource PDF_RESOURCE = loadResource(PDF_NAME);
	public static final Resource NAMED_RESOURCE = new NamedResource(CUSTOM_NAME, getFile(PDF_NAME));
	public static final Resource IMAGE_RESOURCE = loadResource(IMAGE_NAME);
	
	public OutputResourceBean() {
		super(OutputResourceBean.class);
	}
	
	public String getPdfName() { return PDF_NAME; }
	public String getImageName() { return IMAGE_NAME; }
	public Resource getPdfResource() { return PDF_RESOURCE; }
	public Resource getNamedResource() { return NAMED_RESOURCE; }
	public Resource getImageResource() { return IMAGE_RESOURCE; }
	
	private static File getFile(String name) {
	    URL loadUrl = OutputResourceBean.class.getResource(name);
	    
	    if (loadUrl != null) {
	        return new File(loadUrl.getFile());
	    }
	    
	    return null;
	}
	
	private static Resource loadResource(String name) {
	    File toLoad = getFile(name);
	    
	    if (toLoad != null) {
	        return new FileResource(toLoad);
	    }
	    
	    return null;
	}
}
	
class NamedResource implements Resource, Serializable {
    private String name;
    private File file;

    public NamedResource(String name, File file) {
        this.name = name;
        this.file = file;
    }
    
    public InputStream open() throws IOException {
        return new FileInputStream(file);
    }
    
    public String calculateDigest() {
        return name;
    }

    public Date lastModified() {
        return new Date(file.lastModified());
    }

    public void withOptions(Options arg0) throws IOException {
    }
}
