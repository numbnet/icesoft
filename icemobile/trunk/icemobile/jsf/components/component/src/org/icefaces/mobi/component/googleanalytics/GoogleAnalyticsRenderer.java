package org.icefaces.mobi.component.googleanalytics;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.CoreRenderer;
import org.icefaces.mobi.utils.HTML;

public class GoogleAnalyticsRenderer extends CoreRenderer {
	
	private static Logger log = Logger.getLogger(GoogleAnalyticsRenderer.class.getName());
	

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        GoogleAnalytics ga = (GoogleAnalytics) uiComponent;
        String account = ga.getAccount();
        if( account == null ){
			account = System.getenv(GoogleAnalytics.ACCOUNT_ENVVAR);
			if( account == null ){
				log.warning("The Google Analytics account has not been set for the <mobi:googleAnalytics> tag. " +
						"Please use the 'account' tag attribute, or the '"+GoogleAnalytics.ACCOUNT_ENVVAR+"' " +
								"system environment variable. The Google Analytics script will not be generated.");
				return;
			}
		}
		writer.startElement(HTML.SCRIPT_ELEM,null);
		writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript",null);
		writer.write(String.format(GoogleAnalytics.SCRIPT, account));
		writer.endElement(HTML.SCRIPT_ELEM);
    }


}
