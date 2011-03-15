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
import com.icesoft.faces.async.render.SessionRenderer;
import com.icesoft.faces.context.DisposableBean;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;
import com.icesoft.icefaces.samples.datatable.jpa.Customer;
import com.icesoft.icefaces.samples.datatable.jpa.CustomerDAO;
import com.icesoft.icefaces.samples.datatable.jpa.EntityManagerHelper;

import javax.faces.model.DataModel;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * SessionBean is our web application controller class.  This class makes calls
 * to the JPA layer to maintain a list of records displayed by a particular user
 * during his/her session.
 *
 * SessionBean extends DataSource which holds a reference to the dataTable
 * DataModel and dataTable sorting utility methods.
 *
 * SessionBean implements Renderable, enabling changes in the dataTable to be
 * rendered to the session client.
 */
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

    private boolean addRecord;
    private String tempcontactfirstname;
    private String tempcontactlastname;

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

    public boolean isAddRecord() {
        return addRecord;
    }

    public void setAddRecord(boolean addRecord) {
        this.addRecord = addRecord;
    }

    public String getTempcontactfirstname() {
        return tempcontactfirstname;
    }

    public void setTempcontactfirstname(String tempcontactfirstname) {
        this.tempcontactfirstname = tempcontactfirstname;
    }

    public String getTempcontactlastname() {
        return tempcontactlastname;
    }

    public void setTempcontactlastname(String tempcontactlastname) {
        this.tempcontactlastname = tempcontactlastname;
    }

    /**
     * This method is called when a render call is made from the server.  Render
     * calls are only made to views containing an updated record. The data is
     * marked as dirty to trigger a fetch of the updated record from the
     * database before rendering takes place.
     */
    public PersistentFacesState getState() {
        onePageDataModel.setDirtyData();
        return state;
    }

    /**
     * This method is called from faces-config.xml with each new session.
     */
    public void setRenderManager(RenderManager renderManager) {
        this.renderManager = renderManager;
        renderManager.getOnDemandRenderer(SessionRenderer.ALL_SESSIONS).add(this);
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
     * Commit updates Customer data to the database and requests a render of
     * views containing the Customer data so they will be refreshed with the update.
     */
    public void commit(Customer customer) {
        EntityManagerHelper.beginTransaction();
        CUSTOMERDAO.update(customer);
        EntityManagerHelper.commit();
        renderManager.getOnDemandRenderer(customer.getCustomernumber().toString()).requestRender();
    }

    /**
     * Commit updates Customer data to the database and requests a render of
     * views containing the Customer data so they will be refreshed with the update.
     */
    public void delete(Customer customer) {
        EntityManagerHelper.beginTransaction();
        CUSTOMERDAO.delete(customer);
        EntityManagerHelper.commit();
        renderManager.getOnDemandRenderer(customer.getCustomernumber().toString()).requestRender();
        //renderManager.removeRenderer(renderManager.getOnDemandRenderer(customer.getCustomernumber().toString()));
    }

    /**
     * Commit updates Customer data to the database and requests a render of
     * views containing the Customer data so they will be refreshed with the update.
     */
    public void save(Customer customer) {
        EntityManagerHelper.beginTransaction();
        CUSTOMERDAO.save(customer);
        EntityManagerHelper.commit();
        renderManager.getOnDemandRenderer(SessionRenderer.ALL_SESSIONS).requestRender();
    }

    /**
     * <p>Bound to commandButton actionListener in the ui that commits Customer
     * changes to the database.</p>
     */
    public void add(ActionEvent e) {
        Customer newCustomer = new Customer();
        newCustomer.setContactfirstname(tempcontactfirstname);
        newCustomer.setContactlastname(tempcontactlastname);
        save(newCustomer);
        addRecord = !addRecord;
    }

    /**
     * <p>Bound to commandButton actionListener in the ui that cancels potential
     * Customer changes to the database and unrenders the editable Customer
     * details.</p>
     */
    public void toggleAddRecord(ActionEvent e) {
        tempcontactfirstname = "";
        tempcontactlastname = "";
        addRecord = !addRecord;
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
        // The final record has been deleted, pushing us into the previous page
        if(endIndex-startRow == 0){
            startRow = startRow - pageSize;
            onePageDataModel.setRowIndex(startRow);

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

    /**
     * A special type of JSF DataModel to allow a datatable and datapaginator
     * to page through a large set of data without having to hold the entire
     * set of data in memory at once.
     * Any time a managed bean wants to avoid holding an entire dataset,
     * the managed bean declares this inner class which extends PagedListDataModel
     * and implements the fetchData method. fetchData is called
     * as needed when the table requires data that isn't available in the
     * current data page held by this object.
     * This requires the managed bean (and in general the business
     * method that the managed bean uses) to provide the data wrapped in
     * a DataPage object that provides info on the full size of the dataset.
     */
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
    }

}
