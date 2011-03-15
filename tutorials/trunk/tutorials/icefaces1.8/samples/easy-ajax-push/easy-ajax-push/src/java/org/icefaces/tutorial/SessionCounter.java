package org.icefaces.tutorial;

import org.icefaces.x.core.push.SessionRenderer;

public class SessionCounter extends Counter {

    public SessionCounter() {
        SessionRenderer.addCurrentSession("all");
    }

}
