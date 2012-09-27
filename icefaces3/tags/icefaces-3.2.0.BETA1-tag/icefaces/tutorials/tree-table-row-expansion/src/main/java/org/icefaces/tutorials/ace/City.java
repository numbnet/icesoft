package org.icefaces.tutorials.ace;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 4/3/12
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class City {
    String name;
    List<Person> personList;

    public City(String name, List<Person> persons) {
        this.name = name;
        personList = persons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }
}
