package com.icesoft.icefaces.samples.datatable.ui;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;

import com.icesoft.icefaces.samples.datatable.jpa.Customer;
import com.icesoft.icefaces.samples.datatable.jpa.CustomerDAO;
import com.icesoft.icefaces.samples.datatable.jpa.EntityManagerHelper;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.context.DisposableBean;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;

public class SessionBean extends DataSource implements Renderable, DisposableBean{

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

    public SessionBean(){
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
        if (arg0 instanceof TransientRenderingException ){
        
        }
        else if(arg0 instanceof FatalRenderingException){
    		// Remove from existing Customer render groups.
            leaveRenderGroups();
        }
	}

	/**
	 * Remove this Renderable from existing uiCustomerBeans render groups.
	 * OnDemandRenderers are named/created using the underlying Customer Number. 
	 */
    private void leaveRenderGroups(){
		if(onePageDataModel.page != null){
		    for(int i=0; i<uiCustomerBeans.size(); i++){
			    renderManager.getOnDemandRenderer(((CustomerBean)uiCustomerBeans.get(i)).getCustomer().getCustomernumber().toString()).remove(this);
		    }
		}    	
    }

	/**
	 * Add this Renderable to the new uiCustomerBeans render groups.
	 * OnDemandRenderers are named/created using the underlying Customer Number.
	 */
    private void joinRenderGroups(){
		for(int i=0; i<uiCustomerBeans.size(); i++){
			renderManager.getOnDemandRenderer(((CustomerBean)uiCustomerBeans.get(i)).getCustomer().getCustomernumber().toString()).add(this);
		}    	
    }

	/**
	 * This method is called from faces-config.xml with each new session.
	 */
	public void setDirtyDataController(DirtyDataController dirtyDataController){
		this.dirtyDataController = dirtyDataController;
		dirtyDataController.getCURRENT_SESSIONS().add(this);
	}

	/**
	 * Bound to DataTable value in the ui.
	 */
    public DataModel getData() {
    	state = PersistentFacesState.getInstance();
        if(onePageDataModel == null){
        	onePageDataModel = new LocalDataModel(pageSize, this);
        }
    	return onePageDataModel;
    }

	/**
	 * Commit updates Customer data to the database, marks the data as dirty in 
	 * all SessionBeans viewing the Customer and requests a render of those 
	 * views so they will be refreshed with the updated data.
	 */
    public void commit(Customer customer){
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
    	List<Customer> pageCustomers = CUSTOMERDAO.findPageCustomers(sortColumnName, sortAscending, startRow, endIndex-startRow);

		// Remove this Renderable from the existing render groups.
		leaveRenderGroups();

		// Populate the list of uiCustomerBeans for binding to the dataTable. 
    	uiCustomerBeans.clear();
    	for(int i = 0; i < pageCustomers.size(); i++){
			uiCustomerBeans.add(new CustomerBean(pageCustomers.get(i), this));
		}
    	// Add this Renderable to the new render groups.
		joinRenderGroups();	
		
        // Reset the dirtyData flag.
		onePageDataModel.setDirtyData(false);

		return new DataPage<CustomerBean>(totalNumberCustomers,startRow,uiCustomerBeans);
    }

    private class LocalDataModel extends PagedListDataModel {
        public LocalDataModel(int pageSize, SessionBean sessionBean) {
            super(pageSize, sessionBean);
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
		dirtyDataController.getCURRENT_SESSIONS().remove(this);
	}

}
