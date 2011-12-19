/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
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
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.ace.component.themeloader;

import javax.faces.context.FacesContext;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIOutput;
import java.util.Map;
import org.icefaces.ace.util.Constants;

public class ThemeLoader extends UIOutput {

	public ThemeLoader() {
		super();
		
        setRendererType("javax.faces.resource.Stylesheet");
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		String theme = null;
		String themeParamValue = context.getExternalContext().getInitParameter(Constants.THEME_PARAM);

		if (themeParamValue != null) {
			ELContext elContext = context.getELContext();
			ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
			ValueExpression ve = expressionFactory.createValueExpression(elContext, themeParamValue, String.class);

			theme = (String) ve.getValue(elContext);
		}
		
		Map<String, Object> attrs = getAttributes();
		
		if (theme == null || theme.equalsIgnoreCase("sam")) {
			attrs.put("name", "themes/sam/theme.css");
			attrs.put("library", "icefaces.ace");
		} else if(theme.equalsIgnoreCase("rime")) {
			attrs.put("name", "themes/rime/theme.css");
			attrs.put("library", "icefaces.ace");
		}

		else if(!theme.equalsIgnoreCase("none")) {
			attrs.put("name", "theme.css");
			attrs.put("library", "ace-" + theme);
		}
        
        attrs.put("target", "head");
	}
}