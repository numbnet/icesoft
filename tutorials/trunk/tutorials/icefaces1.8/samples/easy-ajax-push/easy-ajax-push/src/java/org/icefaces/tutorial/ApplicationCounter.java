package org.icefaces.tutorial;

import javax.faces.event.ActionEvent;
import org.icefaces.x.core.push.SessionRenderer;

public class ApplicationCounter extends Counter {

    public ApplicationCounter() {
    }

    public synchronized void setCount(int count){
        super.setCount(count);
        SessionRenderer.render("all");
    }

    public synchronized void increment(ActionEvent event) {
        super.increment(event);
        SessionRenderer.render("all");
   }

    public synchronized void decrement(ActionEvent event) {
        super.decrement(event);
        SessionRenderer.render("all");
   }

}
