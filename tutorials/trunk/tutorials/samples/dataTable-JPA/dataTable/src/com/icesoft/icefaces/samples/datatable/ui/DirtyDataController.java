package com.icesoft.icefaces.samples.datatable.ui;

import java.util.ArrayList;
import java.util.List;

import com.icesoft.icefaces.samples.datatable.jpa.Customer;

public class DirtyDataController {
    
    // Used to trigger rendering in the other session ui's
    private static final ArrayList<SessionBean> CURRENT_SESSIONS = new ArrayList<SessionBean>();

	public static ArrayList<SessionBean> getCURRENT_SESSIONS() {
		return CURRENT_SESSIONS;
	}
	
    public void setOtherSessionsDirtyData(Customer customer){
        for (SessionBean CURRENT_SESSION : CURRENT_SESSIONS) {
            List<CustomerBean> sessionView = CURRENT_SESSION.getUiCustomerBeans();
            for (CustomerBean aSessionView : sessionView) {
                if (aSessionView.getCustomer().getCustomernumber().toString().equals(customer.getCustomernumber().toString())) {
                    CURRENT_SESSION.getOnePageDataModel().setDirtyData();
                    break;
                }
            }
        }
    }
}
