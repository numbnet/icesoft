package org.icefaces.sample.portlet.counter.easyajaxpush;

import org.icefaces.application.PushRenderer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "sessionCounter")
@SessionScoped
public class SessionCounter extends Counter {

    public SessionCounter() {
        PushRenderer.addCurrentSession("chat");
    }

}
