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

import com.icesoft.util.CoreComponentUtils;
import com.icesoft.faces.component.datapaginator.DataPaginator;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.component.tree.TreeDataModel;
import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.faces.model.SetDataModel;
import com.icesoft.faces.utils.SeriesStateHolder;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.*;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;
import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitResult;
import java.util.Collection;


/**
 * This is an extended version of UIData, which allows any UISeries type of
 * component to have any type of children, it is not restricted to use the
 * column component as a first child.
 */
public class UISeries extends HtmlDataTable implements SeriesStateHolder {
    public static final String COMPONENT_TYPE = "com.icesoft.faces.series";
    public static final String RENDERER_TYPE = "com.icesoft.faces.seriesRenderer";
    
    private static Class javax_servlet_jsp_jstl_sql_Result_class = null;
    static {
        try {
            javax_servlet_jsp_jstl_sql_Result_class = Class.forName(
                "javax.servlet.jsp.jstl.sql.Result");
        }
        catch(Exception e) {}
    }
    
    protected transient DataModel dataModel = null;
    private int rowIndex = -1;
    protected Map savedChildren = new HashMap();
    protected Map savedSeriesState = new HashMap();
    private String var = null;
    private String varStatus;


    public UISeries() {
        super();
        setRendererType(RENDERER_TYPE);
    }

    /**
     * @see javax.faces.component.UIData#isRowAvailable()
     */
    public boolean isRowAvailable() {
        return (getDataModel().isRowAvailable());
    }

    public Map getSavedChildren(){
    	return savedChildren;
    }

    /**
     * @see javax.faces.component.UIData#getRowCount()
     */
    public int getRowCount() {
        return (getDataModel().getRowCount());
    }


    /**
     * @see javax.faces.component.UIData#getRowData()
     */
    public Object getRowData() {
        return (getDataModel().getRowData());
    }


    /**
     * @see javax.faces.component.UIData#getRowIndex()
     */
    public int getRowIndex() {
        return (this.rowIndex);
    }

    public void setRowIndex(int rowIndex) {
        FacesContext facesContext = getFacesContext();
        // Save current state for the previous row index
        saveChildrenState(facesContext);
        // remove or load the current row data as a request scope attribute        
        processCurrentRowData(facesContext, rowIndex);
        // Reset current state information for the new row index
        restoreChildrenState(facesContext);
    }

    private void processCurrentRowData(FacesContext facesContext,
                                       int rowIndex) {
        this.rowIndex = rowIndex;
        DataModel model = getDataModel();
        model.setRowIndex(rowIndex);

        if (var != null || varStatus != null) {
            Map requestMap = facesContext.getExternalContext().getRequestMap();
            if (rowIndex == -1) {
                removeRowFromRequestMap(requestMap);
            } else if (isRowAvailable()) {
                // Indexes are inclusive
                int firstIndex = getFirst();
                int lastIndex;
                int rows = getRows();
                if (rows == 0) {
                    lastIndex = model.getRowCount() - 1;
                }
                else {
                    lastIndex = firstIndex + rows - 1;
                }
                loadRowToRequestMap(requestMap, firstIndex, lastIndex, rowIndex);
            } else {
                removeRowFromRequestMap(requestMap);
            }
        }
    }

    private void loadRowToRequestMap(Map requestMap, int begin, int end, int index) {
        if (var != null) {
            requestMap.put(var, getRowData());
        }
        if (varStatus != null) {
            requestMap.put(varStatus, new VarStatus(begin, end, index));
        }
    }

    private void removeRowFromRequestMap(Map requestMap) {
        if (var != null) {
            requestMap.remove(var);
        }
        if (varStatus != null) {
            requestMap.remove(varStatus);
        }
    }

    /**
     * @see javax.faces.component.UIData#getVar()
     */
    public String getVar() {
        return (this.var);
    }


    /**
     * @see javax.faces.component.UIData#setVar(String)
     */
    public void setVar(String var) {
        this.var = var;
    }

    /**
     * @return The name of the entry is the request map,
     * where the VarStatus object will be put.
     */
    public String getVarStatus() {
        return this.varStatus;
    }
    
