package org.icefaces.component.inputFiles;

import org.icefaces.context.DOMPartialViewContext;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.context.PartialViewContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Collections;
import java.io.InputStream;

public class InputFilesPhaseListener implements PhaseListener {
    public void afterPhase(PhaseEvent phaseEvent) {
//        System.out.println("InputFilesPhaseListener.afterPhase()   " + phaseEvent.getPhaseId());
//        System.out.println("InputFilesPhaseListener.afterPhase()     renderResponse  : " + phaseEvent.getFacesContext().getRenderResponse());
//        System.out.println("InputFilesPhaseListener.afterPhase()     responseComplete: " + phaseEvent.getFacesContext().getResponseComplete());
    }

    public void beforePhase(PhaseEvent phaseEvent) {
//        System.out.println("InputFilesPhaseListener.beforePhase()  " + phaseEvent.getPhaseId());
        if (!phaseEvent.getPhaseId().equals(PhaseId.RESTORE_VIEW)) {
            // Don't evaluate any of the fields on FacesContext or
            // PartialViewContext until after we've swapped in our
            // HttpServletRequest object
//            System.out.println("InputFilesPhaseListener.beforePhase()    postback: " + phaseEvent.getFacesContext().isPostback());
//            System.out.println("InputFilesPhaseListener.beforePhase()    partialViewContext.class: " + phaseEvent.getFacesContext().getPartialViewContext().getClass().getName());
//            System.out.println("InputFilesPhaseListener.beforePhase()    partialViewContext.isAjaxRequest: " + phaseEvent.getFacesContext().getPartialViewContext().isAjaxRequest());
//            System.out.println("InputFilesPhaseListener.beforePhase()    partialViewContext.isExecuteAll : " + phaseEvent.getFacesContext().getPartialViewContext().isExecuteAll());
//            System.out.println("InputFilesPhaseListener.beforePhase()    partialViewContext.isRenderAll  : " + phaseEvent.getFacesContext().getPartialViewContext().isRenderAll());
        }
        if (!phaseEvent.getPhaseId().equals(PhaseId.RESTORE_VIEW))
            return;
            
        Object requestObject = phaseEvent.getFacesContext().getExternalContext().getRequest();
        if (!(requestObject instanceof HttpServletRequest)) {
//            System.out.println("InputFilesPhaseListener.beforePhase()  requestObject: " + requestObject);
//            if (requestObject != null)
//                System.out.println("InputFilesPhaseListener.beforePhase()  requestObject.class: " + requestObject.getClass().getName());
            return;
        }
        HttpServletRequest request = (HttpServletRequest) requestObject;
//        System.out.println("InputFilesPhaseListener.beforePhase()  contentType: " + request.getContentType());
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//        System.out.println("InputFilesPhaseListener.beforePhase()  isMultipart: " + isMultipart);
        if (isMultipart) {
            final ServletFileUpload uploader = new ServletFileUpload();
            uploader.setFileSizeMax(100000000L);
            uploader.setProgressListener(new ProgressListener() {
                private long lastPercent = 0;
                public void update(long read, long total, int chunkIndex) {
                    if (read > 0 && total > 0) {
                        long currPercent = (read * 100L) / total;
                        if (currPercent >= (lastPercent + 10L)) {
                            lastPercent = currPercent;
                            System.out.println("Progress: " + lastPercent + "%");
                        }
                    }
                    else {
                        lastPercent = 0L;
                    }
                }
            });
            Map<String, List<String>> parameterListMap = new HashMap<String, List<String>>();
            try {
                FileItemIterator iter = uploader.getItemIterator(request);
                while (iter.hasNext()) {
                    FileItemStream item = iter.next();
                    if (item.isFormField()) {
                        String name = item.getFieldName();
                        String value = Streams.asString(item.openStream());
                        System.out.println("Field:  " + name + "  ->  " + value);
                        
                        List<String> parameterList = parameterListMap.get(name);
                        if (parameterList == null) {
                            parameterList = new ArrayList<String>(6);
                            parameterListMap.put(name, parameterList);
                        }
                        parameterList.add(value);
                    } else {
                        String name = item.getName();
                        String fieldName = item.getFieldName();
                        String contentType = item.getContentType();
                        System.out.println("File  name: " + name);
                        System.out.println("File  fieldName: " + fieldName);
                        System.out.println("File  contentType: " + contentType);
                        //FileOutputStream = new FileOutputStream("C:\\aaa\\" + name);
                        InputStream in = item.openStream();
                        byte[] buffer = new byte[4096];
                        while (in.read(buffer) >= 0) {}
                        //TODO
                    }
                }
            }
            catch(Exception e) {
                System.out.println("Problem: " + e);
                e.printStackTrace();
            }
            
            // Map<String, List<String>> parameterListMap = new HashMap<String, List<String>>();
            Map<String, String[]> parameterMap = new HashMap<String, String[]>(
                ((parameterListMap.size() > 0) ? parameterListMap.size() : 1) );
            for(String key : parameterListMap.keySet()) {
                List<String> parameterList = parameterListMap.get(key);
                String[] values = new String[parameterList.size()];
                values = parameterList.toArray(values);
                parameterMap.put(key, values);
            }
            
//            System.out.println("InputFilesPhaseListener.beforePhase()  parameterMap    : " + parameterMap);
            
            HttpServletRequest wrapper = new FileUploadRequestWrapper(request, parameterMap);
            phaseEvent.getFacesContext().getExternalContext().setRequest(wrapper);
            PartialViewContext pvc = phaseEvent.getFacesContext().getPartialViewContext();
            if (pvc instanceof DOMPartialViewContext)
                ((DOMPartialViewContext) pvc).setAjaxRequest(true);
            pvc.setPartialRequest(true);
            // Apparently not necessary, as long as we don't call
            // FacesContext.isPostback() before this point
            //phaseEvent.getFacesContext().getAttributes().remove(
            //   "com.sun.faces.context.FacesContextImpl_POST_BACK");
            
//            System.out.println("InputFilesPhaseListener.beforePhase()  old    : " + request);
//            System.out.println("InputFilesPhaseListener.beforePhase()  wrapper: " + wrapper);
//            System.out.println("InputFilesPhaseListener.beforePhase()  set    : " + phaseEvent.getFacesContext().getExternalContext().getRequest());
        }
        else if (false && phaseEvent.getFacesContext().isPostback()) {
            //TODO
            // This is only for testing with non-file-upload, regular postback,
            // to have it respond with an ajax response. This should typically
            // be disabled
//            System.out.println("InputFilesPhaseListener.beforePhase()  Temporary test of adding Faces-Request HTTP header");
            HttpServletRequest wrapper = new FileUploadRequestWrapper(request, null);
            phaseEvent.getFacesContext().getExternalContext().setRequest(wrapper);
            PartialViewContext pvc = phaseEvent.getFacesContext().getPartialViewContext();
            if (pvc instanceof DOMPartialViewContext)
                ((DOMPartialViewContext) pvc).setAjaxRequest(true);
            pvc.setPartialRequest(true);
//            System.out.println("InputFilesPhaseListener.beforePhase()  partialViewContext.isAjaxRequest: " + phaseEvent.getFacesContext().getPartialViewContext().isAjaxRequest());
            
//            System.out.println("InputFilesPhaseListener.beforePhase()  old    : " + request);
//            System.out.println("InputFilesPhaseListener.beforePhase()  wrapper: " + wrapper);
//            System.out.println("InputFilesPhaseListener.beforePhase()  set    : " + phaseEvent.getFacesContext().getExternalContext().getRequest());
        }
        
        request = (HttpServletRequest) phaseEvent.getFacesContext().getExternalContext().getRequest();
        
        /*
        System.out.println("About to list headers");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println("  HtServReq Header  " + name + " -> " + value);
        }
        Map<String,String> rhm = phaseEvent.getFacesContext().getExternalContext().getRequestHeaderMap();
        for(String key : rhm.keySet()) {
            System.out.println("  RequestHeaderMap  " + key + " -> " + rhm.get(key));
        }
        */
        
        /*
        System.out.println("About to list parameters");
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            System.out.println("  HtServReq Parameter  " + name + " -> " + value);
        }
        Map<String,String> rpm = phaseEvent.getFacesContext().getExternalContext().getRequestParameterMap();
        for(String key : rpm.keySet()) {
            System.out.println("  RequestParameterMap  " + key + " -> " + rpm.get(key));
        }
        */
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
    
    
    private static class FileUploadRequestWrapper extends HttpServletRequestWrapper {
        private static final String FACES_REQUEST = "Faces-Request";
        private static final String PARTIAL_AJAX = "partial/ajax";
        
        private static final String CONTENT_TYPE = "content-type";
        private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
        
        private Map<String,String[]> parameterMap;

        public FileUploadRequestWrapper(HttpServletRequest httpServletRequest,
                                        Map<String,String[]> parameterMap)
        {
            super(httpServletRequest);
            this.parameterMap = parameterMap;
        }

        public String getHeader(String name) {
//System.out.println("getHeader()  " + name);
            if (name != null) {
                if (name.equals(FACES_REQUEST)) {
//System.out.println("getHeader()  FACES_REQUEST -> PARTIAL_AJAX");
//Thread.dumpStack();
                    return PARTIAL_AJAX;
                }
                else if (name.equals(CONTENT_TYPE)) {
                    return APPLICATION_FORM_URLENCODED;
                }
            }
            return super.getHeader(name);
        }

        public java.util.Enumeration<String> getHeaders(java.lang.String name) {
//System.out.println("getHeaders()  " + name);
            if (name != null) {
                if (name.equals(FACES_REQUEST)) {
                    Vector<String> list = new Vector<String>(1);
                    list.add(PARTIAL_AJAX);
//System.out.println("getHeader()  FACES_REQUEST -> PARTIAL_AJAX");
                    return list.elements();
                }
                else if (name.equals(CONTENT_TYPE)) {
                    Vector<String> list = new Vector<String>(1);
                    list.add(APPLICATION_FORM_URLENCODED);
                    return list.elements();
                }
            }
            return super.getHeaders(name);
        }

        public int getIntHeader(java.lang.String name) {
            if (name != null) {
                if (name.equals(FACES_REQUEST))
                    throw new NumberFormatException("Can not convert " + FACES_REQUEST + " to integer");
                else if (name.equals(CONTENT_TYPE))
                    throw new NumberFormatException("Can not convert " + CONTENT_TYPE + " to integer");
            }
            return super.getIntHeader(name);
        }

        public java.util.Enumeration<String> getHeaderNames() {
            java.util.Vector<String> list = new java.util.Vector<String>();
            java.util.Enumeration<String> names = super.getHeaderNames();
            while (names != null && names.hasMoreElements()) {
                list.add(names.nextElement());
            }
            if (!list.contains(FACES_REQUEST))
                list.add(FACES_REQUEST);
            if (!list.contains(CONTENT_TYPE))
                list.add(CONTENT_TYPE);
            return list.elements();
        }
        
        
        // Returns a java.util.Map of the parameters of this request.
        public Map<String, String[]> getParameterMap() {
            if (parameterMap != null) {
                return Collections.unmodifiableMap(parameterMap);
            }
            return super.getParameterMap();
        }
        
        // Returns an Enumeration of String objects containing the names of
        // the parameters contained in this request.
        public Enumeration<String> getParameterNames() {
            if (parameterMap != null) {
                Vector<String> keyVec = new Vector<String>(parameterMap.keySet());
                return keyVec.elements();
            }
            return super.getParameterNames();
        }
        
        // Returns the value of a request parameter as a String, or null if
        // the parameter does not exist.
        public String getParameter(String name) {
            if (parameterMap != null) {
                if (!parameterMap.containsKey(name)) {
                    return null;
                }
                String[] values = parameterMap.get(name);
                if (values != null && values.length >= 1) {
                    return values[0];
                }
                return null; // Or "", since the key does exist?
            }
            return super.getParameter(name);
        }
        
        // Returns an array of String objects containing all of the values the
        // given request parameter has, or null if the parameter does not exist.
        public String[] getParameterValues(String name) {
            if (parameterMap != null) {
                if (!parameterMap.containsKey(name)) {
                    return null;
                }
                return parameterMap.get(name);
            }
            return super.getParameterValues(name);
        }
        
        public String getContentType() {
            return APPLICATION_FORM_URLENCODED;
        }
    }
}
