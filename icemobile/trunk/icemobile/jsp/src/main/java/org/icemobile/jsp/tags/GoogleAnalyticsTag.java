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

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

import static org.icemobile.util.HTML.*;


public class GoogleAnalyticsTag extends BaseSimpleTag{
	
	private static Logger log = Logger.getLogger(GoogleAnalyticsTag.class.getName());
	
	public static final String ACCOUNT_ENVVAR = "org.icemobile.googleAnalyticsAccount";
	public static final String DOMAIN_ENVVAR = "org.icemobile.googleAnalyticsDomain";
	private static final String ACCOUNT_WARNING = "org.icemobile.googleAnalytics.accountWarning";
	
	private String account;
	private String domain;	
	
	public void doTag() throws IOException {
		TagWriter writer = new TagWriter(getContext());
		if( account == null ){
			account = System.getProperty(ACCOUNT_ENVVAR);
			if (account == null)  {
                ServletContext servletContext = getServletContext();
                if (servletContext.getAttribute(ACCOUNT_WARNING) == null) {
                    log.warning("The Google Analytics account has not " +
                        "been set for the <mobi:googleAnalytics> tag. " +
                        "Please use the 'account' tag attribute, or the '" +
                        ACCOUNT_ENVVAR +
                        "' system property. The Google Analytics script " +
                        "will not be generated.");
                    servletContext.setAttribute(ACCOUNT_WARNING, Boolean.TRUE);
                }
                return;
            }
		}
		if( domain == null ){
        	domain = System.getProperty(DOMAIN_ENVVAR);
        }
		writer.startElement(SCRIPT_ELEM);
		writer.writeAttribute(TYPE_ATTR, "text/javascript");
		writer.writeText(getScript(account,domain));
		writer.endElement();
	}

	public String getAccount() {
		return account;
	}


	public void setAccount(String account) {
		this.account = account;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
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
	
	public void release(){
	    super.release();
	    account = null;
	    domain = null;
	}
			

}
