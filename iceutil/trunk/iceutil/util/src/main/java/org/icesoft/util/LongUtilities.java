package org.icesoft.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LongUtilities {
    private static final Logger LOGGER = Logger.getLogger(LongUtilities.class.getName());

    public static int compare(final long long1, final long long2) {
        return long1 > long2 ? 1 : long1 < long2 ? -1 : 0;
    }
}
