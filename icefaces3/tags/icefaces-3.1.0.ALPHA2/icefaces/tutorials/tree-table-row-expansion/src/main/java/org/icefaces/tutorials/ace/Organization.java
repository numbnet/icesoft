package org.icefaces.tutorials.ace;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 4/3/12
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Organization {
    public String name;
    public List<Region> regionList;

    public Organization(String name, List<Region> regions) {
        this.name = name;
        regionList = regions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Region> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<Region> regionList) {
        this.regionList = regionList;
    }
}
