package org.icefaces.mobile.composite;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

/**
 * Value holder bean to get around require attributes on composite component
 * tests.
 */
@ManagedBean
@SessionScoped
public class ValueHolder {

    private String[] array1 = new String[]{"test1", "test2", "test3"};
    private String[] array2 = new String[]{"test1", "test2", "test3"};
    private String[] array3 = new String[]{"test1", "test2", "test3"};

    private boolean boolean1;
    private boolean boolean2;
    private boolean boolean3;

    private String string1 = "test1";
    private String string2 = "test2";
    private String string3 = "test3";

    public void filterChanged(ValueChangeEvent event) {

    }

    public String[] getArray1() {
        return array1;
    }

    public void setArray1(String[] array1) {
        this.array1 = array1;
    }

    public boolean isBoolean1() {
        return boolean1;
    }

    public void setBoolean1(boolean boolean1) {
        this.boolean1 = boolean1;
    }

    public boolean isBoolean2() {
        return boolean2;
    }

    public void setBoolean2(boolean boolean2) {
        this.boolean2 = boolean2;
    }

    public boolean isBoolean3() {
        return boolean3;
    }

    public void setBoolean3(boolean boolean3) {
        this.boolean3 = boolean3;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    public String getString3() {
        return string3;
    }

    public void setString3(String string3) {
        this.string3 = string3;
    }

    public String[] getArray2() {
        return array2;
    }

    public void setArray2(String[] array2) {
        this.array2 = array2;
    }

    public String[] getArray3() {
        return array3;
    }

    public void setArray3(String[] array3) {
        this.array3 = array3;
    }
}
