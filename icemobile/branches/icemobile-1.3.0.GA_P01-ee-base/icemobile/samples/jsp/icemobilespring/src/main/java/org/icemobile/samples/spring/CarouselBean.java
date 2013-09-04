/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icemobile.samples.spring;

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