    public void setVarStatus(String varStatus) {
        this.varStatus = varStatus;
    }
    
    /**
     * @see javax.faces.component.UIData#setValue(Object)
     */
    public void setValue(Object value) {
        this.dataModel = null;
        super.setValue(value);
    }


    public void setValueBinding(String name, ValueBinding binding) {
        if ("value".equals(name)) {
            this.dataModel = null;
        } else if ("var".equals(name) || "rowIndex".equals(name)) {
            throw new IllegalArgumentException();
        }
        super.setValueBinding(name, binding);
    }


    /**
     * @see javax.faces.component.UIData#getClientId(FacesContext)
     */
    public String getClientId(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        String baseClientId = super.getClientId(context);
        if (getRowIndex() >= 0) {
            //this extra if is to produce the same ids among myfaces and sunri
            //myfaces uses the getRowIndex() and SunRI directly using the rowIndex 
            //variable inside its getClientId()
            if (!baseClientId.endsWith(
                    "" + NamingContainer.SEPARATOR_CHAR + getRowIndex())) {
                return (baseClientId + NamingContainer.SEPARATOR_CHAR +
                        getRowIndex());
            }
            return (baseClientId);
        } else {
            return (baseClientId);
        }
    }


    /**
     * @see javax.faces.component.UIData#queueEvent(FacesEvent)
     */
    public void queueEvent(FacesEvent event) {
        FacesEvent rowEvent = new RowEvent(this, event, getRowIndex());
        // ICE-4822 : Don't have UISeries let its superclass UIData broadcast
        // events too, since then we have redundant events. So, do behaviour
        // of UIComponentBase.queueEvent(FacesEvent)
        //super.queueEvent(rowEvent);
        UIComponent parent = getParent();
        if (parent == null) {
            throw new IllegalStateException();
        } else {
            parent.queueEvent(rowEvent);
        }
    }


    /**
     * @see javax.faces.component.UIData#broadcast(FacesEvent)
     */
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (!(event instanceof RowEvent)) {
            super.broadcast(event);
            return;
        }

