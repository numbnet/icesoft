package org.icefaces.sample.portlet.counter.easyajaxpush;

import javax.faces.event.ActionEvent;

public class Counter {

    private int count;

    public Counter() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void increment(ActionEvent event) {
        System.out.println("Counter.increment");
        count++;
    }

    public void decrement(ActionEvent event) {
        System.out.println("Counter.decrement");
        count--;
    }

//    public String increment() {
//        System.out.println("Counter.increment");
//        count++;
//        return "increment";
//    }
//
//    public String decrement() {
//        System.out.println("Counter.decrement");
//        count--;
//        return "decrement";
//    }

}
