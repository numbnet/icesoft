package org.icefaces.samples.showcase.metadata.context;

import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.util.FacesUtils;

import com.icesoft.faces.context.effects.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
 *
 */
public class ComponentExampleImpl<T> implements ComponentExample, ExampleResources, ContextBase {
    private Class<T> parentClass;

    private String id;
    private String parent;
    private String title;
    private String description;
    private String example;

    private ArrayList<ExampleResource> exampleResource;
    private ArrayList<ExampleResource> javaResources;
    private ArrayList<ExampleResource> xhtmlResources;
    private ArrayList<ExampleResource> tldResources;
    private ArrayList<ExampleResource> externalResources;

    private String subMenuTitle;
    private ArrayList<MenuLink> subMenuLinks;
    
    private Effect effect;

    public ComponentExampleImpl(Class<T> parentClass) {
        this.parentClass = parentClass;
        exampleResource = new ArrayList<ExampleResource>();
        javaResources = new ArrayList<ExampleResource>();
        xhtmlResources = new ArrayList<ExampleResource>();
        tldResources = new ArrayList<ExampleResource>();
        externalResources = new ArrayList<ExampleResource>();
        subMenuLinks = new ArrayList<MenuLink>();
    }

    @PostConstruct
    public void initMetaData() {
        // copy data over for the example properties
        if (parentClass.isAnnotationPresent(
                org.icefaces.samples.showcase.metadata.annotation.ComponentExample.class)) {
            org.icefaces.samples.showcase.metadata.annotation.ComponentExample componentExample =
                    parentClass.getAnnotation(org.icefaces.samples.showcase.metadata.annotation.ComponentExample.class);
            parent = componentExample.parent();
            title = componentExample.title();
            description = componentExample.description();
            example = componentExample.example();
        }
        // build up the separate lists of ExampleResources assigned to this class.
        if (parentClass.isAnnotationPresent(
                org.icefaces.samples.showcase.metadata.annotation.ComponentExample.class)) {
            org.icefaces.samples.showcase.metadata.annotation.ExampleResources exampleResources =
                    parentClass.getAnnotation(org.icefaces.samples.showcase.metadata.annotation.ExampleResources.class);
            org.icefaces.samples.showcase.metadata.annotation.ExampleResource[] resources =
                    exampleResources.resources();
            ExampleResource tmpResource;
            for (org.icefaces.samples.showcase.metadata.annotation.ExampleResource resource :
                    resources){
                tmpResource = new ExampleResource(resource.title(), resource.resource(), resource.type());
                exampleResource.add(tmpResource);
                if (resource.type().equals(ResourceType.href)){
                    externalResources.add(tmpResource);
                }else if (resource.type().equals(ResourceType.java)){
                    javaResources.add(tmpResource);
                }else if (resource.type().equals(ResourceType.tld)){
                    tldResources.add(tmpResource);
                }else if (resource.type().equals(ResourceType.xhtml)){
                    xhtmlResources.add(tmpResource);
                }
            }
        }
        if (parentClass.isAnnotationPresent(
                org.icefaces.samples.showcase.metadata.annotation.Menu.class)) {
            org.icefaces.samples.showcase.metadata.annotation.Menu menu =
                    parentClass.getAnnotation(org.icefaces.samples.showcase.metadata.annotation.Menu.class);
            subMenuTitle = menu.title();
            org.icefaces.samples.showcase.metadata.annotation. MenuLink[] menuLinks =  menu.menuLinks();
            MenuLink menuLink;
            for (org.icefaces.samples.showcase.metadata.annotation.MenuLink link : menuLinks ){
                menuLink = new MenuLink(link.title(), link.isDefault(),
                        link.isNew(), link.isDisabled(), link.exampleBeanName());
                subMenuLinks.add(menuLink);
            }
        }
        else {
            // If we don't have a submenu link annotation then check if we have a parent
            // If we have a parent then try to use their submenu links
            // This would be for something like BorderLayout associating itself with the parent of BorderBean
            if ((parent != null) && (!"".equals(parent))) {
                subMenuLinks = ((ComponentExampleImpl)FacesUtils.getManagedBean(parent)).getSubMenuLinks();
            }
        }
        
        // Create the effect used when this item is expanded
        effect = new SlideDown();
        effect.setDuration(0.8f);
        effect.setFired(true);
    }

    public String getId() {
        return id;
    }
    
    public String getParent() {
        return parent;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getExample() {
        return example;
    }

    public ArrayList<ExampleResource> getJavaResources() {
        return javaResources;
    }

    public ArrayList<ExampleResource> getXhtmlResources() {
        return xhtmlResources;
    }

    public ArrayList<ExampleResource> getTldResources() {
        return tldResources;
    }

    public ArrayList<ExampleResource> getExternalResources() {
        return externalResources;
    }

    public ArrayList<ExampleResource> getExampleResource() {
        return exampleResource;
    }

    public ArrayList<MenuLink> getSubMenuLinks() {
        return subMenuLinks;
    }

    public String getSubMenuTitle() {
        return subMenuTitle;
    }
    
    public Effect getEffect() {
        return effect;
    }
    
    public void setEffect(Effect effect) {
        this.effect = effect;
    }
    
    public void prepareEffect() {
        if (effect != null) {
            effect.setFired(false);
        }
    }
}