        // fire row specific event
        ((RowEvent) event).broadcast();
        return;
    }


    /**
     * @see javax.faces.component.UIData#encodeBegin(FacesContext)
     */
    public void encodeBegin(FacesContext context) throws IOException {
        dataModel = null;
        if (!keepSaved(context)) {
            savedChildren = new HashMap();
        }
        synchWithPaginator();
        super.encodeBegin(context);
    }


    
    public void processDecodes(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (!isRendered()) {
            return;
        }
        dataModel = null;
        if (null == savedChildren || !keepSaved(context)) {
            savedChildren = new HashMap();
        }

        iterate(context, PhaseId.APPLY_REQUEST_VALUES);
        decode(context);
    }

    public void processValidators(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (!isRendered()) {
            return;
        }
        if (isNestedWithinUIData()) {
            dataModel = null;
        }
        iterate(context, PhaseId.PROCESS_VALIDATIONS);
    }


    public void processUpdates(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (!isRendered()) {
            return;
        }
        if (isNestedWithinUIData()) {
            dataModel = null;
        }
        iterate(context, PhaseId.UPDATE_MODEL_VALUES);
    }

    /**
     * <p>Return the DataModel containing the Objects that will be iterated over
     * when this component is rendered.</p>
     *
     * @return
     */
    protected DataModel getDataModel() {
        if (null != this.dataModel) {
            return (dataModel);
        }

        Object currentValue = getValue();

        if (null == currentValue) {
            this.dataModel = new ListDataModel(Collections.EMPTY_LIST);
        } else if (currentValue instanceof DataModel) {
            this.dataModel = (DataModel) currentValue;
        } else if (currentValue instanceof List) {
            this.dataModel = new ListDataModel((List) currentValue);
        } else if (Object[].class.isAssignableFrom(currentValue.getClass())) {
            this.dataModel = new ArrayDataModel((Object[]) currentValue);
        } else if (currentValue instanceof ResultSet) {
            this.dataModel = new ResultSetDataModel((ResultSet) currentValue);
        } else if (javax_servlet_jsp_jstl_sql_Result_class != null &&
                   javax_servlet_jsp_jstl_sql_Result_class.isInstance(currentValue)) {
            this.dataModel = new ResultDataModel();
            this.dataModel.setWrappedData(currentValue);
        } else if (currentValue instanceof TreeModel) {
            this.dataModel = new TreeDataModel((TreeModel) currentValue);
        } else if (currentValue instanceof Set) {
            this.dataModel = new SetDataModel((Set) currentValue);
        } else if (currentValue instanceof Map) {
            this.dataModel = new SetDataModel(((Map) currentValue).entrySet());
        } else {
            this.dataModel = new ScalarDataModel(currentValue);
        }

        return (dataModel);
    }

    protected void iterate(FacesContext facesContext, PhaseId phase) {
        // clear rowIndex
        setRowIndex(-1);

        int rowsProcessed = 0;
        int currentRowIndex = getFirst() - 1;
        int displayedRows = getRows();
        // loop over dataModel processing each row once
        while (1 == 1) {
            // break if we have processed the number of rows requested
            if ((displayedRows > 0) && (++rowsProcessed > displayedRows)) {
                break;
            }
            // process the row at currentRowIndex
            setRowIndex(++currentRowIndex);
            // break if we've moved past the last row
            if (!isRowAvailable()) {
                break;
            }
            // loop over children and facets
            Iterator children = getFacetsAndChildren();
            while (children.hasNext()) {
                UIComponent child = (UIComponent) children.next();
                if (phase == PhaseId.APPLY_REQUEST_VALUES) {
                    child.processDecodes(facesContext);
                } else if (phase == PhaseId.PROCESS_VALIDATIONS) {
                    child.processValidators(facesContext);
                } else if (phase == PhaseId.UPDATE_MODEL_VALUES) {
                    child.processUpdates(facesContext);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }

        // clear rowIndex
        setRowIndex(-1);
    }

    /**
     * <p>Return true when we need to keep the child state to display error
     * messages.</p>
     *
     * @param facesContext
     * @return
     */
    private boolean keepSaved(FacesContext facesContext) {
        if (maximumSeverityAtLeastError(facesContext)) {
            return true;
        }
        // return true if this component is nested inside a UIData 
        return (isNestedWithinUIData());
    }

    private boolean maximumSeverityAtLeastError(FacesContext facesContext) {
        FacesMessage.Severity maximumSeverity = facesContext.getMaximumSeverity();
        return ( (maximumSeverity != null) &&
                 (FacesMessage.SEVERITY_ERROR.compareTo(maximumSeverity) <= 0) );
    }
    
    private boolean isNestedWithinUIData() {
        UIComponent parent = this;
        while (null != (parent = parent.getParent())) {
            if (parent instanceof UIData) {
                return true;
            }
        }
        return (false);
    }


    protected void restoreChildrenState(FacesContext facesContext) {
        Iterator children = getFacetsAndChildren();
        while (children.hasNext()) {
            UIComponent child = (UIComponent) children.next();
            restoreChildState(facesContext, child);
        }
    }


    protected void restoreChildState(FacesContext facesContext,
                                     UIComponent component) {
        String id = component.getId();
        if (!isValid(id)) {
            return;
        }
        component.setId(id);
        restoreChild(facesContext, component);
        Iterator children = component.getFacetsAndChildren();
        while (children.hasNext()) {
            restoreChildState(facesContext, (UIComponent) children.next());
        }
    }

    protected void saveChild(FacesContext facesContext, UIComponent component) {
        if (component instanceof EditableValueHolder) {
            EditableValueHolder input = (EditableValueHolder) component;
            String clientId = component.getClientId(facesContext);
            ChildState state = (ChildState) savedChildren.get(clientId);
            if (state == null) {
                state = new ChildState();
                savedChildren.put(clientId, state);
            }
            state.setValue(input.getLocalValue());
            state.setValid(input.isValid());
            state.setSubmittedValue(input.getSubmittedValue());
            state.setLocalValueSet(input.isLocalValueSet());
        } else if (component instanceof HtmlForm) {
            String clientId = component.getClientId(facesContext);
            Boolean isThisFormSubmitted = (Boolean) savedChildren.get(clientId);
            if (isThisFormSubmitted == null) {
                isThisFormSubmitted =
                        new Boolean(((HtmlForm) component).isSubmitted());
                savedChildren.put(clientId, isThisFormSubmitted);
            }
        }
        if(component instanceof SeriesStateHolder) {
            SeriesStateHolder ssh = (SeriesStateHolder) component;
            String clientId = component.getClientId(facesContext);
            Object state = ssh.saveSeriesState(facesContext);
            savedSeriesState.put(clientId, state);
        }
    }

    protected void restoreChild(FacesContext facesContext,
                                UIComponent component) {
        if (component instanceof EditableValueHolder) {
            EditableValueHolder input = (EditableValueHolder) component;
            String clientId = component.getClientId(facesContext);
            ChildState state = (ChildState) savedChildren.get(clientId);
            if (state == null) {
                state = new ChildState();
            }
            input.setValue(state.getValue());
            input.setValid(state.isValid());
            input.setSubmittedValue(state.getSubmittedValue());
            input.setLocalValueSet(state.isLocalValueSet());
        } else if (component instanceof HtmlForm) {
            String clientId = component.getClientId(facesContext);
            Boolean isThisFormSubmitted = (Boolean) savedChildren.get(clientId);
            if (isThisFormSubmitted == null) {
                isThisFormSubmitted = Boolean.FALSE;
            }
            ((HtmlForm) component)
                    .setSubmitted(isThisFormSubmitted.booleanValue());
        }
        if(component instanceof SeriesStateHolder) {
            SeriesStateHolder ssh = (SeriesStateHolder) component;
            String clientId = component.getClientId(facesContext);
            Object state = savedSeriesState.get(clientId);
            if(state != null) {
                ssh.restoreSeriesState(facesContext, state);
            }
        }
    }

    protected void saveChildrenState(FacesContext facesContext) {
        Iterator children = getFacetsAndChildren();
        while (children.hasNext()) {
            UIComponent child = (UIComponent) children.next();
            saveChildState(facesContext, child);
        }
    }

    protected void saveChildState(FacesContext facesContext,
                                  UIComponent component) {
        saveChild(facesContext, component);
        Iterator children = component.getFacetsAndChildren();
        while (children.hasNext()) {
            saveChildState(facesContext, (UIComponent) children.next());
        }
    }

    public Object saveSeriesState(FacesContext facesContext) {
        Object[] values = new Object[1];
        values[0] = new Integer(getFirst());
        return values;
    }

    public void restoreSeriesState(FacesContext facesContext, Object state) {
        Object[] values = (Object[]) state;
        Integer first = (Integer) values[0];
        if (first != null && first.intValue() != getFirst())
            setFirst(first.intValue());
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

    //  Event associated with the specific rowindex
    public class RowEvent extends FacesEvent {
        private FacesEvent event = null;
        private int eventRowIndex = -1;

        public RowEvent(UIComponent component, FacesEvent event,
                        int eventRowIndex) {
            super(component);
            this.event = event;
            this.eventRowIndex = eventRowIndex;
        }

        public FacesEvent getFacesEvent() {
            return (this.event);
        }

        public void setFacesEvent(FacesEvent event) {
            this.event = event;
        }
        public boolean isAppropriateListener(FacesListener listener) {
            return false;
        }

        public void processListener(FacesListener listener) {
            throw new IllegalStateException();
        }

        public PhaseId getPhaseId() {
            return (this.event.getPhaseId());
        }

        public void setPhaseId(PhaseId phaseId) {
            this.event.setPhaseId(phaseId);
        }

        public void broadcast() {
            int oldRowIndex = getRowIndex();
            setRowIndex(eventRowIndex);
            event.getComponent().broadcast(event);
            setRowIndex(oldRowIndex);
        }
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = new Integer(rowIndex);
        values[2] = savedChildren;
        values[3] = savedSeriesState;
        values[4] = var;
        values[5] = varStatus;
        return (values);
    }


    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        rowIndex = ((Integer) values[1]).intValue();
        savedChildren = (Map) values[2];
        savedSeriesState = (Map) values[3];
        var = (String) values[4];
        varStatus = (String) values[5];
    }

    private boolean isValid(String id) {
        if (id == null) {
            return false;
        }
        if (!Character.isLetter(id.charAt(0)) && (id.charAt(0) != '_')) {
            return false;
        }
        return true;
    }
    
    public void ensureFirstRowInRange() {
        int numRowsTotal = getRowCount(); // could be -1
        int numRowsToShow = getRows();    // always >= 0
        int firstRowIdx = getFirst();     // always >= 0

        if (numRowsTotal <= 0) {
            // value of "first" could be from backing bean, therefore don't set indiscriminately
            if (firstRowIdx != 0) {
                setFirst(0);
            }
        } else if (firstRowIdx >= numRowsTotal) {
            if (numRowsToShow == 0) {
                setFirst(0); // all rows in one page
            } else { // first row of last page
                setFirst((numRowsTotal - 1) / numRowsToShow * numRowsToShow);
            }
        }
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
        if (!this.getAttributes().containsKey(DataPaginator.class.getName())) return;
        String dataPaginatorClientId = (String) this.getAttributes().get(DataPaginator.class.getName()); 
        DataPaginator paginator = (DataPaginator) CoreComponentUtils.findComponent(UINamingContainer.getSeparatorChar(getFacesContext()) + dataPaginatorClientId, FacesContext.getCurrentInstance().getViewRoot());
        if (paginator != null && paginator.isRendered()) {
            paginator.getPageIndex();
        }
    }
    
    /**
     * <p class="changed_added_2_0"><span
     * class="changed_modified_2_0_rev_a">Override</span> the behavior
     * in {@link UIComponent#visitTree} to handle iteration
     * correctly.</p>
     *
     * <div class="changed_added_2_0">

     * <p>If the {@link UIComponent#isVisitable} method of this instance
     * returns <code>false</code>, take no action and return.</p>

     * <p>Call {@link UIComponent#pushComponentToEL} and
     * invoke the visit callback on this <code>UIData</code> instance as
     * described in {@link UIComponent#visitTree}.  Let the result of
     * the invoctaion be <em>visitResult</em>.  If <em>visitResult</em>
     * is {@link VisitResult#COMPLETE}, take no further action and
     * return <code>true</code>.  Otherwise, determine if we need to
     * visit our children.  The default implementation calls {@link
     * VisitContext#getSubtreeIdsToVisit} passing <code>this</code> as
     * the argument.  If the result of that call is non-empty, let
     * <em>doVisitChildren</em> be <code>true</code>.  If
     * <em>doVisitChildren</em> is <code>true</code> and
     * <em>visitResult</em> is {@link VisitResult#ACCEPT}, take the
     * following action.<p>

     * <ul>

     * 	  <li><p>If this component has facets, call {@link
     * 	  UIComponent#getFacets} on this instance and invoke the
     * 	  <code>values()</code> method.  For each
     * 	  <code>UIComponent</code> in the returned <code>Map</code>,
     * 	  call {@link UIComponent#visitTree}.</p></li>

     * 	  <li>

     * <div class="changed_modified_2_0_rev_a">

     *  <p>If this component has children, for each
     * 	  <code>UIColumn</code> child:</p>
     *
     *    <p>Call {@link VisitContext#invokeVisitCallback} on that
          <code>UIColumn</code> instance.
     *    If such a call returns <code>true</code>, terminate visiting and
          return <code>true</code> from this method.</p>
     *
     *    <p>If the child <code>UIColumn</code> has facets, call
     *    {@link UIComponent#visitTree} on each one.</p>
     *
     *    <p>Take no action on non-<code>UIColumn</code> children.</p>
     *
     * </div>
     * </li>
     *
     *    <li>

     * <div class="changed_modified_2_0_rev_a">
     *
     * <p>Save aside the result of a call to {@link
     *    #getRowIndex}.</p>

     *    <p>For each child component of this <code>UIData</code> that is
     *    also an instance of {@link UIColumn},
     *    </p>

     * 	  <p>Iterate over the rows.</p>

     * </div>

     * <ul>

     * 	  <li><p>Let <em>rowsToProcess</em> be the return from {@link
     * 	  #getRows}.  </p></li>

     * 	  <li><p>Let <em>rowIndex</em> be the return from {@link
     * 	  #getFirst} - 1.</p></li>

     * 	  <li><p>While the number of rows processed is less than
     * 	  <em>rowsToProcess</em>, take the following actions.</p>

     * <p>Call {@link #setRowIndex}, passing the current row index.</p>

     * <p>If {@link #isRowAvailable} returns <code>false</code>, take no
     * further action and return <code>false</code>.</p>
     *
     * <p class="changed_modified_2_0_rev_a">>Call {@link
     * UIComponent#visitTree} on each of the children of this
     * <code>UIColumn</code> instance.</p>

     *     </li>

     * </ul>

     *    </li>

     * </ul>

     * <p>Call {@link #popComponentFromEL} and restore the saved row
     * index with a call to {@link #setRowIndex}.</p>

     * <p>Return <code>false</code> to allow the visiting to
     * continue.</p>

     * </div>
     *
     * @param context the <code>VisitContext</code> that provides
     * context for performing the visit.
     *
     * @param callback the callback to be invoked for each node
     * encountered in the visit.

     * @throws NullPointerException if any of the parameters are
     * <code>null</code>.

     *
     */
    @Override
    public boolean visitTree(VisitContext context,
                             VisitCallback callback) {

        // First check to see whether we are visitable.  If not
        // short-circuit out of this subtree, though allow the
        // visit to proceed through to other subtrees.
        if (!isVisitable(context))
            return false;

        FacesContext facesContext = context.getFacesContext();
        // NOTE: that the visitRows local will be obsolete once the
        //       appropriate visit hints have been added to the API
        boolean visitRows = requiresRowIteration(context);;

        // Clear out the row index is one is set so that
        // we start from a clean slate.
        int oldRowIndex = -1;
        if (visitRows) {
            oldRowIndex = getRowIndex();
            setRowIndex(-1);
        }

        // Push ourselves to EL
        pushComponentToEL(facesContext, null);

        try {

            // Visit ourselves.  Note that we delegate to the
            // VisitContext to actually perform the visit.
            VisitResult result = context.invokeVisitCallback(this, callback);

            // If the visit is complete, short-circuit out and end the visit
            if (result == VisitResult.COMPLETE)
                return true;

            // Visit children, short-circuiting as necessary
            // NOTE: that the visitRows parameter will be obsolete once the
            //       appropriate visit hints have been added to the API
            if ((result == VisitResult.ACCEPT) && doVisitChildren(context, visitRows)) {

                // First visit facets
                // NOTE: that the visitRows parameter will be obsolete once the
                //       appropriate visit hints have been added to the API
                if (visitFacets(context, callback, visitRows))
                    return true;

                // Next column facets
                // NOTE: that the visitRows parameter will be obsolete once the
                //       appropriate visit hints have been added to the API
                if (visitColumnsAndColumnFacets(context, callback, visitRows))
                    return true;

                // And finally, visit rows
                // NOTE: that the visitRows parameter will be obsolete once the
                //       appropriate visit hints have been added to the API
                if (visitRows(context, callback, visitRows))
                    return true;
            }
        }
        finally {
            // Clean up - pop EL and restore old row index
            popComponentFromEL(facesContext);
            if (visitRows) {
                setRowIndex(oldRowIndex);
            }
        }

        // Return false to allow the visit to continue
        return false;
    }

    /**
     * Called by {@link UIData#visitTree} to determine whether or not the
     * <code>visitTree</code> implementation should visit the rows of UIData
     * or by manipulating the row index before visiting the components themselves.
     *
     * Once we have the appropriate Visit hints for state saving, this method
     * will become obsolete.
     *
     * @param ctx the <code>FacesContext</code> for the current request
     *
     * @return true if row index manipulation is required by the visit to this
     *  UIData instance
     */
    private boolean requiresRowIteration(VisitContext ctx) {
        try { // Use JSF 2.1 hints if available
            return !ctx.getHints().contains(VisitHint.SKIP_ITERATION);
        } catch (NoSuchFieldError e) {
            FacesContext fctx = FacesContext.getCurrentInstance();
            return (!PhaseId.RESTORE_VIEW.equals(fctx.getCurrentPhaseId()));
        }
    }

    // Tests whether we need to visit our children as part of
    // a tree visit
    private boolean doVisitChildren(VisitContext context, boolean visitRows) {

        // Just need to check whether there are any ids under this
        // subtree.  Make sure row index is cleared out since
        // getSubtreeIdsToVisit() needs our row-less client id.
        if (visitRows) {
            setRowIndex(-1);
        }
        Collection<String> idsToVisit = context.getSubtreeIdsToVisit(this);
        assert(idsToVisit != null);

        // All ids or non-empty collection means we need to visit our children.
        return (!idsToVisit.isEmpty());
    }

    // Visit each facet of this component exactly once.
    private boolean visitFacets(VisitContext context,
                                VisitCallback callback,
                                boolean visitRows) {

        if (visitRows) {
            setRowIndex(-1);
        }
        if (getFacetCount() > 0) {
            for (UIComponent facet : getFacets().values()) {
                if (facet.visitTree(context, callback))
                    return true;
            }
        }

        return false;
    }

    // Visit each UIColumn and any facets it may have defined exactly once
    private boolean visitColumnsAndColumnFacets(VisitContext context,
                                                VisitCallback callback,
                                                boolean visitRows) {
        if (visitRows) {
            setRowIndex(-1);
        }
        if (getChildCount() > 0) {
            for (UIComponent column : getChildren()) {
                if (column instanceof UIColumn) {
                    VisitResult result = context.invokeVisitCallback(column, callback); // visit the column directly
                    if (result == VisitResult.COMPLETE) {
                        return true;
                    }
                    if (column.getFacetCount() > 0) {
                        for (UIComponent columnFacet : column.getFacets().values()) {
                            if (columnFacet.visitTree(context, callback)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    // Visit each column and row
    private boolean visitRows(VisitContext context,
                              VisitCallback callback,
                              boolean visitRows) {

        // Iterate over our UIColumn children, once per row
        int processed = 0;
        int rowIndex = 0;
        int rows = 0;
        if (visitRows) {
            rowIndex = getFirst() - 1;
            rows = getRows();
        }

        while (true) {

            // Have we processed the requested number of rows?
            if (visitRows) {
                if ((rows > 0) && (++processed > rows)) {
                    break;
                }
                // Expose the current row in the specified request attribute
                setRowIndex(++rowIndex);
                if (!isRowAvailable()) {
                    break; // Scrolled past the last row
                }
            }

            // Visit as required on the *children* of the UIColumn
            // (facets have been done a single time with rowIndex=-1 already)
            if (getChildCount() > 0) {
                for (UIComponent kid : getChildren()) {
                    if (!(kid instanceof UIColumn)) {
                        if (kid.visitTree(context, callback)) {
                            return true;
                        }
                        continue;
                    }
                    if (kid.getChildCount() > 0) {
                    for (UIComponent grandkid : kid.getChildren()) {
                            if (grandkid.visitTree(context, callback)) {
                                return true;
                            }
                        }
                    }
                }
            }

            if (!visitRows) {
                break;
            }

        }

        return false;
    }
}

class ChildState implements Serializable {

    private Object submittedValue;
    private boolean valid = true;
    private Object value;
    private boolean localValueSet;

    Object getSubmittedValue() {
        return submittedValue;
    }

    void setSubmittedValue(Object submittedValue) {
        this.submittedValue = submittedValue;
    }

    boolean isValid() {
        return valid;
    }

    void setValid(boolean valid) {
        this.valid = valid;
    }

    Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    boolean isLocalValueSet() {
        return localValueSet;
    }

    public void setLocalValueSet(boolean localValueSet) {
        this.localValueSet = localValueSet;
    }
    
    
}


