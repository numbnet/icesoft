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

package org.icefaces.samples.showcase.util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.CustomScoped;
import javax.faces.application.Resource;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@CustomScoped(value = "#{window}")
public class RimeThemeBean implements Serializable {

	private static final String styles = ".wijmo-wijmenu-horizontal.documentation-menu {"
		+ "font-size:1.1em !important;"
		+ "}"
		+ ".containerPanelStyle .ui-panel-titlebar {"
		+ "height: 19px;"
		+ "}";
	
	public String getStyles() {
		return styles;
	}
}
