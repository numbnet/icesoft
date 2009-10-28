package org.icefaces.demo.scopes;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name = "SessionScopedCounter")
@SessionScoped
public class SessionScopedCounter extends Counter implements Serializable {
    public SessionScopedCounter() {
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