
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
import java.util.*;

public class ImageSet implements Serializable
{
    public static ArrayList<ImageInfo> getImages(ImagesSelect imageSetType)
    {
        ArrayList<ImageInfo> imageLocations = new ArrayList<ImageInfo>();
        
        if(imageSetType.equals(ImagesSelect.CARS))
        {
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/bmw.png", "BMW"));
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/camaro.png", "Camaro"));
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/chevroletImpala.png", "Chevrolet Impala"));
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/pickupTruck.png", "Pickup Truck"));
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/vwBeatle.png", "VW Beatle"));
        }
        else if(imageSetType.equals(ImagesSelect.GADGETS))
        {
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/laptop.png", "Laptop"));
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/pda.png", "PDA"));
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/monitor.png", "Monitor"));
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/desktop.png", "Desktop"));
        }
        else if(imageSetType.equals(ImagesSelect.FOOD))
        {
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/aubergine.png", "Aubergine"));
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/capsicum.png", "Capsicum"));
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/chilli.png", "Chili"));
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/egg.png", "Egg"));
            imageLocations.add(new ImageInfo("/resources/css/images/dragdrop/orange.png", "Orange"));
        }
        else if(imageSetType.equals(ImagesSelect.ARROWS))
        {
            imageLocations.add(new ImageInfo("/resources/css/images/navigateForward.png", "Navigate Forward"));
            imageLocations.add(new ImageInfo("/resources/css/images/navigateBack.png", "Navigate Back"));
        }        
        return imageLocations;
    }
    
    public static ImageInfo getImage(ImageSelect imageType)
    {
        ImageInfo image = null;
        if(imageType.equals(ImageSelect.PRINTER))
        {
            image = new ImageInfo("/resources/css/images/printerIcon.png", "Printer");
        }
        else if(imageType.equals(ImageSelect.PICTURE))
        {
            image = new ImageInfo("/resources/css/images/rainbowCalgary.png", "Calgary");
        }
        return image;
    }
    
    public static ImageInfo getRandomImage()
    {
        //get all available image sets
        ImagesSelect[] availableSets = ImagesSelect.values();
        //pick one of available sets randomly
        ImagesSelect randomValue = availableSets[(int)(Math.random()*availableSets.length)];
        //get Images from that set
        ArrayList<ImageInfo> randomSet =  getImages(randomValue);
        //Randomly pick one of the images from that set 
        return randomSet.get((int)(Math.random()*randomSet.size()));
    }


    public enum ImagesSelect {
        CARS,
        GADGETS,
        FOOD,
        ARROWS
    }

    public enum ImageSelect {
        PRINTER,
        PICTURE
    }

    public static class ImageInfo implements Serializable {
        private String path;
        private String description;

        ImageInfo(String path, String description) {
            this.path = path;
            this.description = description;
        }

        public String getPath() {
            return path;
        }

        public String getDescription() {
            return description;
        }
    }
}
