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

    private int carouselOne;


    public CarouselBean() {


    }


    public int getCarouselOne() {
        return carouselOne;
    }

    public void setCarouselOne(int carouselOne) {
        this.carouselOne = carouselOne;
    }

    public Collection getImages() {
        ArrayList images = new ArrayList();
        images.add(new String("<img src=\"resources/desktop.png\" />"));
        images.add(new String("<img src=\"resources/monitor.png\" />"));
        images.add(new String("<img src=\"resources/laptop.png\" />"));
        images.add(new String("<img src=\"resources/pda.png\" />"));
        images.add(new String("<img src=\"resources/desktop.png\" />"));
        images.add(new String("<img src=\"resources/monitor.png\" />"));
        images.add(new String("<img src=\"resources/laptop.png\" />"));
        images.add(new String("<img src=\"resources/pda.png\" />"));
        return images;
    }
}
