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
        images.add(new String("<img src=\"resources/a.png\" />"));
        images.add(new String("<img src=\"resources/b.png\" />"));
        images.add(new String("<img src=\"resources/c.png\" />"));
        images.add(new String("<img src=\"resources/d.png\" />"));
        images.add(new String("<img src=\"resources/e.png\" />"));
        images.add(new String("<img src=\"resources/f.png\" />"));
        images.add(new String("<img src=\"resources/g.png\" />"));
        images.add(new String("<img src=\"resources/h.png\" />"));
        images.add(new String("<img src=\"resources/i.png\" />"));
        return images;
    }
}
