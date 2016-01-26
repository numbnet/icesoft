/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
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
package org.icesoft.util.servlet;

import static org.icesoft.util.StringUtilities.isNullOrIsEmpty;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;

import org.icesoft.util.AbstractConfiguration;
import org.icesoft.util.Configuration;
import org.icesoft.util.ConfigurationException;

public class ServletConfigConfiguration
extends AbstractConfiguration
implements Configuration {
    private static final Logger LOGGER = Logger.getLogger(ServletConfigConfiguration.class.getName());

    private final String prefix;
    private final ServletConfig servletConfig;

    public ServletConfigConfiguration(
        final ServletConfig servletConfig) {

        this(null, servletConfig, null);
    }

    public ServletConfigConfiguration(
        final ServletConfig servletConfig, final Configuration defaultConfiguration) {

        this(null, servletConfig, defaultConfiguration);
    }

    public ServletConfigConfiguration(
        final String prefix, final ServletConfig servletConfig) {

        this(prefix, servletConfig, null);
    }

    public ServletConfigConfiguration(
        final String prefix, final ServletConfig servletConfig, final Configuration defaultConfiguration) {

        super(defaultConfiguration);
        this.prefix = prefix;
        this.servletConfig = servletConfig;
    }

    public String getAttribute(final String name)
    throws ConfigurationException {
        String _prefixedName = getPrefixedName(name);
        String _value = getServletConfig().getInitParameter(_prefixedName);
        if (_value != null) {
            return _value;
        } else {
            if (hasDefaultConfiguration()) {
                // throws ConfigurationException
                return getDefaultConfiguration().getAttribute(name);
            } else {
                throw new ConfigurationException("Cannot find parameter: " + _prefixedName);
            }
        }
    }

    public Configuration getChildConfiguration(final String name)
    throws ConfigurationException {
        String _prefixedName = getPrefixedName(name);
        String _value = getServletConfig().getInitParameter(_prefixedName);
        if (_value != null) {
            if (getDefaultConfiguration() != null) {
                return
                    new ServletConfigConfiguration(
                        _prefixedName, getServletConfig(), getDefaultConfiguration().getChildConfiguration(name)
                    );
            } else {
                return
                    new ServletConfigConfiguration(
                        _prefixedName, getServletConfig()
                    );
            }
        } else {
            throw new ConfigurationException("Cannot find parameter: " + _prefixedName);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String getValue()
    throws ConfigurationException {
        String _value = getServletConfig().getInitParameter(getPrefix());
        if (_value != null) {
            return _value;
        } else {
            throw new ConfigurationException("Cannot find parameter: " + getPrefix());
        }
    }

    protected ServletConfig getServletConfig() {
        return servletConfig;
    }

    private String getPrefixedName(final String name) {
        return (isNullOrIsEmpty(getPrefix())) ? name : getPrefix() + '.' + name;
    }
}
