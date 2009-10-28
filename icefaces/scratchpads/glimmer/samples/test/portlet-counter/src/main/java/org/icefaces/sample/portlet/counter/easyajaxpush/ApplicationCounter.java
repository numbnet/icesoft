package org.icefaces.sample.portlet.counter.easyajaxpush;

import org.icefaces.application.PushRenderer;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "applicationCounter")
@ApplicationScoped
public class ApplicationCounter extends Counter {

    public ApplicationCounter() {
    }

    public synchronized void setCount(int count) {
        super.setCount(count);
        PushRenderer.render("chat");
    }

    public synchronized void increment(ActionEvent event) {
        super.increment(event);
        PushRenderer.render("chat");
    }

    public synchronized void decrement(ActionEvent event) {
        super.decrement(event);
        PushRenderer.render("chat");
    }

//    public synchronized String increment() {
//        String result = super.increment();
//        PushRenderer.render("chat");
//        return result;
//    }
//
//    public synchronized String decrement() {
//        String result = super.decrement();
//        PushRenderer.render("chat");
//        return result;
//    }


}
