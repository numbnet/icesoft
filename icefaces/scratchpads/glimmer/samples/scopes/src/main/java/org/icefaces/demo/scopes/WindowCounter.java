package org.icefaces.demo.scopes;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.SessionScoped;
import javax.annotation.PreDestroy;
import java.io.Serializable;

@ManagedBean(name = "WindowCounter")
@CustomScoped(value = "#{window}")
public class WindowCounter extends Counter implements Serializable {
    public WindowCounter() {
        System.out.println(this);
    }

    @PreDestroy
    public void destroyed() {
        System.out.println("destroyed: " + this);
    }
}
