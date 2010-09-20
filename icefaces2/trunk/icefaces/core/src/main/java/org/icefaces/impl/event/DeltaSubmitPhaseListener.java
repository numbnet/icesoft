/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.impl.event;

import org.icefaces.util.EnvUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.portlet.ActionRequest;
import javax.portlet.filter.ActionRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class DeltaSubmitPhaseListener implements PhaseListener {
    private static final String PreviousParameters = "previous-parameters";
    private static final String[] StringArray = new String[0];
    private static Logger log = Logger.getLogger("org.icefaces.event");

    public DeltaSubmitPhaseListener() {
    }

    public void beforePhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        if (EnvUtils.isDeltaSubmit(facesContext)) {
            reconstructParametersFromDeltaSubmit(facesContext);
        }
    }

    public void afterPhase(PhaseEvent event) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.APPLY_REQUEST_VALUES;
    }

    private void reconstructParametersFromDeltaSubmit(FacesContext facesContext) {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        ExternalContext externalContext = facesContext.getExternalContext();
        Map submittedParameters = externalContext.getRequestParameterValuesMap();

        String[] deltaSubmitFormValues = (String[]) submittedParameters.get("ice.deltasubmit.form");
        if (deltaSubmitFormValues == null) {
            //this is not a delta form submission
            return;
        }
        String formClientID = deltaSubmitFormValues[0];
        UIComponent formComponent = viewRoot.findComponent(formClientID);

        Map formAttributes = formComponent.getAttributes();
        Map previousParameters = (Map) formAttributes.get(PreviousParameters);
        if (previousParameters == null) {
            //todo: use a public constant to lookup old DOM document
            Document oldDOM = (Document) viewRoot.getAttributes().get("org.icefaces.old-dom");
            previousParameters = calculateParametersFromDOM(externalContext, oldDOM);
        }
        final Map parameterValuesMap = new HashMap(previousParameters);
        final ArrayList directParameters = new ArrayList();

        Iterator i = submittedParameters.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            String patchKey = (String) entry.getKey();
            String[] values = (String[]) entry.getValue();

            if (patchKey.startsWith("patch+")) {
                String key = patchKey.substring(6);
                String[] previousValues = (String[]) parameterValuesMap.get(key);
                if (previousValues == null) {
                    parameterValuesMap.put(key, values);
                } else {
                    ArrayList allValues = new ArrayList();
                    allValues.addAll(Arrays.asList(previousValues));
                    allValues.addAll(Arrays.asList(values));
                    parameterValuesMap.put(key, allValues.toArray(StringArray));
                }
            } else if (patchKey.startsWith("patch-")) {
                String key = patchKey.substring(6);
                String[] previousValues = (String[]) parameterValuesMap.get(key);
                if (previousValues == null) {
                    log.warning("Missing previous parameters: " + key);
                } else {
                    ArrayList allValues = new ArrayList();
                    allValues.addAll(Arrays.asList(previousValues));
                    allValues.removeAll(Arrays.asList(values));
                    if (allValues.isEmpty()) {
                        parameterValuesMap.remove(key);
                    } else {
                        parameterValuesMap.put(key, allValues.toArray(StringArray));
                    }
                }
            } else {
                //overwrite parameters
                parameterValuesMap.put(patchKey, values);
                directParameters.add(patchKey);
            }
        }

        //remove parameters that don't participate in the parameter diffing
        Map newPreviousParameters = new HashMap(parameterValuesMap);
        Iterator directParameterIterator = directParameters.iterator();
        while (directParameterIterator.hasNext()) {
            String parameterName = (String) directParameterIterator.next();
            //don't remove parameter when it also participates in the parameter diffing
            if (!submittedParameters.containsKey("patch+" + parameterName)) {
                newPreviousParameters.remove(parameterName);
            }
        }

        formAttributes.put(PreviousParameters, newPreviousParameters);

        Object request = externalContext.getRequest();
        if (EnvUtils.instanceofPortletRequest(request)) {
            externalContext.setRequest(new DeltaActionPortletRequest((ActionRequest) request, parameterValuesMap));
        } else {
            externalContext.setRequest(new DeltaHttpServletRequest((HttpServletRequest) request, parameterValuesMap));
        }
    }

    private Map calculateParametersFromDOM(ExternalContext externalContext, Document doc) {
        Map multiParameters = new HashMap();
        Map parameters = externalContext.getRequestParameterMap();

        NodeList forms = doc.getElementsByTagName("form");
        for (int i = 0; i < forms.getLength(); i++) {
            Element form = (Element) forms.item(i);
            String formID = form.getAttribute("id");
            if (parameters.containsKey(formID)) {
                NodeList inputs = form.getElementsByTagName("input");
                for (int j = 0; j < inputs.getLength(); j++) {
                    Element input = (Element) inputs.item(j);
                    //submitting type elements are present in the form only if they triggered the submission
                    //ignore radio/checkbox buttons
                    String type = input.getAttribute("type");
                    if ("image".equalsIgnoreCase(type) || "button".equalsIgnoreCase(type) || "submit".equalsIgnoreCase(type) || "reset".equalsIgnoreCase(type)) {
                        continue;
                    }

                    String name = input.getAttribute("name");
                    if ("checkbox".equalsIgnoreCase(type) || "radio".equalsIgnoreCase(type)) {
                        if (input.hasAttribute("checked")) {
                            String value = input.getAttribute("value");
                            value = "".equals(value) ? "on" : value;
                            multiParameters.put(name, new String[]{value});
                        }
                    } else {
                        String value = input.getAttribute("value");
                        String[] values = (String[]) multiParameters.get(name);
                        if (values == null) {
                            multiParameters.put(name, new String[]{value});
                        } else {
                            ArrayList list = new ArrayList(Arrays.asList(values));
                            list.add(value);
                            multiParameters.put(name, list.toArray(StringArray));
                        }
                    }
                }
                NodeList textareas = form.getElementsByTagName("textarea");
                for (int j = 0; j < textareas.getLength(); j++) {
                    Element txtarea = (Element) textareas.item(j);
                    String name = txtarea.getAttribute("name");
                    if (!parameters.containsKey(name)) {
                        Node child = txtarea.getFirstChild();
                        String value = child == null ? "" : child.getNodeValue();
                        multiParameters.put(name, new String[]{value});
                    }
                }
                NodeList selects = form.getElementsByTagName("select");
                for (int j = 0; j < selects.getLength(); j++) {
                    Element select = (Element) selects.item(j);
                    String name = select.getAttribute("name");
                    if (!parameters.containsKey(name)) {
                        NodeList options = select.getElementsByTagName("option");
                        ArrayList selectedOptions = new ArrayList();
                        for (int k = 0; k < options.getLength(); k++) {
                            Element option = (Element) options.item(k);
                            if ("selected".equalsIgnoreCase(option.getAttribute("selected"))) {
                                selectedOptions.add(option.getAttribute("value"));
                            }
                        }
                        if (!selectedOptions.isEmpty()) {
                            multiParameters.put(name, selectedOptions.toArray(StringArray));
                        }
                    }
                }
                break;
            }
        }

        return multiParameters;
    }

    private static class DeltaHttpServletRequest extends HttpServletRequestWrapper {
        private final Map parameterValuesMap;

        public DeltaHttpServletRequest(HttpServletRequest originalRequest, Map parameterValuesMap) {
            super(originalRequest);
            this.parameterValuesMap = parameterValuesMap;
        }

        public String getParameter(String s) {
            String[] values = (String[]) parameterValuesMap.get(s);
            return values == null ? null : values[0];
        }

        public String[] getParameterValues(String s) {
            return (String[]) parameterValuesMap.get(s);
        }

        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(parameterValuesMap.keySet());
        }

        public Map getParameterMap() {
            return Collections.unmodifiableMap(parameterValuesMap);
        }
    }

    private static class DeltaActionPortletRequest extends ActionRequestWrapper {
        private final Map parameterValuesMap;

        public DeltaActionPortletRequest(ActionRequest originalRequest, Map parameterValuesMap) {
            super(originalRequest);
            this.parameterValuesMap = parameterValuesMap;
        }

        public String getParameter(String s) {
            String[] values = (String[]) parameterValuesMap.get(s);
            return values == null ? null : values[0];
        }

        public String[] getParameterValues(String s) {
            return (String[]) parameterValuesMap.get(s);
        }

        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(parameterValuesMap.keySet());
        }

        public Map getParameterMap() {
            return Collections.unmodifiableMap(parameterValuesMap);
        }
    }
}
