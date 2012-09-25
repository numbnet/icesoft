package org.icefaces.mobi.component.googleanalytics;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;

@Component(
        tagName = "googleAnalytics",
        componentClass = "org.icefaces.mobi.component.googleanalytics.GoogleAnalytics",
        rendererClass = "org.icefaces.mobi.component.googleanalytics.GoogleAnalyticsRenderer",
        generatedClass = "org.icefaces.mobi.component.googleanalytics.GoogleAnalyticsBase",
        componentType = "org.icefaces.GoogleAnalytics",
        rendererType = "org.icefaces.GoogleAnalyticsRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.GoogleAnalytics",
        tlddoc = "The Google Analytics tag will write out the necessary script for the using " +
        		"the system property org.icemobile.googleAnalyticsAccount or the tag attribute " +
        		"'account'. If the 'account' attribute is set, this will override any System " +
        		"environment variable."
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class GoogleAnalyticsMeta {

	@Property(tlddoc="The Google Analytics account. If none is provided, the system property is used.")
	private String account;

}
