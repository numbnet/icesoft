package org.icefaces.samples.showcase.metadata.context;

import org.icefaces.samples.showcase.metadata.annotation.ResourceType;

/**
 *
 */
public class ExampleResource {

    public String title;
    public String resource;
    public ResourceType type;

    public ExampleResource(String title, String resource, ResourceType type) {
        this.title = title;
        this.resource = resource;
        this.type = type;
    }

    public String getTitle(){
        return title;
    }

    public String getResource(){
        return resource;
    }

    public ResourceType getType(){
        return type;
    }

}
