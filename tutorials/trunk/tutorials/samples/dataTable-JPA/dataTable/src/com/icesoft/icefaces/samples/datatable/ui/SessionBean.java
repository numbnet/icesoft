/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package com.icesoft.icefaces.samples.datatable.ui;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.context.DisposableBean;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;
import com.icesoft.icefaces.samples.datatable.jpa.Customer;
import com.icesoft.icefaces.samples.datatable.jpa.CustomerDAO;
import com.icesoft.icefaces.samples.datatable.jpa.EntityManagerHelper;

import javax.faces.model.DataModel;
import java.util.ArrayList;
import java.util.List;

public class SessionBean extends DataSource implements Renderable, DisposableBean {

    private CustomerDAO CUSTOMERDAO = new CustomerDAO();

    // dataTable column names for display in ui
    private static String FIRSTCOLUMNNAME = "First Name";
    private static String SECONDCOLUMNNAME = "Last Name";
    // database table column names for sorting
    public static final String CONTACTFIRSTNAME = "contactfirstname";
    public static final String CONTACTLASTNAME = "contactlastname";

    // required for rendering api
    private RenderManager renderManager;
    private PersistentFacesState state = PersistentFacesState.getInstance();

    // Current items in ui
    private List<CustomerBean> uiCustomerBeans = new ArrayList<CustomerBean>(pageSize);
    // Used to inform other sessions of dirtyData
    private DirtyDataController dirtyDataController;

    public SessionBean() {
        // default sort header, sorting is performed during database query
        super(CONTACTFIRSTNAME);
    }

    public List<CustomerBean> getUiCustomerBeans() {
        return uiCustomerBeans;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    public String getFIRSTCOLUMNNAME() {
        return FIRSTCOLUMNNAME;
    }

    public String getSECONDCOLUMNNAME() {
        return SECONDCOLUMNNAME;
    }

    public String getCONTACTFIRSTNAME() {
        return CONTACTFIRSTNAME;
    }

    public String getCONTACTLASTNAME() {
        return CONTACTLASTNAME;
    }

    public PersistentFacesState getState() {
        return state;
    }

    /**
     * This method is called from faces-config.xml with each new session.
     */
    public void setRenderManager(RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    public void renderingException(RenderingException arg0) {
        if (arg0 instanceof TransientRenderingException) {

        } else if (arg0 instanceof FatalRenderingException) {
            // Remove from existing Customer render groups.
            leaveRenderGroups();
        }
    }

    /**
     * Remove this Renderable from existing uiCustomerBeans render groups.
     * OnDemandRenderers are named/created using the underlying Customer Number.
     */
    private void leaveRenderGroups() {
        if (onePageDataModel.page != null) {
            for (CustomerBean uiCustomerBean : uiCustomerBeans) {
                renderManager.getOnDemandRenderer(uiCustomerBean.getCustomer().getCustomernumber().toString()).remove(this);
            }
        }
    }

    /**
     * Add this Renderable to the new uiCustomerBeans render groups.
     * OnDemandRenderers are named/created using the underlying Customer Number.
     */
    private void joinRenderGroups() {
        for (CustomerBean uiCustomerBean : uiCustomerBeans) {
            renderManager.getOnDemandRenderer(uiCustomerBean.getCustomer().getCustomernumber().toString()).add(this);
        }
    }

    /**
     * This method is called from faces-config.xml with each new session.
     */
    public void setDirtyDataController(DirtyDataController dirtyDataController) {
        this.dirtyDataController = dirtyDataController;
        DirtyDataController.getCURRENT_SESSIONS().add(this);
    }

    /**
     * Bound to DataTable value in the ui.
     */
    public DataModel getData() {
        state = PersistentFacesState.getInstance();
        if (onePageDataModel == null) {
            onePageDataModel = new LocalDataModel(pageSize);
        }
        return onePageDataModel;
    }

    /**
     * Commit updates Customer data to the database, marks the data as dirty in
     * all SessionBeans viewing the Customer and requests a render of those
     * views so they will be refreshed with the updated data.
     */
    public void commit(Customer customer) {
        EntityManagerHelper.beginTransaction();
        CUSTOMERDAO.update(customer);
        EntityManagerHelper.commit();
        dirtyDataController.setOtherSessionsDirtyData(customer);
        renderManager.getOnDemandRenderer(customer.getCustomernumber().toString()).requestRender();
    }


    /**
     * This is where the Customer data is retrieved from the database and
     * returned as a list of CustomerBean objects for display in the UI.
     */
    private DataPage<CustomerBean> getDataPage(int startRow, int pageSize) {
        // Retrieve the total number of customers from the database.  This
        // number is required by the DataPage object so the paginator will know
        // the relative location of the page data.
        int totalNumberCustomers = CUSTOMERDAO.findTotalNumberCustomers().intValue();

        // Calculate indices to be displayed in the ui.
        int endIndex = startRow + pageSize;
        if (endIndex > totalNumberCustomers) {
            endIndex = totalNumberCustomers;
        }

        // Query database for sorted results.
        List<Customer> pageCustomers = CUSTOMERDAO.findPageCustomers(sortColumnName, sortAscending, startRow, endIndex - startRow);

        // Remove this Renderable from the existing render groups.
        leaveRenderGroups();

        // Populate the list of uiCustomerBeans for binding to the dataTable.
        uiCustomerBeans.clear();
        for (Customer pageCustomer : pageCustomers) {
            uiCustomerBeans.add(new CustomerBean(pageCustomer, this));
        }
        // Add this Renderable to the new render groups.
        joinRenderGroups();

        // Reset the dirtyData flag.
        onePageDataModel.setDirtyData(false);

        // This is required when using Hibernate JPA.  If the EntityManager is not
        // cleared or closed objects are cached and stale objects will show up
        // in the table.
        // This way, the detached objects are reread from the database.
        // This call is not required with TopLink JPA, which uses a Query Hint
        // to clear the l2 cache in CustomerDAO.
        EntityManagerHelper.getEntityManager().clear();

        return new DataPage<CustomerBean>(totalNumberCustomers, startRow, uiCustomerBeans);
    }

    private class LocalDataModel extends PagedListDataModel {
        public LocalDataModel(int pageSize) {
            super(pageSize);
        }

        public DataPage<CustomerBean> fetchPage(int startRow, int pageSize) {
            // call enclosing managed bean method to fetch the data
            return getDataPage(startRow, pageSize);
        }
    }

    /**
     * Cleanup - before this object is disposed, leave RenderGroups and remove
     * this SessionBean from the list of current sessions.
     */
    public void dispose() {
        // Remove from existing Customer render groups.
        leaveRenderGroups();
        DirtyDataController.getCURRENT_SESSIONS().remove(this);
	}

}
