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

package org.icefaces.mobile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="list")
@ViewScoped
public class ListBean implements Serializable {
	private static final Logger logger =
    Logger.getLogger(ListBean.class.toString());
    private List<String> simpleList = new ArrayList<String>() ;
    private List<Car> carList = new ArrayList<Car>();
    private List<LinkItem> linksList = new ArrayList<LinkItem>();

	private String height="12px";
	private String style="display:inline-block;position:relative;top:-25px;left:0;color:white;";


	public ListBean(){
        simpleList.add("Item A.");
        simpleList.add("Item B.");
        simpleList.add("Item C.");
        carList.add(new Car("Ford","Focus","2009","$5,000" ));
        carList.add(new Car("Chrysler","Caravan","2012","$15,000" ));
        carList.add(new Car("Volkswagen", "Jetta", "2008", "$8,000"));
        linksList.add(new LinkItem("carousel","carousel.jsf", "carousel"));
        linksList.add(new LinkItem("fieldSet", "fieldset.jsf","fieldset"));
        linksList.add(new LinkItem("orig outputList", "lists.jsf", "lists"));
        linksList.add(new LinkItem("layout menu", "../layoutComponents.html", "../layoutComponents"));
	}


	public List<Car> getCarList() {
		return carList;
	}

    public List<String> getSimpleList() {
        return simpleList;
    }

    public void setSimpleList(List<String> simpleList) {
        this.simpleList = simpleList;
    }

    public List<LinkItem> getLinksList() {
        return linksList;
    }

    public void setLinksList(List<LinkItem> linksList) {
        this.linksList = linksList;
    }

    public String viewDetail(){
        logger.info("IN METHOD DETAIL") ;
        return "result";
    }
	public void setCarList(List<Car> carList) {
		this.carList = carList;
	}

	public class Car implements Serializable{
    	private String make;
    	private String model;
    	private String year;
    	private String price;
    	
    	public Car (String make, String model, String year, String price){
    		this.make = make;
    		this.model = model;
    		this.year = year;
    		this.price = price;
    	}

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public class LinkItem implements Serializable{

        private String title;
        private String url;
        private String action;

        public LinkItem (String title, String url, String action){
            this.title = title;
            this.url = url;
            this.action = action;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

}
