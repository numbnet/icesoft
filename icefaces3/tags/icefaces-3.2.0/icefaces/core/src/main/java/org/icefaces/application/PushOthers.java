package org.icefaces.application;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PushOthers
extends PushOptions {
    private static final Logger LOGGER = Logger.getLogger(PushOthers.class.getName());

    public static final String PUSH_OTHERS = "pushOthers";

    public PushOthers() {
        getAttributes().put(PUSH_OTHERS, true);
    }
}
