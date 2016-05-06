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
package org.icesoft.util;

public interface Configuration {
    String getAttribute(String name)
    throws ConfigurationException;

    String getAttribute(String name, String defaultValue);

    boolean getAttributeAsBoolean(String name)
    throws ConfigurationException;

    boolean getAttributeAsBoolean(String name, boolean defaultValue);

    double getAttributeAsDouble(String name)
    throws ConfigurationException;

    double getAttributeAsDouble(String name, double defaultValue);

    float getAttributeAsFloat(String name)
    throws ConfigurationException;

    float getAttributeAsFloat(String name, float defaultValue);

    int getAttributeAsInteger(String name)
    throws ConfigurationException;

    int getAttributeAsInteger(String name, int defaultValue);

    long getAttributeAsLong(String name)
    throws ConfigurationException;

    long getAttributeAsLong(String name, long defaultValue);

    Configuration getChildConfiguration(String name)
    throws ConfigurationException;

    String getPrefix();

    String getValue()
    throws ConfigurationException;

    String getValue(String defaultValue);

    boolean getValueAsBoolean()
    throws ConfigurationException;

    boolean getValueAsBoolean(boolean defaultValue);

    double getValueAsDouble()
    throws ConfigurationException;

    double getValueAsDouble(double defaultValue);

    float getValueAsFloat()
    throws ConfigurationException;

    float getValueAsFloat(float defaultValue);

    int getValueAsInteger()
    throws ConfigurationException;

    int getValueAsInteger(int defaultValue);

    long getValueAsLong()
    throws ConfigurationException;

    long getValueAsLong(long defaultValue);
}
