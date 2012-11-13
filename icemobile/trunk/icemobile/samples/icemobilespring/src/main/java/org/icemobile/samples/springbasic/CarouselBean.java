package org.icemobile.samples.springbasic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CarouselBean {

    /**
     * This is a sample backing bean for the MVC supported
     * Carousel
     */
    private int index;
    private List<String> images = new ArrayList<String>();
    
    public CarouselBean(){
        images.add("<img src='resources/flags/Argentina.png' style='width:98px'/>");
        images.add("<img src='resources/flags/Brazil.png' style='width:98px'/>");
        images.add("<img src='resources/flags/Canada.png' style='width:98px'/>");
        images.add("<img src='resources/flags/Denmark.png' style='width:98px'/>");
        images.add("<img src='resources/flags/France.png' style='width:98px'/>");
        images.add("<img src='resources/flags/Germany.png' style='width:98px'/>");
        images.add("<img src='resources/flags/Hong-Kong.png' style='width:98px'/>");
        images.add("<img src='resources/flags/Italy.png' style='width:98px'/>");
        images.add("<img src='resources/flags/Japan.png' style='width:98px'/>");
        images.add("<img src='resources/flags/New-Zealand.png' style='width:98px'/>");
        images.add("<img src='resources/flags/Romania.png' style='width:98px'/>");
        images.add("<img src='resources/flags/Spain.png' style='width:98px'/>");
        images.add("<img src='resources/flags/USA.png' style='width:98px'/>");
        images.add("<img src='resources/flags/Venezuela.png' style='width:98px'/>");
        index = 0;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<String> getImages() {
        return images;
    }
    
    public String getSelectedImage(){
        return images.get(index);
    }

}
