package org.icefaces.mobi.component.dataview;

import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.component.IMobiComponent;
import org.icemobile.model.DataViewDataModel;
import org.icemobile.model.DataViewLazyDataModel;
import org.icemobile.model.DataViewListDataModel;
import org.icemobile.util.ClientDescriptor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright 2010-2013 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils Lundquist
 * Date: 2013-04-01
 * Time: 10:47 AM
 */
public class DataView extends DataViewBase implements IMobiComponent {
    private static Logger logger = Logger.getLogger(DataViewRenderer.class.getName());
    protected static final DataViewDataModel EMPTY_DATA_MODEL = new DataViewListDataModel(Collections.EMPTY_LIST);

    private Map<String, Object> requestMap;

    @Override
    public void processUpdates(FacesContext context) {
        initDetailContext(context);
        super.processUpdates(context);
        removeDetailContext(context);
    }

    @Override
    public void processValidators(FacesContext context) {
        initDetailContext(context);
        super.processValidators(context);
        removeDetailContext(context);
    }

    @Override
    public void processDecodes(FacesContext context) {
        String indexStr = context.getExternalContext().getRequestParameterMap().get(getClientId() + "_active");
        if (indexStr != null) {
            Integer newIndex = Integer.parseInt(indexStr);
            if (newIndex != null) setActiveRowIndex(newIndex);
        }

        initDetailContext(context);
        super.processDecodes(context);
        removeDetailContext(context);
    }

    @Override
    public Object processSaveState(FacesContext context) {
        initDetailContext(context);
        Object o = super.processSaveState(context);
        removeDetailContext(context);
        return o;
    }

    @Override
    public void processRestoreState(FacesContext context, Object state) {
        initDetailContext(context);
        super.processRestoreState(context, state);
        removeDetailContext(context);
    }

    private void removeDetailContext(FacesContext context) {
        getRequestMap(context).remove(getVar());
    }

    private void initDetailContext(FacesContext context) {
        Integer index = getActiveRowIndex();

        if (index != null) {
            getRequestMap(context).put(getVar(), getDataModel().getDataByIndex(index));
        }
    }

    private Map<String,Object> getRequestMap(FacesContext context) {
        if (requestMap == null) {
            requestMap = context.getExternalContext().getRequestMap();
        }
        return requestMap;
    }

    protected DataViewColumns getColumns() {
        for (UIComponent child : getChildren())
            if (child instanceof DataViewColumns)
                return (DataViewColumns)child;

        logger.log(Level.WARNING, "DataView: " + getClientId() + " - doesn't have the nessecary 'ace:dataViewColumns' child component.");

        return null;
    }

    protected DataViewDataModel getDataModel() {
        Object value = getValue();

        if (value != null) {
            if (value instanceof List)
                return new DataViewListDataModel((List) value);
            else if (value instanceof DataViewLazyDataModel)
                return (DataViewDataModel)value;
            else {
                logger.log(Level.WARNING, "DataView: " + getClientId() + " - Invalid type for 'value' attribute.");
                return EMPTY_DATA_MODEL;
            }
        }

        logger.log(Level.WARNING, "DataView: " + getClientId() + " - 'value' attribute is null.");
        return EMPTY_DATA_MODEL;
    }

    protected DataViewDetails getDetails() {
        for (UIComponent child : getChildren())
            if (child instanceof DataViewDetails)
                return (DataViewDetails)child;

        logger.log(Level.WARNING, "DataView: " + getClientId() + " - doesn't have the nessecary 'ace:dataViewDetails' child component.");

        return null;
    }

    public ClientDescriptor getClient() {
        return MobiJSFUtils.getClientDescriptor();
    }
}
