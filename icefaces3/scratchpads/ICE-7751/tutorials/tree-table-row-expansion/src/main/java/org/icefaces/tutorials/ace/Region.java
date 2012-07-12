package org.icefaces.tutorials.ace;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 4/3/12
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Region {
    String name;
    List<City> cityList;

    public Region(String name, List<City> cities) {
        this.name = name;
        cityList = cities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
