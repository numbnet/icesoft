package org.icefaces.demo.scopes;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean(name = "WindowScopedCounter")
@CustomScoped(value = "#{window}")
public class WindowScopedCounter extends Counter implements Serializable {
    public WindowScopedCounter() {
        System.out.println(this);
    }

    @PostConstruct
    public void created() {
        System.out.println("created >> " + this);
    }

    @PreDestroy
    public void destroyed() {
        System.out.println("destroyed >> " + this);
    }
}
