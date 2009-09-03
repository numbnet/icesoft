For further details, see http://jira.icefaces.org/browse/ICE-4839

Summary

In order to properly dispose of all ICEfaces views on a portal page, we've added logic into the core to support tracking of all the portlet views on a page from the server side.  The logic to determine which page a portlet is on can be container specific.  To avoid having compile-time/run-time dependencies on the libraries of a particular container, the platform specific code is kept separately from the core.

To specify the platform-specific logic for determining which page a portlet view is associated with, you use the following context parameter: 

    <context-param> 
        <param-name>com.icesoft.faces.portlet.associatedPageViewsImpl</param-name> 
        <param-value>com.icesoft.faces.webapp.http.portlet.page.JBossAssociatedPageViews</param-value> 
    </context-param> 

The value should be a fully-qualified class name that extends the com.icesoft.faces.webapp.http.portlet.page.AssociatedPageViewsImpl.  The core currently contains an implementation called com.icesoft.faces.webapp.http.portlet.page.NoOpAssociatedPageViews.  Any other implementation would need to be included in the web applications class loading scope.
