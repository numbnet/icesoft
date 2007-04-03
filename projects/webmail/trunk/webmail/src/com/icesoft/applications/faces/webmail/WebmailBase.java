/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail;

/**
 * This inteface should be used for most webmail classes to insure proper
 * initialization and despose of class resources.
 */
public interface WebmailBase {

    public void init();

    public void dispose();

}
