package org.icefaces.demo.scopes;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean(name = "ApplicationCounter")
@ApplicationScoped
public class ApplicationCounter extends Counter implements Serializable {
    public ApplicationCounter() {
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