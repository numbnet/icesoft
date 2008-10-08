package com.icesoft.faces.async.servlet;

import com.icesoft.faces.async.common.UpdatedViews;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;

import java.util.List;
import java.util.Set;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UpdatedViewsResponseHandler
implements ResponseHandler {
    private static final Log LOG =
        LogFactory.getLog(UpdatedViewsResponseHandler.class);

    private final Request request;
    private final List updatedViewsList;
    
    public UpdatedViewsResponseHandler(
        final Request request, final List updatedViewsList)
    throws IllegalArgumentException {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
        if (updatedViewsList == null) {
            throw new IllegalArgumentException("updatedViewsList is null");
        }
        if (updatedViewsList.isEmpty()) {
            throw new IllegalArgumentException("updatedViewsList is empty");
        }
        this.request = request;
        this.updatedViewsList = updatedViewsList;
    }

    public void respond(final Response response)
    throws Exception {
        // preparation
        StringBuffer _sequenceNumbers = new StringBuffer();
        StringBuffer _entityBody = new StringBuffer();
        _entityBody.append("<updated-views>");
        for (int i = 0, _iMax = updatedViewsList.size(); i < _iMax; i++) {
            UpdatedViews _updatedViews = (UpdatedViews)updatedViewsList.get(i);
            if (i != 0) {
                _sequenceNumbers.append(",");
                _entityBody.append(" ");
            }
            _sequenceNumbers.
                append(_updatedViews.getICEfacesID()).append(":").
                append(_updatedViews.getSequenceNumber());
            Set _updatedViewsSet = _updatedViews.getUpdatedViewsSet();
            Iterator _updatedViewsIterator = _updatedViewsSet.iterator();
            for (int j = 0, _jMax = _updatedViewsSet.size() ; j < _jMax; j++) {
                if (j != 0) {
                    _entityBody.append(" ");
                }
                _entityBody.
                    append(_updatedViews.getICEfacesID()).append(":").
                    append(_updatedViewsIterator.next());
            }
        }
        _entityBody.append("</updated-views>\r\n\r\n");
        // general header fields
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, no-store");
        // entity header fields
        response.setHeader("Content-Length", _entityBody.length());
        response.setHeader("Content-Type", "text/xml");
        // extension header fields
        response.setHeader(
            "X-Set-Window-Cookie",
            "Sequence_Numbers=\"" + _sequenceNumbers.toString() + "\"");
        // entity-body
        response.writeBody().write(_entityBody.toString().getBytes("UTF-8"));
    }
}
