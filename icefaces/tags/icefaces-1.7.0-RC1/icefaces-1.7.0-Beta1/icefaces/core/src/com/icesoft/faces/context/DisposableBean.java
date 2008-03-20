package com.icesoft.faces.context;

/**
 * Application beans that need to dispose resources acquired during their
 * lifecycle (such as threads, connections, files...) can implement this
 * interface to be notified when the bean instance is no longer in use.
 */
public interface DisposableBean {

    /**
     * Dispose resources. The invocation of this method is based on the scope of
     * the bean:
     * 'request' -- invoked when the view is disposed. When
     * concurrentDOMViews=false the view is reused on page reload so the
     * application beans are reused as well (method is not invoked)
     * 'session' -- invoked when the session has expired
     * 'application' -- invoked when the web application is shutdown
     *
     * @throws Exception
     */
    void dispose() throws Exception;
}
