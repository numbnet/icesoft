package org.icefaces.bean;

import org.icefaces.impl.application.WindowScopeManager;
import org.icefaces.util.EnvUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * This class can be used to locate window scope beans from a non-JSF environment (mainly servlet environment).
 */
public class WindowScopeBeanLocator {

    /**
     * Lookup a bean by its name in the current window scope.
     * @param request servlet request
     * @param session servlet session
     * @param name the name of the bean as it is declared in its annotation or faces-config.xml file
     * @return the bean when found or null if missing
     */
    public Object lookupBean(ServletRequest request, HttpSession session, String name) {
        HashMap<String, Object> requestMap = new HashMap();
        Enumeration e = request.getAttributeNames();
        while (e.hasMoreElements()) {
            String attributeName = (String) e.nextElement();
            requestMap.put(attributeName, request.getAttribute(attributeName));
        }
        String id = WindowScopeManager.lookupAssociatedWindowID(requestMap);

        WindowScopeManager.State state = (WindowScopeManager.State) session.getAttribute(WindowScopeManager.class.getName());
        if (state == null) {
            return null;
        } else {
            WindowScopeManager.ScopeMap scope = (WindowScopeManager.ScopeMap) state.windowScopedMaps.get(id);
            return scope.get(name);
        }
    }
}
