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

package org.icefaces.mobi.component.googleanalytics;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.CoreRenderer;
import org.icefaces.mobi.utils.HTML;

public class GoogleAnalyticsRenderer extends CoreRenderer {
	private static final String GAR_NAME =
            GoogleAnalyticsRenderer.class.getName();
	private static final Logger log = Logger.getLogger(GAR_NAME);
	

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        GoogleAnalytics ga = (GoogleAnalytics) uiComponent;
        String account = ga.getAccount();
        if( account == null ){
			account = System.getProperty(GoogleAnalytics.ACCOUNT_ENVVAR);
			if( account == null ){
                Object warnOnce = facesContext.getExternalContext()
                    .getApplicationMap().get(GAR_NAME);
                if (null != warnOnce)  {
                    return;
                }
                log.warning("The Google Analytics account has not been set for the <mobi:googleAnalytics> tag. " +
						"Please use the 'account' tag attribute, or the '"+GoogleAnalytics.ACCOUNT_ENVVAR+"' " +
								"system environment variable. The Google Analytics script will not be generated.");
                facesContext.getExternalContext()
                    .getApplicationMap().put(GAR_NAME, GAR_NAME);
				return;
			}
		}
        String domain = ga.getDomain();
        if( domain == null ){
        	domain = System.getProperty(GoogleAnalytics.DOMAIN_ENVVAR);
        }
		writer.startElement(HTML.SCRIPT_ELEM,null);
		writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript",null);
		writer.write(getScript(account,domain));
		writer.endElement(HTML.SCRIPT_ELEM);
    }

	private String getScript(String account, String domain){
		String script = "var _gaq = _gaq || [];";
		script += "_gaq.push(['_setAccount', '"+account+"']);";
		if( domain != null && domain.length() > 0 ){
			script += "_gaq.push(['_setDomainName', '"+domain+"']);";
		}
		script += "_gaq.push(['_trackPageview']);"
			+ "(function() {"
		    + "var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;"
			+ "ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';"
		    + "var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);"
			+"})();";
		return script;
	}


}
