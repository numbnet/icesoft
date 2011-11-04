/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.generator.utils;

import org.icefaces.component.annotation.*;

public class PropertyValues {

    public PropertyValues() {

    }

    public Expression expression = Expression.UNSET;
    public String methodExpressionArgument = Property.Null;
    public String defaultValue = Property.Null;
    public DefaultValueType defaultValueType = DefaultValueType.UNSET;
    public String tlddoc = Property.Null;
    public String javadocGet = Property.Null;
    public String javadocSet = Property.Null;
    public Required required = Required.UNSET;
    public String name = Property.Null;

    public Implementation implementation = Implementation.UNSET;

    // flag to indicate that the property in question was first defined in a superclass
    public boolean overrides = false;

    // flag to indicate that only delegating getter and setter methods should be generated and no state staving code
    public boolean isDelegatingProperty = false;
}