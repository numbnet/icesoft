/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */

package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.ConfigurationException;

import javax.servlet.ServletConfig;

public class ServletConfigConfiguration
extends Configuration {
    private String name;
    private ServletConfig servletConfig;

    public ServletConfigConfiguration(
        final String prefix, final ServletConfig servletConfig) {

        this.name = prefix;
        this.servletConfig = servletConfig;
    }


    public String getAttribute(final String name)
    throws ConfigurationException {
        String _name = postfixWith(name);
        String _value = servletConfig.getInitParameter(_name);
        if (_value == null) {
            throw new ConfigurationException("Cannot find parameter: " + _name);
        } else {
            return _value;
        }
    }

    public Configuration getChild(final String child)
    throws ConfigurationException {
        String _name = postfixWith(child);
        if (servletConfig.getInitParameter(_name) == null) {
            throw new ConfigurationException("Cannot find parameter: " + _name);
        } else {
            return new ServletConfigConfiguration(_name, servletConfig);
        }
    }

    public Configuration[] getChildren(final String name)
    throws ConfigurationException {
        return new Configuration[] { getChild(name) };
    }

    public String getName() {
        return name;
    }

    public String getValue()
    throws ConfigurationException {
        String _value = servletConfig.getInitParameter(name);
        if (_value == null) {
            throw new ConfigurationException("Cannot find parameter: " + name);
        } else {
            return _value;
        }
    }

    private String postfixWith(final String child) {
        return
            new StringBuffer().
                append(name).append('.').append(child).toString();
    }
}
