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

package org.icemobile.component;


import org.icemobile.util.ClientDescriptor;

/*
 * no disabled property on this component so does not implement IMobiComponent
 */
public interface IButtonGroup extends IMobiComponent{

  // Default button types.
    public static final String ORIENTATION_VERTICAL = "vertical";
    public static final String ORIENTATION_HORIZONTAL = "horizontal";

    // button styles.
    public static final String DEFAULT_STYLE_CLASS = "mobi-button-group";
    public static final String VERTICAL_STYLE_CLASS = " mobi-button-group-vert";
    public static final String HORIZONTAL_STYLE_CLASS = " mobi-button-group-hor";

    public void setOrientation(String orientation);
    public String getOrientation();

    public void setStyle(String style) ;
    public String getStyle() ;
    public void setStyleClass(String styleClass);
    public String getStyleClass() ;
    public String getClientId();

    public void setSelectedId(String selected);
    public String getSelectedId();

    public String getName(); //jsp only
    public void setName(String name); //jsp only
 //   public ClientDescriptor getClient();

}
