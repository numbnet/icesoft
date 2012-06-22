package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class CarouselBean {

    /**
     * This is a sample backing bean for the MVC supported
     * Carousel
     */

    private int carouselOneIndex;


    public CarouselBean() {


    }


    public int getCarouselOneIndex() {
        return carouselOneIndex;
    }

    public void setCarouselOneIndex(int carouselOneIndex) {
        this.carouselOneIndex = carouselOneIndex;
    }

    public Collection getImages() {
        ArrayList images = new ArrayList();
        images.add(new String("<img src=\"resources/desktop.png\" />"));
        images.add(new String("<img src=\"resources/monitor.png\" />"));
        images.add(new String("<img src=\"resources/laptop.png\" />"));
        images.add(new String("<img src=\"resources/pda.png\" />"));
        return images;
    }
}
