/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */
package org.icefaces.samples.showcase.example.ace.printer;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;
import org.icefaces.samples.showcase.example.ace.accordionpanel.ImageSet;
import org.icefaces.samples.showcase.example.ace.dataTable.utilityClasses.VehicleGenerator;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;

@ComponentExample(
        title = "example.ace.printer.title",
        description = "example.ace.printer.description",
        example = "/resources/examples/ace/printer/printerOverview.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="printerOverview.xhtml",
                    resource = "/resources/examples/ace/printer/printerOverview.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PrinterBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/printer/PrinterBean.java")
        }
)
@Menu(
            title = "menu.ace.printer.subMenu.title",
            menuLinks = {
                @MenuLink(title = "menu.ace.printer.subMenu.main", isDefault = true, exampleBeanName = PrinterBean.BEAN_NAME)
            }
)
@ManagedBean(name= PrinterBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PrinterBean extends ComponentExampleImpl< PrinterBean > implements Serializable {
    public static final String BEAN_NAME = "printerBean";
    private String imageLocation;
    private List<Car> cars;
    private String printerIcon;
    
    
    public PrinterBean() 
    {
        super(PrinterBean.class);
        ImageSet set = new ImageSet();
        imageLocation = set.getImage(ImageSet.PICTURE_IMAGE);
        printerIcon = set.getImage(ImageSet.PRINTER_IMAGE);
        VehicleGenerator generator = new VehicleGenerator();
        cars = generator.getRandomCars(10);
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public String getPrinterIcon() {
        return printerIcon;
    }

    public void setPrinterIcon(String printerIcon) {
        this.printerIcon = printerIcon;
    }
}