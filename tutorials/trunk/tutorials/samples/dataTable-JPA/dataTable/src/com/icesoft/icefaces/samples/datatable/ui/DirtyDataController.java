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
		for(int i=0; i<CURRENT_SESSIONS.size(); i++){
			List<CustomerBean> sessionView = CURRENT_SESSIONS.get(i).getUiCustomerBeans();
			for(int y=0; y<sessionView.size(); y++){
				if(((CustomerBean)sessionView.get(y)).getCustomer().getCustomernumber().toString().equals(customer.getCustomernumber().toString())){
					CURRENT_SESSIONS.get(i).getOnePageDataModel().setDirtyData();
					break;
				}
			}
		}
    }
}
