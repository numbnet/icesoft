/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.util.db;

import org.hibernate.cfg.Configuration;


public class DBBean {

    public Configuration createSchema(Configuration cfg) {
        return cfg.configure();
    }

    public void load() {
    }

}