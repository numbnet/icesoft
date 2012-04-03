package org.icefaces.tutorials.ace;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 4/3/12
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Person {
    Date born;
    String phone;
    String name;

    public Person(String phone, String name, Date born) {
        this.phone = phone;
        this.born = born;
        this.name = name;
    }

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
