package org.icepush.samples.icechat.spring.impl;

import org.icepush.samples.icechat.AbstractPushRequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasePushRequestContext extends AbstractPushRequestContext {
    public BasePushRequestContext() {
        super();
    }

    public BasePushRequestContext(HttpServletRequest request, HttpServletResponse response) {
        super();

        intializePushContext(request, response);
    }
}
