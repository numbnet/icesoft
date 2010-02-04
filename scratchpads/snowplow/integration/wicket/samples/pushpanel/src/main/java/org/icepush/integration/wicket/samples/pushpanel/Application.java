package org.icepush.integration.wicket.samples.pushpanel;

import org.apache.wicket.protocol.http.WebApplication;
/**
 *
 * @author bkroeger
 * @version
 */

public class Application extends WebApplication {

    public Application() {
    }

    public Class getHomePage() {
        return HomePage.class;
    }
}
