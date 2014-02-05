/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icemobile.jsp.tags;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.Writer;

import org.icemobile.util.ClientDescriptor;

public class GeolocationTag extends BaseSimpleTag {

    private final int DEFAULT_TIMEOUT_VALUE = 60;
    private final int DEFAULT_MAXAGE_VALUE = 3600;
    
    private String name;
    private boolean continuousUpdates;
    private int timeout = -1;
    private int maximumAge = -1;
    private String enableHighPrecision;

    public void doTag() throws IOException {

        PageContext pageContext = (PageContext) getJspContext();

        Writer out = pageContext.getOut();
        out.write("<span ");

        out.write(" id='" + getId() + "'");
        out.write(">");
        out.write("<input type='hidden' ");

        out.write(" id='" + getId() + "_locHidden'");
        out.write(" name='" + getName() + "' ");

        if (disabled) {
            out.write("disabled='true' ");
        }

        out.write(">");  // end input

        out.write("<script ");
        if (id != null && !"".equals(id)) {
            out.write("id ='" + id + "_script' ");
        }
        out.write(">\n");

        boolean includeHighPrecision;
        StringBuilder sb = new StringBuilder(255);
        String highPrecision = getEnableHighPrecision();

        if (highPrecision == null || "asneeded".equalsIgnoreCase(highPrecision)) {
            includeHighPrecision = sniffDevices(pageContext);
        } else {
            includeHighPrecision = Boolean.valueOf(highPrecision);
        }

        int maxAge = getMaximumAge();
        //
        // Values of zero defined from the page are valid, but not necessarily
        // useful. If no values are defined, specify a default, but also allow
        // the user to define a value of zero.
        //
        // maxage of zero will force an update
        if (maxAge < 0) {
            maxAge = DEFAULT_MAXAGE_VALUE;
        }
        int timeout = getTimeout();
        // value of zero causes instant timeout
        if (timeout < 0) {
            timeout = DEFAULT_TIMEOUT_VALUE;
        }
        boolean continuous = isContinuousUpdates();

        if (continuous) {
            sb.append("ice.mobi.geolocation.watchLocation('").append(getId()).append("','");
        } else {
            sb.append("ice.mobi.geolocation.getLocation('").append(getId()).append("','");
        }
        sb.append(includeHighPrecision).append("', '");
        sb.append(maxAge).append("', '").append(timeout).append("'); ");

        sb.append("</script>");
        sb.append("</span>");   // end span
        out.write(sb.toString());

    }

    private boolean sniffDevices(PageContext pageContext) {
        ServletRequest request = pageContext.getRequest();
        if (request instanceof HttpServletRequest) {
            HttpServletRequest hsr = (HttpServletRequest) request;
            ClientDescriptor client = ClientDescriptor.getInstance(hsr);
            return (client.isAndroidOS() & client.isTabletBrowser()) || client.isBlackBerryOS() || client.isBlackBerry10OS();
        } else {
            return true;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getEnableHighPrecision() {
        return enableHighPrecision;
    }

    public void setEnableHighPrecision(String enableHighPrecision) {
        this.enableHighPrecision = enableHighPrecision;
    }

    public boolean isContinuousUpdates() {
        return continuousUpdates;
    }

    public void setContinuousUpdates(boolean continuousUpdates) {
        this.continuousUpdates = continuousUpdates;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(int maximumAge) {
        this.maximumAge = maximumAge;
    }
    
    public void release(){
        super.release();
        name = null;
        continuousUpdates = false;
        timeout = -1;
        maximumAge = -1;
        enableHighPrecision = null;
    }
}