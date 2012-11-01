package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This is a sample backing bean for Cloud Push
 */
@SessionAttributes("cloudpushBean")
public class CloudPushBean {

    String title;
    String message;
    int delay;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
