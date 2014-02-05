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

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is a sample backing bean for the MVC supported state
 * The properties should be the same
 */
@SessionAttributes("ListBean")
public class ListBean {

    @ModelAttribute("listBean")
    public ListBean createBean() {
        return new ListBean();
    }

    public Collection getCarCollection() {
        ArrayList cars = new ArrayList();
        cars.add(new Car("Porsche 924", 45000));
        cars.add(new Car("Audi A8", 90000));
        cars.add(new Car("BMW M3", 500000));
        cars.add(new Car("Bugatti Veyron", 2000000));


        return cars;
    }

    public class Car {
        private String title;
        private int cost;

        Car(String title, int cost) {
            this.title = title;
            this.cost = cost;
        }

        public String getTitle() {
            return title;
        }

        public int getCost() {
            return cost;
        }
    }

}
