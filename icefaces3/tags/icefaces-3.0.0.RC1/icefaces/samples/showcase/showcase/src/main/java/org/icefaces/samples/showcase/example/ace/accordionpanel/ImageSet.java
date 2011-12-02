
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

package org.icefaces.samples.showcase.example.ace.accordionpanel;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageSet implements Serializable
{
    public static final String CARS = "carSet";
    public static final String GADGETS = "electronicsSet";
    public static final String FOOD = "groceriesSet";
    public static final String ARROWS = "navigationArrows";
    public static final String PRINTER_IMAGE = "printer";
    public static final String PICTURE_IMAGE = "pictureOfTheDay";

    
    public ArrayList<String> getImages(String imageSetType) 
    {
        ArrayList<String> imageLocations = new ArrayList<String>();
        
        if(imageSetType.equals(CARS))
        {
            imageLocations.add("/resources/css/images/dragdrop/bmw.png");
            imageLocations.add("/resources/css/images/dragdrop/camaro.png");
            imageLocations.add("/resources/css/images/dragdrop/chevroletImpala.png");
            imageLocations.add("/resources/css/images/dragdrop/pickupTruck.png");
            imageLocations.add("/resources/css/images/dragdrop/vwBeatle.png");
        }
        else if(imageSetType.equals(GADGETS))
        {
            imageLocations.add("/resources/css/images/dragdrop/laptop.png");
            imageLocations.add("/resources/css/images/dragdrop/pda.png");
            imageLocations.add("/resources/css/images/dragdrop/monitor.png");
            imageLocations.add("/resources/css/images/dragdrop/desktop.png");
        }
        else if(imageSetType.equals(FOOD))
        {
            imageLocations.add("/resources/css/images/dragdrop/aubergine.png");
            imageLocations.add("/resources/css/images/dragdrop/capsicum.png");
            imageLocations.add("/resources/css/images/dragdrop/chilli.png");
            imageLocations.add("/resources/css/images/dragdrop/egg.png");
            imageLocations.add("/resources/css/images/dragdrop/orange.png");
        }
        else if(imageSetType.equals(ARROWS))
        {
            imageLocations.add("/resources/css/images/navigateNext.png");
            imageLocations.add("/resources/css/images/navigateBack.png");
        }        
        return imageLocations;
    }
    
    public String getImage(String imageType)
    {
        String imageLocation = "";
        if(imageType.equals(PRINTER_IMAGE))
        {
            imageLocation = "/resources/css/images/printerIcon.png";
        }
        else if(imageType.equals(PICTURE_IMAGE))
        {
            imageLocation = "/resources/css/images/rainbowCalgary.png";
        }
        return imageLocation;
    }
}
