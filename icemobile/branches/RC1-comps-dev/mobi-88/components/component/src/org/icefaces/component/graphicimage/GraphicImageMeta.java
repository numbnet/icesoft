/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */
package org.icefaces.component.graphicimage;


import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;


@Component(
        tagName = "graphicImage",
        componentClass = "org.icefaces.component.graphicimage.GraphicImage",
        rendererClass = "org.icefaces.component.graphicimage.GraphicImageRenderer",
        generatedClass = "org.icefaces.component.graphicimage.GraphicImageBase",
        componentType = "org.icefaces.GraphicImage",
        rendererType = "org.icefaces.GraphicImageRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.GraphicImage",
        tlddoc = "This mobility component creates graphicImage markup while supporting " +
                "the byte[] format for image from database " +
                "based on the compat graphicImage component"
)


public class GraphicImageMeta {
    @Property(tlddoc = "alternate to display if image is unavailable")
    private String alt;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "width of small mobile image")
    private int width;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "height of gallery small mobil image is default")
    private int height;

    @Property(tlddoc = "src is supported similar to compat graphicImage")
    private String src;

    @Property(tlddoc = "value can be byte[] or String filename or Resource")
    private Object value;

    @Property(tlddoc = "url is supported in compat and h graphicImage")
    private String url;

    @Property(tlddoc = "mimetype of graphic")
    private String mimeType;

    @Property(defaultValue = "scope", tlddoc = "scope of object to place resource into")
    private String scope;

    @Property(tlddoc = "resource name for this component")
    private String name;

    @Property(tlddoc = "ismap of html markup-specifies image as server-side image map")
    private String ismap;

    @Property(tlddoc = "URI to a long description of the image")
    private String longdesc;

    @Property(tlddoc = "usemap of html markup, specifies image as client-side image map")
    private String usemap;

    @Property(tlddoc = "style as per basic component standards for overwriting style")
    private String styleClass;

    @Property(tlddoc = "direction indication for text that does not inherit directionality. Valid values are LTR " +
            "or RTL  left to right or right to left")
    private String dir;

    @Property(tlddoc = "title as per UIGraphic")
    private String title;

}


