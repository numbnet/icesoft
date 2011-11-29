/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package com.icesoft.faces.component.panelseries;

import com.icesoft.faces.component.datapaginator.DataPaginator;
import com.icesoft.faces.component.datapaginator.DataPaginatorGroup;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.component.tree.TreeDataModel;
import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.faces.model.SetDataModel;
import org.icefaces.impl.component.UISeriesBase;

import javax.faces.FacesException;
import javax.faces.model.DataModel;
import javax.swing.tree.TreeModel;
import java.util.Map;
import java.util.Set;


/**
 * This is an extended version of UIData, which allows any UISeries type of
 * component to have any type of children, it is not restricted to use the
 * column component as a first child.
 */
public class UISeries extends UISeriesBase {
    public static final String COMPONENT_TYPE = "com.icesoft.faces.series";
    public static final String RENDERER_TYPE = "com.icesoft.faces.seriesRenderer";


    public UISeries() {
        super();
        setRendererType(RENDERER_TYPE);
    }

    protected DataModel wrapDataModel(Object currentValue) {
        if (currentValue instanceof TreeModel) {
            return new TreeDataModel((TreeModel) currentValue);
        } else if (currentValue instanceof Set) {
            return new SetDataModel((Set) currentValue);
        } else if (currentValue instanceof Map) {
            return new SetDataModel(((Map) currentValue).entrySet());
        }
        return null;
    }

    public Object getValue() {
        try {
            return super.getValue();
        } catch (Exception e) {
            //ICE-4066
            if (CustomComponentUtils.isAncestorRendered(this.getParent()) && isRendered()) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.component.UIComponent#isRendered()
     */
    public boolean isRendered() {
        if (!Util.isRenderedOnUserRole(this)) {
            return false;
        }
        return super.isRendered();
    }

    protected void synchWithPaginator() {
    	DataPaginatorGroup.execute(this, new DataPaginatorGroup.Invoker() {
			public void invoke(DataPaginator dataPaginator) {
				dataPaginator.getPageIndex();
			}
		});
    }
}


