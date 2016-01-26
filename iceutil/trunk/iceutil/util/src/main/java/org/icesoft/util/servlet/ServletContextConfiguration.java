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

import javax.servlet.ServletContext;

import org.icesoft.util.AbstractConfiguration;
import org.icesoft.util.Configuration;
import org.icesoft.util.ConfigurationException;

public class ServletContextConfiguration
extends AbstractConfiguration
implements Configuration {
    private static final Logger LOGGER = Logger.getLogger(ServletContextConfiguration.class.getName());

    private final String prefix;
    private final ServletContext servletContext;

    public ServletContextConfiguration(
        final ServletContext servletContext) {

        this(null, servletContext, null);
    }

    public ServletContextConfiguration(
        final ServletContext servletContext, final Configuration defaultConfiguration) {

        this(null, servletContext, defaultConfiguration);
    }

    public ServletContextConfiguration(
        final String prefix, final ServletContext servletContext) {

        this(prefix, servletContext, null);
    }

    public ServletContextConfiguration(
        final String prefix, final ServletContext servletContext, final Configuration defaultConfiguration) {

        super(defaultConfiguration);
        this.prefix = prefix;
        this.servletContext = servletContext;
    }

    public String getAttribute(final String name)
    throws ConfigurationException {
        String _prefixedName = getPrefixedName(name);
        String _value = getServletContext().getInitParameter(_prefixedName);
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
        String _value = getServletContext().getInitParameter(_prefixedName);
        if (_value != null) {
            if (getDefaultConfiguration() != null) {
                return
                    new ServletContextConfiguration(
                        _prefixedName, getServletContext(), getDefaultConfiguration().getChildConfiguration(name)
                    );
            } else {
                return
                    new ServletContextConfiguration(
                        _prefixedName, getServletContext()
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
        String _value = getServletContext().getInitParameter(getPrefix());
        if (_value != null) {
            return _value;
        } else {
            throw new ConfigurationException("Cannot find parameter: " + getPrefix());
        }
    }

    protected ServletContext getServletContext() {
        return servletContext;
    }

    private String getPrefixedName(final String name) {
        return (isNullOrIsEmpty(getPrefix())) ? name : getPrefix() + '.' + name;
    }
}
